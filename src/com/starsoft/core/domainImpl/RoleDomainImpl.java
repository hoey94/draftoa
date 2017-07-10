package com.starsoft.core.domainImpl;



import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.starsoft.core.domain.RoleDomain;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.entity.RoleUsers;
@Service("roleDomain")
public class RoleDomainImpl extends BaseDomainImpl implements RoleDomain {
	/**
	 * 删除对象
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(BaseObject entity){
		String deleteRoleUserHql="delete from "+RoleUsers.class.getName()+" where roleId=?";
		List params=new ArrayList();
		params.add(entity.getId());
		this.executeByHQL(deleteRoleUserHql, params);
		this.getHibernateTemplate().delete(entity);
	}
	public RoleDomainImpl(){
		this.setClassName("com.starsoft.core.entity.Role");
	}

}
