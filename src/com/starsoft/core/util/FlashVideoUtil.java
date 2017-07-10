package com.starsoft.core.util;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.htmlparser.Parser;
import org.htmlparser.visitors.HtmlPage;
import com.starsoft.core.vo.FlashVideo;
/**
 * 视频网址视频信息提取工具类
 * 
 * @author hegf
 * @date 2013-1-22 下午03:28:52
 */
public class FlashVideoUtil {
	private static Logger log = Logger.getLogger(FlashVideoUtil.class);
	/**
	 * 优酷
	 */
	private final static String VIDEO_DOMAIN_YOUKU = "v.youku.com";
	/**
	 * 土豆
	 */
	private final static String VIDEO_DOMAIN_TUDOU = "www.tudou.com";
	/**
	 * 酷6
	 */
	private final static String VIDEO_DOMAIN_KU6 = "v.ku6.com";
	/**
	 * 6间房
	 */
	private final static String VIDEO_DOMAIN_CN6 = "6.cn";
	/**
	 * 我乐
	 */
	private final static String VIDEO_DOMAIN_WOLE = "www.56.com";
	/**
	 * 搜狐
	 */
	private final static String VIDEO_DOMAIN_SOHU = "tv.sohu.com";
	/**
	 * 新浪
	 */
	private final static String VIDEO_DOMAIN_SINA = "video.sina.com.cn";
	/**
	 * 凤凰
	 */
	private final static String VIDEO_DOMAIN_IFENG = "v.ifeng.com";
	/**
	 * 音悦台MV
	 */
	private final static String VIDEO_DOMAIN_YINYUETAI = "www.yinyuetai.com/video";
	/**
	 * QQ
	 */
	private final static String VIDEO_DOMAIN_QQ = "v.qq.com";
	/**
	 * 网易
	 */
	private final static String VIDEO_DOMAIN_WANGYI = "v.163.com";
//
//	/**
//	 * 获取视频信息
//	 * 
//	 * @param url
//	 * @return
//	 */
//	public static FlashVideo getVideoInfo(String url) {
//		try {
//			if (url.indexOf(VIDEO_DOMAIN_YOUKU) != -1) {
//				return getYouKuVideo(url);
//			}
//			if (url.indexOf(VIDEO_DOMAIN_TUDOU) != -1) {
//				return getTudouVideo(url);
//			}
//			if (url.indexOf(VIDEO_DOMAIN_KU6) != -1) {
//				return getKu6Video(url);
//			}
//			if (url.indexOf(VIDEO_DOMAIN_WOLE) != -1) {
//				return get56Video(url);
//			}
//			if (url.indexOf(VIDEO_DOMAIN_SINA) != -1) {
//				return getSinaVideo(url);
//			}
//			if (url.indexOf(VIDEO_DOMAIN_SOHU) != -1) {
//				return getSohuVideo(url);
//			}
//			if (url.indexOf(VIDEO_DOMAIN_IFENG) != -1) {
//				return getIfengVideo(url);
//			}
//			if (url.indexOf(VIDEO_DOMAIN_QQ) != -1) {
//				return getQqVideo(url);
//			}
//			if (url.indexOf(VIDEO_DOMAIN_WANGYI) != -1) {
//				return getWangyiVideo(url);
//			}
//			return null;
//		} catch (Exception e) {
//			log.error(e);
//			return null;
//		}
//	}
//
//	/**
//	 * 根据FLASH地址生成页面代码
//	 * 
//	 * @param flashUrl
//	 * @param width
//	 * @param height
//	 * @return
//	 */
//	public static String getHtmlCode(String flashUrl, int width, int height) {
//		if (StringUtil.isNullOrEmpty(flashUrl)) {
//			return "";
//		}
//		return "<embed src=\""
//				+ flashUrl
//				+ "\" allowFullScreen=\"true\" quality=\"high\" width=\""
//				+ width
//				+ "\" height=\""
//				+ height
//				+ "\" align=\"middle\" allowScriptAccess=\"always\" type=\"application/x-shockwave-flash\"></embed>";
//	}
//
//	/**
//	 * 获取优酷视频
//	 * 
//	 * @param url
//	 *            视频URL
//	 */
//	private static FlashVideo getYouKuVideo(String url) throws Exception {
//		HtmlPage doc = getURLContent(url);
//		FlashVideo video = new FlashVideo();
//		video.setSource("优酷视频");
//		video.setPageUrl(url);
//		// 视频标题
//		video.setTitle(doc.getTitle().split("-")[0].trim());
//		// 视频地址
//		video.setFlashUrl(getElementAttrById(doc, "link2", "value"));
//		// 视频缩略图
//		String id = url.substring(url.lastIndexOf("_") + 1, url
//				.lastIndexOf("."));
//		HtmlPage doc1 = getURLContent("http://v.youku.com/player/getPlayList/VideoIDS/"
//				+ id
//				+ "/timezone/+08/version/5/source/video?password=&ran=5226&n=3");
//		String html = doc1.getBody().toHtml().replaceAll("&quot;", "\"")
//				.replace("\\/", "/");
//		int beginLocal = html.indexOf("logo") + 7;
//		video.setThumbnail(html.substring(beginLocal, html.indexOf("\"",
//				beginLocal)));
//		// 视频网页代码
//		// String htmlCode = getElementAttrById(doc, "link3", "value");
//		// 视频时间
//		// String time = getElementAttrById(doc, "download", "_href");
//		// String[] arrays = time.split("\\|");
//		// time = arrays[4];
//		// 视频简介
//		// Element el = doc.getElementById("long");
//		// String summary = el.select(".item").get(0).html();
//		return video;
//	}
//
//	/**
//	 * 获取土豆视频
//	 * 
//	 * @param url
//	 *            视频URL
//	 */
//	private static FlashVideo getTudouVideo(String url) throws Exception {
//		HtmlPage doc = getURLContent(url);
//		FlashVideo video = new FlashVideo();
//		video.setSource("土豆视频");
//		video.setPageUrl(url);
//		String content = doc.getBody().toHtml();
//		int beginLocal = content.indexOf("itemData");
//
//		content = content.substring(beginLocal, content
//				.indexOf("}", beginLocal) + 1);
//		// 视频标题
//		video.setTitle(doc.getTitle().split("_")[0].trim());
//		// 视频地址
//		video.setFlashUrl("http://www.tudou.com/v/"
//				+ getScriptVarByName("icode", content) + "/v.swf");
//		// 视频缩略图
//		video.setThumbnail(getScriptVarByName("pic", content));
//		// 视频简介
//		// String summary =
//		// doc.select("meta[name=Description]").attr("content");
//		// 视频时间
//		// String time = getScriptVarByName("time", content);
//		return video;
//	}
//
//	/**
//	 * 获取酷6视频
//	 * 
//	 * @param url
//	 *            视频URL
//	 */
//	private static FlashVideo getKu6Video(String url) throws Exception {
//		HtmlPage doc = getURLContent(url);
//		FlashVideo video = new FlashVideo();
//		video.setSource("酷6视频");
//		video.setPageUrl(url);
//		// 视频标题
//		video.setTitle(doc.getTitle().split(" ")[0].trim());
//		// 视频地址
//		video.setFlashUrl(getElementAttrById(doc, "swf_url", "value"));
//		// 视频地址
//		String swfKey = url.substring(url.lastIndexOf("/") + 1, url
//				.indexOf(".html"));
//		video.setFlashUrl("http://player.ku6.com/refer/" + swfKey + "/v.swf ");
//		String content = doc.getBody().toHtml();
//		int beginLocal = content.indexOf("document.domain");
//		content = content.substring(beginLocal, content.indexOf("</script>",
//				beginLocal));
//		video.setThumbnail(getScriptVarByName("cover", content).replaceAll(
//				"\"", ""));
//		return video;
//
//	}
//
//	/**
//	 * 获取56视频
//	 * 
//	 * @param url
//	 *            视频URL
//	 */
//	private static FlashVideo get56Video(String url) throws Exception {
//		HtmlPage doc = getURLContent(url);
//		FlashVideo video = new FlashVideo();
//		video.setSource("56视频");
//		video.setPageUrl(url);
//		String html = doc.getBody().toHtml();
//		System.out.println(html);
//		int begin = html.indexOf("f_js_playObject");
//		System.out.println(begin);
//		html = html.substring(begin, html.indexOf("</script>", begin));
//		System.out.println(html);
//		// 视频标题
//		begin = html.indexOf("tit=");
//		video.setTitle(html.substring(begin + 4, html.indexOf("&", begin)));
//		// 视频缩略图
//		String thumbnail = "http://img.";
//		begin = html.indexOf("img_host=");
//		thumbnail += html.substring(begin + 9, html.indexOf("&", begin));
//		thumbnail += "/images/";
//		begin = html.indexOf("pURL=");
//		thumbnail += html.substring(begin + 5, html.indexOf("&", begin)) + "/";
//		begin = html.indexOf("sURL=");
//		thumbnail += html.substring(begin + 5, html.indexOf("&", begin)) + "/";
//		begin = html.indexOf("user=");
//		thumbnail += html.substring(begin + 5, html.indexOf("&", begin))
//				+ "i56olo56i56.com_";
//		begin = html.indexOf("URLid=");
//		thumbnail += html.substring(begin + 6, html.indexOf("&", begin))
//				+ "_b.jpg";
//		video.setThumbnail(thumbnail);
//		// 视频地址
//		begin = html.indexOf("vid=");
//		video.setFlashUrl("http://player.56.com/v_"
//				+ html.substring(begin + 4, html.indexOf("&", begin)) + ".swf");
//		return video;
//	}
//
//	/**
//	 * 获取新浪视频
//	 * 
//	 * @param url
//	 * @return
//	 * @throws Exception
//	 */
//	private static FlashVideo getSinaVideo(String url) throws Exception {
//		HtmlPage doc = getURLContent(url);
//		String content = doc.getBody().toHtml();
//		int beginLocal = content.indexOf("document.domain");
//		content = content.substring(beginLocal + 2, content
//				.indexOf("</script>"));
//		FlashVideo video = new FlashVideo();
//		video.setSource("新浪视频");
//		video.setPageUrl(url);
//		// 视频标题
//		doc.
//		video.setTitle(doc.getElementById("videoTitle").text());
//		// 视频简介
//		// String summary = doc.getElementById("videoContent").text();
//		// 视频缩略图
//		video.setThumbnail(getScriptVarByName("pic", content));
//		// flash地址
//		video.setFlashUrl(getScriptVarByName("swfOutsideUrl", content));
//		return video;
//	}
//
//	/**
//	 * 获取搜狐视频
//	 * 
//	 * @param url
//	 * @return
//	 * @throws Exception
//	 */
//	private static FlashVideo getSohuVideo(String url) throws Exception {
//		HtmlPage doc = getURLContent(url);
//		FlashVideo video = new FlashVideo();
//		video.setSource("搜狐视频");
//		video.setPageUrl(url);
//		// 视频标题
//		video.setTitle(doc.getTitle().split("-")[0].trim());
//		// 视频简介
//		// String summary = doc.select(".vIntro.clear > p").text();
//		// 视频缩略图
//		video.setThumbnail(doc.getElementById("thumbnail").attr("src"));
//		// FLASH地址
//		video.setFlashUrl("http://share.vrs.sohu.com/"
//				+ video.getThumbnail().split("_")[2] + "/v.swf&autoplay=false");
//		return video;
//	}
//
//	/**
//	 * 获取凤凰视频
//	 * 
//	 * @param url
//	 * @return
//	 * @throws Exception
//	 */
//	private static FlashVideo getIfengVideo(String url) throws Exception {
//		HtmlPage doc = getURLContent(url);
//		String content = doc.html();
//		int beginLocal = content.indexOf("var videoinfo");
//		content = content.substring(beginLocal, content
//				.indexOf("}", beginLocal) + 1);
//		beginLocal = content.indexOf("{");
//		content = content.substring(beginLocal + 1, content.indexOf("}",
//				beginLocal));
//		content = content.replaceAll("\"", "").replaceAll("\n", "").trim();
//		FlashVideo video = new FlashVideo();
//		video.setSource("凤凰视频");
//		video.setPageUrl(url);
//		// 视频标题
//		video.setTitle(getScriptVarByName("name", content));
//
//		if (url.indexOf("#") == -1) {
//			// 视频缩略图
//			String[] params = content.split(",");
//			video.setThumbnail(params[5].substring(4, params[5].length()));
//			// FLASH地址
//			video.setFlashUrl("http://v.ifeng.com/include/exterior.swf?guid="
//					+ getScriptVarByName("id", content) + "&AutoPlay=false");
//		} else {
//			// FLASH地址
//			video.setFlashUrl("http://v.ifeng.com/include/exterior.swf?guid="
//					+ url.substring(url.lastIndexOf("#") + 1)
//					+ "&AutoPlay=false");
//			// 视频缩略图
//			video.setThumbnail(doc.getElementById("listP01").getElementsByTag(
//					"img").first().getElementsByTag("src").html());
//		}
//
//		return video;
//	}
//
//	
//
//	/**
//	 * 获取QQ视频
//	 * 
//	 * @param url
//	 * @return
//	 * @throws Exception
//	 */
//	private static FlashVideo getQqVideo(String url) throws Exception {
//		HtmlPage doc = null;
//		FlashVideo video = new FlashVideo();
//		video.setSource("QQ视频");
//		video.setPageUrl(url);
//
//		int beginLocal = 0;
//		if (url.indexOf("?vid=") == -1) {
//			doc = getURLContent(url);
//			String content = doc.html();
//			beginLocal = content.indexOf("VIDEO_INFO={");
//			// 获取视频信息
//			String VIDEO_INFO = content.substring(beginLocal, content.indexOf(
//					"}", beginLocal) + 1);
//			// 视频标题
//			video.setTitle(doc.title().split("-")[0].trim());
//			// 视频地址
//			video.setFlashUrl("http://static.video.qq.com/TPout.swf?vid="
//					+ getScriptVarByName("vid", VIDEO_INFO));
//			// 获取cover信息
//			String COVER_INFO = content.substring(content
//					.indexOf("COVER_INFO = {"), content
//					.indexOf("}", beginLocal) + 1);
//			// 视频缩略图
//			video.setThumbnail(getScriptVarByName("pic", COVER_INFO));
//		} else {
//			beginLocal = url.indexOf("vid=");
//			if (beginLocal == -1) {
//				return null;
//			}
//			String vid = url.substring(beginLocal + 4);
//			doc = getURLContent("http://vv.video.qq.com/getinfo?platform=1&vids="
//					+ vid);
//			System.out.println(doc);
//			// 视频地址
//			video
//					.setFlashUrl("http://static.video.qq.com/TPout.swf?vid="
//							+ vid);
//			// 视频标题
//			video.setTitle(doc.getElementsByTag("ti").first().html());
//			// 视频内容
//			// String summary =
//			// doc.getElementById("videoContent").select("span")
//			// .first().html();
//			// 视频缩略图
//			video.setThumbnail(doc.getElementsByTag("ui").last()
//					.getElementsByTag("url").html().replace(
//							"video.store.qq.com", "vpic.video.qq.com")
//					+ "/" + vid + "_160_90_3.jpg");
//		}
//		return video;
//	}
//
//	/**
//	 * 获取网易视频
//	 * 
//	 * @param url
//	 * @return
//	 * @throws Exception
//	 */
//	private static FlashVideo getWangyiVideo(String url) throws Exception {
//		HtmlPage doc = getURLContent(url);
//		int beginLocal;
//		String html;
//		FlashVideo video = new FlashVideo();
//		video.setSource("网易");
//		video.setPageUrl(url);
//		// 视频标题
//		video.setTitle(doc.getTitle().split("_")[0].trim());
//		// 视频地址、缩略图
//		Element element = doc.getElementById("js_Movie");
//		if (null == element) {
//			html = doc.getElementById("flashArea").getElementsByTag("script")
//					.last().html();
//			beginLocal = html.indexOf("flashPlayer");
//			html = html.substring(beginLocal + 13, html.indexOf("\",",
//					beginLocal));
//			video.setFlashUrl(html);
//			String[] params = html.split("-");
//			video.setThumbnail("http://" + params[3].replaceAll("_", ".")
//					+ ".jpg");
//		} else {
//			html = element.getElementsByTag("script").last().html();
//			beginLocal = html.indexOf("pltype=6");
//			html = html.substring(beginLocal,
//					html.indexOf("name=\"flashvars\"", beginLocal)).trim();
//			html = html.substring(0, html.length() - 1);
//			beginLocal = html.indexOf("topicid=");
//			video
//					.setFlashUrl("http://swf.ws.126.net/v/ljk/shareplayer/ShareFlvPlayer.swf?"
//							+ html.substring(beginLocal, html.indexOf("&",
//									beginLocal)));
//			beginLocal = html.indexOf("vid=");
//			video
//					.setFlashUrl(video.getFlashUrl()
//							+ "&"
//							+ html.substring(beginLocal, html.indexOf("&",
//									beginLocal)));
//			beginLocal = html.indexOf("coverpic=");
//			video.setThumbnail(html.substring(beginLocal + 9, html.indexOf("&",
//					beginLocal)));
//		}
//		return video;
//	}
//
//	/**
//	 * 获取script某个变量的值
//	 * 
//	 * @param name
//	 *            变量名称
//	 * @return 返回获取的值
//	 */
//	private static String getScriptVarByName(String name, String content) {
//		String script = content;
//		int begin = script.indexOf(name);
//		script = script.substring(begin + name.length() + 2);
//		int end = script.indexOf(",");
//		script = script.substring(0, end);
//		String result = script.replaceAll("'", "");
//		if (script.indexOf("'") != -1) {
//			result = script.replaceAll("'", "");
//		} else if (script.indexOf("\"") != -1) {
//			result = script.replaceAll("\"", "");
//		}
//
//		return result.trim();
//	}
//
//	/**
//	 * 根据HTML的ID键及属于名，获取属于值
//	 * 
//	 * @param id
//	 *            HTML的ID键
//	 * @param attrName
//	 *            属于名
//	 * @return 返回属性值
//	 */
//	private static String getElementAttrById(HtmlPage doc, String id,
//			String attrName) throws Exception {
//		Element et = doc.getElementById(id);
//		if (null == et) {
//			return "";
//		}
//		String attrValue = et.attr(attrName);
//		return attrValue;
//	}

