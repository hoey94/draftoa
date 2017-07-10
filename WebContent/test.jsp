<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
	<script type="text/javascript" src="javascript/jquery-1.11.2.js"></script>
	<style type="text/css">
		.main{background-color:red;display:block;text-decoration:none;margin-top:10px;}
		.name ul{display : none;margin-left:50px;}
		.name .sh{display:block;}
		.on{color:red;}
	</style>
</head>
<body>
	<div >
		<ul class="first">
			<li>第一个</li>
			<li>第二个</li>
		</ul>
		<div class="name">
			<ul class="second">
				<li>标签1</li>
				<li>标签2</li>
				<li>标签3</li>
			</ul>
			<ul class="second">
				<li>标签1</li>
				<li>标签2</li>
				<li>标签3</li>
			</ul>
		</div>
	</div>
</body>

<script type="text/javascript">
	$(".name").children("ul").siblings().removeClass("sh").eq(localStoreage.one).addClass("sh");
	$(".name ul").children("li").siblings().removeClass("on").eq(localStoreage.two).addClass("on");
	$(".main ul li").click(function(){
		
		var _index = $(this).index();
		$(".name").children("ul").siblings().removeClass("sh").eq(_index).addClass("sh");
		localStorage.one = _index;
		alert(_index);
		
		$(".name ul li").click(function(){
			
			var index = $(this).index();
			$(".name ul li").eq(index).addClass("on");
			localStoreage.two = index;
			alert(index);
		});
		
	});

</script>
</html>
