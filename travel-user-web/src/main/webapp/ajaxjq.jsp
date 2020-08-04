<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	request.setAttribute("basePath", request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath() + "/"); 
	/*
	request.setAttribute("basePath", request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort() + "/");
	*/
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<script type="text/javascript" src="${basePath }js/jquery/jquery.min.js"></script>

<title>请求事件</title>
</head>
<body>
	当前的SessionId：${sessionScope.loginSessionId }
	<hr />
	<button onclick="test()">请求测试</button>
	<script>
		function test(){
			$.ajax({
				type:"get",
				url:"http://localhost:8099/user/test",
				dataType:"JSONP",
				//在发送请求前设置请求头
				/*beforeSend:function(request){
					request.setRequestHeader("xydLoginToke","83ab8fe9-fc40-4b43-b509-b0ffde84351e");
				}*/
				headers:{
					"xydLoginToke":"515a7ef5-0e34-447d-aa85-a513f9da44c3"
				},
				success:function(){
					alert("请求成功！");
				},
				error:function(){
					alert("请求失败！");
				}
			});	
		}
	</script>
</body>
</html>