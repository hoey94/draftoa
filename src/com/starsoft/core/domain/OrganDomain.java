package com.starsoft.core.domain;

import java.util.List;

import com.starsoft.core.entity.Users;


public interface OrganDomain extends BaseTreeDomain{
	/**
	 * 查询部门管理员(特别设置的/继承的不算)
	 * @param organId
	 * @return
	 */
	public List<Users> queryOrganAdministrator(String organId);
	/****
	 * 更新部门管理员
	 * @param organId
	 */
	public void updateOrganAdministrator(String organId,List<String> userIds);
	/***
	 * 获取部门的层级路径
	 * @param organId
	 * @return
	 */
	public List<String> queryParentIdPath(String organId);
	/***
	 * 查询用户可以管理的顶级部门
	 * @param userId
	 * @return
	 */
	public String queryTopAdministratorOrgan(String userId);
	/****
	 * 检查用户时候具备修改一个用户的权利
	 * @param organId
	 * @param userId
	 * @return
	 */
	public boolean canEditOrgan(String organId,String userId);
	/***
	 * 查询一个部门下面所有的用户信息
	 * @param organId
	 * @return
	 */
	public List<Users> queryAllUsersByTopOrganId(String organId);
	/***
	 * 查询多个部门下面所有的用户信息
	 * @param organIds
	 * @return
	 */
	public List<Users> queryAllUsersByTopOrganIds(List<String> organIds);
	
}
