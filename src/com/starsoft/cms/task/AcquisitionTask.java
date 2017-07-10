package com.starsoft.cms.task;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.starsoft.cms.domain.AcquisitionHistoryDomain;
import com.starsoft.cms.domain.AcquisitionRuleDomain;
import com.starsoft.cms.domain.ArticleDomain;
import com.starsoft.cms.entity.AcquisitionHistory;
import com.starsoft.cms.entity.AcquisitionRule;
import com.starsoft.cms.entity.Article;
import com.starsoft.cms.entity.Columns;
import com.starsoft.core.util.HtmlParserUtil;
import com.starsoft.core.domain.ClobInfoDomain;
import com.starsoft.core.task.BaseTaskAction;

/***
 * 信息采集任务调度
 * @author lenovo
 *
 */
@Component
public class AcquisitionTask extends BaseTaskAction{
	private AcquisitionRuleDomain acquisitionRuleDomain;
	private AcquisitionHistoryDomain acquisitionHistoryDomain;
	private ClobInfoDomain clobInfoDomain;
	private ArticleDomain articleDomain;
	/***
	 * 1000=1秒
	 * @throws Exception
	 */
	@Scheduled(fixedRate = 3600000)
	public void doSomething() throws Exception {
		if(!power()) return;
		//1.获取采集规则列表
		List acquisitionRuleList=acquisitionRuleDomain.queryByCriteria(acquisitionRuleDomain.getCriteria(true));
		//2.开始信息采集
		int size=acquisitionRuleList.size();
		for(int t=0;t<size;t++){
			AcquisitionRule acquisitionRule=(AcquisitionRule)acquisitionRuleList.get(t);
			//分析采集信息规则1.获取信息列表页面的信息列表并记录实际的地址
			String url=acquisitionRule.getPageUrl();
			String startelement=acquisitionRule.getPageHrefStart();
			String pageCharset=acquisitionRule.getPageCharset();
			Columns columns=acquisitionRule.getColumns();
			if(pageCharset==null||pageCharset.equals("")){
				pageCharset="UTF-8";
			}
			List result=new ArrayList();
			if(startelement!=null&&!startelement.equals("")){//获取信息列表页面的信息列表
				System.out.println("==================获取信息列表页面的信息列表============");
				List hrefList=HtmlParserUtil.getHrefList(url, startelement,pageCharset);
				System.out.println("==================获取信息列表页面的信息列表====size========"+hrefList.size());
				for(int s=0;s<hrefList.size();s++){//处理所有的连接地址
					String hrefurl=hrefList.get(s).toString();
					//检查系统中是否已经记录相应的记录
					List list=acquisitionHistoryDomain.queryByCriteria(acquisitionHistoryDomain.getCriteria(null).add(Restrictions.eq("url", hrefurl)));
					if(list.size()<1){// 说明已经采集过，这里不做处理,<0说明没有采集过，这里要记录要采集的内容
						AcquisitionHistory acquisitionHistory=(AcquisitionHistory)acquisitionHistoryDomain.getBaseObject();
						acquisitionHistory.setUrl(hrefurl);
						this.addBaseInfoToObject(acquisitionHistory);
						//这里采集集体的网页内容信息
						String urlTemp=hrefList.get(s).toString();
						String titleStartTemp=acquisitionRule.getTitleStart();
						String cntentStartTemp=acquisitionRule.getContentStart();
						String title="";
						String content="";
						if(titleStartTemp!=null&&!titleStartTemp.equals("")){
							title=HtmlParserUtil.getParserContent(urlTemp, titleStartTemp,pageCharset);
						}
						if(cntentStartTemp!=null&&!cntentStartTemp.equals("")){
							content=HtmlParserUtil.getParserContent(urlTemp, cntentStartTemp,pageCharset);
						}
						if(!title.equals("")&&!content.equals("")){//标题和内容都存在的时候记录相应的信息
							acquisitionHistory.setTname(title);
							acquisitionHistoryDomain.save(acquisitionHistory);
							clobInfoDomain.save(acquisitionHistory.getId(), content);
							//如果关联的栏目信息，默认保存为待审核的栏目信息下面
							Article article=(Article)articleDomain.getBaseObject();
							article.setId(acquisitionHistory.getId());
							article.setTname(title);
							article.setColumnId(columns.getId());
							this.addBaseInfoToObject(article);
							article.setHits(0);
							articleDomain.save(article);
						}
						System.out.println("================title"+title);
						System.out.println("================content"+content);
						
					}
					
				
				}
			}else{//信息详细页面
				String titleStartTemp=acquisitionRule.getTitleStart();
				String contentStartTemp=acquisitionRule.getContentStart();
				String title="";
				String content="";
				String urls=acquisitionRule.getPageUrl();
				if(titleStartTemp!=null&&!titleStartTemp.equals("")){
					title=HtmlParserUtil.getParserContent(urls, titleStartTemp,pageCharset);
				}
				if(contentStartTemp!=null&&!contentStartTemp.equals("")){
					content=HtmlParserUtil.getParserContent(urls, contentStartTemp,pageCharset);
				}
				
			}
		}
		//3.把采集到信息进行入库
	}
	public void setAcquisitionRuleDomain(AcquisitionRuleDomain acquisitionRuleDomain) {
		this.acquisitionRuleDomain = acquisitionRuleDomain;
	}
	public void setAcquisitionHistoryDomain(
			AcquisitionHistoryDomain acquisitionHistoryDomain) {
		this.acquisitionHistoryDomain = acquisitionHistoryDomain;
	}
	public void setClobInfoDomain(ClobInfoDomain clobInfoDomain) {
		this.clobInfoDomain = clobInfoDomain;
	}
	public void setArticleDomain(ArticleDomain articleDomain) {
		this.articleDomain = articleDomain;
	}

}
