<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
	String result = (String) request.getAttribute("result");
%>
	
<div class="paymain">

	<jsp:include page="step_bar.jsp" flush="true"/>
	
	<div class="pay" style="height:260px;">
	<%
		if(result.equals("SUCCESS"))
		{
	%>
			<div class="cart_row_div font_bold_gray" style="margin-top:75px;">支付成功！请等待店家发货。</div><br>
	<%
		}
		else
		{
			%>
			<div class="cart_row_div font_bold_gray" style="margin-top:75px;">请勿重复支付。</div><br>
			<%
		}
	%>
				
		<div class="cart_row_div font_bold_gray" style="margin-top:0px;">您可到“我的订单”中查看发货情况。</div><br>
		<a href="SearchClServlet?dowhat=searchByRand" class="greenbutton">热销书籍</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<a href='CartClServlet?dowhat=queryCart' class='greenbutton'>返回购物车</a>	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<a href='MyOrderClServlet?dowhat=queryOrder&way=byPageFirst&pageCount=1&pageNow=1&pageFrom=1&pageTo=1' class='greenbutton'>我的订单</a>	
	</div>
</div>
