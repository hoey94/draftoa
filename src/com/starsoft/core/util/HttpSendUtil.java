package com.starsoft.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 * 
 * 文件：HttpSendUtil.java 作者：崔兵兵 时间：2015-4-3 描述：http数据交互
 */
@SuppressWarnings("deprecation")
public class HttpSendUtil {
	private static HttpClient singletonHttpClient;
	public static final String DEFAULT_ENCODING = "UTF-8";
	private static final String CONTENT_TYPE_TEXT_JSON = "text/json";
	private static final String APPLICATION_JSON = "application/json";
	private static Logger logger = Logger.getLogger(HttpSendUtil.class);

	/**
	 * HttpClient单例
	 * 
	 * @return
	 */
	public static synchronized HttpClient getHttpClient(String charset) {
		if(StringUtil.isNullOrEmpty(charset)){
			charset = DEFAULT_ENCODING;
		}
		if (singletonHttpClient == null) {
			HttpParams params = new BasicHttpParams();
			// 设置基本参数
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, charset);
			HttpProtocolParams.setUseExpectContinue(params, true);
			// 超时设置
			/* 从连接池中取连接的超时时间 */
			ConnManagerParams.setTimeout(params, 1000);
			/* 连接超时 */
			HttpConnectionParams.setConnectionTimeout(params, 2000);
			/* 请求超时 */
			HttpConnectionParams.setSoTimeout(params, 40000);

			// 设置我们的HttpClient支持HTTP和HTTPS两种模式
			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			schReg.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), 443));

			// 使用线程安全的连接管理来创建HttpClient
			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
					params, schReg);
			singletonHttpClient = new DefaultHttpClient(conMgr, params);
		}
		return singletonHttpClient;
	}

	/**
	 * 提交Post请求，并获取响应
	 * 
	 * @param url
	 * @param jsonData
	 * @return
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> post(String url, String jsonData)
			throws Exception {
		try {
			Map<String, Object> returnBean = null;
			// HttpClient初始化
			HttpClient httpClient = getHttpClient(null);

			// HttpRequest初始化
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
			// 填充数据
			ByteArrayEntity byteArrayEntity = new ByteArrayEntity(
					jsonData.getBytes(DEFAULT_ENCODING));
			byteArrayEntity.setContentType(CONTENT_TYPE_TEXT_JSON);
			byteArrayEntity.setContentEncoding(new BasicHeader(
					HTTP.CONTENT_TYPE, "application/json"));
			httpPost.setEntity(byteArrayEntity);

			// 提交，返回结果
			HttpResponse httpResponse = httpClient.execute(httpPost);

			if (200 == httpResponse.getStatusLine().getStatusCode()) {
				logger.debug("response code=200 begin");
				String temp = new String(EntityUtils.toByteArray(httpResponse
						.getEntity()), DEFAULT_ENCODING);
				logger.debug("response code=200 mid");
				returnBean = (Map<String, Object>) GsonUtil.getMap(temp);
				logger.debug("response code=200 end");
			} else {
				logger.debug("HttpUtil response error="
						+ httpResponse.getStatusLine().getStatusCode());
			}
			logger.debug("HttpUti response =" + GsonUtil.getJson(returnBean));
			return returnBean;
		} catch (Exception e) {
			throw new Exception("网络访问异常", e);
		}
	}

	/**
	 * 提交Get请求，并获取响应
	 * 
	 * @param url
	 * @param jsonData
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> get(String url) throws Exception {
		try {
			Map<String, Object> returnBean = new HashMap<String, Object>();

			// HttpClient初始化
			HttpClient httpClient = getHttpClient("GB2312");

			// HttpRequest初始化
			HttpGet httpGet = new HttpGet(url);
			// 提交，返回结果
			HttpResponse httpResponse = httpClient.execute(httpGet);

			// Log.d("httpcode", "" +
			// httpResponse.getStatusLine().getStatusCode());
			// String content = new String(EntityUtils.toByteArray(httpResponse
			// .getEntity()), DEFAULT_ENCODING);
			// Log.d("httpdata", content);

			if (200 == httpResponse.getStatusLine().getStatusCode()) {

				// returnBean = (HttpResponseBean) GsonUtil.getObject(content,
				// HttpResponseBean.class);
				// returnBean.setCode("000");
				// returnBean.setData(content);
				logger.debug("HttpUtil response code=200 begin");
				String temp = new String(EntityUtils.toByteArray(httpResponse
						.getEntity()), DEFAULT_ENCODING);
				logger.debug("HttpUtil response code=200 mid");
				returnBean = (Map<String, Object>)JsonTools.parseJSON2Map(temp);
				returnBean.put("success", "success");
				logger.debug("HttpUtil response code=200 end");
			} else {
				returnBean.put("fail", "fail");
				logger.debug("HttpUtil response error="
						+ httpResponse.getStatusLine().getStatusCode());
			}
			return returnBean;
		} catch (Exception e) {
			// throw new Exception("网络访问异常", e);
			throw e;
		}
	}

	public static Map<String, String> getSMSCode(String address) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		try {
			URL url = new URL(address);
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			// GET Request Define:
			urlConnection.setRequestMethod("GET");
			urlConnection.connect();

			// Connection Response From Test Servlet
			InputStream inputStream = urlConnection.getInputStream();

			// Convert Stream to String
			String xml=ConvertToString(inputStream);
			String code=xml.substring(xml.indexOf("<error>")+"<error>".length(), xml.indexOf("</error>")).trim();
			map.put("error", code);
		} catch (IOException e) {
			map.put("error", "-1000");
			e.printStackTrace();
		}
		return map;
	}

	public static String ConvertToString(InputStream inputStream) {
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		StringBuilder result = new StringBuilder();
		String line = null;
		try {
			while ((line = bufferedReader.readLine()) != null) {
				result.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inputStreamReader.close();
				inputStream.close();
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result.toString();
	}

	
	/**
	 * 提交Get请求，并获取响应
	 * 
	 * @param url
	 * @param jsonData
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	@SuppressWarnings("unchecked")
	public static String getjson(String url) throws Exception {
		try {
			String result="";
			// HttpClient初始化
			HttpClient httpClient = getHttpClient("UTF-8");
			// HttpRequest初始化
			HttpGet httpGet = new HttpGet(url);
			// 提交，返回结果
			HttpResponse httpResponse = httpClient.execute(httpGet);

			// Log.d("httpcode", "" +
			// httpResponse.getStatusLine().getStatusCode());
			// String content = new String(EntityUtils.toByteArray(httpResponse
			// .getEntity()), DEFAULT_ENCODING);
			// Log.d("httpdata", content);

			if (200 == httpResponse.getStatusLine().getStatusCode()) {

				// returnBean = (HttpResponseBean) GsonUtil.getObject(content,
				// HttpResponseBean.class);
				// returnBean.setCode("000");
				// returnBean.setData(content);
				logger.debug("HttpUtil response code=200 begin");
				result = new String(EntityUtils.toByteArray(httpResponse.getEntity()), DEFAULT_ENCODING);
				logger.debug("HttpUtil response code=200 end");
			} else {
				logger.debug("HttpUtil response error="
						+ httpResponse.getStatusLine().getStatusCode());
			}
			return result;
		} catch (Exception e) {
			// throw new Exception("网络访问异常", e);
			throw e;
		}
	}
	
	public static void main(String[] args) {
		try {
			// get("http://www.weather.com.cn/data/sk/101110101.html");
//			post("http://127.0.0.1/mobileRegister.do?action=regist", "");
			//http://bao.2edus.com/index.php?c=eqs&a=promotion&type=26
		   String result= getjson("http://bao.2edus.com/index.php?c=eqs&a=promotion&type=26");
			 //最外层解析  
	       JSONObject json = JSONObject.fromObject(result); 
	       String success= json.getString("success");//请求结果状态
	       List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();  
	       if(success.equals("true")){
	    	   Object liststr=json.get("list");
	    	   if(liststr instanceof JSONArray){
	                Iterator<JSONObject> it = ((JSONArray)liststr).iterator();  
	                while(it.hasNext()){  
	                    JSONObject listjson = it.next(); 
	                    String id=listjson.getString("id");
	                    String name=listjson.getString("name");
	                    String createUser=listjson.getString("createUser");
	                    String createTime=listjson.getString("createTime");
	                    String code=listjson.getString("code");
	                    
	                    
//	                   list.add(submap);  
	                }  
	    	   }
	    	   
	       }
	       
	       
	       
			System.out.println(list);
			System.out.println(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
