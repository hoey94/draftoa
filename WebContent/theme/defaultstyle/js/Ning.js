// JavaScript Document - By:zhaoshixing | Http://www.zhxwl.cn
//(function(){//一级栏目 首页 特别定义函数
	//$('.t1:first').addClass('t1_bg2');
//})();
$('a.c').mouseover(function(){//a.c title设置
	var temp = $(this).html();
	$(this).attr('title' , temp);
});
$('.t1').click(//一级菜单事件
		function(){
			$(this).addClass('t1_bg2')								//当前t1切换背景
				.children('.t2').slideDown(300).end()				//当前t1下的t2展开
				.children('a.a').css('color' , '#fff').end()		//当前t1下的a.a文字切换颜色
				.siblings().removeClass('t1_bg2')						//当前t1的兄弟t1切换背景
				.children('a.a').css('color' , '#636363').end()		//兄弟t1下的a.a文字切换颜色
				.children('.t2').slideUp(200)						//兄弟t1下的t2收缩
				.children('a.b').removeClass('b2').end()			//兄弟t2下的a.b切换背景
				.children('.t3').slideUp(100).end();				//兄弟t2下的t3收缩
		}
);
$('.t2').click(//二级菜单事件
	function(){
		$('a.b').removeClass('b2');
		$(this)
			.children('.t3').slideDown(300).end()
			.children('a.b').addClass('b2').end()
			.siblings().removeClass('ccc')
			.children('a.b').removeClass('b2').end()
			.children('.t3').slideUp(100).end();
	}
);
$('a.c').click(//a.c菜单事件
	function(){
		$('.c').removeClass('red');
		$(this).addClass('red').end();
	}
);
/* -------------层级菜单模板------------
<div class="t1" id="a2"><a class="a" href="#">第一季</a>
    <div class="t2" id="b3"><a class="b" href="#">第二季</a>
        <div class="t3"><a class="c" href="#">第三季</a></div>
        <div class="t3"><a class="c" href="#">第三季</a></div>
    </div>
    <div class="t2" id="b4"><a class="b" href="#">第二季</a>
        <div class="t3"><a class="c" href="#">第三季</a></div>
        <div class="t3"><a class="c" href="#">第三季</a></div>
    </div>
</div>

<div class="t1" id="a3"><a class="a" href="#">第一季</a>
    <div class="t2" id="b5"><a class="c" href="zcgl/biao.html" target="main">第二季</a>
<!--        <div class="t3"><a class="c" href="#">第三季</a></div>
        <div class="t3"><a class="c" href="#">第三季</a></div>
-->    </div>
    <div class="t2" id="b6"><a class="c" href="#">第二季</a>
<!--        <div class="t3"><a class="c" href="#">第三季</a></div>
        <div class="t3"><a class="c" href="#">第三季</a></div>
-->    </div>
</div>
*/