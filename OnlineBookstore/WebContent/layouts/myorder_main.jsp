<%@page import="com.nuaa.model.BookInCartBean"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="com.nuaa.model.OrderFormBean"%>
<%@page import="com.nuaa.util.C"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>

<%
	//获取订单列表（包括订单本身信息+订单中的书籍信息）
	List<OrderFormBean> orderList = (ArrayList<OrderFormBean>) request.getAttribute("orderList");
%>

<div id="inner">

	<!-- 左侧导航栏 -->
	<jsp:include page="left.jsp" flush="true"><jsp:param name="curSelected" value="myorder" /></jsp:include>

	<!-- 右侧内容 -->	
	<div class="right">
			<div class="divline" style="font-size:15px;">我的订单</div>
			
			<div class="contentline">
			
			<%
				if(orderList == null || orderList.size() == 0) //一个订单也没有订单
				{
					%>
					<div class='emptyorder_div'>
						<span class='font_bold_gray'>一份订单记录也没有哦~快去其它地方逛逛吧!</span><br><br>
                		<a href='SearchClServlet?dowhat=searchByRand' class='greenbutton'>热销书籍</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
               	 		<a href="SearchClServlet?dowhat=searchByKey&way=byPageFirst&key=&refer=Bid&pageNow=1&pageCount=1&pageFrom=1&pageTo=1" class="greenbutton">随便看看</a>
            		</div>
					<%
				}
				else
				{
					%>
					<div class="curorder_div">
					<%
					for(int i=0;i<orderList.size();i++)
					{
						OrderFormBean order = orderList.get(i);
						List<BookInCartBean> bookList = order.getBookInCartBeanList();
						%>
						<div class="curorder_row_div" style="margin-bottom:20px; margin-top:20px;">
					
							<!-- 订单号 -->
							<span class="font_bold_green" style="text-decoration:inherit;">订单号：<span><%=order.getOid()%></span></span>		
						
							<!-- 店铺信息 -->
							<div class="curorder_row_saler_div">
								<img src="<%=order.getSicon() %>"/><span><%=order.getSname()%>配送</span>
								<div>
									<span>运费:￥<font><%=order.getOtotaltransprice() %></font></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<span>小计总额:￥<%=order.getOtotalbooksprice()%></span>
								</div>
							</div>
							
							<!-- 订单信息 -->
							<table>
								<tr>
									<td>商品信息</td>
									<td>实付款</td>
									<td>订单信息</td>
									<td>操作时间</td>
									<td>状态</td>
									<td>操作</td>
								</tr>
								<tr>
									<!-- 所有商品的图片缩略图 -->
									<td>
										<ul class="orderul">
										<%
											for(int j=0;j<bookList.size();j++)
											{
												BookInCartBean book = bookList.get(j);
												%>
												<li><a href="DetailClServlet?dowhat=findDetail&Bid=<%=book.getBid()%>" target="_blank"><img src="<%=book.getBimage() %>"/></a></li>
												<%
											}
										%>
										</ul>
									</td>
									<td>￥<%=String.format("%.2f",order.getOtotalbooksprice() + order.getOtotaltransprice()) %></td>
									<td>
										网上支付
									</td>
									
									<%
									if(order.getOstatus() == C.ORDERSTATUS_WAITPAY) //支付
									{
										%>
										<td><%=order.getOsubmittime()%></td>
										<td><span>等待支付</span></td>
										<td>
											<a href="MyOrderClServlet?dowhat=locateOrder&Oid=<%=order.getOid() %>" class="greenbutton">支付</a><br>
											<a href="javascript:void(0)" class="green_a">修改</a><br>
											<a href="MyOrderClServlet?dowhat=locateOrder&Oid=<%=order.getOid() %>" class="green_a">查看</a><br>
											<a href="javascript:void(0)" class="green_a" onclick="deleteOrder(this,'<%= order.getOid()%>')">删除</a>
										</td>
										<%
									}
									else if(order.getOstatus() == C.ORDERSTATUS_CONFIRMRECEIVED) //确认收货
									{
										%>
										<td><%=order.getOpaytime()%></td>
										<td><span>确认收货</span></td>
										<td>
											<a href="MyOrderClServlet?dowhat=locateOrder&Oid=<%=order.getOid() %>" class="greenbutton">收货</a><br>
											<a href="MyOrderClServlet?dowhat=locateOrder&Oid=<%=order.getOid() %>" class="green_a">查看</a><br>
											<a href="javascript:void(0)" class="green_a" onclick="deleteOrder(this,'<%= order.getOid()%>')">删除</a>
										</td>
										<%
									}
									else if(order.getOstatus() == C.ORDERSTATUS_WAITCOMMENT) //评价（只要有一本书没有评价，均会显示）
									{
										%>
										<td><%=order.getOreceivetime()%></td>
										<td><span>等待评价</span></td>
										<td>
											<a href="MyOrderClServlet?dowhat=locateOrder&Oid=<%=order.getOid() %>" class="greenbutton">评价</a><br>
											<a href="MyOrderClServlet?dowhat=locateOrder&Oid=<%=order.getOid() %>" class="green_a">查看</a><br>
											<a href="javascript:void(0)" class="green_a" onclick="deleteOrder(this,'<%= order.getOid()%>')">删除</a>
										</td>
										<%
									}
									else if(order.getOstatus() == C.ORDERSTATUS_FINISHED) //完成
									{
										%>
										<td><%=order.getOfinishedtime()%></td> <!-- 最后一本书评价的时间 -->
										<td><span>交易完成</span></td>
										<td>
											<a href="MyOrderClServlet?dowhat=locateOrder&Oid=<%=order.getOid() %>" class="green_a">查看</a><br>
											<a href="javascript:void(0)" class="green_a" onclick="deleteOrder(this,'<%= order.getOid()%>')">删除</a>
										</td>
										<%								
									}
									%>
								</tr>
								</table>
						</div>
						<%	
					}
					%>
					</div>
					<%
				}
			%>
			</div>
			
			<jsp:include page="myorder_pagebar.jsp" flush="true"/>
	</div>
</div>