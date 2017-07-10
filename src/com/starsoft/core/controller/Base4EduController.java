package com.starsoft.core.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.starsoft.core.domain.DictionaryDomain;
import org.springframework.beans.factory.annotation.Autowired;

import com.starsoft.core.entity.Dictionary;
import com.starsoft.core.util.StringUtil;
import com.starsoft.core.vo.IdName;

public abstract class Base4EduController extends BaseAjaxController{
	@Autowired
	private DictionaryDomain dictionaryDomain;
	public List<IdName> showLiDictionary(HttpServletRequest request,
			HttpServletResponse response, String id) throws Exception {
		Dictionary dictionary=(Dictionary)this.dictionaryDomain.query(id);
		List<IdName> list=new ArrayList<IdName>();
		if(dictionary!=null){
			if(dictionary.isValid()){//如果是静态
				String desc=dictionary.getDescription();
				list=this.dictionaryContentToList(desc);
			}else{//如果是动态
				String sql=dictionary.getDescription();
				list=this.dictionaryContentSqlToList(request,sql);
			}
		}
		return list;
	}
	
	/***
	 * 通过sql语句查询数值
	 * @param request
	 * @param sql
	 * @return
	 */
	private List<IdName> dictionaryContentSqlToList(HttpServletRequest request,
			String sql) {
		List<IdName> result=new ArrayList<IdName>();
		String[] argsTemp=new String[100];
		int argsize=0;
		while(sql.contains(":")){//去除
			String subsqlparam=sql.substring(sql.indexOf(":"));
			if(subsqlparam.indexOf(" ")>-1){
				subsqlparam=subsqlparam.substring(0,subsqlparam.indexOf(" ")).trim();
			}else{
				subsqlparam=subsqlparam.trim();
			}
			String paramName=subsqlparam.replace(":","").trim();
			String paramValue = request.getParameter(paramName);
			if(paramValue!=null){
				argsTemp[argsize]=paramValue;
				argsize++;
				sql=sql.replaceFirst(subsqlparam,"?");
			}else{//清除该参数
				String beforsql=sql.substring(0,sql.indexOf(subsqlparam)).trim();
				if(beforsql.lastIndexOf("")>-1){
					beforsql=sql.substring(beforsql.lastIndexOf(" "), sql.indexOf(subsqlparam))+subsqlparam;
				}
				sql=sql.replaceFirst(beforsql.trim(),"").trim();
				if(sql.endsWith("where")){
					sql=sql+" 1=1";
				}
			}
		}
		String[] args=new String[argsize];
		if(argsize>0){
			for(int t=0;t<argsize;t++){
				args[t]=argsTemp[t];
			}
		}
		try{
			int maxRows=jdbcTemplate.getMaxRows();
			jdbcTemplate.setMaxRows(100);
			List<Map<String, Object>> list=jdbcTemplate.queryForList(sql, args);
			jdbcTemplate.setMaxRows(maxRows);
			for(Map<String, Object> map:list){
				String id=String.valueOf(map.get("id"));
				String name=String.valueOf(map.get("tname"));
				result.add(new IdName(id,name));
			}
		}catch(Exception e){
			logger.info("查询动态字典报错，sql语句="+sql);
		}
		return result;
	}
	/***
	 * 静态内容变成字典列表
	 * @param content
	 * @return
	 */
	private List<IdName> dictionaryContentToList(String content){
		List<IdName> result=new ArrayList<IdName>();
		List<String> items=StringUtil.toList(content, "#");
		for(String item:items){
			if(item.equals("")) continue;
			String itemId="";
			String itemName="";
			if(item.contains("(")){
				itemName=item.substring(0,item.indexOf("("));
				itemId=item.substring(item.indexOf("(")+1, item.length());
				if(itemId.endsWith(")")){
					itemId=itemId.substring(0, itemId.length()-1);
				}
			}else{
				itemId=	item;
				itemName=item;
			}
			result.add(new IdName(itemId,itemName));
		}
		return result;
	}
	
}
