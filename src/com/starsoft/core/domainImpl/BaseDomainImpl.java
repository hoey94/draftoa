package com.starsoft.core.domainImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.transform.ResultTransformer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.starsoft.core.domain.BaseDomain;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.exception.OperateException;
import com.starsoft.core.util.Page;
import com.starsoft.core.util.ReflectionUtils;
import com.starsoft.core.util.StringUtil;
@Transactional
public abstract class BaseDomainImpl extends HibernateDaoSupport implements BaseDomain {
	protected JdbcTemplate jdbcTemplate;
	/**
	 * 实体类全名称
	 */
	private String className;
	/***
	 * 保存对象
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void save(BaseObject entity){
		this.getHibernateTemplate().save(entity);
	}
	/***
	 * 保存多个对象
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void save(List<BaseObject> entitys){
		for(int t=0;t<entitys.size();t++){
			this.getHibernateTemplate().save(entitys.get(t));
			if(t%50==0){
				this.getSession().flush();
				this.getSession().clear();
			}
		}
	}
	/**
	 *更新对象
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void update(BaseObject entity){
		this.getHibernateTemplate().update(entity);
	}
	/**
	 * 删除对象
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(BaseObject entity){
		this.getHibernateTemplate().delete(entity);
	}
	/***
	 * 批量删除对象
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void deletes(final List<String> ids) {
		final String hql="delete from "+this.getClassName()+" as t where t.id in (:ids)";
		getHibernateTemplate().execute(new HibernateCallback() { 
			 public Object doInHibernate(Session session) throws HibernateException {
				 Query query = session.createQuery(hql);
				 query.setParameterList("ids", ids);
                 return query.executeUpdate();
            } 
		 }); 
	}
	/***
	 * 批量删除对象
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void deletes(final String propertyName,final List<String> ids) {
		final String hql="delete from "+this.getClassName()+" as t where t."+propertyName+" in (:ids)";
		getHibernateTemplate().execute(new HibernateCallback() { 
			 public Object doInHibernate(Session session) throws HibernateException {
				 Query query = session.createQuery(hql);
				 query.setParameterList("ids", ids);
                 return query.executeUpdate();
            } 
		 }); 
	}
	/***
	 * 批量禁用对象
	 */
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void markdeletes(final List<String> ids) {
		final String hql = "update "+this.getClassName()+" as t set t.valid=false where t.id in (:ids)";
		getHibernateTemplate().execute(new HibernateCallback() { 
			 public Object doInHibernate(Session session) throws HibernateException {
				 Query query = session.createQuery(hql);
				 query.setParameterList("ids", ids);
                 return query.executeUpdate();
           } 
		 }); 
	}
	/**
	 * 查询所有的数据
	 */
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public List<? extends BaseObject> queryByCriteria(DetachedCriteria criteria) {
		return this.getHibernateTemplate().findByCriteria(criteria);
	}
	/**
	 * 取得实体类型
	 */
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public BaseObject getBaseObject()  {
		try {
			return (BaseObject) Class.forName(this.getClassName()).newInstance();
		} catch (Exception e) {
			throw new OperateException("获取对象出错!对象名称:"+this.getClassName());
		} 
	}
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public Object query(String id) {
		return this.getHibernateTemplate().get(this.getBaseObject().getClass(), id);
	}
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public String queryTnameById(String id) {
		if(!StringUtil.isNullOrEmpty(id)){
			BaseObject obj=this.getHibernateTemplate().get(this.getBaseObject().getClass(), id);
			if(obj!=null){
				return obj.getTname();
			}else{
				return "";
			}
		}else{
			return "";
		}
	}
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public List<? extends BaseObject> queryAll() {
		return this.queryByCriteria(this.getCriteria(null));
	}
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	private int countCriteriaResult(Criteria c) {
		CriteriaImpl impl = (CriteriaImpl) c;
		Projection projection = impl.getProjection();
		ResultTransformer transformer = impl.getResultTransformer();
		List orderEntries = null;
		try {
			orderEntries = (List) ReflectionUtils.getFieldValue(impl,
					"orderEntries");
			ReflectionUtils
					.setFieldValue(impl, "orderEntries", new ArrayList());
		} catch (Exception e) {
			this.logger.info("不可能抛出的异常:{}");
		}
		Integer totalCountObject = (Integer) c.setProjection(Projections.count("id"))
				.uniqueResult();
		int totalCount = (totalCountObject != null) ? totalCountObject.intValue(): 0;
		c.setProjection(projection);
		if (projection == null)
			c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		if (transformer != null)
			c.setResultTransformer(transformer);
		try {
			ReflectionUtils.setFieldValue(impl, "orderEntries", orderEntries);
		} catch (Exception e) {
			this.logger.info("不可能抛出的异常:{}");
		}
		return totalCount;
	}
	/**
	 * 查询指定的数据量列表，不进行分页操作，提高性能
	 * @param detachedCriteria
	 * @return
	 */
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public List  queryByCriteria(final DetachedCriteria detachedCriteria,final int maxSize){
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Criteria criteria = detachedCriteria.getExecutableCriteria(session);
				criteria.setProjection(null);
				criteria.setCacheable(true);
				criteria.setResultTransformer(Criteria.ROOT_ENTITY);
				criteria.setFirstResult(0);
				criteria.setMaxResults(maxSize);
				return criteria.list();
			}
		});
	}
	/**
	 * 分页查询
	 */
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public List<BaseObject> queryByCriteria(final DetachedCriteria detachedCriteria, final Page page) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Criteria criteria = detachedCriteria.getExecutableCriteria(session);
//				criteria.setCacheable(true);
				int totalCount = countCriteriaResult(criteria);
				page.setTotalCount(totalCount);
				criteria.setProjection(null);
				criteria.setResultTransformer(Criteria.ROOT_ENTITY);
				criteria.setFirstResult(page.getFirstResult());
				criteria.setMaxResults(page.getPageSize());
				return criteria.list();
			}
		});
	}
	/***
	 * 通过HQL查询有
	 * @param hql
	 * @param page
	 * @return
	 */
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public List queryByHql(final String hql,final  List parameters, Page page) { 
		String afterfromhql=hql.substring(hql.indexOf("from")+4).trim();
		String counthql="select count(id) from ";
		if(afterfromhql.indexOf("order by")>0){
			afterfromhql=afterfromhql.substring(0,afterfromhql.indexOf("order by"));
		}
		if(afterfromhql.indexOf(" ")>-1&&afterfromhql.indexOf(" as")>-1&&afterfromhql.indexOf(" ")==afterfromhql.indexOf(" as")){//判断实体类是否取别名
			String alisname=afterfromhql.substring(afterfromhql.indexOf(" as")+3).trim();
			if(alisname.indexOf(",")>-1&&alisname.indexOf(",")<alisname.indexOf(" ")){
				alisname=alisname.substring(0,alisname.indexOf(","));
				counthql="select count("+alisname+".id) from "+afterfromhql;
			}else{
				counthql="select count(id) from "+afterfromhql;
			}
			
		}else{
			counthql=counthql+afterfromhql;
		}
		Query query = getSession().createQuery(counthql);
		if(parameters!=null && parameters.size()>0){ 
			for (int i = 0; i < parameters.size(); i++){
				query.setParameter(i, parameters.get(i));
			}
		}
		List list=query.list();
		int listsize=list.size();
		Integer totalCount=listsize;
		if(list==null||listsize==0){
			page.setTotalCount(0);
			return new ArrayList();
		}
		if(listsize==1){
			totalCount=Integer.valueOf(list.get(0).toString());
		}else{
			totalCount=0;
			for(int t=0;t<listsize;t++){
				totalCount=totalCount+Integer.valueOf(list.get(t).toString());
			}
		}
		if(totalCount<page.getPageSize()&&totalCount>0){
			page.setPageSize(totalCount);
		}
		page.setTotalCount(totalCount);
		if(totalCount==0){
			page.setTotalCount(0);
			return new ArrayList();
		}
		Query querylist = getSession().createQuery(hql);
		 if(parameters!=null && parameters.size()>0){ 
			 for (int i = 0; i < parameters.size(); i++){
				 querylist.setParameter(i, parameters.get(i));
			 }
		 }
		 querylist.setFirstResult(page.getFirstResult()); 
        return querylist.setMaxResults(page.getPageSize()).list(); 
	}
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public DetachedCriteria getCriteria(Boolean valid) {
		DetachedCriteria criteria=DetachedCriteria.forClass(getBaseObject().getClass());
		if(valid!=null){//等于null时不过滤
			criteria.add(Restrictions.eq("valid", valid));
		}
		return criteria;
	}
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public List<? extends BaseObject> queryByProperty(String propertyName, String propertyValue) {
		DetachedCriteria criteria=DetachedCriteria.forClass(getBaseObject().getClass());
		criteria.add(Restrictions.eq(propertyName, propertyValue));
		criteria.addOrder(Order.desc("id"));
		return this.queryByCriteria(criteria);
	}
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public List<? extends BaseObject> queryByProperty(String propertyName, List propertyValues) {
		DetachedCriteria criteria=DetachedCriteria.forClass(getBaseObject().getClass());
		criteria.add(Restrictions.in(propertyName, propertyValues));
		criteria.addOrder(Order.desc("id"));
		return this.queryByCriteria(criteria);
	}
	/***
	 * 待优化
	 */
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public BaseObject queryFirstByProperty(String propertyName, String propertyValue) {
		DetachedCriteria criteria=DetachedCriteria.forClass(getBaseObject().getClass());
		criteria.add(Restrictions.eq(propertyName, propertyValue));
		criteria.addOrder(Order.desc("id"));
		List list=this.queryByCriteria(criteria,1);
		if(list.size()>0){
			return (BaseObject)list.get(0);
		}
		return null;
	}
	/***
	 * 待优化
	 */
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public BaseObject queryFirstByProperty(Class cls,String propertyName, String propertyValue) {
		DetachedCriteria criteria=DetachedCriteria.forClass(cls);
		criteria.add(Restrictions.eq(propertyName, propertyValue));
		criteria.addOrder(Order.desc("id"));
		List list=this.queryByCriteria(criteria,1);
		if(list.size()>0){
			return (BaseObject)list.get(0);
		}
		return null;
	}
	
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public List<BaseObject> queryByProperty(Class cls, String propertyName,
			String propertyValue) {
		return this.getHibernateTemplate().findByCriteria(DetachedCriteria.forClass(cls).add(Restrictions.eq(propertyName, propertyValue)))	;
	}
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	@Transactional(propagation = Propagation.REQUIRED)
	public void executeByHQL(final String hql, final List parameters) {
		 getHibernateTemplate().execute(new HibernateCallback() { 
			 public Object doInHibernate(Session session) throws HibernateException {
				 Query query = session.createQuery(hql);
				 if(parameters!=null && parameters.size()>0){ 
					 for (int i = 0; i < parameters.size(); i++){
						 query.setParameter(i, parameters.get(i));
					 }
				 }
                 return query.executeUpdate();
             } 
		 }); 
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteAndSaveAndUpdate(List<BaseObject> deleteList,List<BaseObject> saveList,List<BaseObject> updateList){
		int t=0;
		if(deleteList!=null&&deleteList.size()>0){
			for(BaseObject obj:deleteList){
				this.getHibernateTemplate().delete(obj);
				t++;
				if(t%50==0){
					this.getSession().flush();
					this.getSession().clear();
				}
			}
		}
		if(saveList!=null&&saveList.size()>0){
			for(BaseObject obj:saveList){
				this.getHibernateTemplate().save(obj);
				t++;
				if(t%50==0){
					this.getSession().flush();
					this.getSession().clear();
				}
			}
		}
		if(updateList!=null&&updateList.size()>0){
			for(BaseObject obj:updateList){
				this.getHibernateTemplate().update(obj);
				t++;
				if(t%50==0){
					this.getSession().flush();
					this.getSession().clear();
				}
			}
		}
	}
	/***
	 * 通过SQL查询有
	 * @param Sql
	 * @return
	 */
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public List queryBySql(final String sql,final  List parameters) { 
		SQLQuery querylist = getSession().createSQLQuery(sql);
		 if(parameters!=null && parameters.size()>0){ 
			 for (int i = 0; i < parameters.size(); i++){
				 querylist.setString(i, parameters.get(i).toString());
			 }
		 }
		 List list=querylist.list();
        return list; 
	}
	/***
	 * 执行sql语句,update 或者delete
	 * @param sql
	 * @param parameters
	 */
	public void executeBySQL(final String sql, final  List parameters){
		SQLQuery query = getSession().createSQLQuery(sql);
		 if(parameters!=null && parameters.size()>0){ 
			 for (int i = 0; i < parameters.size(); i++){
				 query.setParameter(i, parameters.get(i));
			 }
		 }
        query.executeUpdate();
	}
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public List statisticsByHql(final String hql,Object[] objs){
		List list=this.getHibernateTemplate().find(hql, objs);
		return list;
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

}
