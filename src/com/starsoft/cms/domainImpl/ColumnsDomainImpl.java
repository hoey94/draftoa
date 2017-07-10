package com.starsoft.cms.domainImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.starsoft.cms.domain.ColumnsDomain;
import com.starsoft.cms.entity.Article;
import com.starsoft.cms.entity.Columns;
import com.starsoft.core.domainImpl.BaseTreeDomainImpl;
import com.starsoft.core.exception.OperateException;
@Service("columnsDomain")
public class ColumnsDomainImpl extends BaseTreeDomainImpl implements ColumnsDomain{
//	public List<Columns> queryColumnsTreeByParentId(String parentId,Boolean valid) {
//		DetachedCriteria criteria = this.getCriteria(valid, false);
//		criteria.add(Restrictions.eq("parentId", parentId));
//		List list=this.queryByCriteria(criteria);
//		List<Columns> result=new ArrayList<Columns>();
//		for(int t=0;t<list.size();t++){
//			Columns columns=(Columns) list.get(t);
//			result.add(columns);
//			result.addAll(queryColumnsByParentId(columns.getId(),valid));
//		}
//		return result;
//	}
	public List<Columns> queryColumnsByParentId(String parentId, Boolean valid) {
		List<Columns> result=new ArrayList<Columns>();
		if(parentId==null||"".equals(parentId)){
			parentId="11111111111111111111111111111111";
		}
		List results=this.queryByParentId(parentId, valid,false);
		for(int t=0;t<results.size();t++){
			Columns columns=(Columns) results.get(t);
			result.add(columns);
			result.addAll(queryColumnsByParentId(columns.getId(),valid));
		}
		return result;
	}
	@Override
	public int getMaxArticleSortCodeByColumnId(String columnId) {
		StringBuffer hql = new StringBuffer();
		hql.append("select max(tmp.sortCode) from "+ Article.class.getName()+" as tmp ");
		List list = getHibernateTemplate().find(hql.toString());
		if(list!=null&&list.size()>0){
			if(list.get(0)!=null){
			    try{
			    	return Integer.parseInt(list.get(0).toString())+1;
			    }catch(Exception e){
			    	throw new OperateException("获取最大排序号出错!数字值:");
			    }
			}else{
				return 1;
			}
		}else{
			return 1;
		}
	}
	public ColumnsDomainImpl(){
		this.setClassName("com.starsoft.cms.entity.Columns");
	}
}
