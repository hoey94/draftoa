package com.starsoft.core.domain;

import java.util.List;

public interface UsersDomain extends BaseDomain{
	/**
	 * 获取当前最大排序号+1
	 * 
	 * @return
	 */
	public Integer getMaxSortCode();
	/**
	 * 获取当前最大排序号+1
	 * 
	 * @return
	 */
	public Integer getMaxSortCodeByProperty(String propertyName,String propertyValue);
	/***
	 * 通过部门标识获取用户列表
	 * @param organId
	 * @return
	 */
	public List<String> getUserIdsByOrganId(String organId);

}
