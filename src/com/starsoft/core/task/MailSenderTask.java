package com.starsoft.core.task;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/***
 * 短信发送任务调度
 * @author lenovo
 *
 */
@Component("mailSenderTask")
public class MailSenderTask extends BaseTaskAction {
	private JavaMailSender  edusmailSender;
	private JavaMailSender  qqMailSender;
    private MimeMessage mimeMessage;  
    private MimeMessageHelper mail;  
	/***
	 * 邮件群送的分类
	 * @param simpleMailMessage
	 * @throws MessagingException 
	 */
	@Async
	public void send(String reciver,String subject,String content,boolean isHtml) throws MessagingException{
		mimeMessage=edusmailSender.createMimeMessage();
		mail = new MimeMessageHelper(mimeMessage,true,"GBK");  
		mail.setTo(reciver);
		mail.setSubject(subject);
		mail.setText(content,isHtml);
		mail.setFrom("2edus@2edus.com");
		edusmailSender.send(mimeMessage);
		
	}
	/***
	 * 邮件群送的分类
	 * @param simpleMailMessage
	 * @throws MessagingException 
	 */
	@Async
	public void sends(List<String> recivers,String subject,String content,boolean isHtml) throws MessagingException{
		InternetAddress[] to = new InternetAddress[recivers.size()];
		for (int i = 0; i < recivers.size(); i++) {
			to[i] = new InternetAddress(recivers.get(i));
		}
		mimeMessage = edusmailSender.createMimeMessage();
		mail = new MimeMessageHelper(mimeMessage, true, "GBK");
		mail.setTo(to);
		mail.setSubject(subject);
		mail.setText(content, isHtml);
		mail.setFrom("2edus@2edus.com");
		edusmailSender.send(mimeMessage);
	}


	public void setQqMailSender(JavaMailSender qqMailSender) {
		this.qqMailSender = qqMailSender;
	}
	public void setEdusmailSender(JavaMailSender edusmailSender) {
		this.edusmailSender = edusmailSender;
	}
}
