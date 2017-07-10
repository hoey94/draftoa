package com.starsoft.cms.domainImpl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.starsoft.cms.domain.ArticleDomain;
import com.starsoft.cms.entity.Article;
import com.starsoft.core.domainImpl.BaseDomainImpl;
import com.starsoft.core.entity.ClobInfo;
import com.starsoft.core.util.Page;
@Service("articleDomain")
public class ArticleDomainImpl extends BaseDomainImpl implements ArticleDomain{
	public ArticleDomainImpl(){
		this.setClassName("com.starsoft.cms.entity.Article");
	}
	@Override
	@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
	public DetachedCriteria getCriteria(Boolean valid) {
		DetachedCriteria criteria=DetachedCriteria.forClass(getBaseObject().getClass());
		if(valid!=null){//等于null时不过滤
			criteria.add(Restrictions.eq("valid", valid));
		}
		return criteria;
	}
	@Override
	public List<Article> queryByColumnsIdAndSize(String columnId,
			int articleSize) {
		 DetachedCriteria criteria=this.getCriteria(true);
		 criteria.add(Restrictions.eq("auditState", true));
		 criteria.add(Restrictions.eq("columnId", columnId));
		 criteria.addOrder(Order.desc("publishTime"));
		 return this.queryByCriteria(criteria, articleSize);
	}
	/***
	 * 批量删除对象
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void deletes(final List ids) {
		final String hql="delete from "+this.getClassName()+" as t where t.id in (:ids)";
		final String infohql="delete from "+ClobInfo.class.getName()+" as t where t.id in (:ids)";
		getHibernateTemplate().execute(new HibernateCallback() { 
			 public Object doInHibernate(Session session) throws HibernateException {
				 Query query = session.createQuery(hql);
				 query.setParameterList("ids", ids);
                 return query.executeUpdate();
            } 
		 }); 
		getHibernateTemplate().execute(new HibernateCallback() { 
			 public Object doInHibernate(Session session) throws HibernateException {
				 Query query = session.createQuery(infohql);
				 query.setParameterList("ids", ids);
                return query.executeUpdate();
           } 
		 });
	}


	@Override
	public List findLimiteByCriteria(DetachedCriteria criteriaqyzp, int i) {
		// TODO Auto-generated method stub
		return null;
	}

}
