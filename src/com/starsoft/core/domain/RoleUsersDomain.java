package com.starsoft.core.domain;
import java.util.List;

import com.starsoft.core.domain.BaseDomain;

public interface RoleUsersDomain extends BaseDomain{
	/**
	 * 更新单个人员对应的角色
	 * @param menuid
	 * @param roleids
	 */
	public void updateRoleUsers(List<String> userId, String roleid,String organIds);
	/**
	 * 通过用户标识查询用户的角色标识
	 * @param menuid
	 * @param roleids
	 */
	public List<String> getRoleIdsByUserId(String userId);
	/**
	 * 通过角色标识查询用户的用户标识
	 * @param menuid
	 * @param roleids
	 */
	public List<String> getUserIdsByRoleId(String roleId);
	
}
