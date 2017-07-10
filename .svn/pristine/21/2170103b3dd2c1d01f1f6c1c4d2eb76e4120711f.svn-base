// JavaScript Document
$(function(){
    //关于图片滚动
   var index=0;
   var pictime=4000;
   var SLIDERS=$('#sliderfade li');
   var num=SLIDERS.length;
   function timepic(){
       index=(index>=num-1?0:++index);
       SLIDERS.css('display','none').eq(index).css({'display':'block','opacity':0}).animate({'opacity':1},500);
	   appendTxt(SLIDERS.eq(index).find('a'),SLIDERS.eq(index).find('img'));
   }
   appendTxt(SLIDERS.eq(index).find('a'),SLIDERS.eq(index).find('img')); //第一张图没有文字的问题
   var interval=setInterval(timepic,pictime);
   SLIDERS.eq(index).css('display','block').siblings('li').css('display','none');
   $(' .buttons .right').bind('click',function(){
	   if(SLIDERS.is(':animated')) return false;clearInterval(interval);
	   index=(index>=num-1?0:++index);
       SLIDERS.css('display','none').eq(index).css({'display':'block','opacity':0}).animate({'opacity':1},500);
	   appendTxt(SLIDERS.eq(index).find('a'),SLIDERS.eq(index).find('img'));
	   interval=setInterval(timepic,pictime);
   })
   $(' .buttons .left').bind('click',function(){
	   if(SLIDERS.is(':animated')) return false;clearInterval(interval);
	   index=(index==0?(num-1):--index);
       SLIDERS.css('display','none').eq(index).css({'display':'block','opacity':0}).animate({'opacity':1},500);
	   appendTxt(SLIDERS.eq(index).find('a'),SLIDERS.eq(index).find('img'));
	   interval=setInterval(timepic,pictime);
   })
   
   function appendTxt(aTag,imgTag)
   {
	   var aT=$("<a>");
	   aT.attr("href",aTag.attr('href'));
	   aT.text(imgTag.attr('alt'));
	   aT.attr("target",'_blank');
	   $('#sliderTxt').html(aT);
   }
   
   //关于图片滚动
   
   
    $(' .userlogin .username').bind('focus',function(){
	   if($(this).val()==this.defaultValue){
	       $(this).val("");
	   }
   }).bind('blur',function(){
       var text=this.defaultValue;
	   if($(this).val().replace(/(^\s*)|(\s*$)/g,"")==""){
	      this.value=text;
	   }
   })
   /*
   $(' .userlogin  #password').bind('focus',function(){
	   $(this).hide();
	   $(' .userlogin  #passwordtext').val("").show().focus();
   })
   $(' .userlogin  #passwordtext').bind('blur',function(){
       var text=document.getElementById('password').defaultValue;
	   if($(this).val().replace(/(^\s*)|(\s*$)/g,"")==""){
	      $(this).hide();
		  $(' .userlogin  #password').show().blur();
	   }
   })
   */
   $(' .userlogin  .passwordtext').bind('focus',function(){
	   $(this).addClass('passwordnone');
   })
   $(' .userlogin  .passwordtext').bind('blur',function(){
       var text="密码";
	   if($(this).val().replace(/(^\s*)|(\s*$)/g,"")==""){
		  $(this).removeClass('passwordnone');
	   }
   })

   $(' .hot .top .menu1 a').bind('click',function(event){
       var dex=$(this).index();
      $(this).addClass('on').siblings().removeClass('on');
      swaptap(dex);
   })
   
   $(' .hot').hover(function(){
           clearInterval(autoswaptapInterval);
       },function(){
                autoswaptapInterval=setInterval(autoswaptap,autoswaptaptime);
          })
   
   var autoswaptaptime=4000;
   var autoswaptapInterval=setInterval(autoswaptap,autoswaptaptime);
   var autoswaptapindex=0;
   function autoswaptap()
   {
       var _menuon=$(' .hot .top .menu1 .on');
       var _menua=$(' .hot .top .menu1 a');
       if(_menuon.index()==_menua.length-1)
       {
           _menua.first().addClass('on').siblings().removeClass('on');
       }else{
           _menuon.next().addClass('on').siblings().removeClass('on');
       }
       var dex=$(' .hot .top .menu1 .on').index();
       swaptap(dex);
   }
   
    function swaptap(dex)
    {
      $('.hot .list_dl').addClass('hide').eq(dex).removeClass('hide');
       if ($.browser.msie && (parseInt($.browser.version,10)<=7) && !$.support.style) {$('.hot .shadow').css('top',parseInt($('.hot>.top')[0].offsetHeight,10)+parseInt($('.hot .list_dl').not('.hide')[0].offsetHeight,10)+10+'px');}
    }

   
   
   $(' .hot .top .sort a').bind('click',function(){
	   var inde=$(this).index()-1;
       $(' .hot .top .menu1 a').eq(inde).trigger('click');
   })


   
   $('#sort a, #stat a').hover(function(){
	   var po=$(this).parent().find('.pop');
	   $(this).parent().find('.pop .txt').html($(this).attr('title'));
	   po.css({'left':$(this).position().left-(po.width()/2)  + ($(this).width()/2) });
	   po.css({'bottom':$(this).position().top+$(this).innerHeight()+8});
	   po.show();
	   }, 
	   function(){
	   $(this).parent().find('.pop').hide();
		   });
	
	
	//关于是否判断登录

   
		 $(document).ready(function() {
		 
		 
			 /*
		 $.ajax({
             type: "get",
             async: false,
             url: "http://career.cic.tsinghua.edu.cn/xsglxt/b/jyxt/anony/checkLogout",
             dataType: "jsonp",
             jsonp: "callback",//传递给请求处理程序或页面的，用以获得jsonp回调函数名的参数名(一般默认为:callback)
             //jsonpCallback:"?",//自定义的jsonp回调函数名称，默认为jQuery自动生成的随机函数名，也可以写"?"，jQuery会自动为你处理数据
             success: function(data){
               
				 if(data.code !=0){
					
					 $('#loginStatePage').hide();
					 $('.menu-logout').show();
				}else{
					 $('.menu-login').show();
				}
             },
             error: function(){
               
				$('#loginStatePage').hide();
				$('.menu-logout').show();
             }
         });
		 */
		 /*
             $('.consult').click(function(){
			    jQuery.ajax({
					  url:'http://166.111.4.89:21006/career.student.byssy.do?m=checkLogout&callback=?', //后台处理程序
					  type:'get',         //数据发送方式
					  dataType:'jsonp',     //接受数据格式
					  //jsonp:'callback',
					  data:{},         //要传递的数据
					  success:function(data){ 
						 
						 if(data.code !=0){
							//alert("只有登陆后才可以访问！");
							 //$('#loginStatePage').hide();
							 //$('.menu-logout').show();
						}else{
						     window.open('http://career.cic.tsinghua.edu.cn/career.v_jy_zyzxyy_hzcx.do?m=index','zxyu');
							 //$('.menu-login').show();
						}
					  } , //回传函数(这里是函数名)
					  error :function(xhr,txtStatus,error){
					     //alert(textStatus);
					      //alert(xhr.responseText);
						  if(xhr.readyState==4){
							if(xhr.state==200){
								$('.menu-login').show();
							}else{
								// alert(xhr.state);
								// return;
								$('#loginStatePage').hide();
								$('.menu-logout').show();
							}
						  }
				
					 }
				});
				*/
			 });
		 
	});