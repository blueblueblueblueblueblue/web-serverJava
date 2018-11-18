<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="com.nuaa.util.C"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="css/mystyle.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="js/public.js"></script>
<title>个人信息</title>
</head>
<body>

	<!-- 导航条 -->
	<jsp:include page="layouts/navi.jsp" flush="true"/>

	<!-- 头部 -->
	<jsp:include page="layouts/header.jsp" flush="true"/>

	<!-- 主体-->
	<jsp:include page="layouts/myinfo_main.jsp" flush="true"/>

	<!-- 尾部-->
	<jsp:include page="layouts/footer.jsp" flush="true"/>
	
	<script type="text/javascript" src="js/public.js"></script>
	<script src="js/myinfo_main.js"></script>
</body>
</html>