//==============================
//获取findObj
//==============================
function findObj(theObj, theDoc) {
	var p, i, foundObj;

	if (!theDoc)
		theDoc = document;
	if ((p = theObj.indexOf("?")) > 0 && parent.frames.length) {
		theDoc = parent.frames[theObj.substring(p + 1)].document;
		theObj = theObj.substring(0, p);
	}
	if (!(foundObj = theDoc[theObj]) && theDoc.all)
		foundObj = theDoc.all[theObj];
	for (i = 0; !foundObj && i < theDoc.forms.length; i++)
		foundObj = theDoc.forms[i][theObj];
	for (i = 0; !foundObj && theDoc.layers && i < theDoc.layers.length; i++)
		foundObj = findObj(theObj, theDoc.layers[i].document);
	if (!foundObj && document.getElementById)
		foundObj = document.getElementById(theObj);

	return foundObj;
}
// ==============================
// 导航焦点（固定样式名称activedTab，nTab）
// ==============================
function doclick(srcObj) {
	var tabList = srcObj.parentNode.getElementsByTagName("li");
	if (srcObj.className == "activedTab")
		return;
	for ( var i = 0; i < tabList.length; i++) {
		if (tabList[i].className == "activedTab")
			tabList[i].className = "nTab";
	}
	srcObj.className = "activedTab";// TAB切换
}

function doclickSub(srcObj) {
	var tabList = srcObj.parentNode.getElementsByTagName("li");
	if (srcObj.className == "activedSubTab")
		return;
	for ( var i = 0; i < tabList.length; i++) {
		if (tabList[i].className == "activedSubTab")
			tabList[i].className = "nTabSub";
	}
	srcObj.className = "activedSubTab";// TAB切换
}

// 改变图片大小
function resizepic(thispic) {
}
// 无级缩放图片大小
function bbimg(o) {
}
