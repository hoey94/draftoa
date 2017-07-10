package com.starsoft.core.domain;
import com.starsoft.core.domain.BaseDomain;
import com.starsoft.core.entity.ClientVersion;

public interface ClientVersionDomain extends BaseDomain{
	/***
	 * 通过客户端标识和客户端类型查询最新的版本
	 * @param clientCode
	 * @param clentType
	 * @return
	 */
	public ClientVersion queryNewVersionByClientCodeAndClientType(String clientCode,String clientType);
	
	
}
