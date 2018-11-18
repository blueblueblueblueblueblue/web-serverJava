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

import com.nuaa.model.AddressBean;
import com.nuaa.model.AddressBeanCl;
import com.nuaa.model.BookInCartBean;
import com.nuaa.model.OrderFormBean;
import com.nuaa.model.OrderFormBeanCl;
import com.nuaa.util.C;

@WebServlet("/MyOrderClServlet")
public class MyOrderClServlet extends HttpServlet {
	
	/**
	 * 处理与"我的订单"相关请求（修改+查看+删除+（支付、确认收货、评价））
	 */
	private static final long serialVersionUID = 1L;

	public MyOrderClServlet() {
        super();
    }

	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		//先验证用户是否登录
		HttpSession session=request.getSession(true);
		String username=(String) session.getAttribute("username");
		String dowhat = request.getParameter("dowhat");//获取操作类型
		
		//清空session中的临时订单表
		clearOrderListInSession(username,session);
		
		if(username == null) //未登录，跳转至登录界面
		{
			request.getRequestDispatcher("login_page.jsp").forward(request, response);
			return;
		}
		else //已登录
		{
			if(dowhat.equals("queryOrder")) //仅为了查看订单
			{
				//获取查询方式、页码等参数
				String way = (String)request.getParameter("way");
				int pageCount=Integer.parseInt((request.getParameter("pageCount")));
				int pageNow=Integer.parseInt(request.getParameter("pageNow"));
				int pageFrom=Integer.parseInt(request.getParameter("pageFrom"));
				int pageTo=Integer.parseInt(request.getParameter("pageTo"));
				
				//进一步判断是那种查询方式(点击我的订单？按页码？)
				OrderFormBeanCl orderFormBeanCl = new OrderFormBeanCl();
				if(way.equals("byPageFirst")) //点击我的订单（证明是第一次，获取要分的总页数）
				{
					pageCount=orderFormBeanCl.getOrderPageCountByUid(username);
				}
				else if(way.equals("byPageLast")) //点击上一页
				{
					pageFrom = pageNow == pageFrom ? pageFrom-1 : pageFrom;
					pageNow--;
				}
				else if(way.equals("byPageNext")) //点击下一页
				{
					pageFrom = pageNow == pageTo ? pageFrom+1 : pageFrom;
					pageNow++;
				}
				else if(way.equals("byPageJump")) //点击跳转
				{
					pageFrom = pageFrom+pageNow-pageTo < 1 ? 1 : pageFrom+pageNow-pageTo;
				}
				pageTo = pageCount <= C.pageRange ? pageCount : pageFrom+C.pageRange-1;
				
				
				//提取该用户的历史所有订单（基本信息）
				ArrayList<OrderFormBean> orderList = orderFormBeanCl.findOrderFormsByUidByPage(username,pageNow);

				//提取订单中的书籍列表信息
				orderFormBeanCl.findBookListInOrderList(orderList);

				//转发
				request.setAttribute("pageCount", pageCount);
				request.setAttribute("pageNow", pageNow);
				request.setAttribute("pageFrom", pageFrom);
				request.setAttribute("pageTo", pageTo);
				request.setAttribute("orderList", orderList);
				request.getRequestDispatcher("myorder_page.jsp").forward(request, response);
				return;
			}
			else if(dowhat.equals("locateOrder")) //定位订单的当前状态所在的页面（查看订单详情）
			{
				String oid = (String)request.getParameter("Oid"); //订单号
				
				//查订单，通过Oid
				OrderFormBeanCl orderFormBeanCl = new OrderFormBeanCl();
				OrderFormBean orderBean = orderFormBeanCl.findOrderFormByOid(oid); //取订单基本信息
				orderFormBeanCl.findBookListInOrder(orderBean); //取订单中的书籍列表

				//查地址，通过order.Aid
				AddressBeanCl addressBeanCl = new AddressBeanCl();
				AddressBean addressBean = addressBeanCl.getAddressByAid(orderBean.getAid());
				
				//转发
				String locateOrderUrl=""; //转发到的页面的url
				request.setAttribute("orderBean", orderBean);
				request.setAttribute("addressBean", addressBean);
				switch(orderBean.getOstatus()) //根据订单Ostatus定位到合适的页面（支付、收货、评价、查看）
				{
					case C.ORDERSTATUS_WAITPAY:locateOrderUrl="locateorder_pay_page.jsp";break; //等待支付状态页面
					case C.ORDERSTATUS_CONFIRMRECEIVED:locateOrderUrl="locateorder_receive_page.jsp";break; //等待收货状态页面
					case C.ORDERSTATUS_WAITCOMMENT:locateOrderUrl="locateorder_comment_page.jsp";break; //等待评价状态页面
					case C.ORDERSTATUS_FINISHED:locateOrderUrl="locateorder_finished_page.jsp";break; //彻底完成页面
				}

				request.getRequestDispatcher(locateOrderUrl).forward(request, response);
			}
			else if(dowhat.equals("deleteOrder")) //删除订单
			{
				String oid=request.getParameter("Oid"); //要删的订单号
				OrderFormBeanCl orderFormBeanCl = new OrderFormBeanCl();
				orderFormBeanCl.deleteOrder(oid);

				return;
			}
			else if(dowhat.equals("modifyOrder")) //修改订单(暂不实现%%%%%%%%%%%%%%%%)
			{
				
			}
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
