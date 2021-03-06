<%@page import="java.util.ArrayList"%>
<%@page import="com.nuaa.model.OrderFormBean"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<%
	//获取订单表，并统计总金额
	String username = (String)session.getAttribute("username");
	List<OrderFormBean> orderBeanList=(ArrayList<OrderFormBean>)session.getAttribute(username+"_orderBeanList");
	String result = (String) request.getAttribute("result");
	
	float payTotal=0.0f; //需支付的总费用
	if(result.equals("SUCCESS"))
	{
		for(int i=0;i<orderBeanList.size();i++)
		{
			OrderFormBean orderBean = orderBeanList.get(i);
			payTotal += orderBean.getOtotalbooksprice() + orderBean.getOtotaltransprice();	
		}
	}
%>	
		
<div class="paymain">

	<jsp:include page="step_bar.jsp" flush="true"/>
	
	
	<!-- 支付主体 -->
	
	<% 
		if(result.equals("INVALID") || orderBeanList == null || orderBeanList.size() == 0)
		{
			%>
				<div class="pay" style="height:260px;">
					<div class="cart_row_div font_bold_gray" style="margin-top:100px;">本次支付失败。请重新选购。</div><br>
					<a href="SearchClServlet?dowhat=searchByRand" class="green_a">热销书籍</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<a href='CartClServlet?dowhat=queryCart' class='green_a'>返回购物车</a>	
				</div>
			<%
		}
		else
		{
			%>
				<!-- 提示语 -->
				<div class="pay">
					<div class="cart_row_div font_bold_gray" style="margin-top:20;">您已成功提交订单！</div><br>
					<div class="cart_row_div font_bold_gray" style="margin-top:0;">现可继续支付，以下是您的支付信息。</div><br>
					
					<!-- 支付信息 -->
					<table>
						<tr>
							<td>支付金额：<font class="font_bold_green" style="font-size:25px;">￥<%=String.format("%.2f",payTotal) %></font></td>
							<td>收款方：<font class="font_normal_green">书丛网</font></td>
							<td>类型：<font class="font_normal_green">订单</font></td>
						</tr>
					</table>
				</div>
				
				<!-- 下部分的支付 -->
				<div class="cart_footer" style="float:right;">
					<a href="PayClServlet?dowhat=payOrderAtOnce" class="settlebtn">立即支付</a>
				</div>
			<%
		}
	%>
</div>