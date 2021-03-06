<%@page import="com.nuaa.util.C"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="css/mystyle.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="js/jquery/jquery.js"></script>

<title>书籍详情</title>
</head>
<body>
	<%
		//token标记(添加至购物车页面提交后，如果刷新会重复提交表单)
		session.setAttribute(C.DETAIL_TOKEN, true);
	%>

	<!-- 导航条 -->
	<jsp:include page="layouts/navi.jsp" flush="true"/>

	<!-- 头部 -->
	<jsp:include page="layouts/header.jsp" flush="true"/>

	<!-- 主体-->
	<jsp:include page="layouts/detail_main.jsp" flush="true"/>

	<!-- 尾部-->
	<jsp:include page="layouts/footer.jsp" flush="true"/>

<script type="text/javascript" src="js/public.js"></script>
</body>
</html>