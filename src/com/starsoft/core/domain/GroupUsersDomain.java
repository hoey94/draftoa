package com.starsoft.core.domain;
import java.util.List;


import com.starsoft.core.domain.BaseDomain;

public interface GroupUsersDomain extends BaseDomain{
	/**
	 * 通过用户标识查询用户的群组标识
	 * @param menuid
	 * @param roleids
	 */
	public List<String> getGroupIdsByUserId(String userId);
	/**
	 * 通过群组标识查询用户的
	 * @param menuid
	 * @param roleids
	 */
	public List<String> getUserIdsByGroupId(String GroupId);
	
}
