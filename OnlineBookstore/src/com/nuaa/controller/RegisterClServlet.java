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
import com.nuaa.util.RegisterResultEnum;

@WebServlet("/RegisterClServlet")
public class RegisterClServlet extends HttpServlet {

	/**
	 *  注册处理
	 */
	private static final long serialVersionUID = 1L;

	public RegisterClServlet() {
		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(true);
		String curPageUrl=new CookieUtil().getCookie(request, "curPageUrl").getValue(); // 获取登录成功后要返回的url相对路径

		if (session.getAttribute(C.REGISTER_TOKEN) != null) 
		{
			session.removeAttribute(C.REGISTER_TOKEN);// 删除token标记

			String username=request.getParameter("username");
			String password=request.getParameter("password");
			
			// 检查用户名是否重复，若不则添加至User表
			UserBeanCl userBeanCl = new UserBeanCl();
			if (userBeanCl.add(new UserBean(username, password)) == RegisterResultEnum.SUCCESS) //未重复
			{
				session.setAttribute("username", username); //放入session以备持续登录用
				session.setAttribute("info", "mail");
				session.setMaxInactiveInterval(C.SESSION_MAX_INTERVAL);// 登录最长未响应时效为3min
				request.removeAttribute("result");
				request.removeAttribute("password");
				request.getRequestDispatcher("mail_info.jsp").forward(request, response);
				return;
			}
			else
			{
				request.setAttribute("result",RegisterResultEnum.USERNAME_DUPLICATED); //返回给注册界面用户名已存在信息
				request.setAttribute("password", password);//把密码信息传回去，以便用户不用再输，自动显示
				request.getRequestDispatcher("register_page.jsp").forward(request, response);
				return;
			}
		}
		else
		{
			request.getRequestDispatcher(curPageUrl).forward(request, response);
			return;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doGet(request, response);
	}

}
