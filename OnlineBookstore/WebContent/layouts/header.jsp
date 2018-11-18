<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
	
<%
	String keyWords=(String)request.getAttribute("key"); //获取上次的关键字放在输入框中
%>	
<div class="header bg_green">
	<ul>
		<li class="li1"><img src="css/images/logo.png" /></li>
		<li class="li2">
			<form action="SearchClServlet" method="get">
				<input type="text" name="key" autofocus="autofocus" class="search_edit" autocomplete="off" placeholder="书名/作者/内容/店家/出版社" <%if(keyWords != null && keyWords != ""){%> value=<%=keyWords%> <%}%>  > 
				<input type="submit" value="搜索" class="search_btn font_normal_white">
				<!-- 隐藏参数 -->
				<input type="hidden" name="dowhat" value="searchByKey">
				<input type="hidden" name="way" value="byPageFirst">
				<input type="hidden" name="refer" value="Bid">
				<input type="hidden" name="pageCount" value="1">
				<input type="hidden" name="pageNow" value="1">
				<input type="hidden" name="pageFrom" value="1">
				<input type="hidden" name="pageTo" value="1">
				
			</form>
			
		</li>
		
	</ul>
</div>