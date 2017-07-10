//公用javascript脚本库
/***
 * 1.用户选择 selectUsers(userIds,userNames,selectType); //selectType 0代表多选，1代表单选
 * 2.人员选择 selectPersons(userIds,userNames,selectType);//selectType 0代表多选，1代表单选
 */
//******************选择用户界面****************************
function selectUsers(userIds,userNames,selectType,organId){
	var userId=document.getElementById(userIds);
	var userName=document.getElementById(userNames);
	var selectUsersIds="";
	if(userId){
		selectUsersIds=userId.value;
	}
	var selecttype=selectType||0;//0代表多选，1代表单选
	var parentId=organId||"11111111111111111111111111111111";
	var result=window.showModalDialog("users.do?action=selectUsers&selectUsersIds="+selectUsersIds+"&selecttype="+selecttype+"&parentId="+parentId,window,"status:false;dialogWidth:610px;dialogHeight:370px;edge:Raised; enter: Yes; help: No; resizable: No; status: No"); 
	if(result){
		var results= result.split("&");
		for(var i = 0; i < results.length; i++) {
		   var cell=results[i];
		   var cells=cell.split("=");
		   if(cells.length==2){
			   var key=cells[0];
			   var value=cells[1];
			   if(key=="userIdValue"){
				   userId.value=value;
			   }else if(key=="userNamesValue"){
				   userName.value=value;
			   }
		   }
		}
	}
}
//******************选择用户界面****************************
function selectPersons(personIds,personNames,selectType){
	var userId=document.getElementById(personIds);
	var userName=document.getElementById(personNames);
	var selectUsersIds="";
	if(userId){
		selectUsersIds=userId.value;
	}
	var selecttype=selectType||0;//0代表多选，1代表单选
	var result=window.showModalDialog("users.do?action=selectUsers&selectUsersIds="+selectUsersIds+"&selecttype="+selecttype,window,"status:false;dialogWidth:610px;dialogHeight:370px;edge:Raised; enter: Yes; help: No; resizable: No; status: No"); 
	if(result){
		var results= result.split("&");
		for(var i = 0; i < results.length; i++) {
		   var cell=results[i];
		   var cells=cell.split("=");
		   if(cells.length==2){
			   var key=cells[0];
			   var value=cells[1];
			   if(key=="userIdValue"){
				   userId.value=value;
			   }else if(key=="userNamesValue"){
				   userName.value=value;
			   }
		   }
		}
	}
}
//******************选择用户界面****************************
function selectUsersbyzige(userIds,userNames,selectType,organId,levelzige){
	var userId=document.getElementById(userIds);
	var userName=document.getElementById(userNames);
	var selectUsersIds="";
	if(userId){
		selectUsersIds=userId.value;
	}
	var selecttype=selectType||0;//0代表多选，1代表单选
	var result=window.showModalDialog("users.do?action=selectUsers&selectUsersIds="+selectUsersIds+"&selecttype="+selecttype+"&levelzige="+levelzige+"&organId="+organId,window,"status:false;dialogWidth:610px;dialogHeight:370px;edge:Raised; enter: Yes; help: No; resizable: No; status: No"); 
	if(result){
		var results= result.split("&");
		for(var i = 0; i < results.length; i++) {
		   var cell=results[i];
		   var cells=cell.split("=");
		   if(cells.length==2){
			   var key=cells[0];
			   var value=cells[1];
			   if(key=="userIdValue"){
				   userId.value=value;
			   }else if(key=="userNamesValue"){
				   userName.value=value;
			   }
		   }
		}
	}
}
//******************选择部门界面***********selectType //0代表多选，1代表单选**********
function selectOrgan(organIds,organNames,selectType){
	var organId=document.getElementById(organIds);
	var organName=document.getElementById(organNames);
	var selectOrganIds="";
	if(organId){
		selectOrganIds=organId.value;
	}
	var result=window.showModalDialog("organ.do?action=selectOrgans&selectOrganIds="+selectOrganIds+"&selectType="+selectType,window,"status:false;dialogWidth:400px;dialogHeight:380px;edge:Raised; enter: Yes; help: No; resizable: No; status: No"); 
	if(result){
		var results= result.split("&");
		for(var i = 0; i < results.length; i++) {
		   var cell=results[i];
		   var cells=cell.split("=");
		   if(cells.length==2){
			   var key=cells[0];
			   var value=cells[1];
			   if(key=="organIdValue"){
				   organId.value=value;
			   }else if(key=="organNamesValue"){
				   organName.value=value;
			   }
		   }
		}
	}
}
//******************全选和全不选****************************
function selectAll(obj) {
	var objs = document.getElementsByName("box");
	var objslength = objs.length;
	if (obj) {
		for ( var i = 0; i < objslength; i++) {
			objs[i].checked = true;
		}
	} else {
		for ( var i = 0; i < objslength; i++) {
			objs[i].checked = false;
		}
	}
}
//*******************得到选择第一条的id**********************************
function getFirstSelectId(){
	var objs = document.getElementsByName("box");
	var objslength = objs.length;
	var id = "";
	for (var i = 0; i < objslength; i++) {
		if (objs[i].checked) {
			id = objs[i].value;
			break;
		}
	}
	return id;
}
//**************************得到所有选择的IDS***********************
function getSelectIds() {
	var objs = document.getElementsByName("box");
	var objslength = objs.length;
	var ids = "";
	for ( var i = 0; i < objslength; i++) {
		if (objs[i].checked) {
			ids += objs[i].value + ";";
		}
	}
	return ids;
}
//************************删除所选择的信息***********************************
function deletes() {
	var ids = getSelectIds();
	if (ids.length > 0) {
		if (window.confirm("你确认要删除信息 吗?")) {
			$.ajax( {
				type : "post",
				url : "?action=delete",
				data:{ids:ids},
				timeout : 10000,
				success : function(msg) {
					if(msg.resultCode==0){
						alert(msg.resultDesc);
					}else{
						//判断是不是子框架
						var parentwindow=window.parent;
						if(parentwindow&&parentwindow.leftTree){
							alert(msg.resultDesc);
							parentwindow.location.reload(true);
						}else{//删除成功，导向下一条信息
							var queryString=$("#commonform").serialize();
							location.href=$("#commonform").attr("action")+"&"+queryString;
						}
					}
				}
			});
		}
	} else {
		alert("请选择要操作的数据！");
	}
}
//************************禁用所选择的信息***********************************
function jsdeletes() {
	var ids = getSelectIds();
	if (ids.length > 0) {
		if (window.confirm("你确认要禁用信息 吗?")) {
			$.ajax({
				type : "post",
				url : "?action=jsdelete",
				data:{ids:ids},
				timeout : 10000,
				success : function(msg) {
					if(msg.resultCode==0){
						alert(msg.resultDesc);
					}else{
						//判断是不是子框架
						var parentwindow=window.parent;
						if(parentwindow&&parentwindow.leftTree){
							parentwindow.location.reload(true);
						}else{//删除成功，导向下一条信息
							var queryString=$("#commonform").serialize();
							location.href=$("#commonform").attr("action")+"&"+queryString;
						}
					}
				}
			});
		}
	} else {
		alert("请选择要操作的数据！");
	}
}
//************************表单数据信息删除信息***********************************
function formdelete(objId){
	if (objId&&window.confirm("你确认要删除该数据 吗?")) {
		$.ajax( {
			type : "post",
			url : "?action=delete",
			data:{ids:objId},
			timeout : 10000,
			success : function(msg) {
				if(msg.resultCode==0){
					alert(msg.resultDesc);
				}else{
					//判断是不是子框架
					var parentwindow=window.parent;
					if(parentwindow&&parentwindow.leftTree){
						parentwindow.location.reload(true);
					}else{//删除成功，导向下一条信息
						location.href=document.referrer;
					}
				}
			}
		});
	}
}
//***********************保存信息********************************
function save(){
	var form=document.getElementById("formId");
	if(Validate.CheckForm(form)){
		$.ajax({
			type:"post",
			dataType:"json",
			url:form.action,
			data:$('#formId').serialize(),
			success:function(msg){
				if(msg.resultCode==0){
					alert(msg.resultDesc);
				}else{
					//判断是不是子框架
					var nextUrl=msg.data;
					if(nextUrl&&nextUrl!=""){
						location.href=nextUrl;
					}else{
						location.href=document.referrer;
					}
				}
			},
			error:function(data){
				alert(data);
			}
		});
	}
}
//***********************表单保存信息****判断是否是自表单****************************
function formsave(){
	var form=document.getElementById("formId");
	if(Validate.CheckForm(form)){
		$.ajax({
			type:"post",
			dataType:"json",
			url:form.action,
			data:$('#formId').serialize(),
			success:function(msg){
			
				if(msg.resultCode==0){
					alert(msg.resultDesc);
				}else{
					//判断是不是子框架
					var parentwindow=window.parent;
					if(parentwindow&&parentwindow.leftTree){
						parentwindow.location.reload(true);
					}else{//保存成功，导向下一条信息
						var nextUrl=msg.data;
						if(nextUrl&&nextUrl!=""){
							location.href=nextUrl;
						}else{
							location.href=document.referrer;
						}
					}
				}
			},
			error:function(data){
				
			}
		});
	}
}
//***********************转到增加页面*********************************
function addInfor(){
	document.getElementById("commonform").action="?action=add";
	document.getElementById("commonform").submit();
}
//*********************更新信息转到编辑信息页面********************
function readInfor(obj){
	if(obj==""){
		alert("请选择要操作的数据！");
	}else{
		var page=0;
		var pageElement=document.getElementById("page");
		if(pageElement&&pageElement.value!=""){
			page=pageElement.value;
		}
		var gotourl=document.getElementById("gotourl");
		if(gotourl&&gotourl.value!=""){
			location.href="?action=read&id="+obj+"&page="+page+"&gotourl="+gotourl.value;
		}else{
			location.href="?action=read&id="+obj+"&page="+page;
		}
	}
}
//**********************转到编辑信息页面******************************
function editInfor(id){
	if(id==""){
		alert("请选择要操作的数据！");
	}else{
		var page=0;
		var pageElement=document.getElementById("page");
		if(pageElement&&pageElement.value!=""){
			page=pageElement.value;
		}
		var gotourl=document.getElementById("gotourl");
		if(gotourl&&gotourl.value!=""){
			location.href="?action=edit&id="+id+"&page="+page+"&gotourl="+gotourl.value;
		}else{
			location.href="?action=edit&id="+id+"&page="+page;
		}
	}
}
//********************异步交互更新信息*****************************
function updateBaseObject(obj,value){
	var id=obj.id;
	var name=obj.name;
	$.ajax({
		type : "post",
		url : "?action=update&"+name+"="+value,
		timeout : 10000,
		data:{id:id},
		success : function(msg) {
			if(msg.resultCode==0){
				alert(msg.resultDesc);
			}else{
				var queryString=$("#commonform").serialize();
				location.href=$("#commonform").attr("action")+"&"+queryString;
			}
		}
	});
}
function resetpassword(obj){
	$.ajax( {
		type : "post",
		url : "?action=resetpassword",
		timeout : 10000,
		data:{id:obj},
		success : function(msg) {
			alert(msg.resultDesc);
		}
	});
}
//***********************打开搜索提交信息****************************
function searchSubmit(){
	document.getElementById("commonform").submit();
}
//***********************批量数据导入****************************
function importdatas(parameters){
	var url=location.href;
	var appesname=url.substring(url.lastIndexOf("/")+1, url.lastIndexOf(".do"));
	location.href="systemIntegration.do?action=importpage&app="+appesname;
}
//以showModalDialog的方式弹出窗口
function openDialog(url){
	window.showModalDialog(url,window,"status:false;dialogWidth:610px;dialogHeight:450px;edge:Raised; enter: Yes; help: No; resizable: No; status: No"); 
	window.location.reload();
}
//以打开详细信息窗口
function openDetail(id,readonly){
	var url="clobInfo.do?id="+id+"&readonly="+readonly;
	window.showModalDialog(url,window,"status:false;dialogWidth:800px;dialogHeight:600px;edge:Raised; enter: Yes; help: No; resizable: No; status: No"); 
}
//********************异步提交信息*****************************
function importflie(){
	var file=document.getElementById("file");
	if(file&&file.value!=""){
		document.getElementById("importDataform").submit();
	}else{
		alert("请选择文件！");
	}
}
//***********************打开上传文件页面****************************
// filetype包含 image flash all media;id 是页面要返回的值赋值指定的id元素
function openfileuplodpage(filetype,Id){
	var returnValue=window.showModalDialog("fileUplod.do?action=uploadFile&fileType="+filetype,window,"status:false;dialogWidth:450px;dialogHeight:200px;edge:Raised; enter: Yes; help: No; resizable: No; status: No"); 
	var file=document.getElementById(Id);
	if(file&&returnValue){
		file.value=returnValue;
		file.readOnly=true;
	}
}
//******************统一单文件上传功能实现*******************************8
function flieUpload(){
	var file=document.getElementById("file");
	var fileTypes=document.getElementById("fileTypes").value;
	if(file&&file.value!=""){
		var filevalue=file.value;
		var fileType=filevalue.substring(filevalue.lastIndexOf(".")+1,filevalue.length).toLowerCase();
		var fileTypesTemp=fileTypes.toLowerCase();
		if(fileTypesTemp.indexOf(fileType)>-1){//判断文件类型
			document.getElementById("importDataform").submit();
		}else{
			alert("请选择系统支持的文件类型："+fileTypes);
		}
	}else{
		alert("请选择文件！");
	}
}
//***********************提交至下一节点信息********************************
function workFlowFormAction(operate){
	var form=document.getElementById("formId");
	if(Validate.CheckForm(form)){
		$.ajax({
			type:"post",
			dataType:"json",
			url:form.action+"&operate="+operate,
			data:$('#formId').serialize(),
			success:function(msg){
				if(msg.resultCode==0){
					alert(msg.resultDesc);
				}else{
					alert(msg.resultDesc);
					//判断是不是子框架
					var nextUrl=msg.data;
					if(nextUrl&&nextUrl!=""){
						location.href=nextUrl;
					}else{
						location.href=document.referrer;
					}
				}
			},
			error:function(data){
				
			}
		});
	}
}
//*****************通过ajax获取表单json数据反填到表单中********************
function setFormValues(jsonValues){
	for (key in jsonValues){
		try {
			var element=document.getElementById(key);
			if(element){
				if(jsonValues[key] instanceof Object){
					element.value=jsonValues[key]['id'];
					element.readOnly=true;
				}else{
					element.value=jsonValues[key];
					element.readOnly=true;
				}
			}
		} catch (e) {
		}
	}
}
//*****************设置元素id的value值********************
function setValue(elementid,value){
	var element=document.getElementById(elementid);
	if(element){
		element.value=value;
	}else{
		alert("页面不存在id为："+elementid+"的元素！");
	}
}
//******************公用分页查询****************start****************
function turntopage(obj){
	document.getElementById("page").value=obj;
	document.getElementById("commonform").submit();
}
//******************公用分页查询****************end****************
//********************列表页面排序设置****************start**********
//初始化显示排序列
function initSort(){
	var sortfield =document.getElementById("sortfield");
	var sortvalue =document.getElementById("sortvalue");
	if(sortfield&&sortvalue){
		var orderElement =document.getElementById("sort"+sortfield.value);
		if(orderElement){
			if(sortvalue.value=="true"){
				orderElement.innerHTML = "↓";
			}else if(sortvalue.value=="false"){
				orderElement.innerHTML = "↑";
			}
		}
	}
}
//初始化按钮信息，displayButtons显示按钮ids，hiddenButtons是隐藏的按钮ids，中间英文分号分隔
function initButton(displayButtonIds,hiddenButtonIds){
	if(displayButtonIds){
		displayButtons(displayButtonIds);
	}
	if(hiddenButtonIds){
		hiddenButtons(hiddenButtonIds);
	}
}
//隐藏指定的按钮，displayButtons要显示的按钮ids，
function displayButtons(displayButtonIds){
	var ids=displayButtonIds.split(";");
	for(var i = 0; i < ids.length; i++) {
	   var id=ids[i];
	   var element=document.getElementById(id);
	   if(element){
		   element.style.display="";
	   }
	}
}
//隐藏指定的按钮，hiddenButtons要隐藏的按钮ids，
function hiddenButtons(hiddenButtonIds){
	var ids=hiddenButtonIds.split(";");
	for(var i = 0; i < ids.length; i++) {
	   var id=ids[i];
	   var element=document.getElementById(id);
	   if(element){
		   element.style.display="none";
	   }
	}
}
//重新设置排序
function resetSort(orderfield){
	var sortfield =document.getElementById("sortfield");
	var sortvalue =document.getElementById("sortvalue");
	if(sortfield&&sortvalue){
		if(sortfield.value==orderfield){
			sortvalue.value=sortvalue.value=="true"?"false":"true";
		}else{
			sortfield.value=orderfield;
			sortvalue.value=sortvalue.value=="true"?"false":"true";
		}
		document.getElementById("commonform").submit();
	}
}
/***
 * 公用选择数据调用方法，弹出选择数据窗，app代表数据查询对象，objectId是返回数据的id标识，objectname是返回数据的标识，selectType是选择数据的方式1代表单选0代表多选,parentId是上级节点，如果是层级关系对象，该属性有效
 */
