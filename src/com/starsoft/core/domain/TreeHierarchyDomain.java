package com.starsoft.core.domain;
import java.util.List;

import com.starsoft.core.entity.BaseTreeObject;
/***
 * 层级关系数组
 * @author Administrator
 *
 */
public interface TreeHierarchyDomain{
	/***
	 * 保存新的节点
	 * @param treeObject
	 * @param appes
	 */
	public void save(BaseTreeObject treeObject,String appes);
	/***
	 * 更新节点信息
	 * @param treeObject
	 * @param appes
	 */
	public void update(BaseTreeObject treeObject,String appes);
	/***
	 * 删除节点信息
	 * @param treeObject
	 * @param appes
	 */
	public void delete(BaseTreeObject treeObject,String appes);
	/**
	 * 根据节点信息查询所有的上级列表
	 * @param id
	 * @return
	 */
	public List queryParentListByChildId(String id,String appes);
	/***
	 * 根据节点信息查询所有的下级级列表
	 * @param id
	 * @param appes
	 * @return
	 */
	public List queryChildListByParentId(String id,String appes);
	/***
	 * 根据节点标识和创建人查询所有的上级
	 * @param id
	 * @param createId
	 * @return
	 */
	public List queryParentListByChildId(String id,String createId,String appes);
	/***
	 * 根据节点标识和创建人查询所有的下级
	 * @param id
	 * @param createId
	 * @return
	 */
	public List queryChildListByParentId(String id,String createId,String appes);
	
}
