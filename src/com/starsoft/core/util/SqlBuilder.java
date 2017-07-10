package com.starsoft.core.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 文件：SqlBuilderForMySql.java
 * 作者：崔兵兵
 * 时间：2015-6-28
 * 描述：SQL语句生成类，最终生成参数查询方式的SQL语句 
 */
public abstract class SqlBuilder {
	private static final String EQUAL = "=";
	private static final String NO_EQUAL = "<>";
	private static final String LESS = "<";
	private static final String NO_LESS = ">=";
	private static final String GREAT = ">";
	private static final String NO_GREAT = "<=";
	private static final String BETWEEN = " between ";
	private static final String NO_BETWEEN = " not between ";
	private static final String IS_NULL = " is null";
	private static final String NOT_NULL = " is not null";
	
	/**
	 * select初始语句包含where子句
	*/
	public static final int WIDTH_WHERE = 1;
	
	/**
	 * select初始语句不包含where子句
	 */
	public static final int NO_WHERE = 2;
	
	
	protected StringBuilder sqlBuilder;
	private int type;//条件类型，指是否生成where关键字
	private StringBuilder selectBuilder;//select构造子句
	protected StringBuilder conditionBuilder;//条件构造子句
	protected List<Object> values;//值集合
	private StringBuilder groupBuilder;//分组构造子句
	private StringBuilder orderBuilder;//排序构造子句
	protected boolean ischange;//判断是否添加了条件
	
	protected Integer firstResult;//查询开始记录
	protected Integer maxResult;//查询数量
	
	/**
	 * 清空内容
	 *
	 */
	
	public void dispose(){
		selectBuilder = null;
		conditionBuilder = null;
		groupBuilder = null;
		orderBuilder = null;
		sqlBuilder = null;
	}
	
	/**
	 * 
	 * @param select 初始查询语句
	 * @param type 
	 */
	protected SqlBuilder(String select,int type){
		selectBuilder = new StringBuilder(select);
		this.ischange = true;
		this.type = type;
	}
	protected SqlBuilder(StringBuilder select,int type){
		selectBuilder = select;
		this.ischange = true;
		this.type = type;
	}
	
	/**
	 * 返回MySql版本实例，初始语句中不包含where子句
	 * @param select
	 * @return
	 */
	public static SqlBuilder builder(String select){
		return SqlBuilderForMySql.builder(select);
	}
	
	public static SqlBuilder builder(StringBuilder select){
		return SqlBuilderForMySql.builder(select);
	}
	
	/**
	 * 返回MySql版本实例，初始语句中包含where子句
	 * @param select
	 * @return
	 */
	public static SqlBuilder builderWithWhere(String select){
		return SqlBuilderForMySql.builderWithWhere(select);
	}
	
	public static SqlBuilder builderWithWhere(StringBuilder select){
		return SqlBuilderForMySql.builderWithWhere(select);
	}
	
	/**
	 * 返回Oracle版本实例，初始语句中不包含where子句
	 * @param select
	 * @return
	 */
	public static SqlBuilder builder4Oracle(String select){
		return SqlBuilderForOracle.builder(select);
	}
	
	public static SqlBuilder builder4Oracle(StringBuilder select){
		return SqlBuilderForOracle.builder(select);
	}
	
	/**
	 * 返回Oracle版本实例，初始语句中包含where子句
	 * @param select
	 * @return
	 */
	public static SqlBuilder builderWithWhere4Oracle(String select){
		return SqlBuilderForOracle.builderWithWhere(select);
	}
	
	public static SqlBuilder builderWithWhere4Oracle(StringBuilder select){
		return SqlBuilderForOracle.builderWithWhere(select);
	}
	
	
	/**
	 * 添加字符串相等条件
	 * @param field 数据库字段名称
	 * @param value　比较的值
	 * @return
	 */
	public SqlBuilder equalsValue(String field,Object value){
		return appendValue(field, value,EQUAL);
	}
	
