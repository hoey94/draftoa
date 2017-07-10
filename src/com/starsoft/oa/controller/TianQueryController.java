package com.starsoft.oa.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.starsoft.core.controller.BaseAjaxController;
import com.starsoft.core.domain.OrganDomain;
import com.starsoft.core.domain.UsersDomain;
import com.starsoft.core.entity.Organ;
import com.starsoft.core.entity.Users;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.Page;
import com.starsoft.oa.domain.FuyiDomain;
import com.starsoft.oa.domain.FuyiRecordDomain;
import com.starsoft.oa.entity.FuyiRecord;
import com.starsoft.oa.entity.Motion;

public class TianQueryController extends BaseAjaxController {
	@Autowired
	private UsersDomain userDomain;
	@Autowired
	private OrganDomain organDomain;
	@Autowired
	private FuyiRecordDomain fuyiRecordDomain;
	@Autowired
	private FuyiDomain fuyiDomain;
	/**
	 * 全部提案
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView queryAll(HttpServletRequest request,HttpServletResponse response)
		throws Exception{
		Map<String,Object> model=new HashMap<String,Object>();
		Page page=HttpUtil.convertPage(request);
		DetachedCriteria criteria=baseDomain.getCriteria(null).add(Restrictions.naturalId());
		List<Motion> list=baseDomain.queryByCriteria(criteria, page);
		for (Motion m : list) {
			Users user=(Users) userDomain.query(m.getCreateId());
			m.setMotionMan(user.getTname());
			Organ o=(Organ) organDomain.query(user.getOrganId());
			m.setDept(o.getTname());
			m.setDuty(user.getDuty());
			m.setMobilNumber(user.getMobilNumber());
			int count=fuyiDomain.findFuyiCount(m.getId(), "1");//赞同票
			int fuyiCount=fuyiDomain.findFuyiCount(m.getId(), null);
			m.setAgreeNum(count+"");
			m.setFyrNum(fuyiCount+"");
		}
		model.put("list", list);
		model.put("page", page);
		return new ModelAndView(this.getCustomPage("QueryAll"),model);
	}
	/**
	 * 最新上报的提案
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView latestSubmit(HttpServletRequest request,HttpServletResponse response)
			throws Exception{
		Map<String,Object> model=new HashMap<String,Object>();
		Page page=HttpUtil.convertPage(request);
		DetachedCriteria criteria=baseDomain.getCriteria(null).add(Restrictions.eq("status", "1"));
		List<Motion> list=baseDomain.queryByCriteria(criteria,page);
		for (Motion m : list) {
			Users u=(Users) userDomain.query(m.getCreateId());
			m.setMotionMan(u.getTname());
			Organ o=(Organ) organDomain.query(u.getOrganId());
			m.setDept(o.getTname());
			m.setDuty(u.getDuty());
			m.setMobilNumber(u.getMobilNumber());
			int count=fuyiDomain.findFuyiCount(m.getId(), "1");//赞同票
			int fuyiCount=fuyiDomain.findFuyiCount(m.getId(), null);
			m.setAgreeNum(count+"");
			m.setFyrNum(fuyiCount+"");
		}
		model.put("list", list);
		model.put("page", page);
		return new ModelAndView(this.getCustomPage("LatestSubmit"),model);
	}
	/**
	 * 最新反馈
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView latestFeedback(HttpServletRequest request,HttpServletResponse response)
			throws Exception{
		Map<String,Object> model=new HashMap<String,Object>();
		Page page=HttpUtil.convertPage(request);
		String sql="select distinct motionid from T_OA_CHENGBANRECORD order by id desc limit "+page.getPageIndex()+","+(page.getPageIndex()+page.getPageSize());
		List<String> motionidList=baseDomain.queryBySql(sql, null);
//		List<Motion> list=baseDomain.queryByHql("from T_OA_MOTION m where m.id=?", motionidList, page);
		if(motionidList.size()>0){
			List<Motion> list=baseDomain.queryByProperty("id", motionidList);
			for (Motion motion : list) {
				Users user=(Users) userDomain.query(motion.getCreateId());
				Organ o=(Organ) organDomain.query(user.getOrganId());
				motion.setDept(o.getTname());
			}
			model.put("list", list);
		}
		page.setTotalCount(motionidList.size());
		model.put("page", page);
		return new ModelAndView(this.getCustomPage("LatestFeedback"),model);
	}
	/**
	 * 最新审批
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView latestExam(HttpServletRequest request,HttpServletResponse response)
			throws Exception{
		Map<String,Object> model=new HashMap<String,Object>();
		Page page=HttpUtil.convertPage(request);
		DetachedCriteria criteria=baseDomain.getCriteria(null).add(Restrictions.eq("status", "3"));
		List<Motion> list=baseDomain.queryByCriteria(criteria,page);
		for (Motion m : list) {
			Users u=(Users) userDomain.query(m.getCreateId());
			m.setMotionMan(u.getTname());
			Organ o=(Organ) organDomain.query(u.getOrganId());
			m.setDept(o.getTname());
			m.setDuty(u.getDuty());
			m.setMobilNumber(u.getMobilNumber());
		}
		model.put("list", list);
		model.put("page", page);
		return new ModelAndView(this.getCustomPage("LatestExam"),model);
	}
	/**
	 * 最新上报详情
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView latestSubmitDetail(HttpServletRequest request,HttpServletResponse response)
			throws Exception{
		//提案信息
		Map<String,Object> model=new HashMap<String,Object>();
		String id=request.getParameter("id");
		Motion m=(Motion) baseDomain.query(id);
		Users u=(Users) userDomain.query(m.getCreateId());
		m.setMotionMan(u.getTname());
		Organ o=(Organ) organDomain.query(u.getOrganId());
		m.setDept(o.getTname());
		m.setDuty(u.getDuty());
		m.setMobilNumber(u.getMobilNumber());
		model.put("obj", m);
		
		//查询处理过程
		DetachedCriteria criteria= fuyiRecordDomain.getCriteria(null).add(Restrictions.eq("motionId", id));
		List<FuyiRecord> list=fuyiRecordDomain.queryByCriteria(criteria);
		model.put("fuyiRecords", list);
		return new ModelAndView(this.getCustomPage("LatestSubmitDetail"),model);
	}
	/**
	 * 最新反馈详情
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView latestFeedbackDetail(HttpServletRequest request,HttpServletResponse response)
			throws Exception{
		Map<String,Object> model=new HashMap<String,Object>();
		String id=request.getParameter("id");
		Motion m=(Motion) baseDomain.query(id);
		Users u=(Users) userDomain.query(m.getCreateId());
		m.setMotionMan(u.getTname());
		Organ o=(Organ) organDomain.query(u.getOrganId());
		m.setDept(o.getTname());
		m.setDuty(u.getDuty());
		m.setMobilNumber(u.getMobilNumber());
		model.put("obj", m);
		
		//查询处理过程
		DetachedCriteria criteria= fuyiRecordDomain.getCriteria(null).add(Restrictions.eq("motionId", id));
		List<FuyiRecord> list=fuyiRecordDomain.queryByCriteria(criteria);
		model.put("fuyiRecords", list);
		return new ModelAndView(this.getCustomPage("LatestFeedbackDetail"),model);
	}
	/**
	 * 最新审批详情
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView latestExamDetail(HttpServletRequest request,HttpServletResponse response)
			throws Exception{
		Map<String,Object> model=new HashMap<String,Object>();
		String id=request.getParameter("id");
		Motion m=(Motion) baseDomain.query(id);
		Users u=(Users) userDomain.query(m.getCreateId());
		m.setMotionMan(u.getTname());
		Organ o=(Organ) organDomain.query(u.getOrganId());
		m.setDept(o.getTname());
		m.setDuty(u.getDuty());
		m.setMobilNumber(u.getMobilNumber());
		model.put("obj", m);
		
		//查询处理过程
		DetachedCriteria criteria= fuyiRecordDomain.getCriteria(null).add(Restrictions.eq("motionId", id));
		List<FuyiRecord> list=fuyiRecordDomain.queryByCriteria(criteria);
		model.put("fuyiRecords", list);
		return new ModelAndView(this.getCustomPage("LatestExamDetail"),model);
	}
}
