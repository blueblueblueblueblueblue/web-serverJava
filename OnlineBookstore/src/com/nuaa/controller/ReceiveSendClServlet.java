package com.nuaa.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.nuaa.model.BookBeanCl;
import com.nuaa.model.OrderFormBeanCl;
import com.nuaa.model.TransactionBeanCl;
import com.nuaa.util.C;

@WebServlet("/ReceiveSendClServlet")
public class ReceiveSendClServlet extends HttpServlet {
	
	/**
	 * 处理与收货、发货相关请求
	 * 买家点击确认收货，改变Transaction和OrderForm表的状态位等信息
	 * 卖家点击确认发货，改变Transaction和OrderForm表的状态位等信息*******暂不实现
	 */
	private static final long serialVersionUID = 1L;

	public ReceiveSendClServlet() {
        super();
    }
	
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//先验证用户是否登录
		HttpSession session=request.getSession(true);
		String username=(String) session.getAttribute("username");
		String dowhat = request.getParameter("dowhat");//获取操作类型
		
		if(username == null) //未登录，跳转至登录界面
		{
			request.getRequestDispatcher("login_page.jsp").forward(request, response);
			return;
		}
		else //已登录
		{	
			if(dowhat.equals("confirmReceived")) //确认收货
			{
				//获取要确认收货的订单
				String oid=request.getParameter("Oid"); //要支付的订单号
				
				//更改oid订单的状态为:已确认收货（等待评价）
				OrderFormBeanCl orderFormBeanCl = new OrderFormBeanCl();
				orderFormBeanCl.modifyOrderStatusByOid(oid, C.ORDERSTATUS_WAITCOMMENT);
				
				//更改oid订单中书籍的状态为：已确认收获(等待评价)
				TransactionBeanCl transactionBeanCl = new TransactionBeanCl();
				transactionBeanCl.modifyTradeStatusByOid(oid, C.TRADESTATUS_WAITCOMMENT);
				
				//更改oid订单中书籍的成交量+1
				BookBeanCl bookBeanCl = new BookBeanCl();
				bookBeanCl.incSaledNum(oid);
				
				//转发
				//自动定位到查看订单（评价状态）页面
				request.getRequestDispatcher("MyOrderClServlet?dowhat=locateOrder&Oid="+oid).forward(request, response);	
			}
			else if(dowhat.equals("confirmSent")) //确认发货
			{
				
			}
				
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
