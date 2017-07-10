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
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.servlet.ModelAndView;

import com.starsoft.core.controller.BaseAjaxController;
import com.starsoft.core.controller.BaseInterface;
import com.starsoft.core.domain.OrganDomain;
import com.starsoft.core.domain.UsersDomain;
import com.starsoft.core.entity.Organ;
import com.starsoft.core.entity.Users;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.Page;
import com.starsoft.oa.domain.FuyiDomain;
import com.starsoft.oa.domain.MotionDomain;
import com.starsoft.oa.domain.QianpiDomain;
import com.starsoft.oa.domain.QianpiRecordDomain;
import com.starsoft.oa.entity.Chengban;
import com.starsoft.oa.entity.Fuyi;
import com.starsoft.oa.entity.Motion;
import com.starsoft.oa.entity.Qianpi;
import com.starsoft.oa.entity.QianpiRecord;

/**
 * 
 * @Description 签批
 * @author 赵一好
 * @date 2016-11-11 上午11:15:35
 * 
 */
public class QianpiController extends MyBaseAjaxController implements
		BaseInterface {

	@Autowired
	private MotionDomain motionDomain;
	@Autowired
	private UsersDomain usersDomain;
	@Autowired
	private OrganDomain organDomain;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private FuyiDomain fuyiDomain;
	@Autowired
	private QianpiDomain qianpiDomain;
	
	@Autowired
	private QianpiRecordDomain qianpiRecordDomain;

	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> model = HttpUtil.convertModel(request);
		Users user = (Users) request.getSession().getAttribute("SESSONUSER");
		Page page = HttpUtil.convertPage(request);
		DetachedCriteria criteria = qianpiDomain.getCriteria(null);
		criteria.add(Restrictions.eq("qpr", user.getId()));
		criteria.add(Restrictions.lt("mark", "2"));
		criteria.addOrder(Property.forName("time").desc());
		List<Qianpi> qianpis = qianpiDomain.queryByCriteria(criteria,page);
		List<Motion> motions = new ArrayList<Motion>();
		for (Qianpi qianpi : qianpis) {
			String motionId = qianpi.getMotionid();
			Motion motion = (Motion) motionDomain.queryFirstByProperty("id",
					motionId);
			motions.add(motion);
		}
		// 查找附议人数
		for (Motion motion : motions) {
			// 查找附议总人数
			String fyrNum = fuyiDomain.findFuyiCount(motion.getId(), null) + "";
			motion.setFyrNum(fyrNum);
		}
		model.put("lists", motions);
		model.put("page", page);
		return new ModelAndView(this.getListPage(), model);
	}
	
	public void save(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String gotourl = "/motion.do";
		String qprId = HttpUtil.getString(request, "qprId", "");
		String motionId = HttpUtil.getString(request, "motionId", "");
		
		// 查找签批记录表中是否存在
		DetachedCriteria criteria = super.convertCriteria(request);
		criteria.add(Restrictions.eq("motionid", motionId));
		List<Qianpi> qianpis = this.baseDomain.queryByCriteria(criteria);
		if(qianpis.size()>0){
			DetachedCriteria criteria2 = qianpiRecordDomain.getCriteria(null);
			criteria2.add(Restrictions.eq("motionId", motionId));
			criteria2.add(Restrictions.eq("qpr", qprId));
			criteria2.add(Restrictions.eq("mark", "0"));
			List<QianpiRecord> qianpiRecords = qianpiRecordDomain.queryByCriteria(criteria2);
			if(qianpiRecords.size()>0){
				this.outFailString(request, response, "已上报过此人未通过，请重新选择其他人!", gotourl);
				return;
			}else{
				this.outFailString(request, response, "已上报过此人,无需重新上报!", gotourl);
				return;
			}
		}else{
			// 向签批表中插入一条数据，同时修改议案的状态为签批中
			Qianpi qianpi = (Qianpi) this.baseDomain.getBaseObject();
			qianpi.setTname("签批");
			qianpi.setTime(new Date());
			qianpi.setMotionid(motionId);
			// 0代表是未读记录
			qianpi.setMark("0");
			// 签批人
			qianpi.setQpr(qprId);
			this.saveBaseInfoToObject(request, qianpi);
			qianpiDomain.saveQpAndUpdateMot(qianpi, motionId);
		}
		this.outSuccessString(request, response, gotourl);
	}

	// 根据id查找出对应议案信息跳转到编辑页面
	public ModelAndView edit(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session =  request.getSession();
		String id = HttpUtil.getString(request, "id", "");
		removeSesMotRec(session, id);
		Motion motion = (Motion) motionDomain.query(id);
		Map<String, Object> model = HttpUtil.convertModel(request);
		if (motion != null) {
			// 查找此提案的提案人信息
			Users users = (Users) usersDomain.query(motion.getCreateId());
			// 查部门信息
			Organ organ = (Organ) organDomain.query(users.getOrganId());

			// 查找该条议案的附议人详情,去附议记录表中查找
			String sql = "select u.tname as tname,u.duty as duty,u.mobilNumber as mobilNumber,o.tname as organ,f.content as ad,f.time as fytime,f.mark as fymark from t_core_user u LEFT JOIN t_oa_fuyirecord f on f.fyr = u.id LEFT JOIN t_core_organ o on u.organId = o.id where f.motionId = ?";
			List<Users> fyrs = jdbcTemplate.query(sql,
					new Object[] { motion.getId() }, new RowMapper() {
						public Object mapRow(ResultSet rs, int i)
								throws SQLException {
							Users user = new Users();
							// 附议人名称
							user.setTname(rs.getString("tname"));
							// 附议人电话
							user.setMobilNumber(rs.getString("mobilNumber"));
							// 附议人职位
							user.setDuty(rs.getString("duty"));
							// 附议人所在单位
							user.setOrganId(rs.getString("organ"));
							// 附议人建议
							user.setRemarker(rs.getString("ad"));
							// 附议时间
							user.setOutDate(rs.getString("fytime"));
							// 附议人是否附议
							user.setFymark(rs.getString("fymark"));
							return user;
						}

					});
			model.put("fyrs", fyrs);
			model.put("organ", organ);
			model.put("users", users);
			model.put("obj", motion);
		}

		// 修改签批表中mark的状态信息为已读未处理
		Users user = (Users)session.getAttribute("SESSONUSER");
		DetachedCriteria criteria = qianpiDomain.getCriteria(null);
		criteria.add(Restrictions.eq("qpr", user.getId()));
		criteria.add(Restrictions.eq("motionid", motion.getId()));
		List lists = qianpiDomain.queryByCriteria(criteria);
		if (lists.size() > 0) {
			Qianpi qianpi = (Qianpi) lists.get(0);
			if(qianpi.getMark().equals("0")){
				qianpi.setMark("1");
			}
			this.baseDomain.update(qianpi);
		}

		findMotRecs(id, model);
		// 跳转到编辑页面
		return new ModelAndView(this.getEditPage(), model);
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
