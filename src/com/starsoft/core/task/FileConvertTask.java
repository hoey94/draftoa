package com.starsoft.core.task;

/**
 * 
 * Created with Eclipse.
 * 文件：FileConvertTask.java
 * 作者：崔兵兵
 * 时间：2015-7-15
 * 描述：异步线程处理转换pdf和flash任务
 * To change this template use File | Settings | File Templates
 */
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import com.starsoft.core.domain.ResourceDomain;
import com.starsoft.core.domain.SystemLogDomain;
import com.starsoft.core.entity.Resource;
import com.starsoft.core.entity.SystemLog;
import com.starsoft.core.util.FlashUtil;

@Component("fileConverTask")
public class FileConvertTask extends BaseTaskAction {
	private Logger logger = Logger.getLogger(FileConvertTask.class);
	@Autowired
	private ResourceDomain resourceDomain;
	@Autowired
	private SystemLogDomain systemLogDomain;

	/**
	 * 
	 * @param createId 上传人ID
	 * @param baseObjectId 关联实体类ID
	 * @param fileNameResult 相对路径
	 * @param uploadPath 绝对路径
	 * @param filename 文件名
	 * @param filelinkname 系统生成文件名
	 * @param fileType  文件类型
	 */
	@Async
	public void fileConverter(String createId, String baseObjectId,
			String fileNameResult, String uploadPath, String filename,
			String filelinkname, String fileType) {
		try {
			String convertPath = FlashUtil.beginConvert(uploadPath,
					filelinkname);//返回生成的swf文件，修改后缀.swf为.pdf可获得pdf文件或者最初的文件
			if (convertPath == null) {
				throw new Exception("操作失败");
			} else {
				String resultUrl = fileNameResult;
				resultUrl = resultUrl + convertPath;
				Resource resource = (Resource) resourceDomain.getBaseObject();
				String resourceName = filename;
				if (filename.indexOf(".") > 0) {
					resourceName = filename.substring(0, filename.indexOf("."));
				}
				resource.setTname(resourceName);
				resource.setFileType(fileType);
				resource.setValid(true);
				resource.setUrl(resultUrl);
				resource.setBaseObjectId(baseObjectId);
				resource.setCreateId(createId);
				resourceDomain.save(resource);
			}
		} catch (Exception e) {
			// TODO: handle exception
			SystemLog systemLog = new SystemLog();
			systemLog.setCreateId("systemAuto");
			systemLog.setTname("文件转PDF或flash失败");
			systemLog.setValid(false);
			systemLog.setInfoClass("com.starsoft.core.task.FileConvertTask");
			systemLog.setInfoContent("转换出错文件名：" + filename);
			systemLogDomain.save(systemLog);
		}
	}
}
