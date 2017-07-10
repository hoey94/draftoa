package com.starsoft.oa.domainImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.starsoft.core.domainImpl.BaseDomainImpl;
import com.starsoft.core.entity.BaseObject;
import com.starsoft.core.util.Page;
import com.starsoft.oa.domain.FuyiDomain;
import com.starsoft.oa.domain.MotionDomain;
import com.starsoft.oa.entity.Fuyi;
import com.starsoft.oa.entity.Motion;

/**
 * 
 * @Description 议案
 * @author 赵一好
 * @date 2016-11-8 下午5:37:11
 * 
 */
@Service("motionDomain")
public class MotionDomainImpl extends BaseDomainImpl implements MotionDomain {

	@Autowired
	private FuyiDomain fuyiDomain;

	public MotionDomainImpl() {
		this.setClassName("com.starsoft.oa.entity.Motion");
	}

	public List<Motion> queryMotions() throws Exception {

		String sql = "select m.id,m.lah,m.tname,m.createId,m.time,u.duty from t_oa_motion m LEFT JOIN t_core_user u on m.createId = u.id where m.status = ?";
		List<Motion> motions = jdbcTemplate.query(sql, new Object[] { "6" },
				new RowMapper() {
					public Object mapRow(ResultSet rs, int i)
							throws SQLException {
						Motion motion = new Motion();
						motion.setId(rs.getString("id"));
						motion.setLah(rs.getString("lah"));
						motion.setTname(rs.getString("tname"));
						motion.setCreateId(rs.getString("createId"));
						motion.setTime(rs.getDate("time"));
						motion.setDuty(rs.getString("duty"));
						return motion;
					}
				});
		return motions;
	}

	public int queryCountForStatus(String status,String createId) throws Exception {
		DetachedCriteria criteria = this.getCriteria(null);
		criteria.add(Restrictions.eq("status", status));
		criteria.add(Restrictions.eq("createId", createId));
		List<? extends BaseObject> motions = this.queryByCriteria(criteria);
		return motions.size();
	}

	public List<Motion> queryMotionsByDync(Map<String, String> map, Page page)
			throws Exception {
		String tname = map.get("tname");
		String time = map.get("time");
		String tar = map.get("tar");
		String status = map.get("status");
		String dept = map.get("dept");
		String zlh = map.get("zlh");
		StringBuffer sb = new StringBuffer();
		if (tname != null && !tname.equals("")) {
			sb.append(" and m.tname like '%" + tname + "%' ");
		}
		if (time != null && !time.equals("")) {
			sb.append(" and m.time like '" + time + "%' ");
		}
		if (tar != null && !tar.equals("")) {
			sb.append(" and m.createId like '%" + tar + "%' ");
		}
		if (status != null && !status.equals("")) {
			sb.append(" and m.status =  '" + status + "' ");
		}
		if (dept != null && !dept.equals("")) {
			sb.append(" and o.tname like '%" + dept + "%' ");
		}
		if (zlh != null && !zlh.equals("")) {
			sb.append(" and m.zlh like '%" + zlh + "%' ");
		}
		String sql = "select m.id,m.tname,m.createId,o.tname dept,u.duty,u.mobilNumber,m.time,m.status from t_oa_motion m LEFT JOIN t_core_user u on m.createId = u.id LEFT JOIN t_core_organ o on u.organId = o.id where 1=1 "
				+ sb.toString() + " LIMIT ?,? ";
		String sql2 = "select m.id,m.tname,m.createId,o.tname dept,u.duty,u.mobilNumber,m.time,m.status from t_oa_motion m LEFT JOIN t_core_user u on m.createId = u.id LEFT JOIN t_core_organ o on u.organId = o.id where 1=1 "
				+ sb.toString();
		List<Motion> motions = new ArrayList<Motion>();
		if (page != null) {
			motions = jdbcTemplate.query(sql,
					new Object[] { page.getFirstResult(), page.getPageSize() },
					new RowMapper() {
						public Object mapRow(ResultSet rs, int i)
								throws SQLException {
							Motion motion = new Motion();
							motion.setId(rs.getString("id"));
							motion.setTname(rs.getString("tname"));
							motion.setCreateId(rs.getString("createId"));
							motion.setDept(rs.getString("dept"));
							motion.setDuty(rs.getString("duty"));
							motion.setMobilNumber(rs.getString("mobilNumber"));
							motion.setStatus(rs.getString("status"));
							motion.setTime(rs.getDate("time"));
							return motion;
						}
					});
		} else {
			motions = jdbcTemplate.query(sql2, new RowMapper() {
				public Object mapRow(ResultSet rs, int i) throws SQLException {
					Motion motion = new Motion();
					motion.setId(rs.getString("id"));
					motion.setTname(rs.getString("tname"));
					motion.setCreateId(rs.getString("createId"));
					motion.setDept(rs.getString("dept"));
					motion.setDuty(rs.getString("duty"));
					motion.setMobilNumber(rs.getString("mobilNumber"));
					motion.setStatus(rs.getString("status"));
					motion.setTime(rs.getDate("time"));
					return motion;
				}
			});
		}
		return motions;
	}

