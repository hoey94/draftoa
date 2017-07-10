package com.starsoft.cms.domainImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.transform.ResultTransformer;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.starsoft.cms.domain.IndexSearchDomain;
import com.starsoft.cms.entity.IndexSearch;
import com.starsoft.core.util.DateUtil;
import com.starsoft.core.util.Page;
import com.starsoft.core.util.ReflectionUtils;
@Service("indexSearchDomain")
@Transactional
public class IndexSearchDomainImpl extends HibernateDaoSupport implements IndexSearchDomain {

	/***
	 * 保存对象
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void save(IndexSearch entity) {
		this.getHibernateTemplate().save(entity);
	}
	@Override
	public void deletes(final String baseObjectIds) {
		final String hql="delete from "+IndexSearch.class.getName()+" as t where t.baseObjectId = (:id)";
		getHibernateTemplate().execute(new HibernateCallback() { 
			 public Object doInHibernate(Session session) throws HibernateException {
				 Query query = session.createQuery(hql);
				 query.setParameter("id", baseObjectIds);
                 return query.executeUpdate();
            } 
		 }); 

	}
	@Override
	public void deletes(final List<String> baseObjectIds) {
		final String hql="delete from "+IndexSearch.class.getName()+" as t where t.baseObjectId in (:ids)";
		getHibernateTemplate().execute(new HibernateCallback() { 
			 public Object doInHibernate(Session session) throws HibernateException {
				 Query query = session.createQuery(hql);
				 query.setParameterList("ids", baseObjectIds);
                 return query.executeUpdate();
            } 
		 }); 

	}

	@Override
	public List queryByCriteria(final String key,final String startDate,
			final String endDate,final Page page) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				DetachedCriteria detachedCriteria=DetachedCriteria.forClass(IndexSearch.class);
				detachedCriteria.addOrder(Order.desc("createTime"));
				detachedCriteria.add(Restrictions.or(Restrictions.like("tname", key, MatchMode.ANYWHERE), Restrictions.like("searchKey", key, MatchMode.ANYWHERE)));
				if(startDate!=null&&!startDate.equals("")){
					detachedCriteria.add(Restrictions.le("createTime", DateUtil.parseDate(startDate)));
				}
				if(endDate!=null&&!endDate.equals("")){
					detachedCriteria.add(Restrictions.lt("createTime", DateUtil.parseDate(endDate)));
				}
				Criteria criteria = detachedCriteria.getExecutableCriteria(session);
				int totalCount = countCriteriaResult(criteria);
				page.setTotalCount(totalCount);
				criteria.setProjection(null);
				criteria.setCacheable(true);
				criteria.setResultTransformer(Criteria.ROOT_ENTITY);
				criteria.setFirstResult(page.getFirstResult());
				criteria.setMaxResults(page.getPageSize());
				return criteria.list();
			}
		});
	}
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	protected int countCriteriaResult(Criteria c) {
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
}
