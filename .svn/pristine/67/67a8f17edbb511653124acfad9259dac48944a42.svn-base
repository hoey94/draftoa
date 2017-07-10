package com.starsoft.oa.controller;

import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import com.starsoft.core.controller.BaseAjaxController;
import com.starsoft.core.controller.BaseInterface;
import com.starsoft.core.domain.OrganDomain;
import com.starsoft.core.domain.UsersDomain;
import com.starsoft.core.entity.Organ;
import com.starsoft.core.entity.Users;
import com.starsoft.core.util.HashUtil;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.Page;
import com.starsoft.core.util.WEBCONSTANTS;
import com.starsoft.oa.domain.ChengbanDomain;
import com.starsoft.oa.domain.ChengbanRecordDomain;
import com.starsoft.oa.domain.FuyiDomain;
import com.starsoft.oa.domain.LianReturnDomain;
import com.starsoft.oa.domain.LianshenpiDomain;
import com.starsoft.oa.domain.MotionDomain;
import com.starsoft.oa.domain.QianpiDomain;
import com.starsoft.oa.domain.StatisticDomain;
import com.starsoft.oa.entity.Chengban;
import com.starsoft.oa.entity.ChengbanRecord;
import com.starsoft.oa.entity.Fuyi;
import com.starsoft.oa.entity.LianReturn;
import com.starsoft.oa.entity.Lianshenpi;
import com.starsoft.oa.entity.Motion;
import com.starsoft.oa.entity.MotionsCount;
import com.starsoft.oa.entity.Qianpi;

/**
 * 
 * @Description 首页控制器
 * @author 赵一好
 * @date 2016-11-8 上午10:24:46
 * 
 */
