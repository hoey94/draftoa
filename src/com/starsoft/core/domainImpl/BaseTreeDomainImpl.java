package com.starsoft.core.domainImpl;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.starsoft.core.domain.BaseTreeDomain;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.entity.BaseTreeObject;
import com.starsoft.core.exception.OperateException;
@Transactional
public abstract class BaseTreeDomainImpl extends BaseDomainImpl implements BaseTreeDomain {
	/****
	 * 获取最大的排序号码+1
	 */
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public Integer getMaxSortCode() {
		StringBuffer hql = new StringBuffer();
		hql.append("select max(tmp.sortCode) from "+ getBaseObject().getClass().getName()+" as tmp ");
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
	/****
	 * 根据属性获取最大的排序号码+1
	 */
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public Integer getMaxSortCodeByProperty(String propertyName,String propertyValue) {
		StringBuffer hql = new StringBuffer();
		hql.append("select max(tmp.sortCode) from "+ getBaseObject().getClass().getName()+" as tmp ");
		hql.append("where tmp."+propertyName+"='"+propertyValue+"'");
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
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public DetachedCriteria getCriteria(Boolean valid,Boolean sortCode) {
		DetachedCriteria criteria=getCriteria(valid);
		if(sortCode!=null){
			if(sortCode){
				criteria.addOrder(Order.desc("sortCode"));
			}else{
				criteria.addOrder(Order.asc("sortCode"));
			}
		}else{
			criteria.addOrder(Order.desc("id"));
		}
		return criteria;
	}
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public List<? extends BaseObject> queryByParentId(String parentId, Boolean valid,Boolean order) {
		DetachedCriteria criteria=this.getCriteria(valid,order);
		if(parentId!=null){
			criteria.add(Restrictions.eq("parentId", parentId));
		}else{
			criteria.add(Restrictions.isNull("parentId"));
		}
		
		return this.queryByCriteria(criteria);
	}
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public List<BaseTreeObject> queryTreeByParentId(String parentId,Integer level,Boolean valid,Boolean order) {
		List list=this.queryByParentId(parentId,valid,order);
		List<BaseTreeObject> result=new ArrayList<BaseTreeObject>();
		Integer newlevel=level-1;
		for(int t=0;t<list.size();t++){
			BaseTreeObject obj=(BaseTreeObject) list.get(t);
			if(level>0){
				obj.setSubset(this.queryTreeByParentId(obj.getId(),newlevel,valid,order));
			}
			result.add(obj);
		}
		return result;
	}
	/***
	 * 查询以一个节点为根的指定层级的树形结构，并返回该根节点
	 */
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public BaseTreeObject queryTreeByBaseObjectId(String baseObjectId,Integer level,Boolean valid,Boolean order) {
		BaseTreeObject result=(BaseTreeObject) this.query(baseObjectId);
		if(result!=null){
			result.setSubset(this.queryTreeByParentId(baseObjectId, level, valid, order));
		}
		return result;
	}
	/***
	 * 查询下级(所有)节点,包含选择的对象,不包含扣除的节点对象
	 * @param baseObjectId 根节点
	 * @param notContainId 扣除节点
	 * @param level查询级别
	 * @param valid
	 * @param order sortCode 按排序的方式  null 不按排序号排序，true 按desc 降序排列 排序  false按 ase 升序 排序
	 * @return
	 */
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public List<BaseTreeObject> queryTreeByBaseObjectIdNotContainId(String baseObjectId,String notContainId,Integer level,Boolean valid,Boolean order){
		List result=new ArrayList();
		BaseTreeObject baseTree=(BaseTreeObject) this.query(baseObjectId);
		baseTree.setSubset(this.queryTreeByParentIdAndNotContainId(baseObjectId,notContainId,level, valid, order));
		result.add(baseTree);
		return result;
	}
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	private List<BaseTreeObject> queryTreeByParentIdAndNotContainId(String parentId,String notContainId,Integer level,Boolean valid,Boolean order) {
		List list=this.queryByParentIdAndNotContainId(parentId,notContainId,valid,order);
		List<BaseTreeObject> result=new ArrayList<BaseTreeObject>();
		Integer newlevel=level-1;
		for(int t=0;t<list.size();t++){
			BaseTreeObject obj=(BaseTreeObject) list.get(t);
			if(level>0){
				obj.setSubset(this.queryTreeByParentIdAndNotContainId(obj.getId(),notContainId,newlevel,valid,order));
			}
			result.add(obj);
		}
		return result;
	}
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	private List<? extends BaseObject> queryByParentIdAndNotContainId(String parentId,String notContainId,Boolean valid,Boolean order) {
		DetachedCriteria criteria=this.getCriteria(valid,order);
		if(parentId!=null){
			criteria.add(Restrictions.eq("parentId", parentId));
		}else{
			criteria.add(Restrictions.isNull("parentId"));
		}
		if(notContainId!=null){
			criteria.add(Restrictions.ne("id", notContainId));
		}
		return this.queryByCriteria(criteria);
	}
}
