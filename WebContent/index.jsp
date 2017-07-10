<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<title>——电子提案系统!</title>
<meta name="Keywords" content="电子提案系统|电子提案系统" />
<meta name="Description" content="电子提案系统，电子提案系统。" />
<head>
<script type="text/javascript">  
//平台、设备和操作系统   
var system ={  

    win : false,  

    mac : false,  

    xll : false  

};  
//检测平台   
var p = navigator.platform;  
system.win = p.indexOf("Win") == 0;  

system.mac = p.indexOf("Mac") == 0;  

system.x11 = (p == "X11") || (p.indexOf("Linux") == 0);  
//跳转语句   

if(system.win||system.mac||system.xll){//转向后台登陆页面  
    window.location.href="index.do";  
}else{  
   /*  window.location.href="mobileIndex.do";   */
	window.location.href="index.do";
}  
</script>  
</head>
<body>
</body>
</html>