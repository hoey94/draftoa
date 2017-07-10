package com.starsoft.core.task;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.starsoft.core.domain.UsersDomain;
import com.starsoft.core.entity.UserNumber;
import com.starsoft.core.util.DateUtil;
@Component("zhuceSenderTask")
public class UserNumberTask extends BaseTaskAction{
	
	private JdbcTemplate jdbcTemplate;
	private UsersDomain usersDomain;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	public void setUsersDomain(UsersDomain usersDomain) {
		this.usersDomain = usersDomain;
	}
	/***
	 * 1000=1秒
	 * 判断数据库连接是否正常，24个小时判断一次
	 * @throws Exception
	 */
	@Scheduled(cron="0 55 23 ? * *")
	public void doSomething() throws Exception {
		String currday=DateUtil.getCurDate();
		String todayspark7sql="select count(id) from T_CORE_USER where createTime >'"+currday+" 00:00:01'";//今日注册用户 
		int newuserNumber=this.jdbcTemplate.queryForInt(todayspark7sql);
		UserNumber userNumber=new UserNumber();
		userNumber.setTname(currday);
		userNumber.setUserNum(newuserNumber);
		userNumber.setValid(true);
		this.usersDomain.save(userNumber);
	}	
}
