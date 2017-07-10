package com.starsoft.core.domain;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.util.Page;

public interface BaseDomain {
	/***
	 * 保存对象
	 * @param entity
	 */
	public void save(BaseObject entity);
	/***
	 * 保存多个对象(支持事务)
	 * @param entity
	 */
	public void save(List<BaseObject> entitys);
	/**
	 * 更新对象
	 * @param entity
	 */
	public void update(BaseObject entity);
	/***
	 * 删除对象
	 * @param entity
	 * @throws Exception 
	 */
	public void delete(BaseObject entity);
	/***
	 * 批量删除对象(支持事务)
	 * @param entity
	 */
	public void deletes(List<String> ids);
	/***
	 * 批量删除对象(支持事务)
	 * @param entity
	 */
	public void deletes(final String propertyName,List<String> ids);
	/***
	 * 批量标记删除对象 
	 * @param entity
	 */
	public void markdeletes(List<String> ids);
	/***
	 *查询对象
	 * @param entity
	 * @param id
	 * @return
	 */
	public Object query(String id);
	/***
	 * 通过标识查询名称
	 * @param id
	 * @return
	 */
	public String queryTnameById(String id);
	/***
	 * 通过属性查找所有符合条件
	 * @param propertyName
	 * @param propertyValue
	 * @return
	 */
	public List queryByProperty(final String propertyName,final String propertyValue);
	/***
	 * 通过属性查找所有符合条件
	 * @param propertyName
	 * @param propertyValue
	 * @return
	 */
	public List queryByProperty(final String propertyName,final List propertyValues);
	/***
	 * 通过属性查找第一个符合条件的
	 * @param propertyName
	 * @param propertyValue
	 * @return
	 */
	public BaseObject queryFirstByProperty(final String propertyName,final String propertyValue);
	/***
	 * 通过属性查找第一个符合条件的
	 * @param cls
	 * @param propertyName
	 * @param propertyValue
	 * @return
	 */
	public BaseObject queryFirstByProperty(Class cls,final String propertyName,final String propertyValue);
	/**
	 * 查询数据列表
	 * @param detachedCriteria
	 * @return
	 */
	public List  queryByCriteria(final DetachedCriteria criteria);
	/**
	 * 查询指定的数据量列表，不进行分页操作，提高性能
	 * @param detachedCriteria
	 * @return
	 */
	public List  queryByCriteria(final DetachedCriteria criteria,final int maxSize);
	/**
	 * 查询数据列表加分页
	 * @param detachedCriteria
	 * @return
	 */
	public List queryByCriteria(final DetachedCriteria criteria,final Page page);
	/***
	 * 查询所有的信息
	 * @param entity
	 * @param valid
	 * @return
	 */
	public List queryAll();
	/***
	 * 取得对应的新的实体
	 * @return
	 * @throws Exception
	 */
	public BaseObject getBaseObject()throws Exception ;
	/***
	 * 取得实体类的名称
	 * @return
	 */
	public String getClassName();
	/**
	 * 设置实体类的
	 * @return
	 */
	public void setClassName(String className);
//	/**
//	    * 获取当前最大排序号+1
//	    * @return
//	    */
//	public Integer getMaxSortCode();
//	  /**
//	    * 获取当前最大排序号+1
//	    * @return
//	    */
//	public Integer getMaxSortCode(String className);
//	 /**
//	    * 获取当前最大排序号+1
//	    * @return
//	    */
//	public Integer getMaxSortCodeByProperty(String propertyName,String propertyValue);
	/***
	 * 通过HQL查询
	 * @param hql
	 * @param page
	 * @return
	 */
	public List queryByHql(final String hql, final  List parameters,final Page page);
	/**
	 * 查询对应类查询条件
	 * @param valid 查询是否有效数据 null 代表查询所有 true 查询所有有启用数据 false 查询所有禁用数据
	 * @return
	 */
	public DetachedCriteria getCriteria(Boolean valid);
	/***
	 * 执行hql语句,update 或者delete
	 * @param hql
	 * @param parameters
	 */
	public void executeByHQL(final String hql, final  List parameters);
	/**
	 * 执行大事务操作执行顺序 deleteList saveList updateList
	 * @param deleteList
	 * @param saveList
	 * @param updateList
	 */
	public void deleteAndSaveAndUpdate(List<BaseObject> deleteList,List<BaseObject> saveList,List<BaseObject> updateList);
	/***
	 * 通过SQL查询
	 * @param sql
	 * @return
	 */
	public List queryBySql(final String sql, final  List parameters);
	/***
	 * 执行sql语句,update 或者delete
	 * @param sql
	 * @param parameters
	 */
	public void executeBySQL(final String sql, final  List parameters);
	/***
	 * 自由参数查询
	 * @param hql
	 * @param objs
	 * @return
	 */
	public List statisticsByHql(final String hql,Object[] objs);
}
