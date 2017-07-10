package com.starsoft.core.domain;
import java.util.List;

public interface DelegationAuthorizationDomain extends BaseDomain{
	/***
	 * 通过被委托人的标识，查询所有委托人的角色标识
	 * @param userId
	 * @return
	 */
	List<String> queryRoleIdsByUserId(String userId);
}
