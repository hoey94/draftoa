package com.starsoft.core.domainImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.starsoft.core.domain.RemindSenderDomain;
import com.starsoft.core.domainImpl.BaseDomainImpl;
import com.starsoft.core.util.StringUtil;
@Service("remindSenderDomain")
@Transactional
public class RemindSenderDomainImpl extends BaseDomainImpl implements RemindSenderDomain{
	public RemindSenderDomainImpl(){
		this.setClassName("com.starsoft.edu.entity.Course");
	}
	//num查询条数
	public List queryHightZan(Integer num){
		String sql = "select * from(select t.id as id,t.tname as tname,t.courseImage as courseImage,t.courseType as courseType,t.courseVideo as courseVideo,t.description as description,t.tag as tag,t.chapterMode as chapterMode,t.coursetime as coursetime,t.sortCode as sortCode,t.courseObjective as courseObjective,t.showStudentNumber as showStudentNumber,t.userCate as userCate,t.xuexiNumber as xuexiNumber,t.shoucangNumber as shoucangNumber,t.zanNumber as zanNumber,t.price as price,t.discount as discount,o.tname as organId from t_edu_course t left join t_core_organ o on t.organId=o.id order by t.zanNumber desc) as toal limit ?";
		 List<Map<String,Object>> list = (ArrayList) this.jdbcTemplate.queryForList(sql, new  Object[]{num});	
		 return list;
	}
	//num查询条数
	public List queryHightZan(Integer num,String organId){
		String sql = "select * from(select t.id as id,t.tname as tname,t.courseImage as courseImage,t.courseType as courseType,t.courseVideo as courseVideo,t.description as description,t.tag as tag,t.chapterMode as chapterMode,t.coursetime as coursetime,t.sortCode as sortCode,t.courseObjective as courseObjective,t.showStudentNumber as showStudentNumber,t.userCate as userCate,t.xuexiNumber as xuexiNumber,t.shoucangNumber as shoucangNumber,t.zanNumber as zanNumber,t.price as price,t.discount as discount,o.tname as organId from t_edu_course t left join t_core_organ o on t.organId=o.id and o.id=? order by t.zanNumber desc) as toal limit ?";
		 List<Map<String,Object>> list = (ArrayList) this.jdbcTemplate.queryForList(sql, new  Object[]{organId,num});	
		 return list;
	}
	/**
	 * 传入查询的个数
	 * @param num
	 * @return
	 */
	public List queryHightLearn(Integer num,String stname){
		String sql="select * from(select t.id as id,t.tname as tname,t.courseImage as courseImage,t.courseType as courseType,t.courseVideo as courseVideo,t.description as description,t.tag as tag,t.chapterMode as chapterMode,t.coursetime as coursetime,t.sortCode as sortCode,t.courseObjective as courseObjective,t.showStudentNumber as showStudentNumber,t.userCate as userCate,t.xuexiNumber as xuexiNumber,t.shoucangNumber as shoucangNumber,t.zanNumber as zanNumber,t.price as price,t.discount as discount,o.tname as organId from t_edu_course t left join t_core_organ o on t.organId=o.id";
		if(!StringUtil.isNullOrEmpty(stname)){
			sql=sql+" where o.tname like '%"+stname+"%'";
		}
		sql=sql+" order by xuexiNumber desc) as toal limit ?";
		List<Map<String,Object>> list = this.jdbcTemplate.queryForList(sql,new Object[]{num});
		return list;
	}
	@Override
	public Integer queryNunberByCourseTypeId(String courseTypeId) {
		String sql="select count(id) from t_edu_course where courseType=?";
		return this.jdbcTemplate.queryForInt(sql, new Object[]{courseTypeId});
	}
	@Override
	public boolean subscribeCourse(final String courseId, final String organId) {
		String result = this.jdbcTemplate.queryForObject("call copy_course(?,?)", new Object[] { courseId,organId }, String.class);
		if("success".equals(result)){
			return true;
		}
		return false;
	}
	/***
	 * 查询一个学校的课程信息
	 * @param schooleId
	 * @return
	 */
	public List queryCourseBySchoolId(String schooleId){
		return this.queryByCriteria(this.getCriteria(true).add(Restrictions.eq("organId", schooleId)).addOrder(Order.desc("sortCode")));
	}
	@Override
	public List queryHightLearnByOrganId(Integer num, String organId) {
		String sql="select * from(select t.id as id,t.tname as tname,t.courseImage as courseImage,t.courseType as courseType,t.courseVideo as courseVideo,t.description as description,t.tag as tag,t.chapterMode as chapterMode,t.coursetime as coursetime,t.sortCode as sortCode,t.courseObjective as courseObjective,t.showStudentNumber as showStudentNumber,t.userCate as userCate,t.xuexiNumber as xuexiNumber,t.shoucangNumber as shoucangNumber,t.zanNumber as zanNumber,t.price as price,t.discount as discount,o.tname as organId from t_edu_course t left join t_core_organ o on t.organId=o.id";
		if(!StringUtil.isNullOrEmpty(organId)){
			sql=sql+" where o.id = '"+organId+"'";
		}
		sql=sql+" order by xuexiNumber desc) as toal limit ?";
		List<Map<String,Object>> list = this.jdbcTemplate.queryForList(sql,new Object[]{num});
		return list;
	}
	
}
