<%@page import="com.nuaa.util.C"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.nuaa.model.BookInCartBean"%>
<%
	//获取当前username的购物框（列表）
	String username=(String)session.getAttribute("username");
	ArrayList<BookInCartBean> bookInCartBeanList=(ArrayList<BookInCartBean>)session.getAttribute(username+"_bookInCartBeanList");
	boolean hasChecked=false; //是否有选中的商品
	boolean isCheckedAllBooks=true; //全选标志
%>	
	
<div class="cartmain">

	<jsp:include page="step_bar.jsp" flush="true"/>
	
	<!-- 购物车主体 -->
	<div class="cart">

		<%
		//判断购物篮有没有信息，没有则显示为空
		if(bookInCartBeanList == null || bookInCartBeanList.size() == 0)
		{
			%>
				<div class="cart_row_div font_bold_gray">购物车还是空的哟~快去书城逛逛吧!</div><br>
				<a href="SearchClServlet?dowhat=searchByRand" class="greenbutton">热销书籍</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="SearchClServlet?dowhat=searchByKey&way=byPageFirst&key=&refer=Bid&pageNow=1&pageCount=1&pageFrom=1&pageTo=1" class="greenbutton">随便看看</a>		
			<%
		}
		else
		{
		%>
			<!-- 表头 -->
			<div class="cart_title">
				<input type="checkbox" id="allbooksincart_checkbox" onclick="checkAllBooksInCartBox()"/>全选&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" id="deleteall" style="visibility:hidden;" onclick="deleteAllBooksInCart()">批量删除</a>
				<div style="width:44%;">商品信息</div>
				<div style="width:12%;">单价(元)</div>
				<div style="width:11%;">数量</div>
				<div style="width:12%;">小计</div>
				<div style="width:6.5%;">操作</div>
			</div>	
		<% 
			for(int i=0;i<bookInCartBeanList.size();i++)
			{
				BookInCartBean bookInCartBean=bookInCartBeanList.get(i);	
				%>
				<div class="cart_row_div">
					<div class="cart_saler_div">
						<img src="<%=bookInCartBean.getSicon() %>"/><span><%=bookInCartBean.getSname() %></span>
					</div>
					<div class="cart_value_div">
					
						<!-- 勾选 -->
						<% 
							if(bookInCartBean.isChecked() == true)
							{
								hasChecked=true;
							%>
								<input type="checkbox" name="bookincart_checkbox" value=<%=i%> checked="checked" onclick="checkBookInCartBox(this)"/>
							<% 
							}
							else
							{
								isCheckedAllBooks = false;
								%>
								<input type="checkbox" name="bookincart_checkbox" value=<%=i%> onclick="checkBookInCartBox(this)"/>
								<% 	
							}
						%>
						
						<!-- 图书及说明 -->
						<div style="width:52.5%;">
							<a href="DetailClServlet?dowhat=findDetail&Bid=<%=bookInCartBean.getBid()%>" target="_blank" class="detail_a"><img src="<%=bookInCartBean.getBimage() %>"/></a>
							<span style="float"><%=bookInCartBean.getBname() %></span>
						</div>
						
						<!-- 单价-->
						<div style="width:13%;">
							<del class="unitprice">￥<%=bookInCartBean.getBoriprice() %></del><br>
							<span>￥</span><span><%=bookInCartBean.getBprice() %></span>
						</div>
						
						<!-- 数量-->
						<div style="width:11%;">
							<input type="button" class="numbutton" value="-" onclick="subNum(this)">
							<input type="text" class="numedit" value=<%=bookInCartBean.getTboughtnum() %> onblur="editNum(this);" onfocus="getFocusedNum(this);">
							<input type="button" class="numbutton" value="+" onclick="addNum(this)">
						</div>
						
						<!-- 小计-->
						<div style="width:13%;">
							<span class="font_bold_green">￥</span><span class="font_bold_green"><%=String.format("%.2f", bookInCartBean.getTboughtnum()*bookInCartBean.getBprice())%></span>
						</div>
						
						<!-- 操作 -->
						<div style="width:6.5%;">
							<a href="javascript:void(0)" onclick="deleteABookInCart(this)" class="greenbutton" style="width:30px;position:relative;top:40px;">删除</a>
						</div>
					</div>
				</div>
				<%
			}
		}
	%>	
	</div>
	
	<!-- 下部分的结算 -->
	<div class="cart_footer">
		<div>已选商品<span class="font_bold_green">0</span>件</div>
		<div>合计（不含运费）:<span class="font_bold_green">￥</span><span class="font_bold_green"></span></div>
		<a class="settlebtn" href="OrderClServlet?dowhat=writeorder">结算</a>
	</div>
</div>
<script src="js/cart_main.js"></script>
<script>
$(document).ready(function(){
	
	//步骤条
	$(".cartmain .stepcircle").eq(0).css("background","#009966");
	$(".cartmain .stepname").eq(0).css("color","#009966");
	
	//如果选中了所有，则设置全选、批量删除
	if(<%=bookInCartBeanList != null%> && <%=isCheckedAllBooks%> == true){
		$('#allbooksincart_checkbox').attr('checked','checked');
		$('#deleteall').css("visibility","visible");
	}
	
	//
	if(<%=hasChecked%> == true)
		$('.cart_footer').show();
	else
		$('.cart_footer').hide();
	
	//计算合计
	updateTotal();

});
</script>
