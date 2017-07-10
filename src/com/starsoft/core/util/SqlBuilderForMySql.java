package com.starsoft.core.util;

import java.util.ArrayList;

/**
 * 
 * 文件：SqlBuilderForMySql.java
 * 作者：崔兵兵
 * 时间：2015-6-28
 * 描述：针对MySql数据库的版本 
 */
public class SqlBuilderForMySql extends SqlBuilder{

	/**
	 * 
	 * @param select 初始查询语句
	 * @param type 
	 */
	private SqlBuilderForMySql(String select,int type){
		super(select,type);
	}
	private SqlBuilderForMySql(StringBuilder select,int type){
		super(select,type);
	}
	
	/**
	 * 返回conditionBuilder实例，初始语句中不包含where子句
	 * @param select
	 * @return
	 */
	public static SqlBuilderForMySql builder(String select){
		return new SqlBuilderForMySql(select,NO_WHERE);
	}
	
	public static SqlBuilderForMySql builder(StringBuilder select){
		return new SqlBuilderForMySql(select,NO_WHERE);
	}
	
	/**
	 * 返回conditionBuilder实例，初始语句中包含where子句
	 * @param select
	 * @return
	 */
	public static SqlBuilderForMySql builderWithWhere(String select){
		return new SqlBuilderForMySql(select,WIDTH_WHERE);
	}
	
	public static SqlBuilderForMySql builderWithWhere(StringBuilder select){
		return new SqlBuilderForMySql(select,WIDTH_WHERE);
	}
	
	/**
	 * 添加like前缀"%"(field like '%value')(MySql实现)
	 */
	public SqlBuilder likePrex(String field,Object value){
		if(value == null || "".equals(value))
			return this;
		
		if(conditionBuilder == null){
			conditionBuilder = new StringBuilder();
		}
		conditionBuilder.append(" and ");
		conditionBuilder.append(field);
		conditionBuilder.append(" like concat('%',?)");
		if(values == null)
			values = new ArrayList<Object>();
		values.add(value);
		
		ischange = true;
		return this;
	}
	
	/**
	 * 添加like后缀"%"(field like 'value%')
	 */
	public SqlBuilder likeSuffix(String field,Object value){
		if(value == null || "".equals(value))
			return this;
		
		if(conditionBuilder == null){
			conditionBuilder = new StringBuilder();
		}
		conditionBuilder.append(" and ");
		conditionBuilder.append(field);
		conditionBuilder.append(" like concat(?,'%')");
		if(values == null)
			values = new ArrayList<Object>();
		values.add(value);
		
		ischange = true;
		return this;
	}
	
	/**
	 * 添加like前后缀"%"(field like '%value%')
	 */
	public SqlBuilder like(String field,Object value){
		if(value == null || "".equals(value))
			return this;
		
		if(conditionBuilder == null){
			conditionBuilder = new StringBuilder();
		}
		conditionBuilder.append(" and ");
		conditionBuilder.append(field);
		conditionBuilder.append(" like concat('%',?,'%')");
		if(values == null)
			values = new ArrayList<Object>();
		values.add(value);
		
		ischange = true;
		return this;
	}
	
	protected void appenResultCount() {
		if(this.firstResult != null && this.maxResult != null){
			sqlBuilder.append(" limit ?,?");
		}
	}
}
