package com.starsoft.oa.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.servlet.ModelAndView;

import com.starsoft.core.controller.BaseAjaxController;
import com.starsoft.core.controller.BaseInterface;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.entity.Users;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.Page;
import com.starsoft.oa.domain.ChengbanDomain;
import com.starsoft.oa.domain.ChengbanRecordDomain;
import com.starsoft.oa.domain.FuyiDomain;
import com.starsoft.oa.domain.LianshenpiDomain;
import com.starsoft.oa.domain.MotionDomain;
import com.starsoft.oa.entity.Chengban;
import com.starsoft.oa.entity.ChengbanRecord;
import com.starsoft.oa.entity.Lianshenpi;
import com.starsoft.oa.entity.Motion;

/**
 * 
 * @Description 承办部门领导
 * @author 赵一好
 * @date 2016-11-15 上午8:47:38
 * 
 */
public class ChengbanRecordController extends MyBaseAjaxController implements
		BaseInterface {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ChengbanRecordDomain chengbanRecordDomain;
	@Autowired
	private ChengbanDomain chengbanDomain;

	@Autowired
	private MotionDomain motionDomain;

	@Autowired
	private LianshenpiDomain lianshenpiDomain;
	
	@Autowired
	private FuyiDomain fuyiDomain;

	// 查找出所有发给自己的记录信息
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> model = HttpUtil.convertModel(request);
		Page page=HttpUtil.convertPage(request);
		Users user = (Users) request.getSession().getAttribute("SESSONUSER");
		DetachedCriteria criteria = chengbanRecordDomain.getCriteria(null);
		criteria.add(Restrictions.eq("cbr", user.getId()));
		criteria.add(Restrictions.lt("read_index", "2"));
		List<ChengbanRecord> chengbanRecords = chengbanRecordDomain.queryByCriteria(criteria,page);
		List<Motion> motions = new ArrayList<Motion>();
		for (ChengbanRecord ChengbanRecord : chengbanRecords) {
			String motionId = ChengbanRecord.getMotionId();
			Motion motion = (Motion) motionDomain.queryFirstByProperty("id",
					motionId);
			motions.add(motion);
		}
		// 查找附议人数
		for (Motion motion : motions) {
			// 查找附议总人数
			String fyrNum = fuyiDomain.findFuyiCount(motion.getId(), null) + "";
			motion.setFyrNum(fyrNum);
			
			String agreeNum = fuyiDomain.findFuyiCount(motion.getId(), "1") + "";
			motion.setAgreeNum(agreeNum);
		}
		model.put("lists", motions);
		model.put("page", page);

		return new ModelAndView(this.getListPage(), model);
	}

	// 跳转到编辑页面,回显议案信息
	public ModelAndView edit(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> model = HttpUtil.convertModel(request);
		String motionId = HttpUtil.getString(request, "id", "");
		HttpSession session =  request.getSession();
		removeSesMotRec(session, motionId);
		Users session_user = (Users) session.getAttribute(
				"SESSONUSER");

		// 自动将信息封装到model中
		findMotionById(request, model);
		// 查找承办人
		DetachedCriteria criteria = chengbanDomain.getCriteria(null);
		criteria.add(Restrictions.eq("cbr", session_user.getId()));
		criteria.add(Restrictions.eq("motionId", motionId));
		List<Chengban> chengbans = chengbanDomain.queryByCriteria(criteria);
		if (chengbans.size() > 0) {
			Chengban chengban = (Chengban) chengbans.get(0);
			model.put("chengban", chengban);
		}

		// 修改承办回复表中未已读1
		// updateOperationMark(request,"1",motionId);
		ChengbanRecord chengbanRecord = (ChengbanRecord) this.baseDomain
				.queryFirstByProperty("motionId", motionId);
		if(chengbanRecord.getRead_index().equals("0")){
			chengbanRecord.setRead_index("1");
		}
		this.baseDomain.update(chengbanRecord);

		return new ModelAndView(this.getEditPage(), model);
	}

	// 保存承办领导回复信息,保存到承办部门领导表中
	public void save(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String gotourl = "/index.do";
		// 将信息保存到承办部门领导回复表中
		String motionId = HttpUtil.getString(request, "motionId", "");
		String mark = HttpUtil.getString(request, "mark","");
		Users user = (Users) request.getSession().getAttribute("SESSONUSER");
		ChengbanRecord chengbanRecord = (ChengbanRecord) this.baseDomain
				.queryFirstByProperty("motionId", motionId);
		this.bind(request, chengbanRecord);
		chengbanRecordDomain.saveCbRecAndUpdateMot(motionId,mark,chengbanRecord,user.getId());
		
		this.outSuccessString(request, response, gotourl);

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
