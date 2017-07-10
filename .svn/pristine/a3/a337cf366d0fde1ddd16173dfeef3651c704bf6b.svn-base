package com.starsoft.oa.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.starsoft.core.controller.BaseAjaxController;
import com.starsoft.core.domain.OrganDomain;
import com.starsoft.core.domain.UsersDomain;
import com.starsoft.core.entity.Users;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.oa.domain.ChengbanDomain;
import com.starsoft.oa.domain.ChengbanRecordDomain;
import com.starsoft.oa.domain.FuyiDomain;
import com.starsoft.oa.domain.FuyiRecordDomain;
import com.starsoft.oa.domain.LianReturnRecordDomain;
import com.starsoft.oa.domain.LianshenpiDomain;
import com.starsoft.oa.domain.MotionDomain;
import com.starsoft.oa.domain.QianpiRecordDomain;
import com.starsoft.oa.entity.Chengban;
import com.starsoft.oa.entity.ChengbanRecord;
import com.starsoft.oa.entity.FuyiRecord;
import com.starsoft.oa.entity.LianReturnRecord;
import com.starsoft.oa.entity.Lianshenpi;
import com.starsoft.oa.entity.Motion;
import com.starsoft.oa.entity.QianpiRecord;
/**
 * 
 * @Description 继承BaseAjaxController拓展或重写方法
 * @author 赵一好
 * @date 2016-11-24 上午11:40:43
 *
 */
public class MyBaseAjaxController extends BaseAjaxController{

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private MotionDomain motionDomain;
	
	@Autowired
	private FuyiRecordDomain fuyiRecordDomain;
	@Autowired
	private QianpiRecordDomain qianpiRecordDomain;
	
	@Autowired
	private ChengbanDomain chengbanDomain;
	
	@Autowired
	private ChengbanRecordDomain chengbanRecordDomain;
	
	@Autowired
	private LianshenpiDomain lianshenpiDomain;
	
	@Autowired
	private LianReturnRecordDomain lianReturnRecordDomain;
	
	public void findMotionById(HttpServletRequest request,
			Map<String, Object> model) {
		// 根据页面传来的议案id查找出议案信息
		String id = HttpUtil.getString(request, "id", "");
		Motion motion = (Motion) motionDomain.query(id);
		model.put("obj", motion);

		// 根据议案的id查找出提案人信息
		String createId = motion.getCreateId();
		String sql1 = "select u.id,u.tname,u.duty,o.tname organId,u.mobilNumber from t_core_user u LEFT JOIN t_core_organ o on u.organId = o.id where u.id = ?";
		List lists = mySelect(createId, sql1);
		model.put("user", lists.get(0));

		// 去附议记录表中查找附议记录信息
		String sql2 = "select u.id,u.tname,u.duty,o.tname organId,u.mobilNumber from t_core_user u  LEFT JOIN t_core_organ o on u.organId = o.id where u.id in( select fyr from t_oa_fuyirecord where motionId = ?)";
		List<Users> fyrs = mySelect(id, sql2);
		model.put("fyrs", fyrs);
	}
	
	public List mySelect(String param, String sql) {
		return jdbcTemplate.query(sql, new Object[] { param }, new RowMapper() {

			public Object mapRow(ResultSet rs, int i) throws SQLException {
				Users user = new Users();
				user.setId(rs.getString("id"));
				user.setTname(rs.getString("tname"));
				user.setDuty(rs.getString("duty"));
				// 查到所属部门
				user.setOrganId(rs.getString("organId"));
				user.setMobilNumber(rs.getString("mobilNumber"));
				return user;
			}
		});
	}
	/**
	 * 
	 * 移除session中的现有提案提醒记录
	 * @param session
	 * @param id
	 */
	public void removeSesMotRec(HttpSession session, String id) {
		List<Motion> xytas = (List<Motion>) session.getAttribute("XYTA");
		for (Motion m : xytas) {
			if(m.getId().equals(id)){
				xytas.remove(m);
				break;
			}
		}
	}
	
	/** 
	 * 根据议案id查找出对应的过程信息，封装在model中
	 * @param id
	 * @param model
	 */
	public void findMotRecs(String id, Map<String, Object> model) {
		// 从附议记录表中，查找出复议流程信息
		DetachedCriteria fuyiCriteria = fuyiRecordDomain.getCriteria(null);
		fuyiCriteria.add(Restrictions.eq("motionId", id));
		List<FuyiRecord> fuyiRecords = fuyiRecordDomain.queryByCriteria(fuyiCriteria);
		model.put("fuyiRecords", fuyiRecords);
		
		// 从签批记录表中查找出签批流程信息
		DetachedCriteria qianpiCriteria = qianpiRecordDomain.getCriteria(null);
		qianpiCriteria.add(Restrictions.eq("motionId", id));
		List<QianpiRecord> qianpiRecords = qianpiRecordDomain.queryByCriteria(qianpiCriteria);
		model.put("qianpiRecords", qianpiRecords);
		
		// 从承办表中查找出承办流程信息
		DetachedCriteria chengbanCriteria = chengbanDomain.getCriteria(null);
		chengbanCriteria.add(Restrictions.eq("motionId", id));
		chengbanCriteria.add(Restrictions.isNotNull("clr"));
		List<Chengban> chengbans = chengbanDomain.queryByCriteria(chengbanCriteria);
		model.put("chengbans", chengbans);
		
		// 从承办回复表中查找出承办回复流程信息
		DetachedCriteria chengbanRecordCriteria = chengbanRecordDomain.getCriteria(null);
		chengbanRecordCriteria.add(Restrictions.eq("motionId", id));
		chengbanRecordCriteria.add(Restrictions.isNotNull("mark"));
		List<ChengbanRecord> chengbanRecords = chengbanRecordDomain.queryByCriteria(chengbanRecordCriteria);
		model.put("chengbanRecords", chengbanRecords);
		
		// 从立案记录表查找流程信息
		DetachedCriteria lianCriteria = lianshenpiDomain.getCriteria(null);
		lianCriteria.add(Restrictions.eq("motionId", id));
		lianCriteria.add(Restrictions.isNotNull("mark"));
		List<Lianshenpi> lianshenpis = lianshenpiDomain.queryByCriteria(lianCriteria);
		model.put("lianshenpis", lianshenpis);
		
		// 从立案回复记录表中查找流程信息
		DetachedCriteria lianReturnCriteria = lianReturnRecordDomain.getCriteria(null);
		lianReturnCriteria.add(Restrictions.eq("motionId", id));
		lianReturnCriteria.add(Restrictions.isNotNull("mark"));
		List<LianReturnRecord> lianReturnRecords = lianReturnRecordDomain.queryByCriteria(lianReturnCriteria);
		model.put("lianReturnRecords", lianReturnRecords);
	}
	
}
