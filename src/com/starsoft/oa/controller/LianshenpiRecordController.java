package com.starsoft.oa.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.json.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.servlet.ModelAndView;

import com.starsoft.core.controller.BaseAjaxController;
import com.starsoft.core.controller.BaseInterface;
import com.starsoft.core.util.HttpUtil;
import com.starsoft.oa.domain.FuyiDomain;
import com.starsoft.oa.domain.LianshenpiRecordDomain;
import com.starsoft.oa.domain.MotionDomain;
import com.starsoft.oa.entity.Motion;

/**
 * 
 * @Description 立案审批回复
 * @author 赵一好
 * @date 2016-11-16 上午10:56:16
 * 
 */
public class LianshenpiRecordController extends BaseAjaxController implements
		BaseInterface {

	@Autowired
	private MotionDomain motionDomain;
	
	@Autowired
	private FuyiDomain fuyiDomain;
	
	// 查找出状态为6的议案信息,并跳转到list界面
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Map<String,Object> model = HttpUtil.convertModel(request);
		List<Motion> motions = motionDomain.queryMotions();
		if(motions!=null){
			// 查找附议同意总人数
			for (Motion motion : motions) {
				String fyrNum = fuyiDomain.findFuyiCount(motion.getId(), "1")+"";
				motion.setFyrNum(fyrNum);
			}
			model.put("lists", motions);
		}
		
		return new ModelAndView(this.getListPage(),model);
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

	public void save(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

	}

	public void update(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

	}

}
