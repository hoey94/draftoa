package com.starsoft.core.domain;
import java.util.List;

import com.starsoft.core.domain.BaseDomain;

public interface RemindSenderDomain extends BaseDomain{
	
	public List queryHightZan(Integer num);
	
	public List queryHightZan(Integer num,String organId);
	
	public List queryHightLearn(Integer num,String stname);
	
	public List queryHightLearnByOrganId(Integer num,String organId);
	
	public Integer queryNunberByCourseTypeId(String courseTypeId);
	
	public boolean subscribeCourse(final String course,final String organId);
	/***
	 * 查询一个学校的课程信息
	 * @param schooleId
	 * @return
	 */
	public List queryCourseBySchoolId(String schooleId);
	
}
