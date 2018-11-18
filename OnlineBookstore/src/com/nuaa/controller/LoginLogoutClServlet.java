package com.nuaa.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.nuaa.model.UserBean;
import com.nuaa.model.UserBeanCl;
import com.nuaa.util.C;
import com.nuaa.util.CookieUtil;
import com.nuaa.util.LoginResultEnum;

@WebServlet("/LoginLogoutClServlet")
public class LoginLogoutClServlet extends HttpServlet {

	/**
	 * 处理登录、退出
	 */
	private static final long serialVersionUID = 1L;

	public LoginLogoutClServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(true);
		String dowhat=request.getParameter("dowhat");
		String curPageUrl=new CookieUtil().getCookie(request, "curPageUrl").getValue(); // 获取登录成功后要返回的url相对路径

		if(dowhat.equals("logout")) //注销退出
		{
			clearOrderListInSession(session);//先删除订单表
			session.removeAttribute("username");
			request.getRequestDispatcher(curPageUrl).forward(request, response);
			return;
		}
		else if(session.getAttribute(C.LOGIN_TOKEN) != null) //登录
		{
			session.removeAttribute(C.LOGIN_TOKEN);// 删除token标记
			
			String username = request.getParameter("username");
			String password = request.getParameter("password");

			// 检查User表中是否有该用户及对应密码
			UserBeanCl userBeanCl = new UserBeanCl();
			LoginResultEnum result = userBeanCl.checkLogin(new UserBean(username,password));
			if (result == LoginResultEnum.SUCCESS) // 登录成功
			{
				session.setAttribute("username", username); // 放入session以备持续登录用
				session.setMaxInactiveInterval(C.SESSION_MAX_INTERVAL);// 登录最长未响应时效为3min
				request.removeAttribute("result");
				request.getRequestDispatcher(curPageUrl).forward(request, response);
				return;
			} 
			else if (result == LoginResultEnum.USERNAME_UNEXISTED) // 用户名不存在
			{
				request.setAttribute("result", LoginResultEnum.USERNAME_UNEXISTED);
				request.getRequestDispatcher("login_page.jsp").forward(request, response);
				return;
			} 
			else if (result == LoginResultEnum.PASSWORD_ERROR) // 密码错误或者未激活
			{
				request.setAttribute("result", LoginResultEnum.PASSWORD_ERROR);
				request.getRequestDispatcher("login_page.jsp").forward(request, response);
				return;
			}
		}
		else //刷新
		{
			request.getRequestDispatcher(curPageUrl).forward(request, response);
			return;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}
	
	//删除存在session中的临时订单表
	void clearOrderListInSession(HttpSession session)
	{
		String username=(String)session.getAttribute("username");
		if(username != null)
			session.removeAttribute(username+"_orderBeanList"); //删除临时的订单表	
	}
}
