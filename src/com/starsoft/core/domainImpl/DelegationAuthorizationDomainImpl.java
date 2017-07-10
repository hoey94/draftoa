package com.starsoft.core.domainImpl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.starsoft.core.domain.DelegationAuthorizationDomain;
import com.starsoft.core.entity.DelegationAuthorization;
import com.starsoft.core.entity.RoleUsers;
@Service("delegationAuthorizationDomain")
@Transactional
public class DelegationAuthorizationDomainImpl extends BaseDomainImpl implements DelegationAuthorizationDomain{
	public DelegationAuthorizationDomainImpl(){
		this.setClassName("com.starsoft.core.entity.DelegationAuthorization");
	}

	@Override
	public List<String> queryRoleIdsByUserId(String userId) {
		List<DelegationAuthorization> dlist=(List<DelegationAuthorization>)this.queryByCriteria(this.getCriteria(true).add(Restrictions.eq("delegateTo", userId)));
		List<String> delegateFromIds=new ArrayList<String>();
		List<String> result=new ArrayList();
		for(DelegationAuthorization delegationAuthorization:dlist){
			delegationAuthorization.getDelegateFrom();
			delegateFromIds.add(delegationAuthorization.getDelegateFrom());
		}
		if(delegateFromIds.size()>0){
			List<RoleUsers> list=(List<RoleUsers>) this.queryByCriteria(DetachedCriteria.forClass(RoleUsers.class).add(Restrictions.in("usersId", delegateFromIds)));
			for(RoleUsers obj:list){
				result.add(obj.getRoleId());
			}
		}
		return result;
	}
	
}
