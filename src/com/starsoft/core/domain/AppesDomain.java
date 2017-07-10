package com.starsoft.core.domain;
import com.starsoft.core.entity.Appes;

public interface AppesDomain extends BaseDomain{
	/***
	 * 取得应用的所以相关信息
	 * @param id
	 * @return
	 */
	public Appes getAppes(String id);
	/***
	 * 更新缓存信息
	 * @param id
	 */
	public void updateCache(String id);
	/***
	 * 查询对象
	 * @param entity
	 * @throws Exception 
	 */
	public Object query(Class cls, String id);
}
