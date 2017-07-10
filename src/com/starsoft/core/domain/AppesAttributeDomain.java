package com.starsoft.core.domain;
import com.starsoft.core.domain.BaseDomain;

public interface AppesAttributeDomain extends BaseDomain{
	/**
	 * 获取指定应用当前最大排序号+1
	 * 
	 * @return
	 */
	public Integer getMaxSortCodeByProperty(String propertyName,String propertyValue);
	
}
