/*
*---------------客户端表单通用验证CheckForm(oForm)-----------------
*功能:通过正则表达式实现通用验证所有的表单元素.
*使用:
*<formname="form1"onsubmit="returnCheckForm(this)">
*<inputtype="text"name="name"check="notBlank"required="true"showName="用户姓名">
*<inputtype="submit">
*</form>
*通过对required的属性设置true或false，断定该项是否必填.
*showName为alert时候要显示的对象.
*check的属性设置重点说明如下:
*对于不带参数的规则，可直接设置规则名，如notBlank或者notBlank().
*对于带参数的规则，需要设置对应的规则参数，如isDate('YYYY-MM-DD').
*对于本JS文件未提供的正则类型判定，可自行设置正则表达式验证，如验证三到五位的字符，设置reg('^\\S{3,5}$').
*可给check设置多个规则，各个规则之间使用";"隔开，如"不能含有空格，且字数不能超过10",设置"notBlank;isString('#',10)".
*规则函数的设定，有两种方式,说明如下:
*1.正则验证，返回数组rt，rt[0]为对应的正则表达式，rt[1]为对应要警告的语句.
*2.普通的判断function，返回false或true，函数体中通过this.value和this.showName获取页面数据，自行判定value的合法性.
*
*---------------客户端表单通用验证CheckForm(oForm)-----------------
*/
///////////////////////////////////////////////////////////////////////////////////
Validate={
		value:"",
		elespan:"",
		browserType:"msie",
		//主函数
		CheckForm:function(oForm){
			var els=oForm.elements;
			var firstErrorEle="";
			var result=true;
			//检查浏览器的类型
			if(window.navigator.userAgent.toLowerCase().indexOf("msie")>=1){//IE
				this.browserType="msie";
			}else if(window.navigator.userAgent.toLowerCase().indexOf("firefox")>=1){
				this.browserType="firefox"; 
			}else if(window.navigator.userAgent.toLowerCase().indexOf("chrome")>=1){
				this.browserType="chrome"; 
			}else{
				this.browserType="chrome";
			}
			//遍历所有表元素
			for(var i=0;i<els.length;i++){
				if(!this.validateElement(els[i])){
					result=false;
					if(firstErrorEle==""){
						firstErrorEle=els[i];
					}
				}
			}
			if(firstErrorEle!=""){
				var firstErrorEleValue=firstErrorEle.value;
				firstErrorEle.focus();
				firstErrorEle.value=firstErrorEleValue;
			}
			return result;
		},
		validateElement:function(ele){
			var sType=ele.type;
			if(sType=="button"||sType=="hidden"||sType=="image"||sType=="reset"||sType=="submit"){//隐藏域\图片和按钮不处理
				return true;
			}
			var sname=ele.name;
			this.value = this.jstrim(this.GetValue(ele));
			this.elespan=document.getElementById(sname+"_span");
			if(this.elespan){
				this.elespan.innerText="";
			}
			var required = ele.getAttribute("required");
			if(required!="undefined"&&(required=="true"||required==true)&&required!=false){//是否为空需要验证
				if(this.value==""){//当数据为空的时候判断
					this.showmessage("不能为空");
					return false;
				}
			}else{
				if(this.value==""){//当数据为空的时候判断
					return true;
				}
			}
			if(sType=="text"&&this.value!=""){//单纯的输入狂长度不可以大于200
				var lengths=this.value.length;
				if(lengths>200){
					this.showmessage("长度不能超过200");
					return false;
				}
			}
			var checkValue = ele.getAttribute("check");
			if(null==checkValue||""==checkValue||!checkValue){
				return true;
			}
			//取得表单的值,用通用取值函数
			var checkArr=checkValue.split(";");
			for(var i=0;i<checkArr.length;i++){
				if(checkArr[i]=="")continue;
				//取得验证的规则函数
				varsReg="";
				try{
					sReg=this.executeFunc(checkArr[i]);
				}catch(e){
					alert(e+"使用错误:类型'"+checkArr[i]+"'对应的验证规则函数未定义！");
					return false;
				}
				if(sReg==null||sReg=="undefined"||sReg[0]==""){
					continue;
				}
				if(sReg==false||sReg=="false"){
					return false;
				}
				//判定是否符合正则表达式
				if(!this.regValidate(this.value,sReg[0])){
					//验证不通过,弹出提示warning
					try{
						this.showmessage(sReg[1]);
					}catch(e){
						this.showmessage("格式不正确！");
					}
					return false;
				}
			}
			return true;
		},
		showmessage:function(str){
			if(this.elespan){
				if(this.browserType=="msie"){//IE
					this.elespan.innerText=str;
				}else if(this.browserType=="firefox"){
					this.elespan.textContent=str; 
				}else{
					this.elespan.innerText=str;
				}
			}else{
				alert("验证显示域未定义，提示信息："+str);
			}
		},
		//通用取值函数分三类进行取值
		//文本输入框,直接取值el.value
		//单多选,遍历所有选项取得被选中的个数返回结果"00"表示选中两个
		//单多下拉菜单,遍历所有选项取得被选中的个数返回结果"0"表示选中一个
		GetValue:function(el){
			//取得表单元素的类型
			var sType=el.type;
			switch(sType){
				case"text":
				case"hidden":
				case"password":
				case"file":
				case"textarea":return el.value;
				case"checkbox":
				case"radio":return GetValueChoose(el);
				case"select-one":
				case"select-multiple":return GetValueSel(el);
			}
		//取得radio,checkbox的选中数,用"0"来表示选中的个数,我们写正则的时候就可以通过0{1,}来表示选中个数
		function GetValueChoose(el){
			var sValue="";
			//取得第一个元素的name,搜索这个元素组
			var tmpels=document.getElementsByName(el.name);
			for(var i=0;i<tmpels.length;i++){
				if(tmpels[i].checked){
					sValue+="0";
				}
			}
			return sValue;
		}
		//取得select的选中数,用"0"来表示选中的个数,我们写正则的时候就可以通过0{1,}来表示选中个数
		function GetValueSel(el){
			var sValue="";
			for(var i=0;i<el.options.length;i++){
				//单选下拉框提示选项设置为value=""
				if(el.options[i].selected&&el.options[i].value!=""){
					sValue+="0";
				}
			}
			return sValue;
		}
	},
	//通用返回函数,验证没通过返回的效果.分三类进行取值
	//文本输入框,光标定位在文本输入框的末尾
	//单多选,第一选项取得焦点
	//单多下拉菜单,取得焦点
	GoBack:function(el){
		//取得表单元素的类型
		varsType=el.type;
		switch(sType){
			case"text":
			case"hidden":
			case"password":
			case"file":
			case"textarea":
			try{
				el.focus();varrng=el.createTextRange();rng.collapse(false);rng.select();
			}catch(e){}
			break;
			case"checkbox":
			case"radio":
				try{
				varels=document.getElementsByName(el.name);els[0].focus();
				}catch(e){}
				break;
			case"select-one":
			case"select-multiple":
				try{
					el.focus();
				}catch(e){}
			}
		},
		executeFunc:function(name){
			if(name.indexOf("(")==-1){
				return eval("this."+name+"()");
			}else{
				return eval("this."+name);
			}
		},
		//去除空格
		jstrim:function(values){
			if(values){
				return values.replace(/(^\s*)|(\s*$)/g,"");
			}else{
				return "";
			}
		},
		//判定某个值与表达式是否相符
		regValidate:function(value,sReg){
			//字符串->正则表达式,不区分大小写
			var reg=new RegExp(sReg,"i");
			if(reg.test(value)){
				return true;
			}else{
				return false;
			}
		},
		//////////////////////////////////验证规则定义////////////////////////////////
		reg:function(sReg){
			var rt=new Array();
			rt[0]=sReg;
			rt[1]="格式不匹配正则表达式:"+sReg;
			return rt;
		},
		/**
		*字符串判定
		*如min设为"'#'",表示字数不能大于max
		*如max设为"'#'",表示字数不能小于min
		*/
		isString:function(min,max){
			var rt=new Array();
			if(min==null&&max==null){
				rt[0]="";
				rt[1]="";
				return rt;
			}
			if(max==null)
				max=min;
			if(min=="#"&&max=="#"){
				//任意字符
				rt[0]="[\\S|\\s]";
				rt[1]="";
				return rt;
			}
			if(min=="#"){
				rt[0]="^[\\S|\\s]{0,"+max+"}$";
				rt[1]="字数不能大于"+max;
				return	rt;
			}
			if(max=="#"){
				rt[0]="^[\\S|\\s]{"+min+",}$";
				rt[1]="字数不能小于"+min;
				return rt;
			}
			rt[0]="^[\\S|\\s]{"+min+","+max+"}$";
			if(min==max){
				rt[1]="字数应为"+min+"个！";
			}else{
				rt[1]="字数介于"+min+"和"+max+"之间！";
			}
			return rt;
		},
		strStartsWith:function(str){
			if(this.value.indexOf(str)!=0){
				this.showmessage("必须以字符‘"+str+"’开头！");
				return false;
			}
		},
		isContains:function(str){
			if(this.value.indexOf(str)==-1){
				this.showmessage("必须包含字符‘"+str+"’！");
				return false;
			}
		},
		strEndsWith:function(str){
			if(this.value.lastIndexOf(str)+str.length!=this.value.length){
				this.showmessage("必须以字符‘"+str+"’结束！");
				return false;
			}
		},
		//判断email
		isEmail:function(){
			var rt=new Array();
			rt[0]="\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
			rt[1]="应为正确的EMAIL格式！";
			return rt;
		},
		//只能输入中文
		onlyZh:function(){
			var rt=new Array();
			rt[0]="^[\u0391-\uFFE5]+$";
			rt[1]="应输入中文！";
			return	rt;
		},
		//只可输入英文
		onlyEn:function(){
			var rt=new Array();
			rt[0]="^[A-Za-z]+$";
			rt[1]="只能输入英文！";
			return rt;
		},
		enOrNum:function(){
			var rt=new Array();
			rt[0]="^[A-Za-z0-9]+$";
			rt[1]="只能输入英文和数字,且不能有空格！";
			return rt;
		},
		/**
		*整数的判定
		*@paramtype
		*为空任意整数
		*'0+'非负整数
		*'+'正整数
		*'-0'非正整数
		*'-'负整数
		*/
		isInt:function(type){
			var rt=new Array();
			if(type=="0+"){
				rt[0]="^\\d+$";
				rt[1]="应输入非负整数!";
			}else if(type=="+"){
				rt[0]="^\\d*[1-9]\\d*$";
				rt[1]="应输入正整数!";
			}else if(type=="-0"){
				rt[0]="^((-\\d+)|(0+))$";
				rt[1]="应输入非正整数!";
			}else if(type=="-"){
				rt[0]="^-\\d*[1-9]\\d*$";
				rt[1]="应输入负整数!";
			}else{
				rt[0]="^-?\\d+$";
				rt[1]="应输入整数值！";
			}
			return rt;
		},
		/**
		*浮点数的判定
		*@paramtype
		*为空任意浮点数
		*'0+'非负浮点数
		*'+'正浮点数
		*'-0'非正浮点数
		*'-'负浮点数
		*/
		isFloat:function(type){
			var rt=new Array();
			if(type=="0+"){
				rt[0]="^\\d+(\.\\d+)?$";
				rt[1]="应输入非负浮点数!";
			}else if(type=="+"){
				rt[0]="^((\\d+\\.\\d*[1-9]\\d*)|(\\d*[1-9]\\d*\\.\\d+)|(\\d*[1-9]\\d*))$";
				rt[1]="应输入正浮点数!";
			}else if(type=="-0"){
				rt[0]="^((-\\d+(\.\\d+)?)|(0+(\\.0+)?))$";
				rt[1]="应输入非正浮点数!";
			}else if(type=="-"){
				rt[0]="^(-((\\d+\\.\\d*[1-9]\\d*)|(\\d*[1-9]\\d*\\.\\d+)|(\\d*[1-9]\\d*)))$";
				rt[1]="应输入负浮点数!";
			}else{
				rt[0]="^(-?\\d+)(\\.\\d+)?$";
				rt[1]="应输入浮点数值！";
			}
			return rt;
		},
		/**
		*数字大小判定
		*如min设为"'#'",表示不能大于max
		*如max设为"'#'",表示不能小于min
		*/
		setNumber:function(min,max){
			if(!this.regValidate(this.value,"^(-?\\d+)(\\.\\d+)?$")){
				this.showmessage("应输入数字！");
				return false;
			}
			if(min==null&&max==null){
				//任意数字,不判定范围
				return isFloat();
			}
			if(max==null)
				max=min;
			if(min=="#"&&max=="#"){
				//任意数字,不判定范围
				return isFloat();
			}
			if(min=="#"){
				if(this.value>max){
					this.showmessage("不能大于"+max);
					return false;
				}
			}
			if(max=="#"){
				if(this.value<min){
					this.showmessage("不能小于"+min);
					return false;
				}
			}
			if(this.value<min||this.value>max){
				if(min==max){
					this.showmessage("的值应为"+min);
				}else{
					this.showmessage("应介于"+min+"和"+max+"之间！");
				}
				return false;
			}
			return true;
		},
		isPhone:function(){
			var rt=new Array();
			rt[0]="^((\\(\\d{2,3}\\))|(\\d{3}\\-))?(\\(0\\d{2,3}\\)|0\\d{2,3}-)?[1-9]\\d{6,7}(\\-\\d{1,4})?$";
			rt[1]="应输入正确的电话号码格式！";
			return rt;
		},
		isMobile:function(){
			var rt=new Array();
			rt[0]="^((\\(\\d{2,3}\\))|(\\d{3}\\-))?1\\d{10}$";
			rt[1]="应输入正确的手机号码格式！";
			return rt;
		},
		isUrl:function(){
			var rt=new Array();
			rt[0]="^http[s]?:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\\?%\\-&_~`@[\\]\\':+!]*([^<>\"\"])*$";
			rt[1]="应输入正确的URL（必须以http(s)://开头）！";
			return rt;
		},
		isZip:function(){
			var rt=new Array();
			rt[0]="^[0-9]\\d{5}$";
			rt[1]="应输入正确的编码格式！";
			return rt;
		},
		//目标至少需要选择(通常用于select-multiple/checkbox)
		select:function(num){
			var rt=new Array();
			rt[0]="^0{"+num+",}$";
			rt[1]="应至少选择"+num+"项！";
			return rt;
		}
}
