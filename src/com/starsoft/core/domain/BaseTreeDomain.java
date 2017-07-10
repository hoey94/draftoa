package com.starsoft.core.domain;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.starsoft.core.entity.BaseTreeObject;

public interface BaseTreeDomain extends BaseDomain{
	/**
	    * 获取当前最大排序号+1
	    * @return
	    */
	public Integer getMaxSortCode();
	/**
	 * 获取当前最大排序号+1
	 * 
	 * @return
	 */
	public Integer getMaxSortCodeByProperty(String propertyName,String propertyValue);
	/**
	 * 查询对应类查询条件
	 * @param valid 查询是否有效数据 null 代表查询所有 true 查询所有有启用数据 false 查询所有禁用数据
	 * @param desc sortCode 按排序的方式  null 不按排序号排序，true 按desc 降序排列 排序  false按 ase 升序 排序
	 * @return
	 */
	public DetachedCriteria getCriteria(Boolean valid,Boolean desc);
	/**
	 * 查询下级信息
	 * @param parentId 父节点
	 * @param valid  查询是否有效数据 null 代表查询所有 true 查询所有有启用数据 false 查询所有禁用数据
	 * @param desc sortCode 按排序的方式  null 不按排序号排序，true 按desc 降序排列 排序  false按 ase 升序 排序
	 * @return
	 */
	public List queryByParentId(String parentId,Boolean valid,Boolean desc);
	/***
	 * 查询下级(所有)节点,供属性节点展开
	 * @param parentId
	 * @param level查询级别
	 * @param valid
	 * @param order sortCode 按排序的方式  null 不按排序号排序，true 按desc 降序排列 排序  false按 ase 升序 排序
	 * @return
	 */
	public List<BaseTreeObject> queryTreeByParentId(String parentId,Integer level,Boolean valid,Boolean order);
	/***
	 * 查询下级(所有)节点,包含选择的对象
	 * @param baseObjectId
	 * @param level查询级别
	 * @param valid
	 * @param order sortCode 按排序的方式  null 不按排序号排序，true 按desc 降序排列 排序  false按 ase 升序 排序
	 * @return
	 */
	public BaseTreeObject queryTreeByBaseObjectId(String baseObjectId,Integer level,Boolean valid,Boolean order);
	/***
	 * 查询下级(所有)节点,包含选择的对象,不包含扣除的节点对象
	 * @param baseObjectId 根节点
	 * @param notContainId 扣除节点
	 * @param level查询级别
	 * @param valid
	 * @param order sortCode 按排序的方式  null 不按排序号排序，true 按desc 降序排列 排序  false按 ase 升序 排序
	 * @return
	 */
	public List<BaseTreeObject> queryTreeByBaseObjectIdNotContainId(String baseObjectId,String notContainId,Integer level,Boolean valid,Boolean order);
}
