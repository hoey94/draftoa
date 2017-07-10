package com.starsoft.core.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Voip接口
 * @author 
 *
 * @date 2014-3-4
 */
public class XmppUtil {
	private static Logger _log = LoggerFactory.getLogger(XmppUtil.class);
	private static Connection conn = null;
	private static String host = null;
	private static String user = null;
	private static String pwd = null;
	
	private static Connection getXMPPConnection() throws XMPPException, IOException {
		Connection conn = null;
		ConfigUtil config = new ConfigUtil("config.properties");
		String iphost = config.getPropByName("xmpp_host");
		int port = Integer.valueOf(config.getPropByName("xmpp_port"));
		user = config.getPropByName("xmpp_user");
		pwd = config.getPropByName("xmpp_user_pwd");
		ConnectionConfiguration ccf = new ConnectionConfiguration(iphost,port);
		ccf.setSASLAuthenticationEnabled(false);
		ccf.setSecurityMode(SecurityMode.disabled);
		conn = new XMPPConnection(ccf);
		conn.connect();
		conn.login(user, pwd);
		return conn;
	}
	
	private static MessageListener listener = new MessageListener() {

		@Override
		public void processMessage(Chat arg0, Message arg1) {
			_log.info("receipt msg=" + arg1.getBody());
		}
	};
	
	private static ReceiptReceivedListener rrl = new ReceiptReceivedListener() {
		
		@Override
		public void onReceiptReceived(String fromJid, String toJid, String receiptId) {
			_log.info("fromJid=" + fromJid);
		}
	};
	
	/**
	 * XMPP消息发送
	 * @throws XMPPException 
	 * @throws IOException 
	 */
	public static void xmpp_send(String xmppid, Message msg) throws XMPPException, IOException{
		if(conn == null){
			_log.info("xmpp conn is null");
			conn = getXMPPConnection();
		}
		
		if(!conn.isConnected()){
			_log.info("xmpp not conn, to reconn");
			conn.connect();
			conn.login(user, pwd);
		}
		
		if(host == null){
			_log.info("xmpp host is null");
			host = conn.getServiceName();
		}
		String jid = xmppid + "@" + host;
		String from = msg.getFrom() + "@" + host;
		msg.setFrom(from);
		Chat chat = conn.getChatManager().createChat(jid, listener);
		chat.sendMessage(msg);
		_log.info("xmpp conn  jid="+jid+"===========from="+from);
	}
	
	/**
	 * XMPP创建用户
	 * @throws XMPPException 
	 * @throws IOException 
	 */
	public static void xmpp_create(String xmppid, String password,String name) throws XMPPException, IOException{
		if(conn == null){
			_log.info("xmpp conn is null");
			conn = getXMPPConnection();
		}
		
		if(!conn.isConnected()){
			_log.info("xmpp not conn, to reconn");
			conn.connect();
			conn.login(user, pwd);
		}
		
		if(host == null){
			_log.info("xmpp host is null");
			host = conn.getServiceName();
		}
		AccountManager manager = conn.getAccountManager();
		Map<String, String> attr = new HashMap<String, String>();
		attr.put("name", name);
		manager.createAccount(xmppid,password,attr);
	}
	/***
	 * 修改帐号密码
	 * @param xmppid
	 * @param oldpassword
	 * @param newpassword
	 */
	public static void xmpp_modiypassword(String xmppid, String oldpassword,String newpassword)throws XMPPException, IOException{
		if(conn == null){
			_log.info("xmpp conn is null");
			conn = getXMPPConnection();
		}
		
		if(!conn.isConnected()){
			_log.info("xmpp not conn, to reconn");
			conn.connect();
			conn.login(user, pwd);
		}
		
		if(host == null){
			_log.info("xmpp host is null");
			host = conn.getServiceName();
		}
		AccountManager manager = conn.getAccountManager();
		if(manager!=null){
			String name=manager.getAccountAttribute("name");
			Map<String, String> attr = new HashMap<String, String>();
			attr.put("name", manager.getAccountAttribute("name"));
			manager.deleteAccount();
			manager.createAccount(xmppid, newpassword,attr );
		}
	}
	
	public String register(String loginName, String pwd) {
		if(!conn.isConnected()){
			_log.info("xmpp not conn, to reconn");
//			conn.connect();
		}
		Registration re = new Registration();
		re.setType(IQ.Type.SET);
		re.setTo(conn.getServiceName());
//		re.setPassword(pwd);
//		re.setUsername(loginName);
//		re.addAttribute("android", "geolo_createUser_android");// 不能为空

		System.out.println("注册到openfire中的姓名是=====" + loginName + "密码是====="
				+ pwd);

		PacketFilter filter = new AndFilter(
				new PacketIDFilter(re.getPacketID()), new PacketTypeFilter(
						IQ.class));
		PacketCollector collector = conn.createPacketCollector(filter);

		conn.sendPacket(re);
		IQ result = (IQ) collector.nextResult(SmackConfiguration
				.getPacketReplyTimeout());

		collector.cancel();// 停止请求result（是否成功的结果）
		if (result == null)
			return "0";
		else if (result.getType() == IQ.Type.RESULT) {
			return "1";
		} else {
			if (result.getError().toString().equalsIgnoreCase("conflict(409)")) {
				return "2";
			} else {
				return "3";
			}
		}
	}
	
	
}
