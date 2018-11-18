<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.nuaa.model.BookSnapshotBean"%>

<%
	ArrayList<BookSnapshotBean> bookSnapshotList=(ArrayList<BookSnapshotBean>)request.getAttribute("bookSnapshotList");
	if(bookSnapshotList.size() == 0) //未搜索到记录
	{
		%>
			<!-- 显示未搜索到界面 -->
			<div class="searchmain">暂时还没有您想要的书籍诶...</div>
		<%	
	}
	else
	{
		%>
			<div class="searchmain">
				<!-- 排序条+根据检索得到的pageCount，pageNow生成翻页块 -->
				<jsp:include page="orderbar.jsp" flush="true"/>
			
				<!--书籍快照信息-->
				<jsp:include page="searchshow.jsp" flush="true"/>
			
				<!-- 根据检索后得到的pageCount,pageNow,产生主分页 -->
				<jsp:include page="search_pagebar.jsp" flush="true"/>
			</div>
		<%
	}

%>