	/**
	 * 获取网页的内容
	 */
	private static HtmlPage getURLContent(String url) throws Exception {
		Parser parser = new Parser(url);
		HtmlPage page = new HtmlPage(parser); 
		return page;
	}

	public static void main(String[] args) throws Exception {
		// String tudouUrl = "http://www.tudou.com/programs/view/1JQDkiQrRIw/";
//		String tudouUrl = "http://www.tudou.com/listplay/mI4BrZYVpDQ.html";
//		String youkuUrl = "";
		String ku6Url = "http://v.ku6.com/show/-7af-KZz8cw9dyyITvi1FA...html";
//		String cn6Url = "";
//		String woleUrl = "http://www.56.com/u90/v_ODYzNjk4Nzk.html";
		// String
		// woleUrl="http://www.56.com/w97/play_album-aid-9974138_vid-ODg4MDg4MzY.html";
//		String souhuUrl = "";
//		String sinaUrl = "";
//		String ifengUrl = "http://v.ifeng.com/ent/mingxing/201303/b737c337-57bf-46fa-b660-0cc80f13c2ac.shtml";
		// String
//		// ifengUrl="http://v.ifeng.com/v/2013lhrdbmfh/index.shtml#3a8e0cde-7689-4c3e-af23-feb817f31b94";
//		String yinyuetaiUrl = "";
//		// String qqUrl = "http://v.qq.com/zt/oreo.html";
//		// String
//		// qqUrl="http://v.qq.com/cover/2/284ltkvsj79kgy7.html?vid=y0111lfkevj";
//		String qqUrl = "http://v.qq.com/cover/d/dusgyw61ld1bij0.html?vid=i0012l6nyg8&pgv_ref=aio2012&ptlang=20525";
//		String wangyiUrl = "";

//		FlashVideo video = FlashVideoUtil.getVideoInfo(ku6Url);
//		if (null == video) {
//			System.out.println("无法解析的视频地址");
//			return;
//		}
//		System.out.println("视频标题：" + video.getTitle());
//		System.out.println("视频地址：" + video.getFlashUrl());
//		System.out.println("视频来源：" + video.getSource());
//		System.out.println("视频缩略图：" + video.getThumbnail());
//		System.out.println("视频原始地址：" + video.getPageUrl());
	}

}