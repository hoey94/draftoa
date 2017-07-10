package com.starsoft.core.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

public interface BaseInterface {
	/**
	 * 展示信息列表视图
	 */
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception;
	/**
	 * 增加对象视图
	 */
	public ModelAndView add(HttpServletRequest request,HttpServletResponse response) throws Exception;
	/**
	 * 编辑对象视图
	 */
	public ModelAndView edit(HttpServletRequest request,HttpServletResponse response) throws Exception;
	/**
	 * 只读对象视图
	 */
	public ModelAndView read(HttpServletRequest request,HttpServletResponse response) throws Exception;
	/**
	 * 保存对象功能视图
	 */
	public void save(HttpServletRequest request,HttpServletResponse response) throws Exception;
	/**
	 * 更新对象功能视图
	 */
	public void update(HttpServletRequest request,HttpServletResponse response) throws Exception;
	
}
