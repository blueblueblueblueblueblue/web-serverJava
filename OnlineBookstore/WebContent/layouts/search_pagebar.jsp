<%@page import="com.nuaa.util.C"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<% 
	String dowhat=(String)request.getAttribute("dowhat");//当前书籍搜索方式
	
	String key=(String)request.getAttribute("key");//当前搜索的关键字
	String type=(String)request.getAttribute("type");//当前搜索的类别
	String refer=(String)request.getAttribute("refer");//当前排序参照列
	int pageCount=(Integer)request.getAttribute("pageCount"); //分页数
	int pageNow=(Integer)request.getAttribute("pageNow"); //当前页数
	int pageFrom=(Integer)request.getAttribute("pageFrom"); //页码起始数
	int pageTo=(Integer)request.getAttribute("pageTo"); //页码终止数
%>

<script type="text/javascript" src="js/checkJumpPageInfo.js"></script>

<div class="searchdiv2">
	<%
		if(pageCount > 0)
		{
			/******数字页码条******/
			
			//显示上一页
			if(pageNow > 1)
			{
				%>
				<a href="SearchClServlet?dowhat=<%=dowhat%>&key=<%=key%>&type=<%=type%>&way=byPageLast&refer=<%=refer%>&pageCount=<%=pageCount%>&pageNow=<%=pageNow%>&pageFrom=<%=pageFrom%>&pageTo=<%=pageTo%>">&lt;</a>
				<%
			}
			
			//显示页码
			for(int i=pageFrom;i<=pageTo;i++)
			{
				if(i == pageNow)
				{
					%>
						<a href="SearchClServlet?dowhat=<%=dowhat%>&key=<%=key%>&type=<%=type%>&way=byPageNow&refer=<%=refer%>&pageCount=<%=pageCount%>&pageNow=<%=i%>&pageFrom=<%=pageFrom%>&pageTo=<%=pageTo%>" class="aseleted"><%=i%></a>
					<%		
				}
				else
				{
					%>
						<a href="SearchClServlet?dowhat=<%=dowhat%>&key=<%=key%>&type=<%=type%>&way=byPageNow&refer=<%=refer%>&pageCount=<%=pageCount%>&pageNow=<%=i%>&pageFrom=<%=pageFrom%>&pageTo=<%=pageTo%>"><%=i%></a>
					<%
				}
			}		
		
			//显示下一页
			if(pageNow < pageCount)
			{
				%>	
				<a href="SearchClServlet?dowhat=<%=dowhat%>&key=<%=key%>&type=<%=type%>&way=byPageNext&refer=<%=refer%>&pageCount=<%=pageCount%>&pageNow=<%=pageNow%>&pageFrom=<%=pageFrom%>&pageTo=<%=pageTo%>">&gt;</a>
				<%
			}
			
			%>
	
			<!-- 直接输入跳转 -->	
			<span>&nbsp;&nbsp;共<%=pageCount%>页，到第</span>
			<form action="SearchClServlet?dowhat=<%=dowhat%>&key=<%=key%>&type=<%=type%>&way=byPageJump&refer=<%=refer%>&pageCount=<%=pageCount%>&pageFrom=<%=pageFrom %>&pageTo=<%=pageTo%>" method="post">
				<input type="text" class="searchdiv2edit" name="pageNow">	
				<span>页&nbsp;</span>
				<input type="submit" class="graybutton" onclick="return checkJumpPageInfo(pageNow.value,<%=pageCount%>)">	
			</form>
			<%
		}
	%>
	
</div>