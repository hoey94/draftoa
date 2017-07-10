package com.starsoft.oa.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.starsoft.core.controller.BaseAjaxController;
import com.starsoft.core.controller.BaseInterface;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.oa.domain.LianReturnDomain;
import com.starsoft.oa.domain.LianReturnRecordDomain;
import com.starsoft.oa.domain.MotionDomain;
import com.starsoft.oa.entity.LianReturn;
import com.starsoft.oa.entity.LianReturnRecord;
import com.starsoft.oa.entity.Motion;

/**
 * 
 * @Description 立案回复记录
 * @author 赵一好
 * @date 2016-11-18 上午11:59:57
 *
 */

public class LianReturnRecordController extends BaseAjaxController implements
		BaseInterface {

	@Autowired
	private LianReturnDomain lianReturnDomain;
	
	@Autowired
	private LianReturnRecordDomain lianReturnRecordDomain;
	
	
	// 向立案记录表中插入一条数据
	public void save(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String gotourl = "/index.do";
		LianReturnRecord lianReturnRecord = (LianReturnRecord) this.baseDomain.getBaseObject();
		String mark = HttpUtil.getString(request, "mark","0");
		this.bind(request, lianReturnRecord);
		this.saveBaseInfoToObject(request, lianReturnRecord);
		lianReturnRecord.setTime(new Date());
		lianReturnRecord.setTname("立案回复");
		String motionId = HttpUtil.getString(request, "motionId","");
		LianReturn lianReturn = (LianReturn) lianReturnDomain.queryFirstByProperty("motionId", motionId);
		lianReturnRecord.setLar(lianReturn.getCreateId());
		// 更新立案表中为已读状态
		lianReturn.setRead_index((Integer.parseInt(lianReturn.getRead_index())+1)+"");
		lianReturnDomain.update(lianReturn);
		lianReturnRecordDomain.saveLaReturnRecAndUpdateMot(motionId, mark,lianReturnRecord);
		
		/*
		
		this.saveBaseInfoToObject(request, lianReturnRecord);
		lianReturnRecordDomain.save(lianReturnRecord);
		
		// 更新立案表中为已读状态
		lianReturn.setRead_index((Integer.parseInt(lianReturn.getRead_index())+1)+"");
		lianReturnDomain.update(lianReturn);
		
		// 设置议案状态为7
		Motion motion = (Motion) motionDomain.query(motionId);
		motion.setStatus("7");
		motionDomain.update(motion);
		*/
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
