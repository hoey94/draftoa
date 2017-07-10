package com.starsoft.core.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;

public class FTPUtil {
	public static FTPUtil FTPUtil = null;
	public static final int BINARY_FILE_TYPE = 2;
	public static final int ASCII_FILE_TYPE = 0;
	String ftp_ip = "61.158.140.90";
	int ftp_port = 21;
	String ftp_username = "Sub42646";
	String ftp_password = "hnxtjc123456";
	FTPClient ftpClient = null;
	Logger log = Logger.getLogger(FTPUtil.class);

	private FTPUtil() {
		this.ftpClient = this.connectFTP(ftp_ip, ftp_port, ftp_username, ftp_password);
	}
	 /**
     * 建立连接
     * @param Ip String
     * @param Port int
     * @param UserName String
     * @param Password String
     * @return FtpClient
     */
    private FTPClient connectFTP(String Ip, int Port, String UserName,
                                 String Password) {
    	FTPClient ftpClient = null;
		try {
			this.ftpClient.connect(this.ftp_ip, ftp_port);
			this.ftpClient.login(this.ftp_username, this.ftp_password);
			this.ftpClient.enterLocalPassiveMode();
			this.log.info("连接ftp成功");
		} catch (Exception e) {
			this.log.info("连接ftp失败");
			e.printStackTrace();
		}
        return ftpClient;
    }
	 /**
     * 建立连接
     * @param Ip String
     * @param Port int
     * @param UserName String
     * @param Password String
     * @return FtpClient
     */
    private FTPClient reConnect() {
    	FTPClient ftpClient = null;
		try {
			this.ftpClient.connect(this.ftp_ip, ftp_port);
			this.ftpClient.login(this.ftp_username, this.ftp_password);
			this.ftpClient.enterLocalPassiveMode();
			this.log.info("连接ftp成功");
		} catch (Exception e) {
			this.log.info("连接ftp失败");
			e.printStackTrace();
		}
        return ftpClient;
    }
	public static FTPUtil getInstance() {
		if (FTPUtil == null)
			FTPUtil = new FTPUtil();
		return FTPUtil;
	}
	 /**
     * 关闭连接
     * @param ftp FtpClient
     */
    private void disconnectFTP(FTPClient ftp) {
        try {
        	ftp.disconnect();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 创建目录
     * @param Ip String
     * @param Port int
     * @param UserName String
     * @param Password String
     * @param RemotePath String
     * @param DirName String
     */
    public void mkDir(String RemotePath, String DirName) {
    	if (!(this.ftpClient.isConnected())){
			reConnect();
		}
        try {
			ftpClient.makeDirectory(RemotePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        disconnectFTP(ftpClient);
    }
	public void upload(String remotePath, String localPath) {
		if (!(this.ftpClient.isConnected())){
			reConnect();
		}
		FileInputStream fis = null;
		try {
			File srcFile = new File(localPath);
			fis = new FileInputStream(srcFile);

			this.ftpClient.setBufferSize(1024);
			this.ftpClient.setControlEncoding("GBK");

			this.ftpClient.setFileType(2);
			this.ftpClient.storeFile(remotePath, fis);
			this.log.info("上传文件成功");
		} catch (IOException e) {
			this.log.info("上传文件出现异常");
			e.printStackTrace();
			throw new RuntimeException("上传文件抛出异常", e);
		} finally {
			IOUtils.closeQuietly(fis);
			try {
				this.ftpClient.disconnect();
			} catch (IOException e) {
				this.log.info("关闭ftp异常");
				e.printStackTrace();
				throw new RuntimeException("关闭ftp出现异常", e);
			}
		}
	}

	public void download(String remotePath, String localPath) {
		if (!(this.ftpClient.isConnected())){
			reConnect();
		}
		FileOutputStream fos = null;
		try {
			String remoteFileName = remotePath;
			File file = new File(localPath);
			if (!(file.exists()))
				file.createNewFile();
			fos = new FileOutputStream(localPath);
			this.ftpClient.setBufferSize(1024);
			this.ftpClient.setFileType(2);
			this.ftpClient.retrieveFile(remoteFileName, fos);
			fos.flush();
			this.log.info("ftp下载文件成功");
		} catch (IOException e) {
			this.log.info("FTP下载文件出现异常");
			e.printStackTrace();
			throw new RuntimeException("FTP下载文件抛出异常", e);
		} finally {
			IOUtils.closeQuietly(fos);
			try {
				this.ftpClient.disconnect();
			} catch (IOException e) {
				this.log.info("ftp下载文件关闭时抛出异常");
				e.printStackTrace();
				throw new RuntimeException("ftp下载文件关闭时抛出异常", e);
			}
		}
	}

	public void delete(String path) {
		if (!(this.ftpClient.isConnected())){
			reConnect();
		}
		try {
			this.ftpClient.deleteFile(path);
			this.log.info("ftp删除文件成功");
		} catch (Exception e) {
			this.log.info("FTP删除文件出现异常");
			e.printStackTrace();
			throw new RuntimeException("FTP删除文件抛出异常", e);
		} finally {
			try {
				this.ftpClient.disconnect();
			} catch (IOException e) {
				this.log.info("FTP删除文件出现异常");
				e.printStackTrace();
				throw new RuntimeException("FTP删除文件出现异常", e);
			}
		}
	}

	public void downloadAll() {
		if (!(this.ftpClient.isConnected())){
			reConnect();
		}
		FileOutputStream fos = null;
		try {
			FTPFile[] arrayOfFTPFile1;
			this.ftpClient.setControlEncoding("GBK");
			FTPFile[] files = this.ftpClient.listFiles();
			int j = (arrayOfFTPFile1 = files).length;
			for (int i = 0; i < j; ++i) {
				FTPFile file = arrayOfFTPFile1[i];
				if (file.getType() == 0) {
					String filename = file.getName();
					String localname = "temp/" + filename;
					File file1 = new File(localname);
					if (!(file1.exists()))
						file1.createNewFile();

					fos = new FileOutputStream(localname);
					this.ftpClient.setBufferSize(1024);

					this.ftpClient.setFileType(2);
					this.ftpClient.retrieveFile(filename, fos);
					fos.flush();
				}
			}
		} catch (IOException e) {
			this.log.info("FTP删除所有文件成功");
			e.printStackTrace();
			throw new RuntimeException("FTP删除所有文件成功", e);
		} finally {
			IOUtils.closeQuietly(fos);
			try {
				this.ftpClient.disconnect();
			} catch (IOException e) {
				this.log.info("FTP删除所有文件异常");
				e.printStackTrace();
				throw new RuntimeException("FTP删除所有文件异常", e);
			}
		}
	}

}