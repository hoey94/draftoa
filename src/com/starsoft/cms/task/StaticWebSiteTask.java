package com.starsoft.cms.task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import freemarker.template.Configuration;
import freemarker.template.Template;

/***
 * 网站静态化任务调度
 * @author Administrator
 *
 */
@Component("staticWebSiteTask")
public class StaticWebSiteTask{
	/***
	 * 静态化网站首页
	 */
	@Async
	public void staticPage(ServletContext context,
			Map<String, Object> model, String templatePath,
			String targetHtmlPath) {
		// 加载模版
		Configuration freemarkerCfg = new Configuration();
		freemarkerCfg.setServletContextForTemplateLoading(context, "/");
		freemarkerCfg.setEncoding(Locale.getDefault(), "UTF-8");
		try {
			// 指定模版路径
			Template template = freemarkerCfg
					.getTemplate(templatePath, "UTF-8");
			template.setEncoding("UTF-8");
			// 静态页面路径
			String htmlPath = context.getRealPath("/html/") + targetHtmlPath;
			File htmlFile = new File(htmlPath);
			Writer out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(htmlFile), "UTF-8"));
			// 处理模版
			template.process(model, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
