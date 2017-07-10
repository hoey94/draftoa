package com.starsoft.oa.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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

import org.apache.commons.fileupload.util.Streams;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.starsoft.cms.task.DownLoadUtil;
import com.starsoft.cms.task.UpLoadUtil;
import com.starsoft.core.controller.BaseInterface;
import com.starsoft.core.domain.UsersDomain;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.entity.Users;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.core.util.Page;
import com.starsoft.core.vo.FileUpload;
import com.starsoft.oa.domain.ChengbanDomain;
import com.starsoft.oa.domain.ChengbanRecordDomain;
import com.starsoft.oa.domain.FuyiDomain;
import com.starsoft.oa.domain.MotionDomain;
import com.starsoft.oa.entity.Chengban;
import com.starsoft.oa.entity.ChengbanRecord;
import com.starsoft.oa.entity.Lianshenpi;
import com.starsoft.oa.entity.Motion;

/**
 * 
 * @Description 承办
 * @author 赵一好
 * @date 2016-11-14 下午2:23:41
 * 
 */
public class ChengbanController extends MyBaseAjaxController{

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private MotionDomain motionDomain;
	@Autowired
	private FuyiDomain fuyiDomain;
	@Autowired
	private ChengbanDomain chengbanDomain;
	@Autowired
	private ChengbanRecordDomain chengbanRecordDomain;

	// 跳转到承办列表,查询状态为3的议案信息
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> model = HttpUtil.convertModel(request);
		Page page = HttpUtil.convertPage(request);
		DetachedCriteria criteria = chengbanDomain.getCriteria(null);
		criteria.add(Restrictions.lt("read_index", "2"));
		List<Chengban> chengbans = chengbanDomain.queryByCriteria(criteria,page);
		List<Motion> motions = new ArrayList<Motion>();
		for (Chengban chengban : chengbans) {
			String motionId = chengban.getMotionId();
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

	// 跳转到编辑页面
	public ModelAndView edit(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String motionId = HttpUtil.getString(request, "id", "");
		HttpSession session =  request.getSession();
		removeSesMotRec(session, motionId);
		Map<String, Object> model = HttpUtil.convertModel(request);
		// 查找副单位是承办部门的领导，也就是校长
		String sql = "select id,tname from t_core_user where organid = ?";
		List<Users> users = jdbcTemplate.query(sql, new Object[] { "003" },
				new RowMapper() {
					public Object mapRow(ResultSet rs, int i)
							throws SQLException {
						Users user = new Users();
						user.setId(rs.getString("id"));
						user.setTname(rs.getString("tname"));
						return user;
					}
				});
		model.put("lists", users);

		// 修改承办表中的状态为已读
		Chengban chengban = (Chengban) chengbanDomain.queryFirstByProperty(
				"motionId", motionId);
		if(chengban.getRead_index().equals("0")){
			chengban.setRead_index("1");
		}
		chengbanDomain.update(chengban);
		findMotionById(request, model);
		findMotRecs(motionId, model);
		return new ModelAndView(this.getEditPage(), model);
	}

	public ModelAndView save(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 向承办表中插入一条数据
		String motionId = HttpUtil.getString(request, "motionId", "");
		Chengban chengban = (Chengban) this.baseDomain.queryFirstByProperty("motionId", motionId);
		this.bind(request, chengban);
		String cbr = HttpUtil.getString(request, "cbr","");
		Users user = (Users) request.getSession().getAttribute("SESSONUSER");
		chengbanDomain.saveChengbanAndRec(chengban,motionId,cbr,user.getId());
		
		/*// 修改承办表为2
		chengban.setRead_index((Integer.parseInt(chengban.getRead_index()) + 1) + "");

		// 修改议案的状态为4,表示为办理中
		Motion motion = (Motion) motionDomain.queryFirstByProperty("id",
				motionId);
		motion.setStatus((Integer.parseInt(motion.getStatus()) + 1) + "");
		motionDomain.update(motion);

		// 向承办回复表中插入一条数据
		ChengbanRecord chengbanRecord = new ChengbanRecord();
		chengbanRecord.setTname("承办回复");
		this.bind(request, chengbanRecord);
		chengbanRecord.setRead_index("0");
		chengbanRecord.setTime(new Date());
		chengbanRecord.setContent("");
		this.saveBaseInfoToObject(request, chengbanRecord);
		chengbanRecordDomain.save(chengbanRecord);*/
		//上传文件
		FileUpload entity=new FileUpload();
		bind(request, entity);
		String url=UpLoadUtil.myUpLoad(entity);
		if(!"".equals(url)){
			chengban.setUrl(url);
		}
		this.baseDomain.update(chengban);
		return new ModelAndView("redirect:/chengban.do?action=list");
	}
	
	public void download(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String motionId = HttpUtil.getString(request, "motionId","");
		// 获取提案
		Chengban chengban = (Chengban) this.baseDomain.queryFirstByProperty("motionId", motionId);
		String path = chengban.getUrl();
		// 获取文件名称
        String[] split = path.split("\\\\");
        String fileName = split[split.length-1];
        
        DownLoadUtil.download(request, response, path, fileName);
        
	}
	
	public ModelAndView read(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		return null;
	}

	public void update(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

	}
}
