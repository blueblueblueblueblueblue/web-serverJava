<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="com.nuaa.util.C"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="css/mystyle.css" type="text/css" rel="stylesheet">
<title>注册</title>
<script type="text/javascript" src="js/checkRegisterInfo.js"></script>
</head>
<body>
	<%	
		//token标记
		session.setAttribute(C.REGISTER_TOKEN, true);
		
		//注册出错以后
		String username="";
		String password="";
		String tip="";
		
		if(request.getAttribute("result") != null) //用户名重复
		{
			tip="用户名已存在";
			password=(String)request.getAttribute("password");
		}
	%>


	<!-- 头部 -->
	<jsp:include page="layouts/title.jsp" flush="true" />
	
	<!-- 注册框-->

	<div class="regdiv1">
		<div class="regdiv2">
			<span class="font_bold_black">新用户注册</span>
		</div>
		<div class="regdiv3">
			<div class="regdiv4">
			 	<form action="RegisterClServlet"  method="post">
					<label for="id_username">邮箱</label>
					<input type="text" placeholder="邮箱" class="tipedit" id="id_username" name="username"  autofocus="autofocus"><br>
					<span class="tip font_normal_red"><%=tip %></span><br> <!-- 用户名已存在提示 -->
					
					<label for="id_password">密&nbsp;&nbsp;&nbsp;码</label>
					<input type="password" placeholder="不少于6位" class="normaledit" id="id_password" name="password" value="<%= password%>"><br>
									
							
					<label for="id_repassword">确&nbsp;&nbsp;&nbsp;认</label>
					<input type="password" placeholder="再次输入上面的密码" class="normaledit" id="id_repassword" name="repassword" value="<%= password%>"><br>	
					
					
					<input type="submit" value="立即注册" class="greenbtn font_bold_white" 
							onclick="return checkRegisterInfo(username.value,password.value,repassword.value,captcha.value,agree.value)">
				</form>
			</div>
		</div>
	</div>

	<!-- 尾部-->
	<jsp:include page="layouts/footer.jsp" flush="true" />
</body>
</html>