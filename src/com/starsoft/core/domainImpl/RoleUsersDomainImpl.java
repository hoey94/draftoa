package com.starsoft.core.domainImpl;

import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.User;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.starsoft.core.domain.RoleUsersDomain;
import com.starsoft.core.domain.UsersDomain;
import com.starsoft.core.entity.Role;
import com.starsoft.core.entity.RoleUsers;
import com.starsoft.core.entity.Users;
@Service("roleUsersDomain")
@Transactional
public class RoleUsersDomainImpl extends BaseDomainImpl implements RoleUsersDomain{
	@Autowired
	private UsersDomain usersDomain;
	public RoleUsersDomainImpl(){
		this.setClassName("com.starsoft.core.entity.RoleUsers");
	}

	public void updateRoleUsers(List<String> userId, String roleid,String organIds) {
		List list=new ArrayList();
		if(!roleid.equals("")){
		List parameters=new ArrayList();
		parameters.add(roleid);
		parameters.add(organIds);
		String sql="SELECT id FROM t_core_roleusers as role WHERE role.roleId = ? AND role.usersId in(select id from t_core_user as users where users.organId=?)";
		list=this.queryBySql(sql, parameters);
		List<String> idList = new ArrayList<String>();
		for(int i=0; i<list.size();i++)
		{
			idList.add((String)list.get(i));
		}if (list.size()>0) {
			this.deletes(idList);
		}
		}
		
			if(!"".equals(userId)){
				List roleUsersList =new  ArrayList();
				for(int t=0;t<userId.size();t++){
					Users user = (Users) usersDomain.query(userId.get(t));
					if(user!=null)
					{
						RoleUsers roleUsers=(RoleUsers) this.getBaseObject();
						roleUsers.setTname(user.getTname());
						roleUsers.setUsersId(userId.get(t));
						roleUsers.setRoleId(roleid);
						roleUsers.setValid(true);
						roleUsersList.add(roleUsers);
					}
				}
				this.save(roleUsersList);
			}
		 

	}
	/**
	 * 通过用户标识查询用户的角色标识
	 * @param menuid
	 * @param roleids
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public List<String> getRoleIdsByUserId(String userId){
		List<String> result=new ArrayList();
		List<RoleUsers> list=(List<RoleUsers>) this.queryByCriteria(this.getCriteria(true).add(Restrictions.eq("usersId", userId)));
		for(RoleUsers obj:list){
			result.add(obj.getRoleId());
		}
		return result;
	}
	/**
	 * 通过角色标识查询用户的用户标识
	 * @param menuid
	 * @param roleids
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public List<String> getUserIdsByRoleId(String roleId){
		List<String> result=new ArrayList();
		List<RoleUsers> list=(List<RoleUsers>) this.queryByCriteria(this.getCriteria(true).add(Restrictions.eq("roleId", roleId)));
		for(RoleUsers obj:list){
			result.add(obj.getUsersId());
		}
		return result;
	}
}
