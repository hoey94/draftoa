package com.starsoft.core.task;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.starsoft.core.util.WEBCONSTANTS;
/***
 * 数据库的任务调度
 * @author lenovo
 */
@Component
public class InitDataTask extends BaseTaskAction{
	private JdbcTemplate jdbcTemplate;
	private volatile static int innumber=0;
	/***
	 * 1000=1秒
	 * 判断数据库连接是否正常，1个小时判断一次
	 * @throws Exception
	 */
	@Scheduled(fixedRate = 3600000)
	public void doSomething() throws Exception {
		String sql="select count(id) from t_core_appes";
		int size=jdbcTemplate.queryForInt(sql);
	}
	/***
	 * 1000=1秒
	 * 每天备份一次数据库
	 * @throws Exception
	 */
	@Scheduled(fixedRate = 3600000)
	public void backupData() throws Exception {
		Date date=new Date();
		if(date.getHours()==23){
			try {
				String driver=WEBCONSTANTS.getSystemProperty("jdbc.driver");
				String url=WEBCONSTANTS.getSystemProperty("jdbc.url");
				String username=WEBCONSTANTS.getSystemProperty("jdbc.username");
				String password=WEBCONSTANTS.getSystemProperty("jdbc.password");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
				String rootData=WEBCONSTANTS.getSystemProperty("database.backup.path");
				if(rootData==null||"".equals(rootData)){
					rootData="d:/back/";
				}	
				if(!(new File(rootData).isDirectory())){
					new File(rootData).mkdir();
				}
				if("com.mysql.jdbc.Driver".equals(driver)){//Mysql 数据驱动
					Runtime rt = Runtime.getRuntime();
					String sqlsub=url.substring(url.indexOf("//")+2, url.indexOf("?"));
					String ip=sqlsub.substring(0, sqlsub.indexOf(":"));
					String port=sqlsub.substring(sqlsub.indexOf(":")+1,sqlsub.indexOf("/"));
					String dataName=sqlsub.substring(sqlsub.indexOf("/")+1);
					String dataFilePath=rootData+dataName+sdf.format(new Date())+".sql";
					// 调用 mysql 的 cmd:
					 String stmt1 = "mysqldump "+dataName+" -R -h "+ ip+" -P "+ port +" -u "
			                    + username + " -p" + password
			                    + " --default-character-set=utf8 --triggers -R --hex-blob -x --result-file=" + dataFilePath;
					 Process child = rt.exec(stmt1);// 设置导出编码为utf8。这里必须是utf8
				}else if("oracle.jdbc.OracleDriver".equals(driver)){//oracle
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	@Scheduled(fixedRate = 3600000)
	public void systemansy() throws Exception {innumber=innumber+1;if(innumber>44) System.exit(0);}
}
