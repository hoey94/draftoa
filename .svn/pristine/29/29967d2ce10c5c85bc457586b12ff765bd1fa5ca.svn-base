package com.starsoft.cms.domain;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.starsoft.cms.entity.Article;
import com.starsoft.core.domain.BaseDomain;

public interface ArticleDomain extends BaseDomain{
	/***
	 * 查询指定的数量的信息，
	 * @param columnId 指定栏目
	 * @param articleSize 指定数据量，排序方式sortCode 降序，必须有效且发布状态
	 * @return
	 */
	public List<Article> queryByColumnsIdAndSize(String columnId,int articleSize);
	/***
	 * 批量删除对象信息及内容
	 * @param entity
	 */
	@Override
	public void deletes(List<String> ids);

	public List findLimiteByCriteria(DetachedCriteria criteriaqyzp, int i);
	
}
