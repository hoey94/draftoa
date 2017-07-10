package com.starsoft.core.task;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//@Component("downfileTask")
public class DownFileTask extends BaseTaskAction {
	private volatile static boolean isRunningSgip=false;
	/* 1000=1秒
	 * 判断数据库连接是否正常，1个小时判断一次
	 * @throws Exception
	 */
	@Scheduled(fixedRate = 3600000)
	public void downLoadFromUrl() throws Exception {
		if(isRunningSgip) return;
		try{
			String sql="select id,videoUrl,courseImage from t_edu_lesson where  id >'20160501000004590192928391011048' and id<'20160601000004590192928391011048'";
			List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
			String httppar="http://218.29.223.74:88";
			for(Map map:list){
				String id=map.get("id")==null?"":String.valueOf(map.get("id"));
				String videoUrl=map.get("videoUrl")==null?"":String.valueOf(map.get("videoUrl"));
				String courseImage=map.get("courseImage")==null?"":String.valueOf(map.get("courseImage"));
				logger.info("========================videoUrl=="+videoUrl);
				logger.info("========================courseImage=="+courseImage);
				if(!videoUrl.equals("")){
				  if(videoUrl.indexOf("/201603/")>-1){
					String url=httppar+videoUrl;
					String filename=this.getFileNameFromUrl(videoUrl);
					File file=new File("E:\\systemInfo\\upload\\201603\\"+filename);
					logger.info("========================url=="+url);
					logger.info("========================filename=="+filename);
					if(!file.exists()){
						isRunningSgip=true;
						FileUtils.copyURLToFile(new URL(url), file);
						isRunningSgip=false;
					}
				  }
				  if(videoUrl.indexOf("/201604/")>-1){
						String url=httppar+videoUrl;
						String filename=this.getFileNameFromUrl(videoUrl);
						File file=new File("E:\\systemInfo\\upload\\201605\\"+filename);
						logger.info("========================url=="+url);
						logger.info("========================filename=="+filename);
						if(!file.exists()){
							isRunningSgip=true;
							FileUtils.copyURLToFile(new URL(url), file);
							isRunningSgip=false;
						}
					  }
				  if(videoUrl.indexOf("/201605/")>-1){
						String url=httppar+videoUrl;
						String filename=this.getFileNameFromUrl(videoUrl);
						File file=new File("E:\\systemInfo\\upload\\201605\\"+filename);
						logger.info("========================url=="+url);
						logger.info("========================filename=="+filename);
						if(!file.exists()){
							isRunningSgip=true;
							FileUtils.copyURLToFile(new URL(url), file);
							isRunningSgip=false;
						}
					  }
//					
				}
//				if(!courseImage.equals("")){
//					String url=httppar+courseImage;
//					String filename=this.getFileNameFromUrl(url);
//					File file=new File("E:\\"+filename);
//					logger.info("========================url=="+url);
//					logger.info("========================filename=="+filename);
//					FileUtils.copyURLToFile(new URL(url), file);
//				}
			}
		}catch(Exception e){
			
		}
		
		
	}
	
	
	public String getFileNameFromUrl(String url){
		int index=url.lastIndexOf("/");
		String result="";
		if(index>0){
			result=url.substring(index+1).trim();
		}
		return result;
	}
}