function commonselect(app,objectId,objectName,selectType,parentId){
	var objectId=document.getElementById(objectId);
	var objectName=document.getElementById(objectName);
	var result=window.showModalDialog(app+".do?action=commonselectlist&selectType="+selectType+"&parentId="+parentId+"&objectId="+objectId.value,window,"status:false;dialogWidth:640px;dialogHeight:440px;edge:Raised; enter: Yes; help: No; resizable: No; status: No"); 
	if(result){
		var results= result.split("&");
		for(var i = 0; i < results.length; i++) {
		   var cell=results[i];
		   var cells=cell.split("=");
		   if(cells.length==2){
			   var key=cells[0];
			   var value=cells[1];
			   if(key=="objectId"){
				   objectId.value=value;
			   }else if(key=="objectName"){
				   objectName.value=value;
			   }
		   }
		}
	}
}
//通讯录人员选择，多选
function selectAddressBook(userIds,userNames){
	var userId=document.getElementById(userIds);
	var userName=document.getElementById(userNames);
	var selectUsersIds="";
	if(userId){
		selectUsersIds=userId.value;
	}
	var result=window.showModalDialog("addressBook.do?action=selectAddressBook&selectUsersIds="+selectUsersIds,window,"status:false;dialogWidth:600px;dialogHeight:360px;edge:Raised; enter: Yes; help: No; resizable: No; status: No"); 
	if(result){
		var results= result.split("&");
		for(i = 0; i < results.length; i++) {
		   var cell=results[i];
		   var cells=cell.split("=");
		   if(cells.length==2){
			   var key=cells[0];
			   var value=cells[1];
			   if(key=="userIdValue"){
				   userId.value=value;
			   }else if(key=="userNamesValue"){
				   userName.value=value;
			   }
		   }
		}
	}
}
/***
 * 清除页面中的对象的值
 * @param obj
 */
