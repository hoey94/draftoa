package com.starsoft.cms.domain;

import java.util.List;

import com.starsoft.cms.entity.IndexSearch;
import com.starsoft.core.util.Page;

public interface IndexSearchDomain {
	/***
	 * 保存对象
	 * @param entity
	 */
	public void save(IndexSearch entity);
	/***
	 * 批量删除对象(支持事务)
	 * @param entity
	 */
	public void deletes(String baseObjectIds);
	/***
	 * 批量删除对象(支持事务)
	 * @param entity
	 */
	public void deletes(List<String> baseObjectIds);
	/**
	 * 查询数据列表加分页
	 * @param key 搜索关键字
	 * @param indexType 索引类别
	 * @return
	 */
	public List queryByCriteria(final String key,final String startDate,final String endDate,final Page page);

}
