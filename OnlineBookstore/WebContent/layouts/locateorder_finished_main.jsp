<%@page import="com.nuaa.model.OrderFormBean"%>
<%@page import="com.nuaa.model.AddressBean"%>
<%@page import="com.nuaa.model.BookInCartBean"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%
	//获取订单
	OrderFormBean orderBean = (OrderFormBean)request.getAttribute("orderBean");
%>

<div class="queryordermain">
	<jsp:include page="step_bar.jsp" flush="true"/>
	<div style="text-align: center"> <!-- 步骤对应的时间 -->
		<div class="steptime"></div>
		<div class="steptime"><%=orderBean.getOsubmittime()%></div>
		<div class="steptime"><%=orderBean.getOpaytime()%></div>
		<div class="steptime"><%=orderBean.getOreceivetime()%></div>
		<div class="steptime"><%=orderBean.getOfinishedtime()%></div>
		<div class="steptime"></div>
	</div>
	
	<ul class="orderinfo_ul">

		<jsp:include page="locateorder_address.jsp" flush="true"/>

		<li>
			<span class="font_bold_black">商品清单：</span><br>
			
			
			<div class="curorder_div" style="background: white; margin-left:20px;">
			
				<div class="curorder_row_div">
				
					<!-- 店铺信息 -->
					<div class="curorder_row_saler_div">
						<img src="<%=orderBean.getSicon() %>"/><span><%=orderBean.getSname() %>配送</span>
						<div>
							<span>运费:￥<font><%=orderBean.getOtotaltransprice() %></font></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<span>小计总额:<font class="font_bold_green">￥<font><%=orderBean.getOtotalbooksprice() %></font></font></span>
						</div>
					</div>
					
					<!-- 订单信息 -->
					<table>
						<tr>
							<td>商品信息</td>
							<td>原价</td>
							<td>单价</td>
							<td>数量</td>
							<td>小计</td>
						</tr>
						<% 
						List<BookInCartBean> bookList = orderBean.getBookInCartBeanList();
						for(int j=0;j<bookList.size();j++)
						{
							BookInCartBean book=bookList.get(j);
						%>
							<tr>
								<td><a href="DetailClServlet?dowhat=findDetail&Bid=<%=book.getBid()%>" target="_blank" class="book_snap"><img src="<%=book.getBimage() %>"/></a><span class="book_name"><%=book.getBname() %></span></td>
								<td><del>￥<%=book.getBoriprice() %></del></td>
								<td>￥<%=book.getBprice() %></td>
								<td><%=book.getTboughtnum() %></td>
								<td>￥<span><%=String.format("%.2f",book.getBprice() * book.getTboughtnum()) %></span></td>
							</tr>
						<%
						}
						%>
					</table>
				</div>
			</div>			
		</li>
	</ul>
	
</div>