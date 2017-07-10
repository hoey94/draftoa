/*!
 * RevealTrans
 * Copyright (c) 2010 cloudgamer
 * Blog: http://cloudgamer.cnblogs.com/
 * Date: 2008-5-23
 */
var isIE = (document.all) ? true : false;
var Class = {
	create : function() {
		return function() {
			this.initialize.apply(this, arguments);
		}
	}
}
var Extend = function(destination, source) {
	for ( var property in source) {
		destination[property] = source[property];
	}
}
var Bind = function(object, fun) {
	return function() {
		return fun.apply(object, arguments);
	}
}
var Each = function(list, fun) {
	for ( var i = 0, len = list.length; i < len; i++) {
		fun(list[i], i);
	}
};
// ie only
var RevealTrans = Class.create();
RevealTrans.prototype = {
	initialize : function(container, options) {
		this._img = document.createElement("img");
		this._a = document.createElement("a");
		this._timer = null;// 计时器
		this.Index = 0;// 显示索引
		this._onIndex = -1;// 当前索引
		this.SetOptions(options);
		this.Auto = !!this.options.Auto;
		this.Pause = Math.abs(this.options.Pause);
		this.Duration = Math.abs(this.options.Duration);
		this.Transition = parseInt(this.options.Transition);
		this.List = this.options.List;
		this.onShow = this.options.onShow;
		// 初始化显示区域
		this._img.style.visibility = "hidden";// 第一次变换时不显示红x图
		this._img.style.width = this._img.style.height = "100%";
		this._img.style.border = 0;
		this._img.onmouseover = Bind(this, this.Stop);
		this._img.onmouseout = Bind(this, this.Start);
		isIE && (this._img.style.filter = "revealTrans()");
		this._a.target = "_blank";
		document.getElementById(container).appendChild(this._a).appendChild(
				this._img);
	},
	// 设置默认属性
	SetOptions : function(options) {
		this.options = {// 默认值
			Auto : true,// 是否自动切换
			Pause : 2000,// 停顿时间(微妙)
			Duration : 1,// 变换持续时间(秒)
			Transition : 23,// 变换效果(23为随机)
			List : [],// 数据集合,如果这里不设置可以用Add方法添加
			onShow : function() {
			}// 变换时执行
		};
		Extend(this.options, options || {});
	},
	Start : function() {
		clearTimeout(this._timer);
		// 如果没有数据就返回
		if (!this.List.length)
			return;
		// 修正Index
		if (this.Index < 0 || this.Index >= this.List.length) {
			this.Index = 0;
		}
		// 如果当前索引不是显示索引就设置显示
		if (this._onIndex != this.Index) {
			this._onIndex = this.Index;
			this.Show(this.List[this.Index]);
		}
		// 如果要自动切换
		if (this.Auto) {
			this._timer = setTimeout(Bind(this, function() {
				this.Index++;
				this.Start();
			}), this.Duration * 1000 + this.Pause);
		}
	},
	// 显示
	Show : function(list) {
		if (isIE) {
			// 设置变换参数
			with (this._img.filters.revealTrans) {
				Transition = this.Transition;
				Duration = this.Duration;
				apply();
				play();
			}
		}
		this._img.style.visibility = "";
		// 设置图片属性
		this._img.src = list.img;
		this._img.alt = list.text;
		// 设置链接
		!!list["url"] ? (this._a.href = list["url"]) : this._a
				.removeAttribute("href");
		// 附加函数
		this.onShow();
	},
	// 添加变换对象
	Add : function(sIimg, sText, sUrl) {
		this.List.push( {
			img : sIimg,
			text : sText,
			url : sUrl
		});
	},
	//停止
	Stop : function() {
		clearTimeout(this._timer);
	}
};

/***
 * 添加页面图片播放器代码
 * @param divId 页面div的Id
 * @param playCode 系统页面播放器代码
 */