	/**
	 * 添加字符串不相等条件(field<>value)
	 * @param field 数据库字段名称
	 * @param value　比较的值
	 * @return
	 */
	public SqlBuilder noEqualsValue(String field,Object value){
		return appendValue(field, value,NO_EQUAL);
	}
	
	/**
	 * 添加字段值小于字符串条件(field<value)
	 * @param field 数据库字段名称
	 * @param value　比较的值
	 * @return
	 */
	public SqlBuilder lessValue(String field,Object value){
		return appendValue(field, value,LESS);
	}
	
	/**
	 * 添加字段值不小于字符串条件(field>=value)
	 * @param field 数据库字段名称
	 * @param value　比较的值
	 * @return
	 */
	public SqlBuilder noLessValue(String field,Object value){
		return appendValue(field, value,NO_LESS);
	}
	
	/**
	 * 添加字段值大于字符串条件(filed>value)
	 * @param field 数据库字段名称
	 * @param value　比较的值
	 * @return
	 */
	public SqlBuilder greatValue(String field,Object value){
		return appendValue(field, value,GREAT);
	}
	
	/**
	 * 添加字段值不大于字符串条件(filed<=value)
	 * @param field 数据库字段名称
	 * @param value　比较的值
	 * @return
	 */
	public SqlBuilder noGreatValue(String field,Object value){
		return appendValue(field, value,NO_GREAT);
	}
	
	/**
	 * 添加字段值在两个字符串值之间的条件(filed between start and end)
	 * @param field 数据库字段名称
	 * @param value　比较的值
	 * @return
	 */
	public SqlBuilder betweenValues(String field,Object start,Object end){
		return appendValue(field, start, end,BETWEEN);
	}

	/**
	 * 添加字段值不在两个字符串值之间的条件(filed not between start and end)
	 * @param field 数据库字段名称
	 * @param value　比较的值
	 * @return
	 */
	public SqlBuilder noBetweenValues(String field,Object start,Object end){
		return appendValue(field, start, end,NO_BETWEEN);
	}

	/**
	 * 添加字段值在两个日期之间(field between start and end)
	 * @param field
	 * @param start
	 * @param end
	 * @return
	 */
	public SqlBuilder betweenDate(String field,String start,String end){
		if(start == null || end == null ||start.equals("") || end.equals(""))
			return this;
		return this.betweenValues(field, this.toStartDate(start),this.toEndDate(end));
	}
	
	/**
	 * 添加字段值不在两个日期之间(field not between start and end)
	 * @param field
	 * @param start
	 * @param end
	 * @return
	 */
	public SqlBuilder noBetweenDate(String field,String start,String end){
		if(start == null || end == null ||start.equals("") || end.equals(""))
			return this;
		return this.noBetweenValues(field, this.toStartDate(start),this.toEndDate(end));
	}
	
	/**
	 * 添加字段值大于一个日期的条件(field > value)
	 */
	public SqlBuilder greatDate(String field,String value){
		if(value == null || value.equals(""))
			return this;
		return this.greatValue(field, this.toEndDate(value));
	}
	
	/**
	 * 添加字段值不大于一个日期的条件(field <= value)
	 */
	public SqlBuilder noGreatDate(String field,String value){
		if(value == null || value.equals(""))
			return this;
		return this.noGreatValue(field, this.toEndDate(value));
	}
	
	/**
	 * 添加字段值小于一个日期的条件(field < value)
	 */
	public SqlBuilder lessDate(String field,String value){
		if(value == null || value.equals(""))
			return this;
		return this.lessValue(field, this.toStartDate(value));
	}
	
	/**
	 * 添加字段值不小于一个日期的条件(field >= value)
	 */
	public SqlBuilder noLessDate(String field,String value){
		if(value == null || value.equals(""))
			return this;
		return this.noLessValue(field, toStartDate(value));
	}
	
	/**
	 * 返回数据库的开始日期转换方式字符串
	 * @param value
	 * @return
	 */
	public String toStartDate(String value){
		return value + " 00:00:00";
	}
	
	/**
	 * 返回数据库的结束日期转换方式字符串
	 * @param value
	 * @return
	 */
	public String toEndDate(String value){
		return value + " 23:59:59";
	}
	
