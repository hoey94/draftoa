package com.starsoft.core.mobilecontroller;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class BaseMobileController extends MultiActionController implements
		Controller {
	public final Logger logger = LogManager.getLogger(getClass());
	protected JdbcTemplate jdbcTemplate;
	/**
	 * 字符流转为字符串
	 * @param stream
	 * @return
	 * @throws IOException
	 */
	protected String InputStream2String(InputStream stream)
			throws IOException {
		StringBuffer buffer = new StringBuffer();
		InputStreamReader in = new InputStreamReader(stream, "UTF-8");
		int c = 0;
		while ((c = in.read()) != -1) {
			buffer.append((char) c);
		}
		String inputString=buffer.toString();
		this.logger.info("===============InputStream2String======"+inputString);
		return inputString;
	}
	/**
	 * HTTP 输出内容
	 * @param response
	 * @param msg
	 * @throws IOException
	 */
	protected void outputResponseMsg(HttpServletResponse response,
			String msg) throws IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/xml");
		PrintWriter outWriter = response.getWriter();
		outWriter.write(msg);
		outWriter.flush();
		outWriter.close();
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
