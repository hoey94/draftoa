package com.starsoft.core.domain;

import java.util.List;

import com.starsoft.core.entity.BaseTreeObject;

public interface MenuDomain extends BaseTreeDomain{
	/**
    * 获取当前最大排序号+1
    * @return
    */
	public Integer getMaxSortCode();
	/***
	 * 根据角色权限查询下一级菜单
	 * @param parentId
	 * @param roles
	 * @return
	 */
	public List<BaseTreeObject> querySubTreeByParentIdAndRoles(String parentId,List<String> roles);
	/***
	 * 根据角色权限查询下多级菜单
	 * @param parentId
	 * @param roles
	 * @return
	 */
	public List<BaseTreeObject> querySubTreesByParentIdAndRoles(String parentId,List<String> roles);
	
}