function clearValue(obj){
	var objId=document.getElementById(obj);
	if(objId){
		objId.value="";
	}
}
/***
 * 页面转向
 * @param url
 */
function gotourl(url){
	location.href=url;
}
/***
 * 加入收藏
 */
function addFavorite() {  
	var  sURL=window.location;
	var name=window.document.title;
    try {   
        window.external.addFavorite(sURL, name);   
    } catch (e) {   
        try {   
            window.sidebar.addPanel(name, sURL, "");   
        } catch (e) {   
            alert("加入收藏失败");   
        }   
    }   
}
/***
 * 设置为首页
 * @param obj
 * @param vrl
 */
function setHome(obj, vrl) {   
    try {   
        obj.style.behavior = 'url(#default#homepage)';   
        obj.setHomePage(vrl);   
    } catch (e) {   
        if (window.netscape) {   
            try {   
                netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");   
            } catch (e) {   
                alert("此操作被浏览器拒绝！\n请在浏览器地址栏输入“about:config”并回车\n然后将 [signed.applets.codebase_principal_support]的值设置为'true',双击即可。");   
            }   
            var prefs = Components.classes['@mozilla.org/preferences-service;1'].getService(Components.interfaces.nsIPrefBranch);   
            prefs.setCharPref('browser.startup.homepage', vrl);   
        }   
    }   
} 
/***
 * 搜索判断
 */
