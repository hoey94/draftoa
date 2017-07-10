package com.starsoft.oa.controller;

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
import com.starsoft.core.entity.Users;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.oa.domain.QianpiRecordDomain;
import com.starsoft.oa.entity.Motion;
import com.starsoft.oa.entity.QianpiRecord;

/**
 * 
 * @Description 签批记录
 * @author 赵一好
 * @date 2016-11-11 上午11:27:12
 * 
 */
public class QianpiRecordController extends BaseAjaxController implements
		BaseInterface {

	@Autowired
	private QianpiRecordDomain qianpiRecordDomain;

	// 当签批人点击保存时，向签批记录表中插入一条数据
	public void save(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String gotourl = "/index.do";
		QianpiRecord qianpiRecord = (QianpiRecord) this.baseDomain
				.getBaseObject();
		this.bind(request, qianpiRecord);
		String qpr = HttpUtil.getString(request, "qpr");
		qianpiRecord.setQpr(qpr);
		qianpiRecord.setTime(new Date());
		qianpiRecord.setTname("签批");
		this.saveBaseInfoToObject(request, qianpiRecord);

		String mark = HttpUtil.getString(request, "mark", "");
		String motionId = HttpUtil.getString(request, "motionId", "");
		Users user = (Users) request.getSession().getAttribute("SESSONUSER");
		qianpiRecordDomain.saveQianpiRecord(qianpiRecord, mark, motionId,
				user.getId());

		this.outSuccessString(request, response, gotourl);
	}

	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		return null;
	}

	public ModelAndView add(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		return null;
	}

	public ModelAndView edit(HttpServletRequest request,
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
