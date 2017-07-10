package com.starsoft.cms.domain;

import java.util.List;

import com.starsoft.cms.entity.Columns;
import com.starsoft.core.domain.BaseTreeDomain;
public interface ColumnsDomain extends BaseTreeDomain{
	/***
	 * 查询子栏目(所有)
	 * @param parentId 
	 * @param valid
	 * @return
	 */
	List<Columns>  queryColumnsByParentId(String parentId,Boolean valid);
	/***
	 * 查询信息的最大可用编号，通过栏目区分
	 * @param entity
	 */
	public int getMaxArticleSortCodeByColumnId(String columnId);
}
