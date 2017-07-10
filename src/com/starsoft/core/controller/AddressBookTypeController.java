package com.starsoft.core.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.web.servlet.ModelAndView;
import com.starsoft.core.domain.AddressBookDomain;
import com.starsoft.core.domain.AddressBookTypeDomain;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.entity.BaseTreeObject;
import com.starsoft.core.entity.Users;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.Page;
import com.starsoft.core.util.WEBCONSTANTS;

public class AddressBookTypeController extends BaseTreeController implements BaseInterface {
	private AddressBookDomain addressBookDomain;
	private AddressBookTypeDomain addressBookTypeDomain;
	/***
	 * 公共的列表方法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView subframetree(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model = new HashMap<String,Object>();
		BaseObject loginUser=HttpUtil.getLoginUser(request);
		String parentId=HttpUtil.getString(request, "parentId", loginUser.getId());
		List list=new ArrayList();
		if(!parentId.equals(rootValue)){
			BaseTreeObject topNode=(BaseTreeObject) baseDomain.query(parentId);
			if(topNode!=null){
				topNode.setParentId(rootValue);
				list.add(topNode);
			}else{
				topNode=(BaseTreeObject)baseDomain.getBaseObject();
				topNode.setParentId(rootValue);
				topNode.setTname("我的分组");
				topNode.setId(parentId);
				list.add(topNode);
			}
		}
		List lists= addressBookTypeDomain.queryTreeByParentId(parentId,5,null,false);
		list.addAll(lists);
		model.put("list", list);
		model.put("title", "通讯录组");
		model.put("deleteAndAdd", "true");// 添加节点和删除节点的权利
		model.put("rightContent", "addressBook.do?action=list&sortfield=sortCode&sortvalue=false");
		model.put("urlLink", "addressBook.do?action=list&addressBookTypeId=");
		model.put("addLink", "?action=add&parentId=");
		return new ModelAndView("baseframe/subframetree",model);
	}
	/***
	 * 公共的列表方法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model=HttpUtil.convertModel(request);
		Page page=HttpUtil.convertPage(request);
		DetachedCriteria criteria = this.convertCriteria(request);
		List list= baseDomain.queryByCriteria(criteria, page);
		model.put("list", list);
		model.put("page", page);
		return new ModelAndView(this.getListPage(),model);
	}
	/***
	 * 公共的编辑方法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView read(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView model=this.edit(request, response);
		return new ModelAndView(this.getReadPage(),model.getModel());
	}
	/***
	 * 公共的新建方法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView add(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model = new HashMap<String,Object>();
		return new ModelAndView(this.getAddPage(),model);
	}
	/***
	 * 公共的编辑方法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView edit(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String id=HttpUtil.getString(request, "id", "");
		int page=HttpUtil.getInt(request, "page", 0);
		Map<String,Object> model = new HashMap<String,Object>();
		BaseObject obj=(BaseObject) baseDomain.query(id);
		if(obj!=null){
			model.put("obj", obj);
			model.put("page", page);
		}else{
			model.put("msg", "编辑的信息不存在!可能已经被删除!");
			return new ModelAndView(CloseMessagePage,model);
		}
		return new ModelAndView(this.getEditPage(),model);
	}
	/***
	 * 保存
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void save(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String method=request.getMethod().toLowerCase();
		String gotourl=request.getRequestURI();
		if("post".equals(method)){
			try{
				BaseTreeObject entity=(BaseTreeObject)baseDomain.getBaseObject();
				this.bind(request, entity);
				this.saveBaseInfoToObject(request, entity);
				entity.setParentId(entity.getCreateId());
				Users user=HttpUtil.getLoginUser(request);
				entity.setSortCode(addressBookTypeDomain.getMaxSortCodeByProperty("parentId", user.getId()));
				this.baseDomain.save(entity);
				this.outSuccessString(request,response, gotourl);
			}catch(Exception e){
				e.getStackTrace();
				this.outFailString(request,response, WEBCONSTANTS.FAILINFOR,gotourl);
			}
		}else{
			this.outFailString(request,response, WEBCONSTANTS.FAILINFOR,gotourl);
		}
	}
	/***
	 * 更新
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void update(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String id=HttpUtil.getString(request, "id", "");
		int page=HttpUtil.getInt(request, "page", 0);
		String method=request.getMethod().toLowerCase();
		String gotourl=request.getRequestURI()+"?page="+page;
		if("post".equals(method)){
			try{
				BaseObject entity=(BaseObject) baseDomain.query(id);
				if(entity!=null){
					this.bind(request, entity);
				}
				this.baseDomain.update(entity);
				this.outSuccessString(request,response, gotourl);
			}catch(Exception e){
				e.getStackTrace();
				this.outFailString(request,response, WEBCONSTANTS.FAILINFOR,gotourl);
			}
		}else{
			this.outFailString(request,response, WEBCONSTANTS.FAILINFOR,gotourl);
		}
	}
	/**
	 * 彻底删除对象功能
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@Override
	public void delete(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String ids=HttpUtil.getString(request, "ids", "");
		String method=request.getMethod().toLowerCase();
		if("post".equals(method)&&ids.length()>0){
				List<String> ides=HttpUtil.getList(request, "ids", ";");
				String msg="";
				for(String id:ides){
					List list=addressBookDomain.queryByProperty("addressBookTypeId",id);
					if(list.size()>0){
						msg=msg+"数据已经被使用，不能删除!"+id+"\n";
					}else{
						BaseObject obj=(BaseObject)this.baseDomain.query(id);
						if(obj!=null){
							this.baseDomain.delete(obj);
						}
					}
				}
				this.outSuccessString(request,response,msg);
				
		}else{
			this.outFailString(request,response, "对不起您没有删除权限 !","");
		}
	}
	public void setAddressBookDomain(AddressBookDomain addressBookDomain) {
		this.addressBookDomain = addressBookDomain;
	}
	public void setAddressBookTypeDomain(AddressBookTypeDomain addressBookTypeDomain) {
		this.addressBookTypeDomain = addressBookTypeDomain;
		this.setBaseTreeDomain(addressBookTypeDomain);
	}
}
