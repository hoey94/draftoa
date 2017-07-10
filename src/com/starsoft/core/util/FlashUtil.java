package com.starsoft.core.util;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;

/**
 * Created with Eclipse.
 * 文件：FlashUtil.java
 * 作者：崔兵兵
 * 时间：2015-7-15
 * 描述：flash转pdf等等
 * To change this template use File | Settings | File Templates
 */
public class FlashUtil {
	private static ConfigUtil cu = new ConfigUtil("OpenOfficeService.properties");
    public static  void main(String[] args){
        String outPath = beginConvert("D:\\SOURCE","123.doc");
        System.out.println("生成swf文件:" + outPath);
//        boolean outPath = new FlashUtil().isExistFlash("123.pdf");
        System.out.println("是否存在swf文件:" + outPath);
    }
    private static final String DOC = ".doc";
    private static final String DOCX = ".docx";
    private static final String XLS = ".xls";
    private static final String XLSX = ".xlsx";
    private static final String PDF = ".pdf";
    private static final String SWF = ".swf";
    private static final String TOOL = "pdf2swf.exe";

    /**
     * 入口方法-通过此方法转换文件至swf格式
     * @param filePath 上传文件所在文件夹的绝对路径
     * @param fileName	文件名称
     * @return			生成swf文件名
     */
    public static String beginConvert(String filePath,String fileName) {
    	try {
    	       String outFile = "";
    	        String fileNameOnly = "";
    	        String fileExt = "";
    	        if (null != fileName && fileName.indexOf(".") > 0) {
    	            int index = fileName.indexOf(".");
    	            fileNameOnly = fileName.substring(0, index);
    	            fileExt = fileName.substring(index).toLowerCase();
    	        }
    	        String inputFile = filePath + File.separator + fileName;
    	        String outputFile = filePath;
    	        //如果是flash文件，直接显示
    	        if(fileExt.equals(SWF)){
    	            outFile = fileName;
    	        }else {
    	            //主要是针对中文汉字转拼音
    	            fileNameOnly = new CnToSpell().getPinYin(fileNameOnly);
    	            //如果存在对应的flash文件
    	            boolean  isExistFlash = isExistFlash(filePath,fileNameOnly);
    	            if(isExistFlash){
    	                outFile = fileNameOnly + SWF;
    	            }else {
    	                //如果是office文档，先转为pdf文件
    	                if (fileExt.equals(DOC) || fileExt.equals(DOCX) || fileExt.equals(XLS)
    	                        || fileExt.equals(XLSX)) {
    	                    outputFile = filePath + File.separator + fileNameOnly + PDF;
    	                    File pdfFile = new File(outputFile);
    	                    if(!pdfFile.exists()){//判断pdf文件是否已经生成
    	                        office2PDF(inputFile, outputFile);
    	                    }
    	                    inputFile = outputFile;
    	                    fileExt = PDF;
    	                }
    	                if (fileExt.equals(PDF)) {
    	                    outputFile = filePath + File.separator + fileNameOnly + SWF;
    	                    outputFile = outputFile.replace("\\","/");
    	                    File swfFile = new File(outputFile);
    	                    if(!swfFile.exists()){//判断swf文件是否已经生成
    	                        File parentFolder = swfFile.getParentFile();
    	                        if(parentFolder!=null&&!parentFolder.exists()){
    	                            parentFolder.mkdirs();
    	                        }
    	                        String toolFile = "C:\\Program Files (x86)\\SWFTools\\pdf2swf.exe";
//    	                        if(filePath.indexOf("flexpaper")==-1){
//    	                            toolFile = filePath + File.separator +"flexpaper"+ File.separator + TOOL;
//    	                        }else{
//    	                            toolFile = filePath + File.separator + TOOL;
//    	                        }
    	                        convertPdf2Swf(inputFile, outputFile, toolFile);
    	                    }
    	                    outFile = fileNameOnly + SWF;
    	                }
    	            }
    	        }
    	        return outFile;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
    }

    /**
     * 将pdf文件转换成swf文件
     * @param sourceFile pdf文件绝对路径
     * @param outFile	 swf文件绝对路径
     * @param toolFile	 转换工具绝对路径
     */
    private static void convertPdf2Swf(String sourceFile, String outFile,
                                String toolFile) {
        String command = toolFile + " \"" + sourceFile + "\" -o  \"" + outFile
                + "\" -s flashversion=9 ";
        try {
            Process process = Runtime.getRuntime().exec(command);
            System.out.println(loadStream(process.getInputStream()));
            System.err.println(loadStream(process.getErrorStream()));
            System.out.println(loadStream(process.getInputStream()));
            System.out.println("###--Msg: swf 转换成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检测文件夹下是否已存在对应的flash文件
     * @return
     */
        private static boolean isExistFlash(String filePath,String fileNameOnly){
            String fileName = fileNameOnly.substring(fileNameOnly.lastIndexOf("/")+1);
            String newFilePath = fileNameOnly.substring(0 ,fileNameOnly.lastIndexOf("/")+1);
            File file = new File(filePath + File.separator+newFilePath);
            if(!file.exists()){//判断是否已经生成新文件夹，然后再去判断文件夹是否存在对应的flash文件
                return false;
            }
            File[] files = file.listFiles();
            for(int j=0;j<files.length;j++){
                if(files[j].isFile()){
                    String filesName = files[j].getName();
                    if(filesName.indexOf(".")!=-1){
                        if(SWF.equals(filesName.substring(filesName.lastIndexOf(".")).toLowerCase())){
                                if(fileName.equals(filesName.substring(0,filesName.lastIndexOf(".")))){
                                    return true;
                                }
                        }
                    }
                }
            }
            return false;
        }

    /**
     * office文档转pdf文件
     * @param sourceFile	office文档绝对路径
     * @param destFile		pdf文件绝对路径
     * @return
     */
    private static int office2PDF(String sourceFile, String destFile) {
        String OpenOffice_HOME = cu.getPropByName("OO_HOME");
        String host_Str = cu.getPropByName("oo_host");
        String port_Str = cu.getPropByName("oo_port");
        try {
            File inputFile = new File(sourceFile);
            if (!inputFile.exists()) {
                return -1; // 找不到源文件
            }
            // 如果目标路径不存在, 则新建该路径
            File outputFile = new File(destFile);
            if (!outputFile.getParentFile().exists()) {
                outputFile.getParentFile().mkdirs();
            }
            // 启动OpenOffice的服务
            String command = OpenOffice_HOME
                    + "\\program\\soffice.exe -headless -accept=\"socket,host="
                    + host_Str + ",port=" + port_Str + ";urp;\"";
            System.out.println("###\n" + command);
            Process pro = Runtime.getRuntime().exec(command);
            // 连接openoffice服务
            OpenOfficeConnection connection = new SocketOpenOfficeConnection(
                    host_Str, Integer.parseInt(port_Str));
            connection.connect();
            // 转换
            DocumentConverter converter = new OpenOfficeDocumentConverter(
                    connection);
            converter.convert(inputFile, outputFile);

            // 关闭连接和服务
            connection.disconnect();
            pro.destroy();

            return 0;
        } catch (FileNotFoundException e) {
            System.out.println("文件未找到！");
            e.printStackTrace();
            return -1;
        } catch (ConnectException e) {
            System.out.println("OpenOffice服务监听异常！");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }

    static String loadStream(InputStream in) throws IOException{
        int ptr = 0;
        in = new BufferedInputStream(in);
        StringBuffer buffer = new StringBuffer();

        while ((ptr=in.read())!= -1){
            buffer.append((char)ptr);
        }
        return buffer.toString();
    }
}