	public List<Motion> queryMotionsByStatus(String status, Page page)
			throws Exception {

		String sql = "select m.id,m.tname,m.createId,o.tname dept,u.duty,u.mobilNumber,m.time,m.status from t_oa_motion m LEFT JOIN t_core_user u on m.createId = u.id LEFT JOIN t_core_organ o ON u.organId = o.id where m.status = ? ORDER BY m.time desc LIMIT ?,?  ";
		String sql2 = "select m.id,m.tname,m.createId,o.tname dept,u.duty,u.mobilNumber,m.time,m.status from t_oa_motion m LEFT JOIN t_core_user u on m.createId = u.id LEFT JOIN t_core_organ o ON u.organId = o.id where m.status = ? ORDER BY m.time desc ";
		List<Motion> motions = new ArrayList<Motion>();
		if (page != null) {

			motions = jdbcTemplate.query(
					sql,
					new Object[] { status, page.getFirstResult(),
							page.getPageSize() }, new RowMapper() {
						public Object mapRow(ResultSet rs, int i)
								throws SQLException {
							Motion motion = new Motion();
							motion.setStatus(rs.getString("status"));
							motion.setId(rs.getString("id"));
							motion.setTname(rs.getString("tname"));
							motion.setCreateId(rs.getString("createId"));
							motion.setDept(rs.getString("dept"));
							motion.setDuty(rs.getString("duty"));
							motion.setMobilNumber(rs.getString("mobilNumber"));
							motion.setTime(rs.getDate("time"));
							return motion;
						}
					});
		} else {
			motions = jdbcTemplate.query(sql2, new Object[] { status },
					new RowMapper() {
						public Object mapRow(ResultSet rs, int i)
								throws SQLException {
							Motion motion = new Motion();
							motion.setStatus(rs.getString("status"));
							motion.setId(rs.getString("id"));
							motion.setTname(rs.getString("tname"));
							motion.setCreateId(rs.getString("createId"));
							motion.setDept(rs.getString("dept"));
							motion.setDuty(rs.getString("duty"));
							motion.setMobilNumber(rs.getString("mobilNumber"));
							motion.setTime(rs.getDate("time"));
							return motion;
						}
					});
		}

		return motions;
	}

	@Transactional
	public void saveMotAndRec(Motion motion, String createId, String fyr)
			throws Exception {

		this.save(motion);

		// 向操作记录表中插入一条记录
		Fuyi fuyi = new Fuyi();
		fuyi.setFyr(fyr);
		fuyi.setMotionid(motion.getId());
		// 表示未读
		fuyi.setTime(motion.getTime());
		fuyi.setTname("附议");
		fuyi.setMark("0");
		fuyi.setValid(true);
		fuyi.setCreateId(createId);
		fuyiDomain.save(fuyi);

	}

}
