package com.starsoft.core.domainImpl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.starsoft.core.domainImpl.BaseDomainImpl;

import org.springframework.transaction.annotation.Transactional;

import com.starsoft.core.domain.ResourceDomain;
import com.starsoft.core.entity.Resource;
@Service("resourceDomain")
@Transactional
public class ResourceDomainImpl extends BaseDomainImpl implements ResourceDomain{
	public ResourceDomainImpl(){
		this.setClassName("com.starsoft.core.entity.Resource");
	}
	/***
	 * 通过关联对象查找所有对应的资源
	 */
	@Override
	public List<Resource> queryByLinkId(String baseObjectId) {
		DetachedCriteria criteria=this.getCriteria(true);
		criteria.add(Restrictions.eq("baseObjectId", baseObjectId));
		criteria.addOrder(Order.desc("id"));
		return this.getHibernateTemplate().findByCriteria(criteria);
	}
	
}
