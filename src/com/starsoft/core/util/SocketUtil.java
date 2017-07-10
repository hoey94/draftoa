package com.starsoft.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import org.apache.log4j.Logger;


//import com.sinoufc.base.GlobalParameter;
//import com.sinoufc.base.util.DateSplitUtil;

public class SocketUtil {
		
	public static Logger m_log = Logger.getLogger(SocketUtil.class);
	
    //server地址
    private String host = null;

    //server端口
    private  int port = 0;

    private Socket socket = null;
    
    private InputStream pis = null;     
    private OutputStream pos = null;
    
    //休眠时间为SLEEP_TIME/1000秒
    private final int SLEEP_TIME = 3000; 
    
  //Socket读超时时间，SOCKET_TIMEOUT/1000秒
    private final int SOCKET_TIMEOUT = 20000; 

    
    //循环处理次数，默认为0，无限次
    //private final int MaxCount =0;
    
    
	public void setHost(String host) { 
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}   
	
    
    public SocketUtil(){   	
    	
    }
    
    public SocketUtil(Socket socket) throws IOException{    	
			pis = socket.getInputStream();
			pos = socket.getOutputStream();
    }
    
    
    /**
     * 建立Socket连接。
     * 
     * @param 无
     * @return true:创建成功   false 创建失败
     */
    public boolean createConnection() {
        boolean bRet = false;
    	m_log.info("进入createConnection()函数");

        pis = null;
        try {
            socket = new Socket(this.host, this.port);
            //socket.setSoTimeout(SOCKET_TIMEOUT);
            
            pis = socket.getInputStream();
            pos = socket.getOutputStream();

           // m_log.info("成功获得网络输入输出流");
        } catch (IOException ioe) {
        	
        	ioe.printStackTrace();

            m_log.error("IO异常" + ioe.getMessage());
            return bRet;
        } catch (Exception e) {
        	
        	e.printStackTrace();

            m_log.error("其他异常" + e.getMessage());
            return bRet;
        }
        
       // m_log.info("结束createConnection()函数");
        bRet = true;
        return bRet;
    }
    
