package com.starsoft.cms.controller;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.starsoft.core.controller.BaseAjaxController;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.Page;
import com.starsoft.core.vo.IdName;
/***
 * 系统文件查询及修改
 * @author lenovo
 *
 */
public class SystemFileController extends BaseAjaxController{
	/***
	 * 公共的列表方法
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String,Object> model=HttpUtil.convertModel(request);
		String projectPath = getServletContext().getRealPath("/");//获取文件路径
		File file=new File(projectPath+"/page");
		File[] tempList = file.listFiles();
		List filesList=new ArrayList();
		for(File fileTemp:tempList){
			IdName obj=new IdName(fileTemp.getAbsolutePath(),fileTemp.getName());
			filesList.add(obj);
		}
		Page page=HttpUtil.convertPage(request);
		model.put("page", page);
		model.put("filesList", filesList);
		return new ModelAndView(this.getListPage(),model);
	}
	
	
}
