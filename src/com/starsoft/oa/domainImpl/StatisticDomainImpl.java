package com.starsoft.oa.domainImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.starsoft.core.domainImpl.BaseDomainImpl;
import com.starsoft.oa.domain.StatisticDomain;
import com.starsoft.oa.entity.MotionsCount;

/**
 * 
 * @Description 统计
 * @author 赵一好
 * @date 2016-11-10 上午10:54:31
 * 
 */
@Service("statisticDomain")
public class StatisticDomainImpl extends BaseDomainImpl implements StatisticDomain{

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public StatisticDomainImpl(){
		this.setClassName("com.starsoft.oa.entity.Motion");
	}
	
	// 统计本年度每个月份议案的数量
	public List staMotionPerMonth(int year) throws Exception{
		String sql = "select MONTH(time) month,count(*) count from t_oa_motion where YEAR(time) = '"+year+"' GROUP BY MONTH(time);";
		return jdbcTemplate.query(sql, new RowMapper(){
			public Object mapRow(ResultSet rs, int i) throws SQLException {
				MotionsCount motionsCount = new MotionsCount();
				motionsCount.setMonth(rs.getInt("month"));
				motionsCount.setCount(rs.getInt("count"));
				return motionsCount;
			}
		});
	}
	
	
}
