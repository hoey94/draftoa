package com.starsoft.core.task;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.starsoft.core.util.HttpSendUtil;

/***
 * 短信发送任务调度
 * @author lenovo
 *
 */
@Component("smsSenderTask")
public class SMSSenderTask extends BaseTaskAction {
	public static String message_content="【双元微课】您正在修改双元微课会员密码，您的验证码是：${code}，此验证码30分钟内有效，为了信息安全，请勿向他人泄漏。回复TD退订";
	/***
	 * 短信发送的分类
	 * @param simpleMailMessage
	 * @return 
	 * @throws Exception 
	 */
	@Async
	public Map<String, String> send(String phone,String message) throws Exception{
		StringBuilder builder=new StringBuilder("http://sdk4report.eucp.b2m.cn:8080/sdkproxy/sendsms.action?cdkey=6SDK-EMY-6688-KGSSS&password=990521");
		builder.append("&phone=");
		builder.append(phone);
		builder.append("&message=");
		builder.append(URLEncoder.encode(message, "UTF-8"));
		builder.append("&addserial=");
		return HttpSendUtil.getSMSCode(builder.toString());
	}
	/***
	 * 短信群送的分类
	 * @param simpleMailMessage
	 * @throws Exception 
	 */
	@Async
	public Map<String, String> sends(List<String> phones,String message) throws Exception{
		StringBuilder builder=new StringBuilder("http://sdk4report.eucp.b2m.cn:8080/sdkproxy/sendsms.action?cdkey=6SDK-EMY-6688-KGSSS&password=990521");
		builder.append("&phone=");
		builder.append(phones.get(0));
		for(int i=1;i<phones.size();i++){
			builder.append(",");
			builder.append(phones.get(i));
		}
		builder.append("&message=");
		builder.append(URLEncoder.encode(message, "UTF-8"));
		builder.append("&addserial=");
		return HttpSendUtil.getSMSCode(builder.toString());
	}
}
