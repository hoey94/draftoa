package com.starsoft.core.util;

import java.util.ArrayList;

/**
 * 针对Oracle数据库的版本 
 * com.hebrf.common.dao
 * 2010-6-8
 * @author 黄建伟
 */
public class SqlBuilderForOracle extends SqlBuilder {

	/**
	 * 
	 * @param select 初始查询语句
	 * @param type 
	 */
	private SqlBuilderForOracle(String select,int type){
		super(select,type);
	}
	private SqlBuilderForOracle(StringBuilder select,int type){
		super(select,type);
	}
	
	/**
	 * 返回conditionBuilder实例，初始语句中不包含where子句
	 * @param select
	 * @return
	 */
	public static SqlBuilderForOracle builder(String select){
		return new SqlBuilderForOracle(select,NO_WHERE);
	}
	
	public static SqlBuilderForOracle builder(StringBuilder select){
		return new SqlBuilderForOracle(select,NO_WHERE);
	}
	
	/**
	 * 返回conditionBuilder实例，初始语句中包含where子句
	 * @param select
	 * @return
	 */
	public static SqlBuilderForOracle builderWithWhere(String select){
		return new SqlBuilderForOracle(select,WIDTH_WHERE);
	}
	
	public static SqlBuilderForOracle builderWithWhere(StringBuilder select){
		return new SqlBuilderForOracle(select,WIDTH_WHERE);
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
		conditionBuilder.append(" like '%'||?");
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
		conditionBuilder.append(" like ?||'%'");
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
		conditionBuilder.append(" like '%'||?||'%'");
		if(values == null)
			values = new ArrayList<Object>();
		values.add(value);
		
		ischange = true;
		return this;
	}
	
	/**
	 * 添加字段值在两个日期之间(field between start and end)
	 * @param field
	 * @param start yyyy-MM-dd
	 * @param end yyyy-MM-dd
	 * @return
	 */
	public SqlBuilder betweenDate(String field,String start,String end){
		if(start == null || end == null ||start.equals("") || end.equals(""))
			return this;
		
		if(conditionBuilder == null){
			conditionBuilder = new StringBuilder();
		}
		conditionBuilder.append(" and ");
		conditionBuilder.append(field);
		conditionBuilder.append(" between to_date(? || ' 00:00:00','yyyy-MM-dd hh24:mi:ss') and to_date(? || ' 23:59:59','yyyy-MM-dd hh24:mi:ss')");
		if(values == null)
			values = new ArrayList<Object>();
		values.add(start);
		values.add(end);
		
		ischange = true;
		return this;
	}
	
	/**
	 * 添加字段值不在两个日期之间(field not between start and end)
	 */
	@Override
	public SqlBuilder noBetweenDate(String field,String start,String end){
		if(start == null || end == null ||start.equals("") || end.equals(""))
			return this;
		if(conditionBuilder == null){
			conditionBuilder = new StringBuilder();
		}
		conditionBuilder.append(" and ");
		conditionBuilder.append(field);
		conditionBuilder.append(" not between to_date(? || ' 00:00:00','yyyy-MM-dd hh24:mi:ss') and to_date(? || ' 23:59:59','yyyy-MM-dd hh24:mi:ss')");
		if(values == null)
			values = new ArrayList<Object>();
		values.add(start);
		values.add(end);
		
		ischange = true;
		return this;
	}
	
	/**
	 * 添加字段值大于一个日期的条件(field > value)
	 */
	@Override
	public SqlBuilder greatDate(String field,String value){
		if(value == null || value.equals(""))
			return this;
		if(conditionBuilder == null){
			conditionBuilder = new StringBuilder();
		}
		conditionBuilder.append(" and ");
		conditionBuilder.append(field);
		conditionBuilder.append(">to_date(? || ' 23:59:59','yyyy-MM-dd hh24:mi:ss')");
		if(values == null)
			values = new ArrayList<Object>();
		values.add(value);
		
		ischange = true;
		return this;
	}
	
	/**
	 * 添加字段值不大于一个日期的条件(field <= value)
	 */
	@Override
	public SqlBuilder noGreatDate(String field,String value){
		if(value == null || value.equals(""))
			return this;
		
		if(conditionBuilder == null){
			conditionBuilder = new StringBuilder();
		}
		conditionBuilder.append(" and ");
		conditionBuilder.append(field);
		if(value.length()==10){
			conditionBuilder.append("<=to_date(? || ' 23:59:59','yyyy-MM-dd hh24:mi:ss')");
		}else{
			conditionBuilder.append("<=to_date(?,'yyyy-MM-dd hh24:mi:ss')");
		}
		
		if(values == null)
			values = new ArrayList<Object>();
		values.add(value);
		
		ischange = true;
		return this;
	}
	
	/**
	 * 添加字段值小于一个日期的条件(field < value)
	 */
	@Override
	public SqlBuilder lessDate(String field,String value){
		if(value == null || value.equals(""))
			return this;
		if(conditionBuilder == null){
			conditionBuilder = new StringBuilder();
		}
		conditionBuilder.append(" and ");
		conditionBuilder.append(field);
		conditionBuilder.append("<to_date(? || ' 00:00:00','yyyy-MM-dd hh24:mi:ss')");
		if(values == null)
			values = new ArrayList<Object>();
		values.add(value);
		
		ischange = true;
		return this;
	}
	
	/**
	 * 添加字段值不小于一个日期的条件(field >= value)
	 */
	@Override
	public SqlBuilder noLessDate(String field,String value){
		if(value == null || value.equals(""))
			return this;
		if(conditionBuilder == null){
			conditionBuilder = new StringBuilder();
		}
		conditionBuilder.append(" and ");
		conditionBuilder.append(field);
		if(value.length() == 10){
			conditionBuilder.append(">=to_date(? || ' 00:00:00','yyyy-MM-dd hh24:mi:ss')");
		}else{
			conditionBuilder.append(">=to_date(?,'yyyy-MM-dd hh24:mi:ss')");
		}
		if(values == null)
			values = new ArrayList<Object>();
		values.add(value);
		
		ischange = true;
		return this;
	}
	
	/**
	 * 获取所有值集合
	 * @return
	 */
	@Override
	public Object[] getValues() {
		if(values != null){
			if(this.firstResult != null && this.maxResult != null){
				Object[] params = new Object[values.size() + 2];
				values.toArray(params);
				params[params.length - 2] = (maxResult + firstResult);
				params[params.length - 1] = firstResult;
				return params;
			}else{
				return values.toArray();
			}
		}else if(this.firstResult != null && this.maxResult != null){
			Object[] params = new Object[]{(maxResult + firstResult),this.firstResult};
			return params;
		}else{
			return null;
		}
	}
	
	@Override
	protected void appenResultCount() {
		if(this.firstResult == null || this.maxResult == null)
			return;
		
		this.sqlBuilder.insert(0, "select * from (select tmpa.*, rownum rn from (");
		this.sqlBuilder.insert(sqlBuilder.length(), ") tmpa where rownum <= ?) where rn > ?");
	}
}
