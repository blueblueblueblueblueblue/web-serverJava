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

import com.nuaa.model.BookInCartBean;
import com.nuaa.util.C;

@WebServlet("/CartClServlet")
public class CartClServlet extends HttpServlet{
    
	/**
	 * 点击加入购物车，进入购物车界面时的处理
	 */
	private static final long serialVersionUID = 1L;

	public CartClServlet() {
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
			clearOrderListInSession(username,session);
			if(session.getAttribute(C.DETAIL_TOKEN) != null)
			{
				session.removeAttribute(C.DETAIL_TOKEN);

				if(dowhat.equals("addBookIntoCart")) //点击添加至购物车按钮时
				{
					//获取session中的购物篮BookInCartBeanList表
					ArrayList<BookInCartBean> bookInCartBeanList=(ArrayList<BookInCartBean>) session.getAttribute(username+"_bookInCartBeanList");
					if(bookInCartBeanList == null) //购物框为空，第一次
						bookInCartBeanList=new ArrayList<BookInCartBean>();
					
					//根据Bid检查该书是否已在购物框
					String Bid=request.getParameter("Bid");
					int i;
					for(i=0;i<bookInCartBeanList.size();i++)
					{
						BookInCartBean bookInCartBean=bookInCartBeanList.get(i);
						if(Bid.equals(bookInCartBean.getBid())) //已存在，旧的，改变成数量+1
						{
							int tboughtnum=bookInCartBean.getTboughtnum();
							bookInCartBean.setTboughtnum(++tboughtnum);
							break;
						}
					}
					if(i == bookInCartBeanList.size()) //不存在，新的,封装并添加
					{
						//将本次欲加入购物车的书籍信息封装成BookInCartBean
						BookInCartBean bookInCartBean=new BookInCartBean(Bid,request.getParameter("Bimage"), 
								Float.parseFloat(request.getParameter("Bprice")),request.getParameter("Bname"),Integer.parseInt(request.getParameter("Sid")),
								request.getParameter("Sicon"),request.getParameter("Sname"),Float.parseFloat(request.getParameter("Boriprice")),
								Integer.parseInt(request.getParameter("Tboughtnum")));	
						
						 //添加至购物框
						bookInCartBeanList.add(bookInCartBean);
					}				

					//转发
					session.setAttribute(username+"_bookInCartBeanList", bookInCartBeanList);	//更新session(注意!!是username+_bookInCartBeanList这个账户的)
					request.getRequestDispatcher("cart_page.jsp").forward(request, response);
					return;
				}
			}
			else //提交ajax请求 || 一味刷新导致重复提交
			{
				List<BookInCartBean> bookInCartBeanList=(List<BookInCartBean>) session.getAttribute(username+"_bookInCartBeanList");
				
				if(dowhat.equals("checkABookInCart")) //ajax请求选中某本书
				{
					int index=Integer.parseInt(request.getParameter("index")); //购物车中第几本书要做改变
					boolean checked=Boolean.parseBoolean(request.getParameter("checked")); //改变成几本
					
					bookInCartBeanList.get(index).setChecked(checked);
					return;
				}
				else if(dowhat.equals("checkAllBooksInCart")) //ajax请求选中所有书
				{
					boolean checked=Boolean.parseBoolean(request.getParameter("checked")); //改变成几本
					if(bookInCartBeanList != null)
						for(int index=0;index<bookInCartBeanList.size();index++)
							bookInCartBeanList.get(index).setChecked(checked);
					return;
				}
				else if(dowhat.equals("updateBoughtNumInCart")) //ajax请求改变书籍数目
				{
					int index=Integer.parseInt(request.getParameter("index")); //购物车中第几本书要做改变
					int boughtNum=Integer.parseInt(request.getParameter("boughtNum")); //改变成几本
					
					bookInCartBeanList.get(index).setTboughtnum(boughtNum);
					return;
				}
				else if(dowhat.equals("delABookInCart")) //ajax请求删除购物车某本书
				{
					int index=Integer.parseInt(request.getParameter("index")); //购物车中第几本书要做改变
					
					if(bookInCartBeanList != null && bookInCartBeanList.size() > 0)
						bookInCartBeanList.remove(index);
					return;
				}
				else if(dowhat.equals("delAllBooksInCart")) //ajax请求清空所有书籍
				{
					if(bookInCartBeanList != null)
						bookInCartBeanList.clear();
					return;
				}
			}
			
			//重复刷新提交、或在导航栏直接点击购物篮（均属于浏览性质，而非操作性质）
			request.getRequestDispatcher("cart_page.jsp").forward(request, response);
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
