package com.starsoft.core.domain;
import java.util.List;

import com.starsoft.core.entity.Resource;

public interface ResourceDomain extends BaseDomain{
	/**
	 * 通过关联对象查找资源信息
	 * @param baseObjectId
	 * @return
	 */
	public List<Resource> queryByLinkId(String baseObjectId);
}
