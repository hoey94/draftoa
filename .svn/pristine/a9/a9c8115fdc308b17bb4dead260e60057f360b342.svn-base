package com.starsoft.oa.controller;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.starsoft.core.controller.BaseAjaxController;
import com.starsoft.core.controller.BaseInterface;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.entity.Users;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.oa.domain.FuyiDomain;
import com.starsoft.oa.domain.FuyiRecordDomain;
import com.starsoft.oa.domain.MotionDomain;
import com.starsoft.oa.entity.Fuyi;
import com.starsoft.oa.entity.FuyiRecord;
import com.starsoft.oa.entity.Motion;

/**
 * 
 * @Description 附议
 * @author 赵一好
 * @date 2016-11-9 下午3:39:31
 * 
 */
public class FuyiRecordController extends BaseAjaxController implements
		BaseInterface {

	@Autowired
	private MotionDomain motionDomain;

	@Autowired
	private FuyiRecordDomain fuyiRecordDomain;

	public ModelAndView edit(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// 修改操作记录表中信息的状态

		return new ModelAndView(this.getEditPage());
	}

	// 向附议表中插入一条记录
	public void save(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String gotourl = "/index.do";
		String motionid = HttpUtil.getString(request, "id", "");
		// 向附议记录表中插入一条记录
		FuyiRecord fuyi = new FuyiRecord();
		Users session_user = (Users) request.getSession().getAttribute(
				"SESSONUSER");
		fuyi.setFyr(session_user.getId());
		fuyi.setMotionId(motionid);
		fuyi.setTname("附议");
		fuyi.setTime(new Date());
		fuyi.setContent(HttpUtil.getString(request, "content", ""));
		fuyi.setMark(HttpUtil.getString(request, "mark", ""));
		this.saveBaseInfoToObject(request, fuyi);

		Motion motion = (Motion) motionDomain.query(motionid);

		fuyiRecordDomain.saveFyRecAndUpdateMot(fuyi, motion,
				session_user.getId());

		this.outSuccessString(request, response, gotourl);
	}

	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		return new ModelAndView("oa/info/infoIndex");
	}

	public ModelAndView add(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		return null;
	}

	public ModelAndView read(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		return null;
	}

	public void update(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

	}

}
