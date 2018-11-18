package com.nuaa.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.nuaa.model.OrderFormBean;
import com.nuaa.model.OrderFormBeanCl;
import com.nuaa.model.TransactionBeanCl;
import com.nuaa.util.C;

@WebServlet("/PayClServlet")
public class PayClServlet extends HttpServlet {
	
	/**
	 * 处理与支付相关请求
	 * 点击支付，改变Transaction和OrderForm表的状态位等信息
	 */
	private static final long serialVersionUID = 1L;

	public PayClServlet() {
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
			if(session.getAttribute(C.PAY_TOKEN) != null && dowhat.equals("payOrderAtOnce")) //点击“立即支付”，且不是刷新情况
			{	
				session.removeAttribute(C.PAY_TOKEN);

				List<OrderFormBean> orderBeanList = (ArrayList<OrderFormBean>) session.getAttribute(username + "_orderBeanList");
				if(orderBeanList != null && orderBeanList.size() > 0)
				{
					//更改所有订单的状态为:已支付
					OrderFormBeanCl orderFormBeanCl = new OrderFormBeanCl();
					orderFormBeanCl.modifyOrderListStatusByOids(orderBeanList, C.ORDERSTATUS_CONFIRMRECEIVED);
					
					//更改所有订单中书籍的状态为：等待收货（已支付）
					TransactionBeanCl transactionBeanCl = new TransactionBeanCl();
					transactionBeanCl.modifyTradeStatusByOids(orderBeanList, C.TRADESTATUS_CONFIRMRECEIVED);
					
					//清空session中的订单表
					clearOrderListInSession(username,session);
					request.setAttribute("result", "SUCCESS"); //成功标记
				}
				else
					request.setAttribute("result", "INVALID"); //失败标记
			}
			else if(dowhat.equals("payOrderLaterOn")) //点击“支付”（并不是立即支付的情况，而是从订单进入稍后支付的那种）
			{
				String oid=request.getParameter("Oid"); //要支付的订单号
				
				//更改oid订单的状态为:已支付（等待收货）
				OrderFormBeanCl orderFormBeanCl = new OrderFormBeanCl();
				orderFormBeanCl.modifyOrderStatusByOid(oid, C.ORDERSTATUS_CONFIRMRECEIVED);
				
				//更改oid订单中书籍的状态为：已支付(等待收货)
				TransactionBeanCl transactionBeanCl = new TransactionBeanCl();
				transactionBeanCl.modifyTradeStatusByOid(oid, C.TRADESTATUS_CONFIRMRECEIVED);
				
				request.setAttribute("result", "SUCCESS"); //成功标记
			}
			else
				request.setAttribute("result", "INVALID"); //失败标记
			
			//跳转到收货提示界面
			request.getRequestDispatcher("paysuccess_page.jsp").forward(request, response);
			return;	
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	//删除存在session中的临时订单表
	void clearOrderListInSession(String username,HttpSession session)
	{
		if(username != null)
			session.removeAttribute(username+"_orderBeanList"); //删除临时的订单表
	}
}
