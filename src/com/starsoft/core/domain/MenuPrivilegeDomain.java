package com.starsoft.core.domain;

import java.util.List;

public interface MenuPrivilegeDomain extends BaseDomain {
	/**
	 * 更新单个菜单的权限对应的角色
	 * @param menuid
	 * @param roleids
	 */
	public void updatePrivilege(String menuid,List<String> roleids);
	
	public boolean queryPrivilegeByMenuIdAndRoleIds(String menuid,List<String> roleids);
}
