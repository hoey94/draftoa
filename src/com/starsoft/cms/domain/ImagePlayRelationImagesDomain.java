package com.starsoft.cms.domain;
import com.starsoft.core.domain.BaseDomain;

public interface ImagePlayRelationImagesDomain extends BaseDomain{
	/**
	    * 获取当前最大排序号+1
	    * @return
	    */
		public Integer getMaxSortCode();
	
}