    /**
     * 关闭连接。
     * 
     */
    public void closeConnection() {
    	
//        m_log.info("进入closeConnection()函数");
        try {
            if (pis != null) {
                pis.close();
            }
            if (pos != null)
            {
            	pos.close();
            }
            if (socket != null) {
                socket.close();
            }
            m_log.info("正常关闭连接");
        } catch (Exception e) {

            m_log.info("关闭网络连接异常" + e.getMessage());
        }

//        m_log.info("结束closeConnection()函数");
    }

    
    public int getData(byte[] outData) throws Exception
    {
    	m_log.info("begin getData..................");
    	int iRet = -1;
    	//m_log.info("进入getData()函数");
    	if(null == outData)
    	{
    		m_log.error("传入的缓冲区为空");
    		return iRet;
    	}
    	if(outData.length==0)
    	{
    		iRet = 0;
    		m_log.error("传入的缓冲区长度为0");
    		return iRet;
    	}
    	
        String messageStr = null;     

        try {
        	for(int i =0;i<outData.length;i++){
        		int data = pis.read();        		
        		if(data==-1){
        			m_log.info("实际读取长度["+iRet+"] 期望长度["+outData.length+"]");   
        			return iRet;
        		}
        		outData[i]=(byte)data;
        		iRet = i+1;
        	}
        	
        	// iRet =  pis.read(outData);
        	// m_log.info("实际读取长度["+iRet+"] 期望长度["+outData.length+"]");     
        	 
//        	 if(iRet!=outData.length)
//        	 {
//        		 m_log.info("读到的数据与要求的函数不符合,返回-1");        		 
//        	 } 
        	 
             messageStr = new String(outData);

             m_log.info("getData成功获取报文串" + new String(messageStr.getBytes("gb2312"),"UTF-8"));   //打开测试
           //  m_log.info("16进制表示:" + HexData.Bytes2HexString(outData));        	 

        } catch (Exception ioe) {

            m_log.error("网络IO异常" + ioe.getMessage());
            throw new Exception(ioe.getMessage());
        }

        //m_log.info("结束getData()函数");
        return iRet;	
    }
    /**
     * 前六位为报文长度(长度包含六位头)
     * 前index为实际长度
     * @param outData
     * @return
     * @throws Exception
     */
    public byte[] getData() throws Exception
    {
    	m_log.info("begin getData..................");
    	int iRet = -1;
    	int index=6;
    	int reportlengthInt;
    	byte[]outData;
        String messageStr = null; 
        //取报文实际长度
        byte[] reportlength=new byte[index];
        try {
        	for(int i =0;i<reportlength.length;i++){
        		int data = pis.read();        		        		        			          		        		
        		reportlength[i]=(byte)data;
//        		按十六进制打印字符
        		//System.out.print(Integer.toString(data, 16));
        	   
        	}
        	System.out.print("++++++6");
        	System.out.print(reportlength);
        	String reportlengthStr=new String(reportlength,"GB2312");
        	String reportlengthStr1=new String(reportlength,"gb2312");
        	String reportlengthStrgbk=new String(reportlength,"GBK");
        	String reportlengthStrgbk1=new String(reportlength,"UTF-8");
        	reportlengthInt=Integer.parseInt(reportlengthStr.trim());
        	m_log.info("实际读取长度["+reportlengthInt+"] "); 
        	outData=new byte[reportlengthInt];
        	for(int i =0;i<outData.length;i++){
        		if(i<index){
        			outData[i]=reportlength[i];
        		}else{
        			int data = pis.read();        		
            		if(data==-1){
            			m_log.info("实际读取长度["+iRet+"] 期望长度["+outData.length+"]");   
            			return outData;
            		}
            		outData[i]=(byte)data;
            		
            		//System.out.print(Integer.toString(data, 16));
            		
            		iRet = i+1;
        		}
        		
        	}
        	
        	System.out.print("++++++后6");
        	// iRet =  pis.read(outData);
        	// m_log.info("实际读取长度["+iRet+"] 期望长度["+outData.length+"]");     
        	 
//        	 if(iRet!=outData.length)
//        	 {
//        		 m_log.info("读到的数据与要求的函数不符合,返回-1");        		 
//        	 } 
             messageStr = new String(outData,"GBK");

             m_log.info("getData成功获取报文串" + messageStr);   //打开测试
           //  m_log.info("16进制表示:" + HexData.Bytes2HexString(outData));        	 

        } catch (Exception ioe) {

            m_log.error("网络IO异常" + ioe.getMessage());
            throw new Exception(ioe.getMessage());
        }

        //m_log.info("结束getData()函数");
        return outData;	
    }	
    /**
     * 前六位为报文长度(长度不包含六位头)
     * 前index为实际长度
     * @param outData
     * @return
     * @throws Exception
     */
    public byte[] getData_new() throws Exception
    {
    	m_log.info("begin getData..................");
    	int iRet = -1;
    	int index=6;
    	int reportlengthInt;
    	byte[]outData;
        String messageStr = null; 
        //取报文实际长度
        byte[] reportlength=new byte[index];
        try {
        	for(int i =0;i<reportlength.length;i++){
        		int data = pis.read();        		        		        			          		        		
        		reportlength[i]=(byte)data;
        	}
        	
        	String reportlengthStr=new String(reportlength,"GB2312");
        	reportlengthInt=Integer.parseInt(reportlengthStr.trim());
        	reportlengthInt+=6;
        	m_log.info("实际读取长度["+reportlengthInt+"] "); 
        	outData=new byte[reportlengthInt];
        	for(int i =0;i<outData.length;i++){
        		if(i<index){
        			outData[i]=reportlength[i];
        		}else{
        			int data = pis.read();        		
            		if(data==-1){//只有socket 关闭才能得到结束fu
            			m_log.info("实际读取长度["+iRet+"] 期望长度["+outData.length+"]");   
            			return outData;
            		}
            		outData[i]=(byte)data;
            		iRet = i+1;
        		}
        	}
        	
        	// iRet =  pis.read(outData);
        	// m_log.info("实际读取长度["+iRet+"] 期望长度["+outData.length+"]");     
        	 
//        	 if(iRet!=outData.length)
//        	 {
//        		 m_log.info("读到的数据与要求的函数不符合,返回-1");        		 
//        	 } 
        	 
             messageStr = new String(outData,"GBK");

             m_log.info("getData成功获取报文串" + messageStr);   //打开测试
           //  m_log.info("16进制表示:" + HexData.Bytes2HexString(outData));        	 

        } catch (Exception ioe) {

            m_log.error("网络IO异常" + ioe.getMessage());
            throw new Exception(ioe.getMessage());
        }

        //m_log.info("结束getData()函数");
        return outData;	
    }	
    /**
     * 
     * @param output
     *  @param   unicode 编码格式
     * @param resultFlagStr  “=”前字段名
     * @return  返回后“=”前字段值
     * @throws UnsupportedEncodingException 
     */
    public String getReportResult(byte[] output,String unicode,String resultFlagStr) {
		String ret=null;
		String str="";
		try {
			str = new String(output,unicode);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//str="154   01    0099mdbrch                                          systti=08\\:49\\:20|pckgsq=|erorcd=00|erortx=|srvseq=|srvdte=|trandt=20150920|trantm=084920|";
		String [] strRet=str.split("\\|");
		for (int i = 0; i < strRet.length; i++) {
			if(strRet[i].indexOf(resultFlagStr)!=-1){
				if(strRet[i].split("=").length>1){
					int index = strRet[i].indexOf("=");
					ret = strRet[i].substring(index+1, strRet[i].length());
				}
			}
		}
		return ret;
	}
    public void putData(byte[] btData) throws IOException
    {
    	if(btData !=null)
    	{
    		String messageStr = new String(btData);
    	    m_log.info("putData需要发送的串" + messageStr);  //打开测试
    	   // m_log.info("16进制表示:" + HexData.Bytes2HexString(btData));     		
    	}else
    	{
    		m_log.error("发送数据为空");
    	}   	
   
       pos.write(btData);
       pos.flush();
    }
    
   /**
    * 根据报文类型得到发报文信息（组报文）
    * @param socketMsgType 报文类型
    * @param msgBody 报文体
    * @return
    */
    /**
	public String buildSocketMsgByType(String socketMsgType, String msgBody){
		
    	String retScoketMsg = "";
    	try{
	    	String scoketMsgHead = buildSocketMsgHead(socketMsgType);
	    	//String socketMsgContext = buildSocketMsgContext(socketMsgType,trDateStr);
	    	retScoketMsg = buildSocketMsg(scoketMsgHead,msgBody);
	    	m_log.info("发送报文信息为=="+retScoketMsg);
    	}catch (Exception e) {
    		m_log.error("SocketUtil.buildSocketMsgByType:ERROR",e);
    	}
    	return retScoketMsg;
    }
	
	public String buildSocketMsgByType1(String socketMsgType, String msgBody){
		
    	String retScoketMsg = "";
    	try{
	    	String scoketMsgHead = buildSocketMsgHead(socketMsgType);
	    	//String socketMsgContext = buildSocketMsgContext(socketMsgType,trDateStr);
	    	retScoketMsg = buildSocketMsgByPrcs(scoketMsgHead,msgBody);
	    	m_log.info("发送报文信息为=="+retScoketMsg);
    	}catch (Exception e) {
    		m_log.error("SocketUtil.buildSocketMsgByType:ERROR",e);
    	}
    	return retScoketMsg;
    }
	 /**
	    * 根据处理码得到发报文信息（组报文）
	    * @param 处理码 报文类型
	    * @param msgBody 报文体
	    * @return
	    
		public String buildSocketMsgByPrcscd(String prcscd, String msgBody){
			
	    	String retScoketMsg = "";
	    	try{
		    	String scoketMsgHead = buildSocketMsgHeadByPrcscd(prcscd);
		    	//String socketMsgContext = buildSocketMsgContext(socketMsgType,trDateStr);
		    	retScoketMsg = buildSocketMsgByPrcs(scoketMsgHead,msgBody);
		    	m_log.info("发送报文信息为=="+retScoketMsg);
	    	}catch (Exception e) {
	    		m_log.error("SocketUtil.buildSocketMsgByType:ERROR",e);
	    	}
	    	return retScoketMsg;
	    }
	    */
    /**
	 * 根据报文头，报文体组织报文信息
	 * @param scoketMsgHead
	 * @param socketMsgContext
	 * @return
	 */
	public String buildSocketMsg(String scoketMsgHead, String socketMsgContext) {
		String socketMsg = "";
		String msgLengthStr = "";
	
		int msgLength = scoketMsgHead.getBytes().length + socketMsgContext.getBytes().length;
		if(msgLength > 9 && msgLength<100) {
			msgLengthStr = msgLength + "    ";
		}else if(msgLength > 99 && msgLength<1000) {
			msgLengthStr = msgLength + "   ";
		}else if(msgLength > 999 && msgLength<10000) {
			msgLengthStr = msgLength + "  ";
		}else if(msgLength > 9999 && msgLength<100000) {
			msgLengthStr = msgLength + " ";
		}else if(msgLength > 99999 && msgLength<1000000) {
			msgLengthStr = msgLength + "";
		}else{
			msgLengthStr = msgLength + "     ";
		}
		socketMsg = msgLengthStr + scoketMsgHead + socketMsgContext;
		return socketMsg;
	}
	/**
	 * 按处理码  zhuxy
	 * @param scoketMsgHead
	 * @param socketMsgContext
	 * @return
	 */
	public String buildSocketMsgByPrcs(String scoketMsgHead, String socketMsgContext) {
		String socketMsg = "";
		String msgLengthStr = "";
	
		int msgLength=0;
		try {
			msgLength = scoketMsgHead.getBytes("GBK").length + socketMsgContext.getBytes("GBK").length;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(msgLength > 9 && msgLength<100) {
			msgLengthStr = msgLength + "    ";
		}else if(msgLength > 99 && msgLength<1000) {
			msgLengthStr = msgLength + "   ";
		}else if(msgLength > 999 && msgLength<10000) {
			msgLengthStr = msgLength + "  ";
		}else if(msgLength > 9999 && msgLength<100000) {
			msgLengthStr = msgLength + " ";
		}else if(msgLength > 99999 && msgLength<1000000) {
			msgLengthStr = msgLength + "";
		}else{
			msgLengthStr = msgLength + "     ";
		}
		socketMsg = msgLengthStr + scoketMsgHead + socketMsgContext;
		return socketMsg;
	}
	
	/**
	 * 根据类型组织报文体
	 * @param socketMsgType
	 * @return
	 
	public String buildSocketMsgContext(String socketMsgType, String trDateStr) {
		StringBuffer scoketMsgContext = new StringBuffer();
		if(socketMsgType != null) {
			String xmlKey = socketMsgType+"context.key";
			String xmlValue = socketMsgType + "context.value";
			String keys = GlobalParameter.getProperty(xmlKey);
			String values = GlobalParameter.getProperty(xmlValue);
			String keyArr[] = keys.split(",");
			String valueArr[] = values.split(",");
			for(int i = 0; i < keyArr.length; i++) {
				String key = keyArr[i];
				String value = valueArr[i];
				if(value.trim().equals("nowdate")) {
					value = DateSplitUtil.getCurrentDateStr(DateSplitUtil.SDF_MMDDHHMMSS);
				}
				if(value.trim().equals("nowdateymd")) {
					value = trDateStr;
				}
				scoketMsgContext.append(key).append("=").append(value).append("|");
			}
		}
		return scoketMsgContext.append("*").toString();
	}
	*/
	/**
	 * 根据报文类型组织报文头
	 * @param socketMsgType
	 * @return
	 
	public String buildSocketMsgHead(String socketMsgType) {
		StringBuffer socketMsgHead = new StringBuffer();
		String msgNo = "01    "; 	//报文号
		String msgType = "00"; 		//报文类型
		String msgBz = "00";		//报文标志
		if(socketMsgType.substring(0, 1).trim().equals("0")) {
			socketMsgType = "z"+ socketMsgType;
		}
		String xmlValue = socketMsgType + "head.value";
		String values = GlobalParameter.getProperty(xmlValue);
		String head[] = values.split(",");
		String userId = "        ";
		String sessionId = "                    ";
		socketMsgHead.append(msgNo).append(msgType).append(msgBz).append(head[0]).append(userId).append(sessionId).append(head[1]);
		return socketMsgHead.toString();
	}
	*/
	/**
	 * 根据处理码组织报文头
	 * @param socketMsgType
	 * @return
	
	public String buildSocketMsgHeadByPrcscd(String prcscd) {
		StringBuffer socketMsgHead = new StringBuffer();
		String msgNo = "01    "; 	//报文号
		String msgType = "00"; 		//报文类型
		String msgBz = "00";		//报文标志
		
		String xmlValue =  "Synchronizehead.value";
		String values = GlobalParameter.getProperty(xmlValue);
		values=prcscd+values;
		String head[] = values.split(",");
		String userId = "        ";
		String sessionId = "                    ";
		socketMsgHead.append(msgNo).append(msgType).append(msgBz).append(head[0]).append(userId).append(sessionId).append(head[1]);
		return socketMsgHead.toString();
	}
	 */
	/**  
     * 发送信息.  
     */  
    public void send(String msg) {   
        if (socket != null && !socket.isClosed()) {   
            try {   
            	PrintWriter pw = new PrintWriter(   
                        new OutputStreamWriter(socket.getOutputStream()));   
                pw.println(msg);   
                pw.flush();   
                pw.close();   
            } catch (IOException e) {   
                e.printStackTrace();   
            }   
        } else {   
            throw new NullPointerException();   
        }   
    }   
  
    /**  
     * 接受信息  
     */  
    public String receive() { 
    	String msg=null;
        if (socket != null && !socket.isClosed()) {   
            try {   
            	BufferedReader br = new BufferedReader(new InputStreamReader(socket   
                        .getInputStream()));   
                 
                while (!socket.isClosed()) {   
                    msg = br.readLine();   
                    
                    if (msg!=null && !"".equals(msg)) {   
                       br.close();
                        break;   
                    }   
                }   
            } catch (IOException e){   
                e.printStackTrace();   
            }   
        }  
        return msg;
    }
  
}
