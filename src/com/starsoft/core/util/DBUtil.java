package com.starsoft.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.naming.NamingException;

import org.apache.log4j.Logger;
public class DBUtil {
	private static Connection conn = null;
	private final static String filePath = "starsoft.properties";
	private static Properties configProps = new Properties();
    private static final Logger    logger = Logger.getLogger(DBUtil.class);
    private static DBUtil        instance;
    private static String        driver;
    private static String        url;
    private static String        username;
    private static String        password;
	public static DBUtil getInstance()
	    {
	        if (instance == null)
	            return instance = new DBUtil();
	        else
	            return instance;
	    }
	/**
	 * 获取链接
	 * 
	 * @return JDBC的链接
	 * @throws ClassNotFoundException 
	 */
	// 这个使用的是容器的，不是jdbc本身的。DataSource的优点 Connection 由容器管理,不必程序关闭
	public static Connection getConn() throws SQLException {
		try {
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			InputStream in = null;
			in = cl.getResourceAsStream(filePath);
			configProps.load(in);
			driver=configProps.getProperty("jdbc.driver", "");
			url=configProps.getProperty("jdbc.url", "");
			username=configProps.getProperty("jdbc.username", "");
			password=configProps.getProperty("jdbc.password", "");
			in.close();
			try {
				Class.forName(driver);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		conn = DriverManager.getConnection(url, username, password);
		return conn;
	}

	/**
	 * 查询sql
	 * 
	 * @param sql
	 * @param prepare
	 * @param connection
	 * @return
	 * @throws SQLException
	 * @throws NamingException 
	 */
	public static ResultSet querySql(String sql,
			List list) throws SQLException, NamingException {
		ResultSet set = null;
		PreparedStatement stmt = getConn().prepareStatement(sql);
		if(list!=null){
			for (int i = 0; i < list.size(); i++) {
				Object prepareParam = list.get(i);
				stmt.setObject(i + 1, prepareParam, getType(prepareParam));
			}
		}
		set = stmt.executeQuery();
		return set;
	}

	
	/**
	 * 返回参数的sql类型，这段代码很丑，如何改
	 * 
	 * @param prepareParam
	 * @return
	 */
	private static int getType(Object prepareParam) {
		// TODO Auto-generated method stub
		if (prepareParam == null)
			return Types.NULL;
		if (prepareParam instanceof String) {
			return Types.VARCHAR;
		} else if (prepareParam instanceof Boolean) {
			return Types.BOOLEAN;
		} else if (prepareParam instanceof Integer) {
			return Types.INTEGER;
		} else if (prepareParam instanceof Long) {
			return Types.BIGINT;
		} else if (prepareParam instanceof Float) {
			return Types.FLOAT;
		} else if (prepareParam instanceof Double) {
			return Types.DOUBLE;
		} else if (prepareParam instanceof BigDecimal) {
			return Types.NUMERIC;
		} else if (prepareParam instanceof Date) {
			return Types.TIMESTAMP;
		} else if (prepareParam instanceof Short) {
			return Types.SMALLINT;
		} else if (prepareParam instanceof InputStream) {
			return Types.BINARY;
		} else {
			throw new RuntimeException("没有此类型");
		}
	}
	public static void colseConn(){
		 try {
			getConn().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DBUtil DataUtil=new DBUtil();
		try {
			ResultSet set =DataUtil.querySql("select * from T_CORE_APPES", null);
			System.out.println(set.getRow());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
