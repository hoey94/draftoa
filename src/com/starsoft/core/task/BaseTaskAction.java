package com.starsoft.core.task;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.starsoft.core.domain.SystemLogDomain;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.entity.SystemLog;
import com.starsoft.core.util.WEBCONSTANTS;

/***
 * 任务调度使用的基类操作系统
 * @author Administrator
 *
 */
public abstract class BaseTaskAction {
	private SystemLogDomain systemLogDomain;
	public JdbcTemplate jdbcTemplate;
	/**
	 * 是否显示搜索影藏页面
	 */
	public final Logger logger = LogManager.getLogger(getClass());
	/***
	 * 增加保存时间、保存人相关信息
	 * @param request
	 * @param obj
	 */
	public void addBaseInfoToObject(BaseObject obj) {
		obj.setValid(false);
		obj.setCreateId("admin");
	}
	/***
	 * 任务调度是否打开
	 * @return
	 */
	public boolean power(){
		String value=WEBCONSTANTS.getSystemProperty("system.task.power");
		if("1".equals(value)){//代表打开了开关
			return true;
		}
		return false;
	}
	public void saveTaskExceptionInfo(Exception ex){
		SystemLog systemLog=new SystemLog();
		systemLog.setTname("任务执行出现异常");
		systemLog.setIp("");
		Class classs=this.getClass();
		systemLog.setInfoClass(classs.getName());
		systemLog.setQpath(ex.getMessage());
		StringBuffer msg = new StringBuffer("");  
		if (ex != null) {
			int length = ex.getStackTrace().length;//行号
			for (int i = 0; i < length; i++) {
				msg.append("\t" + ex.getStackTrace()[i] + "\n");
				if(i>30) break;
			}
			systemLog.setExceptionClass(ex.toString());
		}
		systemLog.setInfoContent(msg.toString());
		systemLogDomain.save(systemLog);
	}
	public void setSystemLogDomain(SystemLogDomain systemLogDomain) {
		this.systemLogDomain = systemLogDomain;
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
