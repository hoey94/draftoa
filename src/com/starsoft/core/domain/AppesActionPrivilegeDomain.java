package com.starsoft.core.domain;
import java.util.List;

import com.starsoft.core.entity.Appes;

public interface AppesActionPrivilegeDomain extends BaseDomain{
	/***
	 * 为角色增加所有权限
	 * @param appes
	 * @param role
	 * @param defaultAction
	 */
	public void addAllPrivilegeForRole(Appes appes,String role,String defaultAction); 
	/***
	 * 为角色增加所有权限
	 * @param appes
	 * @param role
	 * @param defaultAction
	 */
	public void addAllPrivilegeForRole(String appes,String role,String defaultAction); 
	/***
	 * 为角色去掉指定的操作权限
	 * @param role
	 * @param defaultAction
	 */
	public void deletePrivilegeForRole(Appes appes,String role,String defaultAction);
	/***
	 * 为角色去掉指定的操作权限
	 * @param role
	 * @param defaultAction
	 */
	public void deletePrivilegeForRole(String appes,String role,String defaultAction);
	/***
	 * 判断是否有操作权限
	 * @param appes
	 * @param action
	 * @param roleIds
	 * @return
	 */
	public boolean hasPrivilege(String appes,String action,List<String> roleIds);
	
	
}