function doSearch(){
	var keyword=document.getElementById("keyword").value;
	if(keyword==""){
		alert("关键字不能为空！");
		document.getElementById("keyword").focus();
	}else{
		document.getElementById("searchForm").submit();
	}
}
//表格切换效果
function selectout(obj){//暂时屏蔽错误用
	alert("out");
	return false;
}
//表格切换效果
function selectover(obj){//暂时屏蔽错误用
	alert("over");
	return false;
}

/**这个方法用来对select对象进行初始化.
 * @param elementId 需要初始化的列表的id
 * @param param 需要向后台传送的参数，key/value传值对。
 * displayTitle:是否创建列表的表头（表头显示为：--请选择--）,
 * 		默认创建表头，只有displayTitle值为字符串false时，才不创建
 * defaultValue：初始化元素默认值
 * @param callback 需要执行的回调函数
 */
function initSelectElement(elementId,param,callback){
	$("#"+elementId).load("dictionary.do?action=showDictionary",param,function(){});
}
/**这个方法用来对select对象进行初始化.
 * 
 */
function initSelect(){
	var selects=document.getElementsByTagName("select");
	for(var t=0;t<selects.length;t++){
		var select=selects[t];
		var id = select.getAttribute("id");
		var param = select.getAttribute("param");
		if(id!=null&&param!=null){
			var newparam="";
			var results= param.split("&");
			for(var i = 0; i < results.length; i++) {
			   var cell=results[i];
			   var cells=cell.split("=");
			   if(cells.length==2){
				   var key=cells[0];
				   var value=cells[1];
				   newparam=newparam+key+"="+encodeURI(value)+"&";
			   }
			}
			initSelectElement(id,newparam);
		}
	}
	var tname=document.getElementById("tname");
	if(tname){
		var namevalue=tname.value;
		if(namevalue!=""){
			$("#tname").val("").focus().val(namevalue);
		}else{
			tname.focus();
		}
	}
}
function loginServer(){
	var form=document.getElementById("formId");
	var loginId=document.getElementById("loginId");
	var password=document.getElementById("password");
	if(loginId.value==""){
		alert("登录帐号不能为空！");
		loginId.focus();
		return;
	}
	if(password.value==""){
		alert("登录密码不能为空！");
		password.focus();
		return;
	}
	$.ajax({
		type:"post",
		dataType:"json",
		url:form.action,
		data:$('#formId').serialize(),
		success:function(msg){
			if(msg.resultCode==0){
				alert(msg.resultDesc);
			}else{
				//判断是不是子框架
				var nextUrl=msg.data;
				if(nextUrl&&nextUrl!=""){
					location.href=nextUrl;
				}else{
					location.href=document.referrer;
				}
			}
		},
		error:function(data){
			
		}
	});
}
