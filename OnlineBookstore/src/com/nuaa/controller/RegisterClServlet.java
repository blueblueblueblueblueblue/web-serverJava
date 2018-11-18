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
	 *  ע�ᴦ��
	 */
	private static final long serialVersionUID = 1L;

	public RegisterClServlet() {
		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(true);
		String curPageUrl=new CookieUtil().getCookie(request, "curPageUrl").getValue(); // ��ȡ��¼�ɹ���Ҫ���ص�url���·��

		if (session.getAttribute(C.REGISTER_TOKEN) != null) 
		{
			session.removeAttribute(C.REGISTER_TOKEN);// ɾ��token���

			String username=request.getParameter("username");
			String password=request.getParameter("password");
			
			// ����û����Ƿ��ظ��������������User��
			UserBeanCl userBeanCl = new UserBeanCl();
			if (userBeanCl.add(new UserBean(username, password)) == RegisterResultEnum.SUCCESS) //δ�ظ�
			{
				session.setAttribute("username", username); //����session�Ա�������¼��
				session.setAttribute("info", "mail");
				session.setMaxInactiveInterval(C.SESSION_MAX_INTERVAL);// ��¼�δ��ӦʱЧΪ3min
				request.removeAttribute("result");
				request.removeAttribute("password");
				request.getRequestDispatcher("mail_info.jsp").forward(request, response);
				return;
			}
			else
			{
				request.setAttribute("result",RegisterResultEnum.USERNAME_DUPLICATED); //���ظ�ע������û����Ѵ�����Ϣ
				request.setAttribute("password", password);//��������Ϣ����ȥ���Ա��û��������䣬�Զ���ʾ
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
