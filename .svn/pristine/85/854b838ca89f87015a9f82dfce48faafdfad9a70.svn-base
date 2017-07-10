//对联广告
var delta=0.115 
var collection; 
function floaters(){ 
this.items = []; 
//在页面中动态添加div，参数依次代表：div的id，x坐标,y坐标,显示的内容
this.addItem= function(id,x,y,content){ 
            document.write('<DIV id='+id+' style="Z-INDEX: 0; POSITION: absolute;  width:96px; height:360px;left:'+(typeof(x)=='string'?eval(x):x)+';top:'+(typeof(y)=='string'?eval(y):y)+'">'+content+'</DIV>'); 
             var newItem                = {}; 
            newItem.object            = document.getElementById(id); 
            newItem.x                = x; 
            newItem.y                = y; 
            this.items[this.items.length]  = newItem; 
          } 
           this.play= function(){ 
            collection = this.items 
            setInterval('play()',10); 
          } 
} 
//显示对联，此方法绑定到定时器
function play(){ 
    if(screen.width<=800){ //宽度小于800时，不显示对联
        for(var i=0;i<collection.length;i++){ 
            collection[i].object.style.display    = 'none'; 
        } 
        return; 
    } 
    for(var i=0;i<collection.length;i++){ 
        var followObj        = collection[i].object; 
        var followObj_x        = (typeof(collection[i].x)=='string'?eval(collection[i].x):collection[i].x); 
        var followObj_y        = (typeof(collection[i].y)=='string'?eval(collection[i].y):collection[i].y); 
        if(followObj.offsetLeft!=(document.documentElement.scrollLeft+followObj_x)) { 
            var dx=(document.documentElement.scrollLeft+followObj_x-followObj.offsetLeft)*delta; 
            dx=(dx>0?1:-1)*Math.ceil(Math.abs(dx)); 
            followObj.style.left=followObj.offsetLeft+dx; 
            } 
        if(followObj.offsetTop!=(document.documentElement.scrollTop+followObj_y)) { 
            var dy=(document.documentElement.scrollTop+followObj_y-followObj.offsetTop)*delta; 
            dy=(dy>0?1:-1)*Math.ceil(Math.abs(dy)); 
            followObj.style.top=followObj.offsetTop+dy; 
            } 
        followObj.style.display    = ''; 
    } 
}    
var theFloaters= new floaters(); //创建悬浮对联广告
//添加2幅广告
theFloaters.addItem('div1','document.documentElement.clientWidth-120',200,'<br><a href="javascript:void(0);"><img src="upload/201009/20100911075552.jpg" width="100" height="267" border="0" /></a>'); 
theFloaters.addItem('div2',20,200,'<br><a href="javascript:void(0);"><img src="upload/201009/20100911075552.jpg" width="100" height="267" border="0" /></a>'); 
theFloaters.play();

function advertisement(adId,imgurl,adtext,adurl,adtype,width,height,left,top){
	this.adId  = adId;
	this.imgurl = imgurl;
	this.adtext = adtext;
	this.adurl  = adurl;
	this.adtype = adtype;
	this.width = width;
	this.height = height;
	this.left = left;
	this.top=top;
	this.paly=function(){
		if(this.adtype="A"){// 对联效果浮动
			alert("A");
		}else if(this.adtype="B"){
			alert("B");
		}else if(this.adtype="C"){
			alert("C");
		}else if(this.adtype="D"){
			alert("D");
		}else if(this.adtype="E"){
			alert("E");
		}else if(this.adtype="F"){
			alert("F");
		}
	};
}
var temp=new advertisement("ee","url","adtext","adurl","A",-100,200,100,200);
var xPos = 20;
var yPos = document.documentElement.clientHeight;
var step = 1;
var delay = 30;
var height = 0;
var Hoffset = 0;
var Woffset = 0;
var yon = 0;
var xon = 0;
var interval;
var img = document.getElementById("flowimg");
var flowimgurl = document.getElementById("flowimgurl");
var flowimgsrc = document.getElementById("flowimgsrc");
flowimgurl.href="#";// 这里是连接地址
flowimgsrc.src="upload/zhaopin.png";// 这里是图片的地址
img.style.top = yPos;
function changePos() {
    width=document.documentElement.clientWidth;
    height=document.documentElement.clientHeight;
    Hoffset=img.offsetHeight;   Woffset=img.offsetWidth;
    img.style.left=xPos+document.body.scrollLeft+"px";
    img.style.top=yPos+document.body.scrollTop+"px";
    if(yon) {
        yPos=yPos+step;
    }else {
        yPos=yPos-step;
    }if(yPos<0) {
        yon=1;      yPos=0;
    }if(yPos>=(height-Hoffset)) {
        yon=0;      yPos=(height-Hoffset);
    }if(xon) {
        xPos=xPos+step;
    }else {
        xPos=xPos-step;
    }if(xPos<0) {
        xon=1;      xPos=0;
    }if(xPos>=(width-Woffset)) {
        xon=0;      xPos=(width-Woffset);
    }
}
function start() {
    img.style.visibility="visible";
    interval=setInterval('changePos()',delay);
}
start();


