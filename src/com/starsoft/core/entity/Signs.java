package com.starsoft.core.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.starsoft.core.util.InitFieldAnnotation;
import com.starsoft.core.util.InitNameAnnotation;
import com.starsoft.core.util.StringUtil;

@Entity
@InitNameAnnotation("我的签到")
@Table(name="T_CORE_SIGNS")
public class Signs extends BaseObject {
	@InitFieldAnnotation("课程标识")
	private String courseId;
	@InitFieldAnnotation("课程名称")
	private String courseName;
	@InitFieldAnnotation("微课标识")
	private String lessonId;
	@InitFieldAnnotation("班级标识")
	private String classId;
	@InitFieldAnnotation("班级姓名")
	private String className;
	@InitFieldAnnotation("老师标识")
	private String teacherId;
	@InitFieldAnnotation("老师姓名")
	private String teacherName;
	@InitFieldAnnotation("学生标识")
	private String studentId;
	@InitFieldAnnotation("学生姓名")
	private String studentName;
	@InitFieldAnnotation("教学日历标识")
	private String teacherCalendarId;
	@InitFieldAnnotation("教学日历上课时间段")
	private String teacherCalendarTime;
	@InitFieldAnnotation(value="学校标识")
	private String organId;
	@InitFieldAnnotation(value="地理位置")
	private String address;
	@InitFieldAnnotation("理论知识")
	private Integer theroyNumber;
	@InitFieldAnnotation("实践技能")
	private Integer practiceNumber;
	@InitFieldAnnotation("社会能力")
	private Integer socialNumber;
	@InitFieldAnnotation("独立能力")
	private Integer idependentNumber;
	@InitFieldAnnotation("是否已经有评分")
	private boolean showStudentScore;
	@InitFieldAnnotation("自我理论知识")
	private Integer sttheroyNumber;
	@InitFieldAnnotation("自我实践技能")
	private Integer stpracticeNumber;
	@InitFieldAnnotation("自我社会能力")
	private Integer stsocialNumber;
	@InitFieldAnnotation("自我独立能力")
	private Integer stidependentNumber;
	@InitFieldAnnotation("自我是否已经有评分")
	private boolean stshowStudentScore;
	@InitFieldAnnotation("签到时间")
	private Date signsTime;
	public Signs() {
    	this.id = StringUtil.generator(); 
    	this.setValid(true);//待读
    	this.setShowStudentScore(false);//老师待评分
    	this.setStshowStudentScore(false);//自我待评分
    }
	public String getLessonId() {
		return lessonId;
	}
	public void setLessonId(String lessonId) {
		this.lessonId = lessonId;
	}
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getTeacherCalendarId() {
		return teacherCalendarId;
	}
	public void setTeacherCalendarId(String teacherCalendarId) {
		this.teacherCalendarId = teacherCalendarId;
	}
	public String getOrganId() {
		return organId;
	}
	public void setOrganId(String organId) {
		this.organId = organId;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getTeacherCalendarTime() {
		return teacherCalendarTime;
	}
	public void setTeacherCalendarTime(String teacherCalendarTime) {
		this.teacherCalendarTime = teacherCalendarTime;
	}
	public Integer getTheroyNumber() {
		return theroyNumber;
	}
	public void setTheroyNumber(Integer theroyNumber) {
		this.theroyNumber = theroyNumber;
	}
	public Integer getPracticeNumber() {
		return practiceNumber;
	}
	public void setPracticeNumber(Integer practiceNumber) {
		this.practiceNumber = practiceNumber;
	}
	public Integer getSocialNumber() {
		return socialNumber;
	}
	public void setSocialNumber(Integer socialNumber) {
		this.socialNumber = socialNumber;
	}
	public Integer getIdependentNumber() {
		return idependentNumber;
	}
	public void setIdependentNumber(Integer idependentNumber) {
		this.idependentNumber = idependentNumber;
	}
	@Type(type="yes_no")
	public boolean isShowStudentScore() {
		return showStudentScore;
	}
	public void setShowStudentScore(boolean showStudentScore) {
		this.showStudentScore = showStudentScore;
	}
	public Integer getSttheroyNumber() {
		return sttheroyNumber;
	}
	public void setSttheroyNumber(Integer sttheroyNumber) {
		this.sttheroyNumber = sttheroyNumber;
	}
	public Integer getStpracticeNumber() {
		return stpracticeNumber;
	}
	public void setStpracticeNumber(Integer stpracticeNumber) {
		this.stpracticeNumber = stpracticeNumber;
	}
	public Integer getStsocialNumber() {
		return stsocialNumber;
	}
	public void setStsocialNumber(Integer stsocialNumber) {
		this.stsocialNumber = stsocialNumber;
	}
	public Integer getStidependentNumber() {
		return stidependentNumber;
	}
	public void setStidependentNumber(Integer stidependentNumber) {
		this.stidependentNumber = stidependentNumber;
	}
	@Type(type="yes_no")
	public boolean isStshowStudentScore() {
		return stshowStudentScore;
	}
	public void setStshowStudentScore(boolean stshowStudentScore) {
		this.stshowStudentScore = stshowStudentScore;
	}
	public Date getSignsTime() {
		return signsTime;
	}
	public void setSignsTime(Date signsTime) {
		this.signsTime = signsTime;
	}
}
