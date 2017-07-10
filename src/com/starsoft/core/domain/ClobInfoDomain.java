package com.starsoft.core.domain;

import java.util.List;

/***
 * 详细信息接口类
 * @author Administrator
 *
 */
public interface ClobInfoDomain{
	/***
	 * 保存对象
	 * @param entity
	 */
	public void save(String Id,String content);
	/**
	 * 更新对象
	 * @param entity
	 */
	public void update(String Id,String content);
	/***
	 *查询对象
	 * @param entity
	 * @param id
	 * @return
	 */
	public String query(String id);
	public void deletes(final String propertyName,final List<String> ids);
}
