<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div class="navi bg_gray">
	<!-- 登录部分 -->
	<span class="span1 font_normal_gray">欢迎来到书丛网！
	<% 
		//判断用户是否已登录
		String username=(String)session.getAttribute("username");

		if(username == null) //未登录
		{
			%>
			<a href="login_page.jsp" target="_self">请登录</a> 
			<a href="register_page.jsp" target="_self">免费注册</a>
			<%
		}
		else //已登录，显示用户名和退出链接
		{
			%>
			您好，<a href="MyInfoClServlet?dowhat=queryMyInfo" target="_self" class="green_bold_a"><%=username %></a> 
			<a href="LoginLogoutClServlet?dowhat=logout">退出</a>
			<%	
		}
	%>
	</span>
	
	<!-- 后面连接部分 -->
	<span class="span2">
	<img src="css/images/home.jpg">
	<a href="SearchClServlet?dowhat=searchByRand">书丛首页</a>
	<img src="css/images/cart.jpg">
	<a href="CartClServlet?dowhat=queryCart">购物车</a>
	<img src="css/images/order.jpg">
	<a href="MyOrderClServlet?dowhat=queryOrder&way=byPageFirst&pageCount=1&pageNow=1&pageFrom=1&pageTo=1">个人中心</a>
	</span>	
</div>