function addImagePlay(divId,playCode){
	var div=document.getElementById(divId);
	var imagePlay=null;
	$.ajax({
		   type: "post",
		   url: "web.do?action=getImagePlayJson",
		   data:{playCode:playCode},
		   timeout: 5000,
		   success: function(msg){
			   imagePlay = eval('(' + msg + ')');
			   if(imagePlay!=null){
					var width=imagePlay.width+"px";
					var height=imagePlay.height+"px";
					var playType=imagePlay.playType;
					if("javascriptplay"==playType){
						var dheight=imagePlay.height+25+"px";
						var ddiv=document.createElement("div");
						ddiv.id=divId+"_"+playCode;
						div.style.cssText="width:"+width+";height:"+dheight;
						ddiv.style.cssText="width:"+width+50+";height:"+height+";border:1px solid #eee;position:relative;";
						var oText=document.createElement("div"), arrImg = [];
						var oNum = document.createElement("ul"), arrNum = [];
						oText.id=divId+"PicText";
						oNum.id=divId+"PicNum";
						oText.style.cssText="width:"+width+";background:#eee;line-height:25px;text-align:center;font-weight:bold;white-space:nowrap;overflow:hidden;font-size:12px;";
						oNum.style.cssText="position:absolute; right:5px; bottom:1px;";
						//设置主对象样式
						ddiv.appendChild(oNum);
						div.appendChild(ddiv);
						div.appendChild(oText);
						//添加变换对象
						var rvt = new RevealTrans(divId+"_"+playCode);
						for(var t=0;t<imagePlay.rows.length;t++){
							rvt.Add(imagePlay.rows[t].imageurl,imagePlay.rows[t].linktext,imagePlay.rows[t].linkurl);
						}
						//设置图片列表
						Each(rvt.List, function(list, i){
							//图片式
							var img = document.createElement("img");
							img.src = list["img"]; img.alt = list["text"];
							img.width=width;
							img.height=height;
							arrImg[i] = img;
							//按钮式
							var li = document.createElement("li");
							li.innerHTML = i + 1;
							arrNum[i] = li;
							li.style.cssText="float: left;list-style:none;color: #fff;text-align: center;line-height: 16px;width: 16px;height: 16px;font-family: Arial;font-size: 12px;cursor: pointer;margin: 1px;border: 1px solid #707070;background-color: #060a0b;";
							oNum.appendChild(li);
							//事件设置
							img.onmouseover = li.onmouseover = function(){ rvt.Auto = false; rvt.Index = i; rvt.Start(); };
							img.onmouseout = li.onmouseout = function(){ rvt.Auto = true; rvt.Start(); };
						});
						//设置图片列表样式 文本显示区域
						rvt.onShow = function(){
							var i = this.Index, list = this.List[i];
							//图片式
							arrImg[i].style.cssText = "width:"+width+";height:"+height;
							//按钮式
							Each(arrNum, function(o){ 
								o.style.cssText = "float: left;list-style:none;color: #fff;text-align: center;line-height: 16px;width: 16px;height: 16px;font-family: Arial;font-size: 12px;cursor: pointer;margin: 1px;border: 1px solid #707070;background-color: #060a0b;";
								}); 
							arrNum[i].style.cssText = "float: left;list-style:none;color: #fff;text-align: center;line-height: 16px;width: 16px;height: 16px;font-family: Arial;font-size: 12px;cursor: pointer;margin: 1px;border: 1px solid #707070;background-color: #FF6600;";
							//文本区域
							oText.innerHTML = !!list.url ? "<a href='" + list.url + "' target='_blank' style='text-decoration:none;color:#333;display:block;'>" + list.text + "</a>" : list.text;
						}
						//文本显示区域
						oText.onmouseover = function(){ rvt.Auto = false; rvt.Stop(); };
						oText.onmouseout = function(){ rvt.Auto = true; rvt.Start(); };
						rvt.Start();
					}else if("imageplay"==playType){
						var ddiv=document.createElement("div");
						ddiv.id=divId+"_"+playCode;
						div.style.cssText="width:"+width+";height:"+height;
						ddiv.style.cssText="width:"+width+";height:"+height+";border:1px solid #eee;position:relative;";
						div.appendChild(ddiv);
						//添加变换对象
						var rvt = new RevealTrans(divId+"_"+playCode);
						for(var t=0;t<imagePlay.rows.length;t++){
							rvt.Add(imagePlay.rows[t].imageurl,imagePlay.rows[t].linktext,imagePlay.rows[t].linkurl);
						}
						rvt.Start();
					}
					
				}
		   }			
	});
	
}