public class IndexController extends BaseAjaxController implements
		BaseInterface {

	@Autowired
	private UsersDomain usersDomain;
	
	@Autowired
	private MotionDomain motionDomain;

	@Autowired
	private OrganDomain organDomain;
	
	@Autowired
	private QianpiDomain qianpiDomain;

	@Autowired
	private ChengbanDomain chengbanDomain;

	@Autowired
	private FuyiDomain fuyiDomain;
	
	@Autowired
	private ChengbanRecordDomain chengbanRecordDomain;
	
	@Autowired
	private LianshenpiDomain lianshenpiDomain;
	
	@Autowired
	private LianReturnDomain lianReturnDomain;
	
	@Autowired
	private StatisticDomain statisticDomain;
	

	public ModelAndView list(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		Map<String,Object> model = HttpUtil.convertModel(req);
		Users session_users = (Users) req.getSession().getAttribute(
				"SESSONUSER");
		if (session_users == null) {
			// 跳转到登录界面
			return new ModelAndView(this.getCustomPage("login"),model);
		}else{
			// 查找附议中的议案数
			int fyCount = motionDomain.queryCountForStatus("0",session_users.getId());
			model.put("fyCount", fyCount);
			// 查找承办中的议案数
			int cbCount = motionDomain.queryCountForStatus("4",session_users.getId());
			model.put("cbCount", cbCount);
			// 查找立案中的议案数
			int laCount = motionDomain.queryCountForStatus("6",session_users.getId());
			model.put("laCount", laCount);
			// 查找已通过
			int tgCount = motionDomain.queryCountForStatus("7",session_users.getId());
			model.put("tgCount", tgCount);	
			
			// 统计当前年份的数量信息
			Calendar a = Calendar.getInstance();
			int year = a.get(Calendar.YEAR);
			List<MotionsCount> counts = statisticDomain.staMotionPerMonth(year);
			
			/*
			Integer[][] data=new Integer[counts.size()][2];
			for(int i = 0;i<counts.size();i++){
				 MotionsCount swdao = counts.get(i);
				 data[i][0]=swdao.getMonth();
				 data[i][1]=swdao.getCount();
			}
			System.out.println(Arrays.toString(data));
			model.put("myData", data);
			*/
			
			/*
			JSONArray jsonArray = new JSONArray();
			for (MotionsCount motionsCount : counts) {
				JSONObject jsonObject = new JSONObject();
				for (int i = 1; i < 13; i++) {
					if(motionsCount.getMonth() == i){
						jsonObject.put(i+"",motionsCount.getCount());
					}
				}
				jsonArray.put(jsonObject);
			}
			System.out.println(jsonArray.toString());
			model.put("myData", jsonArray);
			*/
			
			return new ModelAndView(this.getCustomPage("index"),model);
		}

	}
	

	public ModelAndView login(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String gotourl="index.do";
		Map<String,Object> model = HttpUtil.convertModel(request);
		String method=request.getMethod().toLowerCase();
		Page page = HttpUtil.convertPage(request);
		String id=HttpUtil.getString(request, "id","");
		String passWord=HttpUtil.getString(request, "password","");
		String txtyzm=HttpUtil.getString(request, "txtyzm","");
		String password = HashUtil.toMD5(passWord);
		DetachedCriteria criteria = usersDomain.getCriteria(null);
		criteria.add(Restrictions.and(Restrictions.eq("id", id), Restrictions.eq("password", password)));
		List list= usersDomain.queryByCriteria(criteria, page);
		String msg = "";
		if (method.equals("post")) {
		if (txtyzm != null	&& txtyzm.equalsIgnoreCase((String) request.getSession().getAttribute("verifycode"))) {
			if (list.size()>0) {
				WebUtils.setSessionAttribute(request, WEBCONSTANTS.SESSION_USER, (Users)list.get(0));
				findRecord(request, response);
				return new ModelAndView("forward:/index.do?action=list");
			}else{
				//this.outFailString(request, response, "用户名或者密码不正确!",gotourl);
				msg = "用户名或者密码不正确!";
				model.put("msg", msg);
				return new ModelAndView("oa/info/infoLogin",model);
			}
		}else{
			//this.outFailString(request, response, "验证码错误!",gotourl);
			msg = "验证码错误!";
			model.put("msg", msg);
			return new ModelAndView("oa/info/infoLogin",model);
		}
	  }else{
			//this.outFailString(request, response, WEBCONSTANTS.FAILINFOR, gotourl);
		  	msg = "操作失败!";
			model.put("msg", msg);
			return new ModelAndView("oa/info/infoLogin",model);
		}
	}

	public ModelAndView logout(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {

		// 拿到当前session中的user，将其过期
		HttpSession session = req.getSession();
		session.removeAttribute("SESSONUSER");
		session.invalidate();
		return new ModelAndView(this.getCustomPage("login"));
	}


	public void findRecord(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		Users session_users = (Users) req.getSession().getAttribute(
				"SESSONUSER");
		// 查询投给自己的信息记录并且是未读状态的
		Organ organ = (Organ) organDomain.query(session_users.getOrganId());
		String organName = organ.getTname();
		List<Motion> lists = new ArrayList<Motion>();
		if (organName.equals("承办部门主管领导")) {
			// 去承办表中查
			DetachedCriteria criteria = chengbanRecordDomain.getCriteria(null);
			criteria.add(Restrictions.eq("cbr", session_users.getId()));
			criteria.add(Restrictions.eq("read_index", "0"));
			criteria.addOrder(Property.forName("time").desc());
			List<ChengbanRecord> ors = chengbanRecordDomain.queryByCriteria(criteria);
			for (ChengbanRecord chengbanRecord : ors) {
				Motion motion = (Motion) this.motionDomain.query(chengbanRecord.getMotionId());
				lists.add(motion);
			}
		} else if (organName.equals("工会主管领导")) {
			// 去签批表中查
			DetachedCriteria criteria = qianpiDomain.getCriteria(null);
			criteria.add(Restrictions.eq("qpr", session_users.getId()));
			criteria.add(Restrictions.eq("mark", "0"));
			criteria.addOrder(Property.forName("time").desc());
			List<Qianpi> ors = qianpiDomain.queryByCriteria(criteria);
			for (Qianpi qianpi : ors) {
				Motion motion = (Motion) this.motionDomain.query(qianpi
						.getMotionid());
				lists.add(motion);
			}
		}else if (organName.equals("承办部门")) {
			// 去承办表中查找
			DetachedCriteria criteria = chengbanDomain.getCriteria(null);
			criteria.add(Restrictions.eq("read_index", "0"));
			criteria.addOrder(Property.forName("time").desc());
			List<Chengban> ors = chengbanDomain
					.queryByCriteria(criteria);
			for (Chengban chengban : ors) {
				Motion motion = (Motion) this.motionDomain
						.query(chengban.getMotionId());
				lists.add(motion);
			}
		}else if (organName.equals("承办部门领导")) {
			// 去承办表中查找
			DetachedCriteria criteria = chengbanRecordDomain.getCriteria(null);
			criteria.add(Restrictions.eq("read_index", "0"));
			criteria.addOrder(Property.forName("time").desc());
			List<ChengbanRecord> ors = chengbanRecordDomain
					.queryByCriteria(criteria);
			for (ChengbanRecord chengbanRecord : ors) {
				Motion motion = (Motion) this.motionDomain
						.query(chengbanRecord.getMotionId());
				lists.add(motion);
			}
		}else if (organName.equals("校工会")) {
			// 去校工会表中查找
			DetachedCriteria criteria = lianshenpiDomain.getCriteria(null);
			criteria.add(Restrictions.eq("read_index", "0"));
			criteria.addOrder(Property.forName("time").desc());
			List<Lianshenpi> ors = lianshenpiDomain
					.queryByCriteria(criteria);
			for (Lianshenpi lianshenpi : ors) {
				Motion motion = (Motion) this.motionDomain.query(lianshenpi.getMotionId());
				lists.add(motion);
			}
		}else if (organName.equals("校长")) {
			// 去立案回复表中查找
			DetachedCriteria criteria = lianReturnDomain.getCriteria(null);
			criteria.add(Restrictions.eq("read_index", "0"));
			criteria.addOrder(Property.forName("time").desc());
			List<LianReturn> ors = lianReturnDomain
					.queryByCriteria(criteria);
			for (LianReturn lianReturn : ors) {
				Motion motion = (Motion) this.motionDomain.query(lianReturn.getMotionId());
				lists.add(motion);
			}
		}else {
			// 附议表查找
			DetachedCriteria criteria = fuyiDomain.getCriteria(null);
			criteria.add(Restrictions.eq("tname", "附议"));
			criteria.add(Restrictions.eq("fyr", session_users.getId()));
			criteria.add(Restrictions.eq("mark", "0"));
			criteria.addOrder(Property.forName("time").desc());
			List<Fuyi> ors = fuyiDomain
					.queryByCriteria(criteria);
			for (Fuyi fuyi : ors) {
				Motion motion = (Motion) this.motionDomain
						.query(fuyi.getMotionid());
				lists.add(motion);
			}
		}
		req.getSession().setAttribute("XYTA", lists);
	}
	
	public void registry(HttpServletRequest request,HttpServletResponse response) throws Exception{
		try {
			String id = HttpUtil.getString(request, "id", "");
			if(id !=null && !id.equals("")){
				Users user = (Users) usersDomain.query(id);
				if(user != null){
					this.outFailString(request, response, "当前用户已存在", "");
				}else{
					Users entity=new Users();
					this.bind(request, entity);
					this.saveBaseInfoToObject(request, entity);
					entity.setCreateId("admin");
					String tname = HttpUtil.getString(request, "duty","");
					entity.setTname(tname);
					String pass = HttpUtil.getString(request, "password");
					pass = HashUtil.toMD5(pass);
					entity.setPassword(pass);
					baseDomain.save(entity);
					this.outSuccessString(request, response, "");
				}
			}else{
				this.outFailString(request, response, "用户名为空", "");
			}
		} catch (Exception e) {
			this.outFailString(request, response, "注册失败", "");
		}
	}

	public ModelAndView add(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {

		return null;
	}

	public ModelAndView edit(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {

		return null;
	}

	public ModelAndView read(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {

		return null;
	}

	public void save(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {

	}

	public void update(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {

	}

}
