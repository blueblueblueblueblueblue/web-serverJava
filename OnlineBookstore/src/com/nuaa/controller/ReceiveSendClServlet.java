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
	 * �������ջ��������������
	 * ��ҵ��ȷ���ջ����ı�Transaction��OrderForm���״̬λ����Ϣ
	 * ���ҵ��ȷ�Ϸ������ı�Transaction��OrderForm���״̬λ����Ϣ*******�ݲ�ʵ��
	 */
	private static final long serialVersionUID = 1L;

	public ReceiveSendClServlet() {
        super();
    }
	
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//����֤�û��Ƿ��¼
		HttpSession session=request.getSession(true);
		String username=(String) session.getAttribute("username");
		String dowhat = request.getParameter("dowhat");//��ȡ��������
		
		if(username == null) //δ��¼����ת����¼����
		{
			request.getRequestDispatcher("login_page.jsp").forward(request, response);
			return;
		}
		else //�ѵ�¼
		{	
			if(dowhat.equals("confirmReceived")) //ȷ���ջ�
			{
				//��ȡҪȷ���ջ��Ķ���
				String oid=request.getParameter("Oid"); //Ҫ֧���Ķ�����
				
				//����oid������״̬Ϊ:��ȷ���ջ����ȴ����ۣ�
				OrderFormBeanCl orderFormBeanCl = new OrderFormBeanCl();
				orderFormBeanCl.modifyOrderStatusByOid(oid, C.ORDERSTATUS_WAITCOMMENT);
				
				//����oid�������鼮��״̬Ϊ����ȷ���ջ�(�ȴ�����)
				TransactionBeanCl transactionBeanCl = new TransactionBeanCl();
				transactionBeanCl.modifyTradeStatusByOid(oid, C.TRADESTATUS_WAITCOMMENT);
				
				//����oid�������鼮�ĳɽ���+1
				BookBeanCl bookBeanCl = new BookBeanCl();
				bookBeanCl.incSaledNum(oid);
				
				//ת��
				//�Զ���λ���鿴����������״̬��ҳ��
				request.getRequestDispatcher("MyOrderClServlet?dowhat=locateOrder&Oid="+oid).forward(request, response);	
			}
			else if(dowhat.equals("confirmSent")) //ȷ�Ϸ���
			{
				
			}
				
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
