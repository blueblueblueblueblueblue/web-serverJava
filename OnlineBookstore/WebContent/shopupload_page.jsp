<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="com.nuaa.util.C"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="css/mystyle.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="js/public.js"></script>
<title>书籍发布</title>
</head>
<body>

	<%	
		//token标记
		session.setAttribute(C.SHOPUPLOAD_TOKEN, true);
	%>

	<!-- 导航条 -->
	<jsp:include page="layouts/navi.jsp" flush="true"/>

	<!-- 头部 -->
	<jsp:include page="layouts/header.jsp" flush="true"/>

	<!-- 主体-->
	<jsp:include page="layouts/shopupload_main.jsp" flush="true"/>

	<!-- 尾部-->
	<jsp:include page="layouts/footer.jsp" flush="true"/>
	
	<script type="text/javascript" src="js/public.js"></script>
	<script src="js/shopupload_main.js"></script>
</body>
</html>