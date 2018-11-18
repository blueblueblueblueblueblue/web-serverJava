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
	 * �����¼���˳�
	 */
	private static final long serialVersionUID = 1L;

	public LoginLogoutClServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(true);
		String dowhat=request.getParameter("dowhat");
		String curPageUrl=new CookieUtil().getCookie(request, "curPageUrl").getValue(); // ��ȡ��¼�ɹ���Ҫ���ص�url���·��

		if(dowhat.equals("logout")) //ע���˳�
		{
			clearOrderListInSession(session);//��ɾ��������
			session.removeAttribute("username");
			request.getRequestDispatcher(curPageUrl).forward(request, response);
			return;
		}
		else if(session.getAttribute(C.LOGIN_TOKEN) != null) //��¼
		{
			session.removeAttribute(C.LOGIN_TOKEN);// ɾ��token���
			
			String username = request.getParameter("username");
			String password = request.getParameter("password");

			// ���User�����Ƿ��и��û�����Ӧ����
			UserBeanCl userBeanCl = new UserBeanCl();
			LoginResultEnum result = userBeanCl.checkLogin(new UserBean(username,password));
			if (result == LoginResultEnum.SUCCESS) // ��¼�ɹ�
			{
				session.setAttribute("username", username); // ����session�Ա�������¼��
				session.setMaxInactiveInterval(C.SESSION_MAX_INTERVAL);// ��¼�δ��ӦʱЧΪ3min
				request.removeAttribute("result");
				request.getRequestDispatcher(curPageUrl).forward(request, response);
				return;
			} 
			else if (result == LoginResultEnum.USERNAME_UNEXISTED) // �û���������
			{
				request.setAttribute("result", LoginResultEnum.USERNAME_UNEXISTED);
				request.getRequestDispatcher("login_page.jsp").forward(request, response);
				return;
			} 
			else if (result == LoginResultEnum.PASSWORD_ERROR) // ����������δ����
			{
				request.setAttribute("result", LoginResultEnum.PASSWORD_ERROR);
				request.getRequestDispatcher("login_page.jsp").forward(request, response);
				return;
			}
		}
		else //ˢ��
		{
			request.getRequestDispatcher(curPageUrl).forward(request, response);
			return;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}
	
	//ɾ������session�е���ʱ������
	void clearOrderListInSession(HttpSession session)
	{
		String username=(String)session.getAttribute("username");
		if(username != null)
			session.removeAttribute(username+"_orderBeanList"); //ɾ����ʱ�Ķ�����	
	}
}
