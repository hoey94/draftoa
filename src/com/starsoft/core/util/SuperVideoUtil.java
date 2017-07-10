package com.starsoft.core.util;


import com.guihua.video.extract.Video;
import com.guihua.video.extract.VideoUtil;
import com.guihua.video.extract.jsoup.Jsoup;
import com.guihua.video.extract.jsoup.nodes.Document;
/**
 * support 支持
 *  优酷：v.youku.com
 *	土豆：www.tudou.com
 *	腾讯：v.qq.com
 *	网易公开课：v.163.com/movie
 *	乐视TV：www.letv.com
 *	搜狐：tv.sohu.com
 *	酷六：v.ku6.com
 *	音悦台：v.yinyuetai.com/video
 *	凤凰视频：v.ifeng.com
 * 
 *
 *	@author один пар кан
 *	@version 1.0
 */
public class SuperVideoUtil extends VideoUtil {
	/**
	 * 获取视频信息方法
	 * @param url
	 * @return Video
	 * @throws Exception
	 * @Info:
	 *  视频标题：getTitle()
	 *  视频地址：getFlashUrl()
	 *  视频时长：getTime()
	 *  视频来源：getSource()
	 *  视频简介：getSummary()
	 *  视频缩略图：getThumbnail()
	 *  视频原始地址：getPageUrl()
	 *  视频网页代码：getHtmlCode()
	 */
	public static Video getVideoInfo(String url) throws Exception {
		Video video = null;

		if (url.indexOf("v.youku.com") != -1) {
			try {
				video = getYouKuVideo(url);
			} catch (Exception e) {
				video = null;
			}
		} else if (url.indexOf("www.tudou.com") != -1) {
			try {
				video = getTudouVideo(url);
			} catch (Exception e) {
				video = null;
			}
		} else if (url.indexOf("v.ku6.com") != -1) {
			try {
				video = getKu6Video(url);
			} catch (Exception e) {
				video = null;
			}
		} else if (url.indexOf("www.letv.com") != -1) {
			try {
				video = getLetvVideo(url);
			} catch (Exception e) {
				video = null;
			}
		} else if (url.indexOf("v.qq.com") != -1) {
			try {
				video = getQQVideo(url);
			} catch (Exception e) {
				video = null;
			}
		} else if (url.indexOf("tv.sohu.com") != -1) {
			try {
				video = getSohuVideo(url);
			} catch (Exception e) {
				video = null;
			}
		} else if (url.indexOf("v.ifeng.com") != -1) {
			try {
				video = getIfengVideo(url);
			} catch (Exception e) {
				video = null;
			}
		} else if (url.indexOf("v.yinyuetai.com/video") != -1) {
			try {
				video = getYinYueTaiVideo(url);
			} catch (Exception e) {
				video = null;
			}
		}else if (url.indexOf("v.163.com/movie") != -1 || url.indexOf("open.163.com") != -1) {
			try {
				video = getNetEaseVideo(url);
			} catch (Exception e) {
				video = null;
			}
		} else {
			Document doc = getURLContent(url);
			video = new Video();
			video.setTitle(doc.title());
			video.setPageUrl(url);
		}

		return video;
	}

	public static Video getQQVideo(String url) throws Exception {
		Document doc = getURLContent(url);
		String content = doc.html();
		content = content.substring(content.indexOf("var VIDEO_INFO={"));
		content = content.substring(0, content.indexOf("};"));
		content.replace("var VIDEO_INFO={", "");
		content = content.replaceAll("\n", "").trim();
		String contents[] = content.split(",");
		String vid = getScriptVarByName("vid:", contents).replaceAll("\"", "");
		String title = getScriptVarByName("title :", contents).replaceAll("\"", "");
		/**
		 * 视频地址
		 */
		String flash = "http://static.video.qq.com/TPout.swf?vid="+vid+"&auto=0";
		Video video = new Video();
		video.setTitle(title);
		video.setSource("腾讯视频");
		video.setPageUrl(url);
		video.setHtmlCode(getHtmlCode(flash));
		video.setFlashUrl(flash);
		return video;
	}

	public static Video getNetEaseVideo(String url) throws Exception {
		Document doc = getURLContent(url);
		String content = doc.html();
		content = content.substring(content.indexOf("// 当前movie信息"));
		content = content.substring(0, content.indexOf("// 专辑信息"));
		content = content.substring(content.indexOf("id"));
		content = content.substring(0, content.indexOf("};"));
		content = content.replaceAll("\n", "").trim();
		String contents[] = content.split(",");
		
		String summary=doc.getElementsByClass("f-c3").html();
		summary = summary.substring(summary.indexOf("</em>")).replace("</em>","");
		summary = summary.substring(0, summary.indexOf("</div>"));
		/**
		 * 视频标题
		 */
		String title = getScriptVarByName("title :", contents).replaceAll("'", "");
		String topicid = getScriptVarByName("number :", contents).replaceAll("'", "");
		String vid = getScriptVarByName("id :", contents).replaceAll("'", "");
		/**
		 * 视频缩略图
		 */
		String imgpath = getScriptVarByName("image :", contents).replaceAll("'", "").replaceAll(" \\+ ", "");

		/**
		 * 视频地址
		 */
		String flash = getScriptVarByName(" src :", contents).replaceAll("'", "");
		Video video = new Video();
		video.setTitle(title);
		video.setThumbnail(imgpath);
		video.setSummary(summary);
		video.setFlashUrl(flash);
		video.setSource("网易视频");
		video.setPageUrl(url);
		video.setHtmlCode(getHtmlCode(flash));
		return video;
	}

	private static String getHtmlCode(String flashUrl) throws Exception {
		return "<embed src=\""
				+ flashUrl
				+ "\" allowFullScreen=\"true\" quality=\"high\" width=\"655\" height=\"100%\" align=\"middle\" allowScriptAccess=\"always\" type=\"application/x-shockwave-flash\"></embed>";
	}

	private static String getScriptVarByName(String name, String contents[]) {
		String text = "";
		for (String s : contents) {
			if (s.contains(name)) {
				text = s.substring(s.indexOf(name) + name.length());
				break;
			}
		}
		return text;
	}

	private static Document getURLContent(String url) throws Exception {
		Document doc = Jsoup.connect(url).data("query", "Java")
				.userAgent("Mozilla").cookie("auth", "token").timeout(10000)
				.get();

		return doc;
	}
}

