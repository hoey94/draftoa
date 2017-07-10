package com.starsoft.core.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.fileupload.util.Streams;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.HasParentFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.Bullet;
import org.htmlparser.tags.BulletList;
import org.htmlparser.tags.DefinitionList;
import org.htmlparser.tags.DefinitionListBullet;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.HeadingTag;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LabelTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.ParagraphTag;
import org.htmlparser.tags.Span;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableHeader;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.tags.TitleTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.HtmlPage;
import com.starsoft.core.util.StringUtil;
/**
 * 网络爬虫解析类
 * @author Administrator
 *
 */
public  class HtmlParserUtil {
	/***
	 * 获取都是A标签
	 * @param url
	 * @param pretag
	 * @return
	 * @throws ParserException
	 */
	public static List getHrefList(String url,String pretag,String pageCharset) throws ParserException{
		List result=new ArrayList();
		Parser parser = new Parser(url);
		parser.setEncoding(pageCharset);
		HtmlPage page = new HtmlPage(parser); 
		NodeFilter parentFilter=tagToFilter(pretag);
		NodeList nodeList = parser.extractAllNodesThatMatch(parentFilter);
		for (int i = 0; i < nodeList.size(); i++){
			 Node tag=nodeList.elementAt(i);//取得要查看的信息块，在信息块中找连接地址
			 String tagName = tag.getClass().getName();
			 if(tag instanceof LinkTag){//"A"
				 LinkTag linkTag=(LinkTag)tag;
				 String hrefurl=linkTag.getLink();
				 result.add(hrefurl);
			 }else{
				 NodeList subnodelist=tag.getChildren();
				 NodeFilter filter = new TagNameFilter("A");
				 NodeList anodelist = subnodelist.extractAllNodesThatMatch(filter, true); //取得所有A的连节点
				 int sizea=anodelist.size();
				 for(int s=0;s<sizea;s++){
					 Node taga=anodelist.elementAt(s);
					 LinkTag linkTag=(LinkTag)taga;
					 String hrefurl=linkTag.getLink();
					 result.add(hrefurl);
				 }
			 }
	      }
		return result;
	}
	/***
	 * 标签转化为过滤条件
	 * @param tag
	 * @return
	 */
	public static NodeFilter tagToFilter(String tag){
		if(tag.startsWith("<")){
			tag=tag.substring(1);
		}
		String[] list=tag.split("<");
		NodeFilter parentFilter=null;
		for(int t=0;t<list.length;t++){
			NodeFilter subFilter=null;
			String str=list[t];
			if(str.endsWith(">")){
				str=str.substring(0,str.length()-1).trim();
			}
			String[] strs=str.split(" ");
			if(strs.length==1){
				subFilter=new TagNameFilter(strs[0]);
			}else if(strs.length==2){
				String attribute=strs[1];
				String key=attribute.substring(0,attribute.indexOf("="));
				String value=attribute.substring(attribute.indexOf("=")+1,attribute.length());
				if(value.endsWith("\"")||StringUtil.isNumber(value)){
					 value=value.replace("\"", "");
					 subFilter=new AndFilter(new TagNameFilter(strs[0]),new HasAttributeFilter(key, value));
				}else{
					value=value.replace("\"", "");
					subFilter=new AndFilter(new TagNameFilter(strs[0]),new HasAttributeFilter(key));
				}
			}else{
				int strslength=strs.length;
				String attribute=strs[1];
				String key=attribute.substring(0,attribute.indexOf("="));
				String value=attribute.substring(attribute.indexOf("=")+1,attribute.length());
				value=value.replace("\"", "");
				subFilter=new AndFilter(new TagNameFilter(strs[0]),new HasAttributeFilter(key, value));
				for(int s=2;s<strslength;s++){	
					String attributet=strs[s];
					if(attributet.indexOf("=")>-1){
						String keyt=attributet.substring(0,attributet.indexOf("="));
						String valuet=attributet.substring(attributet.indexOf("=")+1,attributet.length());
						if(valuet.endsWith("\"")){
							valuet=valuet.replace("\"", "");
							subFilter=new AndFilter(subFilter,new HasAttributeFilter(keyt, valuet));
						}
					}
				}
			}
			if(t<1){
				parentFilter=subFilter;
			}else{
				parentFilter=new AndFilter(subFilter,new HasParentFilter(parentFilter));
			}
		}
		return parentFilter;
	}
	/***
	 * 取节点内容
	 * @param tag
	 * @return
	 */
	public static String getTagContent(Node tag){
		 String result="";
		 if(tag instanceof LinkTag){//"A"
			 LinkTag linkTag=(LinkTag)tag;
			 result=linkTag.getLinkText();
		 }else if(tag instanceof Div){//DIV
			 Div div=(Div)tag;
			 result= div.getStringText();
		 }else if(tag instanceof Span){//SPAN
			 Span span=(Span)tag;
			 result=span.getStringText();
		 }else if(tag instanceof HeadingTag){//"H1", "H2", "H3", "H4", "H5", "H6"
			 HeadingTag tagTemp=(HeadingTag)tag;
			 result=tagTemp.getStringText();
		 }else if(tag instanceof TableTag){//"TABLE"
			 
		 }else if(tag instanceof DefinitionListBullet){//"DD", "DT"
			 DefinitionListBullet tagTemp=(DefinitionListBullet)tag;
			 result=tagTemp.getStringText();
		 }else if(tag instanceof DefinitionList){//"DL"
			 DefinitionList tagTemp=(DefinitionList)tag;
			 result=tagTemp.getStringText();
		 }else if(tag instanceof TitleTag){//"TITLE"
			 TitleTag tagTemp=(TitleTag)tag;
			 result=tagTemp.getStringText();
		 }else if(tag instanceof TableRow){//"TR"
		 }else if(tag instanceof TableHeader){//"TH"
			 TableHeader tagTemp=(TableHeader)tag;
			 result=tagTemp.getStringText();
		 }else if(tag instanceof TableColumn){//"TD"
			 TableColumn tagTemp=(TableColumn)tag;
			 result=tagTemp.getStringText();
		 }else if(tag instanceof ParagraphTag){//"P"
			 ParagraphTag tagTemp=(ParagraphTag)tag;
			 result=tagTemp.getStringText();
		 }else if(tag instanceof LabelTag){//"LABEL"
			 LabelTag tagTemp=(LabelTag)tag;
			 result=tagTemp.getStringText();
		 }else if(tag instanceof ImageTag){//"IMAGE"
		 }else if(tag instanceof Bullet){//LI
			 Bullet tagTemp=(Bullet)tag;
			 result=tagTemp.getStringText();
		 }else if(tag instanceof BulletList){//"UL", "OL"
			 BulletList tagTemp=(BulletList)tag;
			 result=tagTemp.getStringText();
		 }
		return result;
	}
	/**
	 * 通过标签取页面元素
	 * @param parser
	 * @param tag
	 * @return
	 * @throws ParserException 
	 */
	public static String getParserContent(String url,String tag,String pageCharset) throws ParserException{
		 String result="";
		 Parser parser = new Parser(url);
		 NodeFilter parentFilter=tagToFilter(tag);
		 parser.setEncoding(pageCharset);
		 NodeList nodeList = parser.extractAllNodesThatMatch(parentFilter);
		 if(nodeList.size()>0){
			 Node tagTemp=nodeList.elementAt(0);
			 result=getTagContent(tagTemp);
			 NodeFilter filterimg  = new TagNameFilter("img");//IMAGE
			 NodeList imglist = nodeList.extractAllNodesThatMatch(filterimg, true); //取得所有IMAGE的连节点
			 int imglistsize=imglist.size();
			 if(imglistsize>0){//包含图片
				 for(int s=0;s<imglistsize;s++){
					 Node tagImg=imglist.elementAt(s);
					 if(tagImg instanceof ImageTag){
						 ImageTag temp=(ImageTag)tagImg;
						 String imgUrl=temp.getImageURL();
						 String imgsrc=temp.getAttribute("src");
						 System.out.println("========================imgsrc="+imgsrc);
						 System.out.println("========================imgUrl="+imgUrl);
						 try {
							URL imgurl=new URL(imgUrl);
							URLConnection con;
							con = imgurl.openConnection();
					        InputStream in = con.getInputStream();  
					        String fileName=imgsrc.substring(imgsrc.lastIndexOf("/"), imgsrc.length());
				            FileOutputStream out = new FileOutputStream("F:\\"+fileName);  
				            Streams.copy(in, out, true);
						} catch (MalformedURLException e) {
							e.printStackTrace();
						} catch (IOException e) {
						}   
						result=result.replaceAll(imgsrc, imgUrl);
					 }
				 }
			 }
			 NodeFilter filterA  = new TagNameFilter("A");//A
			 NodeList alist = nodeList.extractAllNodesThatMatch(filterA, true); //取得所有A的连节点
			 int alistsize= alist.size();
			 if(alistsize>0){//包含附件
				 for(int s=0;s<alistsize;s++){
					 Node tagImg=alist.elementAt(s);
					 if(tagImg instanceof LinkTag){
						 LinkTag temp=(LinkTag)tagImg;
						 String alink=temp.getLink();
						 String href=temp.getAttribute("href");
						 System.out.println("========================href="+href);
						 System.out.println("========================alink="+alink);
						 if(!alink.equals(href)){
							 result=result.replaceAll(href, alink);
						 }
					 }
				 }
				 
			 }
				
			 //服务器上资源下载到本地
			 //更新内容中的图片、附件、视频、文档等信息
				
			//替换图片，图片要下载到本地
			//替换超链接文件、文档
			//替换视频
		 }
		
		 return result;
	}
	
}
