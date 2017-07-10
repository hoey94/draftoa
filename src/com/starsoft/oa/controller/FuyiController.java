package com.starsoft.oa.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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
import com.starsoft.core.domain.OrganDomain;
import com.starsoft.core.domain.UsersDomain;
import com.starsoft.core.entity.Organ;
import com.starsoft.core.entity.Users;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.Page;
import com.starsoft.oa.domain.ChengbanDomain;
import com.starsoft.oa.domain.FuyiDomain;
import com.starsoft.oa.domain.MotionDomain;
import com.starsoft.oa.domain.QianpiDomain;
import com.starsoft.oa.entity.Chengban;
import com.starsoft.oa.entity.Fuyi;
import com.starsoft.oa.entity.Motion;
import com.starsoft.oa.entity.Qianpi;

/**
 * 
 * @Description 附议
 * @author 赵一好
 * @date 2016-11-10 上午11:56:08
 * 
 */
public class FuyiController extends MyBaseAjaxController implements
		BaseInterface {

	@Autowired
	private MotionDomain motionDomain;

	@Autowired
	private UsersDomain usersDomain;

	@Autowired
	private OrganDomain organDomain;
	
	@Autowired
	private FuyiDomain fuyiDomain;

	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> model = HttpUtil.convertModel(request);
		Users user = (Users) request.getSession().getAttribute("SESSONUSER");
		Page page = HttpUtil.convertPage(request);
		DetachedCriteria criteria = fuyiDomain.getCriteria(null);
		criteria.add(Restrictions.eq("fyr", user.getId()));
		criteria.add(Restrictions.lt("mark", "2"));
		criteria.add(Restrictions.ne("createId", user.getId()));
		List<Fuyi> fuyis = fuyiDomain.queryByCriteria(criteria,page);
		List<Motion> motions = new ArrayList<Motion>();
		for (Fuyi fuyi : fuyis) {
			String motionId = fuyi.getMotionid();
			Motion motion = (Motion) motionDomain.queryFirstByProperty("id", motionId);
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
	
	public ModelAndView edit(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> model = HttpUtil.convertModel(request);
		HttpSession session = request.getSession();
		// 跳转到编辑页面，根据议案id查找出对应信息
		String id = HttpUtil.getString(request, "id", "");
		removeSesMotRec(session, id);
		Motion motion = (Motion) motionDomain.query(id);
		
		if (motion != null) {
			// 查找此提案的提案人信息
			String createId = motion.getCreateId();
			Users users = (Users) usersDomain.query(createId);
			// 查部门信息
			Organ organ = (Organ) organDomain.query(users.getOrganId());
			model.put("organ", organ);
			model.put("users", users);
			model.put("obj", motion);
		}

		// 修改议案为已读未处理状态
		Users user = (Users) session.getAttribute("SESSONUSER");
		DetachedCriteria criteria = super.convertCriteria(request);
		criteria.add(Restrictions.eq("fyr", user.getId()));
		criteria.add(Restrictions.eq("motionid", motion.getId()));
		List lists = this.baseDomain.queryByCriteria(criteria);
		if (lists.size() > 0) {
			Fuyi fuyi = (Fuyi) lists.get(0);
			if(fuyi.getMark().equals("0")){
				fuyi.setMark("1");
			}
			this.baseDomain.update(fuyi);
		}
		
		findMotRecs(id, model);
		
		return new ModelAndView("oa/fuyi/fuyiEdit", model);
	}
	
	// 添加附议人
	public void addFyr(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		String gotourl = "/motion.do";
		// 向附议表中插入一条数据
		String motionid = HttpUtil.getString(req, "motionid", "");
		String fyr = HttpUtil.getString(req, "createId", "");
		if(motionid==null || motionid.equals("")){
			this.outFailString(req, resp, "操作失败", gotourl);
		}
		if(fyr==null || fyr.equals("")){
			this.outFailString(req, resp, "操作失败", gotourl);
		}
		// 查找是否存在，如果不存在则往表中插入一条数据
		DetachedCriteria criteria = super.convertCriteria(req);
		criteria.add(Restrictions.eq("motionid", motionid));
		criteria.add(Restrictions.eq("fyr",fyr));
		List lists = this.baseDomain.queryByCriteria(criteria);
		if(lists.size()>0){
			this.outFailString(req, resp, "附议人重复,请重试", gotourl);
		}
		// 向附议表中插入一条数据
		Fuyi fuyi = new Fuyi();
		fuyi.setFyr(fyr);
		fuyi.setMotionid(motionid);
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//fuyi.setTime(sdf.format(new Date()));
		fuyi.setTime(new Date());
		fuyi.setTname("附议");
		fuyi.setMark("0");
		this.saveBaseInfoToObject(req, fuyi);
		this.baseDomain.save(fuyi);
		this.outSuccessString(req, resp, gotourl);
	}
	
	public ModelAndView add(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		return null;
	}

	
	public ModelAndView read(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		return null;
	}

	
	public void save(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

	}

	
	public void update(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
	}

}
