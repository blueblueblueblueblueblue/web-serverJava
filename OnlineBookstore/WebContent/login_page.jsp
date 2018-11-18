<%@page import="com.nuaa.util.LoginResultEnum"%>
<%@page import="com.nuaa.util.C"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="css/mystyle.css" type="text/css" rel="stylesheet">
<title>登录</title>
<script type="text/javascript" src="js/checkLoginInfo.js"></script>
</head>
<body>
	<%
		//token标记
		session.setAttribute(C.LOGIN_TOKEN, true);
        
		//登录错误情况提示
		String tip1="";
		String tip2="";
		String tip3 ="";
		LoginResultEnum result=(LoginResultEnum)request.getAttribute("result");
		
		if(result == LoginResultEnum.USERNAME_UNEXISTED)
			tip1="用户名不存在";
		else if(result == LoginResultEnum.PASSWORD_ERROR)
			tip2="密码错误或者账户未激活";
		else if(result == LoginResultEnum.USER_ACTIVE)
			tip3="账户已激活 请登录";
		
		
	%>
	

	<!-- 头部 -->
	<jsp:include page="layouts/title.jsp" flush="true" />
	
	<!-- 登录框-->
	<div class="logindiv1">
		<div class="logindiv2">
			<span class="font_bold_black">用户登录</span>
		</div>
		<div class="logindiv3">
			<div class="logindiv4">
				<form action="LoginLogoutClServlet?dowhat=login" method="post">
				    <span class="tip1 font_normal_blue"><%=tip3 %></span><br>
					<input type="text" placeholder="邮箱" class="userbg bgedit" name="username" autocomplete="off" autofocus="autofocus"><br>
					<span class="tip1 font_normal_red"><%=tip1 %></span><br> <!-- 用户名不存在 -->

					<input type="password" placeholder="密码" class="pwdbg bgedit" name="password" autocomplete="off"><br>
					<span class="tip1 font_normal_red"><%=tip2 %></span><br> <!-- 密码错误 -->
					
					<a href="register_page.jsp" target="_self">免费注册</a><br>
					<input type="submit" value="登     录" class="greenbtn font_bold_white" 
							onclick="return checkLoginInfo(username.value,password.value)">
				</form>
			</div>
		</div>
	</div>

	<!-- 尾部-->
	<jsp:include page="layouts/footer.jsp" flush="true" />
</body>
</html>