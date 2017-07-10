package com.starsoft.core.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.apache.commons.fileupload.util.Streams;

public class FileUtil {
	/**
	 * 创建目录
	 * 
	 * @param folderPath
	 *            :目录路径
	 * @return
	 * @throws IOException
	 */
	public static boolean createFolder(String folderPath) throws IOException {
		boolean result = false;
		File f = new File(folderPath);
		result = f.mkdirs();
		return result;
	}

	/**
	 * 删除目录下所有文件
	 * 
	 * @param directory
	 *            (File 对象)
	 */
	public void emptyDirectory(File directory) {
		File[] entries = directory.listFiles();
		for (int i = 0; i < entries.length; i++) {
			entries[i].delete();
		}
	}

	/**
	 * 创建文件
	 * 
	 * @param filepath
	 *            :文件所在目录路径,比如:c:/test/test.txt
	 * @return
	 */
	public static boolean makeFile(String filepath) throws IOException {
		boolean result = false;
		File file = new File(filepath);
		result = file.createNewFile();
		file = null;
		return result;
	}

	/**
	 * 删除文件
	 * 
	 * @param filepath
	 *            :文件所在物理路径
	 * @return
	 */
	public static boolean isDel(String filepath) {
		boolean result = false;
		File file = new File(filepath);
		result = file.delete();
		file = null;
		return result;
	}

	/**
	 * 文件重命名
	 * 
	 * @param filepath
	 *            :文件所在物理路径
	 * @param destname
	 *            :新文件名
	 * @return
	 */
	public static void renamefile(String filepath, String destname) {
		File fileOld = new File(filepath);
		String fileParent = fileOld.getParent();
		File newFile = new File(fileParent + "//" + destname);
		fileOld.renameTo(newFile);
		fileOld = null;
		newFile = null;
	}

	/**
	 * 1.* 将文件内容写入数据库中 2.* @param filepath:文件所在物理路径 3.* @param content:写入内容 4.* @throws
	 * Exception 5.
	 */
	public static void WriteFile(String filepath, String content)
			throws Exception {
		FileWriter filewriter = new FileWriter(filepath, true);// 写入多行
		PrintWriter printwriter = new PrintWriter(filewriter);
		printwriter.println(content);
		printwriter.flush();
		printwriter.close();
		filewriter.close();
	}
	/**
	 * 1.* 将文件内容写入数据库中 2.* @param filepath:文件所在物理路径 3.* @param content:写入内容 4.* @throws
	 * Exception 5.
	 */
	public static void WriteFile(String filepath, StringBuilder content)
			throws Exception {
		FileWriter filewriter = new FileWriter(filepath, true);// 写入多行
		PrintWriter printwriter = new PrintWriter(filewriter);
		printwriter.println(content);
		printwriter.flush();
		printwriter.close();
		filewriter.close();
	}
	/***
	 * 删除文件
	 * @param file
	 */
	 public static void removeFile(File file) {
		if (file.isFile()) {
			file.delete();
		}
	 }
	 /***
	  * 读取文件
	  * @param file
	  * @param output
	  * @throws IOException
	  */
	 public static void readFile(File file, OutputStream output) throws IOException {
	        FileInputStream input = null;
	        FileChannel fc = null;
	        try {
	            input = new FileInputStream(file);
	            fc = input.getChannel();
	            ByteBuffer buffer = ByteBuffer.allocate(4096);
	            for(;;) {
	                buffer.clear();
	                int n = fc.read(buffer);
	                if(n==(-1))
	                    break;
	                output.write(buffer.array(), 0, buffer.position());
	            }
	        }
	        finally {
	            if(fc!=null) {
	                try {
	                    fc.close();
	                }
	                catch(IOException e) {}
	            }
	            if(input!=null) {
	                try {
	                    input.close();
	                }
	                catch(IOException e) {}
	            }
	        }
	    }

	// 对单个文件进行复制
	public static void fileCopy(File file, String path) {
			try {
				Streams.copy(new FileInputStream(file), new FileOutputStream(path + "\\" + file.getName()), true);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
	    
	//对文件夹及其内部的文件进行复制
	public static void dirCopy(String directory, String path) {
		File folderFile = new File(directory);
		if(folderFile.isDirectory()){
			File[] files = folderFile.listFiles();
			for (File f1 : files) {
				if (f1.isFile()) {
					fileCopy(f1, path);
				} else{
					System.out.println("文件无法识别");
				}
			}
		}
	}

	/***
	 * 文件名编码
	 * 
	 * @param fileName
	 * @return
	 */
	public static String encodingFileName(String fileName) {
		String returnFileName = "";
		try {
			returnFileName = URLEncoder.encode(fileName, "UTF-8");
			returnFileName = returnFileName.replaceAll(" ", "%20");
			if (returnFileName.length() > 150) {
				returnFileName = new String(fileName.getBytes("GBK"),
						"ISO8859-1");
				returnFileName = returnFileName.replaceAll(" ", "%20");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return returnFileName;
	}
	/****
	 * 读取文件txt
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static String readtxt(String fileName,String encoding) throws IOException {
		StringBuffer result = new StringBuffer("");
		File file=new File(fileName);
        if(file.isFile() && file.exists()){ //判断文件是否存在
            InputStreamReader read = new InputStreamReader(
            new FileInputStream(file),encoding);//考虑到编码格式
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while((lineTxt = bufferedReader.readLine()) != null){
                result.append(lineTxt);
            }
            read.close();
        }
		return result.toString();
	}
}