	/**
	 * 添加等于null的条件
	 */
	public SqlBuilder isNull(String field){
		return appendNullValue(field,IS_NULL);
	}

	/**
	 * 添加不等于null的条件
	 */
	public SqlBuilder noNull(String field){
		return appendNullValue(field,NOT_NULL);
	}
	
	/**
	 * 添加like前缀"%"(field like '%value')(MySql实现)
	 */
	public abstract SqlBuilder likePrex(String field,Object value);
	
	/**
	 * 添加like后缀"%"(field like 'value%')
	 */
	public abstract SqlBuilder likeSuffix(String field,Object value);
	
	/**
	 * 添加like前后缀"%"(field like '%value%')
	 */
	public abstract SqlBuilder like(String field,Object value);
	/**
	 * 
	 * @param field
	 * @param value 以逗号分隔的字符串
	 * @return
	 */
	public SqlBuilder in(String field,String value, String sep){
		if(isBlank(value))
			return this;
		createConditionBuilder();
		conditionBuilder.append(" and ");
		conditionBuilder.append(field);
		
		appendInValues(value, sep);
		
		
		ischange = true;
		return this;
	}
	
	/**
	 * 添加exitst条件
	 */
	public SqlBuilder exists(String value){
		if(value == null || "".equals(value))
			return this;
		createConditionBuilder();
		conditionBuilder.append(" and ");
		conditionBuilder.append(value);
		
		ischange = true;
		return this;
	}
	/**
	 * 添加exitst条件
	 */
	public SqlBuilder exists(StringBuilder value){
		if(value == null || "".equals(value))
			return this;
		createConditionBuilder();
		conditionBuilder.append(" and ");
		conditionBuilder.append(value);
		
		ischange = true;
		return this;
	}
	/**
	 * 添加分组语句
	 * @return
	 */
	public SqlBuilder group(String group){
		if(group == null || "".equals(group))
			return this;
		
		if(groupBuilder == null)
			groupBuilder = new StringBuilder(" group by ");

		groupBuilder.append(group);
		ischange = true;
		return this;
	}
	
	/**
	 * 添加排序语句
	 */
	public SqlBuilder order(String order){
		if(order == null || "".equals(order))
			return this;
		
		if(orderBuilder == null)
			orderBuilder = new StringBuilder(" order by ");
		
		orderBuilder.append(order);
		ischange = true;
		return this;
	}
	/**
	 * 构建查询结果数量语句
	 */
	public String getCountSql(){
		if(sqlBuilder == null)//如果sqlBuilder等于null,创建一个新的缓存
			sqlBuilder = new StringBuilder(selectBuilder);
		else{//否则，清空缓存
			sqlBuilder.delete(0,sqlBuilder.length());
			sqlBuilder.append(selectBuilder);
		}
		
		//生成条件语句
		if(conditionBuilder != null){
			if(type == WIDTH_WHERE){
				int start = conditionBuilder.indexOf("and") + 3;
				sqlBuilder.append(" where");
				sqlBuilder.append(conditionBuilder.substring(start,conditionBuilder.length()));
			}else{
				sqlBuilder.append(conditionBuilder);
			}
		}
		String sql = "select count(*) from(" + sqlBuilder.toString() + ") ct";
		this.ischange = true;
		return sql;
	}
	
	/**
	 * 获取所有值集合
	 * @return
	 */
	public Object[] getValues() {
		if(values != null){
			if(this.firstResult != null && this.maxResult != null){
				Object[] params = new Object[values.size() + 2];
				values.toArray(params);
				params[params.length - 2] = firstResult;
				params[params.length - 1] = maxResult;
				return params;
			}else{
				return values.toArray();
			}
		}else if(this.firstResult != null && this.maxResult != null){
			Object[] params = new Object[]{this.firstResult,this.maxResult};
			return params;
		}else{
			return null;
		}
	}
	
