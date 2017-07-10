package com.starsoft.oa.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.starsoft.cms.task.DownLoadUtil;
import com.starsoft.cms.task.UpLoadUtil;
import com.starsoft.core.domain.UsersDomain;
import com.starsoft.core.entity.Users;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.Page;
import com.starsoft.core.util.StringUtil;
import com.starsoft.core.vo.FileUpload;
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

public class MotionController extends MyBaseAjaxController {

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
	@Autowired
	private UsersDomain usersDomain;
	@Autowired
	private FuyiDomain fuyiDomain;

	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// 查找数据库信息
		Map<String, Object> model = HttpUtil.convertModel(request);
		Page page = HttpUtil.convertPage(request);
		DetachedCriteria criteria = this.convertCriteria(request);
		List list = baseDomain.queryByCriteria(criteria, page);
		model.put("lists", list);
		model.put("page", page);
		return new ModelAndView(this.getListPage(), model);

	}

	public ModelAndView save(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String createId = HttpUtil.getString(request, "createId", "");

		String fyr = HttpUtil.getString(request, "fyr", "");
		Motion motion = new Motion();
		this.bind(request, motion);
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		motion.setTime(new Date());
		motion.setStatus("0");
		motion.setValid(true);
		motion.setZlh("ZLH"+StringUtil.generatorShort());
		// 保存提案到数据库
		motionDomain.saveMotAndRec(motion, createId, fyr);

		// 上传文件
		FileUpload entity = new FileUpload();
		bind(request, entity);
		String url = UpLoadUtil.myUpLoad(entity);
		if (!"".equals(url)) {
			motion.setUrl(url);
		}
		this.baseDomain.update(motion);

		// 跳转到列表界面
		return new ModelAndView("redirect:/motion.do?action=list");
	}

	// 查询立案提案(status=5) 通过的天(status=7)
	public ModelAndView pass(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String status = HttpUtil.getString(request, "status", "");

		// 查找数据库信息
		Map<String, Object> model = HttpUtil.convertModel(request);
		Page page = HttpUtil.convertPage(request);

		// 查找副单位是承办部门的领导，也就是校长
		String sql = "select m.id,m.tname,m.content,m.status,m.time,u.duty,u.mobilNumber,u.tname createId,o.tname organ from t_oa_motion m left join t_core_user u on m.createId = u.id LEFT JOIN t_core_organ o on u.organId = o.id where status > ?;";
		List<Motion> motions = jdbcTemplate.query(sql, new Object[] { status },
				new RowMapper() {
					public Object mapRow(ResultSet rs, int i)
							throws SQLException {
						Motion motion = new Motion();
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						motion.setId(rs.getString("id"));
						motion.setTname(rs.getString("tname"));
						motion.setCreateId(rs.getString("createId"));
						motion.setDept(rs.getString("organ"));
						motion.setDuty(rs.getString("duty"));
						motion.setMobilNumber(rs.getString("mobilNumber"));
						try {
							motion.setTime(sdf.parse(rs.getString("time")));
						} catch (ParseException e) {
							e.printStackTrace();
						}
						motion.setStatus(rs.getString("status"));
						return motion;
					}
				});
		model.put("lists", motions);
		model.put("page", page);
		return new ModelAndView("oa/motion/motionPass", model);
	}

	// 跳转到编辑页面
	public ModelAndView edit(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> model = HttpUtil.convertModel(request);
		String sql = "select id,tname from t_core_user where organid = ?";

		List<Users> fyrs = jdbcTemplate.query(sql, new Object[] { "005" },
				new RowMapper() {
					public Object mapRow(ResultSet rs, int i)
							throws SQLException {
						Users user = new Users();
						user.setId(rs.getString("id"));
						user.setTname(rs.getString("tname"));
						return user;
					}
				});
		Users user = (Users) request.getSession().getAttribute("SESSONUSER");
		for (Users users : fyrs) {
			String id = users.getId();
			if (id.equals(user.getId())) {
				fyrs.remove(users);
				break;
			}
		}
		model.put("fyrs", fyrs);
		return new ModelAndView(this.getEditPage(), model);
	}

	public ModelAndView add(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		String motionId = HttpUtil.getString(request, "id", "");
		// 查询提案信息
		Motion motion = (Motion) this.baseDomain.queryFirstByProperty("id",
				motionId);

		// 查找附议人信息
		String sql = "select id,tname from t_core_user where organid = ?";

		List<Users> fyrs = jdbcTemplate.query(sql, new Object[] { "005" },
				new RowMapper() {
					public Object mapRow(ResultSet rs, int i)
							throws SQLException {
						// 去session中的用户信息，如果是本人就直接return
						Users user = new Users();
						user.setId(rs.getString("id"));
						user.setTname(rs.getString("tname"));
						return user;
					}
				});
		Users user = (Users) request.getSession().getAttribute("SESSONUSER");
		for (Users users : fyrs) {
			String id = users.getId();
			if (id.equals(user.getId())) {
				fyrs.remove(users);
				break;
			}
		}
		// 查找签批人
		List<Users> qprs = jdbcTemplate.query(sql, new Object[] { "004" },
				new RowMapper() {
					public Object mapRow(ResultSet rs, int i)
							throws SQLException {
						// 去session中的用户信息，如果是本人就直接return
						Users user = new Users();
						user.setId(rs.getString("id"));
						user.setTname(rs.getString("tname"));
						return user;
					}
				});

		DetachedCriteria criteria = fuyiRecordDomain.getCriteria(null);
		criteria.add(Restrictions.eq("motionId", motionId));
		List<FuyiRecord> fuyiRecords = fuyiRecordDomain
				.queryByCriteria(criteria);

		DetachedCriteria criteria1 = qianpiRecordDomain.getCriteria(null);
		criteria1.add(Restrictions.eq("motionId", motionId));
		List<QianpiRecord> qianpiRecords = qianpiRecordDomain
				.queryByCriteria(criteria1);

		DetachedCriteria criteria2 = chengbanDomain.getCriteria(null);
		criteria2.add(Restrictions.eq("motionId", motionId));
		List<Chengban> chengbans = chengbanDomain.queryByCriteria(criteria2);

		DetachedCriteria criteria3 = chengbanRecordDomain.getCriteria(null);
		criteria3.add(Restrictions.eq("motionId", motionId));
		List<ChengbanRecord> chengbanRecords = chengbanRecordDomain
				.queryByCriteria(criteria3);

		DetachedCriteria criteria4 = lianshenpiDomain.getCriteria(null);
		criteria4.add(Restrictions.eq("motionId", motionId));
		List<Lianshenpi> lianshenpis = lianshenpiDomain
				.queryByCriteria(criteria4);

		DetachedCriteria criteria5 = lianReturnRecordDomain.getCriteria(null);
		criteria5.add(Restrictions.eq("motionId", motionId));
		List<LianReturnRecord> lianReturnRecords = lianReturnRecordDomain
				.queryByCriteria(criteria5);
		model.put("lianReturnRecords", lianReturnRecords);
		model.put("lianshenpis", lianshenpis);
		model.put("chengbanRecords", chengbanRecords);
		model.put("chengbans", chengbans);
		model.put("qianpiRecords", qianpiRecords);
		model.put("fyrs", fyrs);
		model.put("obj", motion);
		model.put("qprs", qprs);
		model.put("fuyiRecords", fuyiRecords);

		return new ModelAndView(this.getAddPage(), model);
	}

	public @ResponseBody void find(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> model = HttpUtil.convertModel(request);
		Page page = HttpUtil.convertPage(request);
		Users user = (Users) request.getSession().getAttribute("SESSONUSER");
		// 查询本人所提交的议案
		
		DetachedCriteria criteria = convertCriteria(request); 
		String zlh = HttpUtil.getString(request, "zlh","");
		if(!zlh.equals("")){
			criteria.add(Restrictions.eq("zlh", zlh));
		}
		String tar = HttpUtil.getString(request, "tar","");
		if(!tar.equals("")){
			criteria.add(Restrictions.eq("createId",tar));
		}
		String status = HttpUtil.getString(request, "status","");
		if(!status.equals("")){
			criteria.add(Restrictions.eq("status", status));
		}
		
		criteria.add(Restrictions.eq("createId", user.getId())); 
		List<Motion> motions = this.baseDomain.queryByCriteria(criteria);
		 

		/*String sql = "select m.id,m.tname,m.content,m.status,m.time,u.duty,u.mobilNumber,u.tname createId,o.tname organ from t_oa_motion m left join t_core_user u on m.createId = u.id LEFT JOIN t_core_organ o on u.organId = o.id where m.createId = ?;";
		List<Motion> motions = jdbcTemplate.query(sql, new Object[] { createId },
				new RowMapper() {
					public Object mapRow(ResultSet rs, int i)
							throws SQLException {
						Motion motion = new Motion();
						motion.setId(rs.getString("id"));
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						motion.setTname(rs.getString("tname"));
						motion.setCreateId(rs.getString("createId"));
						motion.setDept(rs.getString("organ"));
						motion.setDuty(rs.getString("duty"));
						motion.setMobilNumber(rs.getString("mobilNumber"));
						try {
							motion.setTime(sdf.parse(rs.getString("time")));
						} catch (ParseException e) {
							e.printStackTrace();
						}
						motion.setStatus(rs.getString("status"));
						return motion;
					}
				});*/

		// 查询附议同意票数和附议人数
		// 查找附议人数
		for (Motion motion : motions) {
			// 查找附议总人数
			String fyrNum = fuyiDomain.findFuyiCount(motion.getId(), null) + "";
			motion.setFyrNum(fyrNum);
			// 查找附议赞成人数
			String agreeNum = fuyiDomain.findFuyiCount(motion.getId(), "1") + "";
			motion.setAgreeNum(agreeNum);
		}
		
		model.put("lists", motions);
		model.put("page", page);
		this.outJsonObject(response, model);
	}

	public ModelAndView findList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		// 查找数据库信息
		Map<String, Object> model = HttpUtil.convertModel(request);
		Page page = HttpUtil.convertPage(request);
		
		Users user = (Users) request.getSession().getAttribute("SESSONUSER");
		DetachedCriteria criteria = this.convertCriteria(request);
		criteria.add(Restrictions.eq("createId", user.getId()));
		List<Motion> list = baseDomain.queryByCriteria(criteria, page);
		for (Motion motion : list) {
			// 查找附议总人数
			String fyrNum = fuyiDomain.findFuyiCount(motion.getId(), null) + "";
			motion.setFyrNum(fyrNum);
			// 查找附议赞成人数
			String agreeNum = fuyiDomain.findFuyiCount(motion.getId(), "1") + "";
			motion.setAgreeNum(agreeNum);
		}
		model.put("lists", list);
		model.put("page", page);
		return new ModelAndView("oa/motion/motionFind", model);

	}
	
	public void download(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String motionId = HttpUtil.getString(request, "motionId","");
		// 获取提案
		Motion motion = (Motion) this.baseDomain.queryFirstByProperty("id", motionId);
		String path = motion.getUrl();
		// 获取文件名称
        String[] split = path.split("\\\\");
        String fileName = split[split.length-1];
        
        DownLoadUtil.download(request, response, path, fileName);
        
	}
	
	public void update(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

	}

}
