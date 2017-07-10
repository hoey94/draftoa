package com.starsoft.core.domainImpl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.starsoft.core.domain.GroupUsersDomain;
import com.starsoft.core.entity.GroupUsers;
@Service("groupUsersDomain")
@Transactional
public class GroupUsersDomainImpl extends BaseDomainImpl implements GroupUsersDomain{
	public GroupUsersDomainImpl(){
		this.setClassName("com.starsoft.core.entity.GroupUsers");
	}
	/**
	 * 通过用户标识查询用户的角色标识
	 * @param menuid
	 * @param roleids
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public List<String> getGroupIdsByUserId(String userId){
		List<String> result=new ArrayList<String>();
		List<GroupUsers> list=(List<GroupUsers>) this.queryByCriteria(this.getCriteria(true).add(Restrictions.eq("usersId", userId)));
		for(GroupUsers obj:list){
			result.add(obj.getGroupId());
		}
		return result;
	}
	/**
	 * 通过用户标识查询用户的角色标识
	 * @param menuid
	 * @param roleids
	 */
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public List<String> getUserIdsByGroupId(String groupId){
		List<String> result=new ArrayList<String>();
		List<GroupUsers> list=(List<GroupUsers>) this.queryByCriteria(this.getCriteria(true).add(Restrictions.eq("groupId", groupId)));
		for(GroupUsers obj:list){
			result.add(obj.getUsersId());
		}
		return result;
	}
	
}