	/**
	 * 赋值参数
	 * @param object
	 */
	public void setValues(Object[] object){
		if(values == null){
			values = new ArrayList<Object>();
		}
		for (Object o : object) {
			values.add(o);
		}
	}
	/**
	 * 获取查询总数时的值集合
	 */
	public Object[] getCountSqlValues(){
		if(values == null)
			return null;
		return values.toArray();
	}
	
	/**
	 * 构建SQL语句
	 * @return
	 */
	private void build(){
		if(sqlBuilder == null)//如果sqlBuilder等于null,创建一个新的缓存
			sqlBuilder = new StringBuilder(selectBuilder);
		else{//否则，清空缓存
			sqlBuilder.delete(0,sqlBuilder.length());
			sqlBuilder.append(selectBuilder);
		}
		
		//生成条件语句
		if(conditionBuilder != null){
			if(type == WIDTH_WHERE){
				int start = conditionBuilder.indexOf("and") + 3;
				sqlBuilder.append(" where");
				sqlBuilder.append(conditionBuilder.substring(start,conditionBuilder.length()));
			}else{
				sqlBuilder.append(conditionBuilder);
			}
			
		}
		//生成分组语句
		if(groupBuilder != null){
			sqlBuilder.append(groupBuilder);
		}
		
		//生成排序语句
		if(orderBuilder != null){
			sqlBuilder.append(orderBuilder);
		}
		
		//设置结果数量
		appenResultCount();
		ischange = false;
	}
	
	
	protected abstract void appenResultCount();

	public String buildString(){
		if(ischange)
			build();
		
		return sqlBuilder.toString();
		
	}
	
	public String toString(){
		return this.buildString();
	}
	
	public StringBuilder getConditionBuilder() {
		createConditionBuilder();
		return conditionBuilder;
	}
	
	public Integer getFirstResult() {
		return firstResult;
	}

	public SqlBuilder setFirstResult(Integer firstResult) {
		this.firstResult = (firstResult == null ? 0 : firstResult);
		return this;
	}

	public Integer getMaxResult() {
		return maxResult;
	}

	public SqlBuilder setMaxResult(Integer maxResult) {
		this.maxResult = maxResult;
		return this;
	}
	//>>>>>>>>>>>>>>>>>私有方法<<<<<<<<<<<<<<<<<<<<<<<
	private boolean isBlank(Object value) {
		return value == null || "".equals(value.toString());
	}
	
	private SqlBuilder appendValue(String field, Object value,String operator) {
		if(isBlank(value))
			return this;
		
		createConditionBuilder();
		conditionBuilder.append(" and ");
		conditionBuilder.append(field);
		conditionBuilder.append(operator);
		conditionBuilder.append("?");
		
		if(values == null)
			values = new ArrayList<Object>();
		
		values.add(value);		
		ischange = true;
		return this;
	}
	
	private SqlBuilder appendValue(String field, Object start, Object end,String operator) {
		if(start == null || end == null)
			return this;
		
		createConditionBuilder();
		conditionBuilder.append(" and ");
		conditionBuilder.append(field);
		conditionBuilder.append(operator);
		conditionBuilder.append("? and ?");
		
		if(values == null)
			values = new ArrayList<Object>();
		values.add(start);
		values.add(end);
		
		ischange = true;
		return this;
	}
	
	private SqlBuilder appendNullValue(String field,String operator) {
		createConditionBuilder();
		
		conditionBuilder.append(" and ");
		conditionBuilder.append(field);
		conditionBuilder.append(operator);
		
		ischange = true;
		return this;
	}
	
	private void createConditionBuilder() {
		if(conditionBuilder == null){
			conditionBuilder = new StringBuilder();
		}
	}
	
	private void appendInValues(String value, String sep) {
		conditionBuilder.append(" in (");
		if(values == null)
			values = new ArrayList<Object>();
		
		String[] vp = value.split(sep);
		for(int i = 0;i < vp.length;){
			conditionBuilder.append("?");
			values.add(vp[i]);
			
			if(++i < vp.length){
				conditionBuilder.append(",");
			}
		}
		conditionBuilder.append(")");
	}
}
