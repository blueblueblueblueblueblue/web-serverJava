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
	 * ������֧���������
	 * ���֧�����ı�Transaction��OrderForm���״̬λ����Ϣ
	 */
	private static final long serialVersionUID = 1L;

	public PayClServlet() {
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
			if(session.getAttribute(C.PAY_TOKEN) != null && dowhat.equals("payOrderAtOnce")) //���������֧�������Ҳ���ˢ�����
			{	
				session.removeAttribute(C.PAY_TOKEN);

				List<OrderFormBean> orderBeanList = (ArrayList<OrderFormBean>) session.getAttribute(username + "_orderBeanList");
				if(orderBeanList != null && orderBeanList.size() > 0)
				{
					//�������ж�����״̬Ϊ:��֧��
					OrderFormBeanCl orderFormBeanCl = new OrderFormBeanCl();
					orderFormBeanCl.modifyOrderListStatusByOids(orderBeanList, C.ORDERSTATUS_CONFIRMRECEIVED);
					
					//�������ж������鼮��״̬Ϊ���ȴ��ջ�����֧����
					TransactionBeanCl transactionBeanCl = new TransactionBeanCl();
					transactionBeanCl.modifyTradeStatusByOids(orderBeanList, C.TRADESTATUS_CONFIRMRECEIVED);
					
					//���session�еĶ�����
					clearOrderListInSession(username,session);
					request.setAttribute("result", "SUCCESS"); //�ɹ����
				}
				else
					request.setAttribute("result", "INVALID"); //ʧ�ܱ��
			}
			else if(dowhat.equals("payOrderLaterOn")) //�����֧����������������֧������������ǴӶ��������Ժ�֧�������֣�
			{
				String oid=request.getParameter("Oid"); //Ҫ֧���Ķ�����
				
				//����oid������״̬Ϊ:��֧�����ȴ��ջ���
				OrderFormBeanCl orderFormBeanCl = new OrderFormBeanCl();
				orderFormBeanCl.modifyOrderStatusByOid(oid, C.ORDERSTATUS_CONFIRMRECEIVED);
				
				//����oid�������鼮��״̬Ϊ����֧��(�ȴ��ջ�)
				TransactionBeanCl transactionBeanCl = new TransactionBeanCl();
				transactionBeanCl.modifyTradeStatusByOid(oid, C.TRADESTATUS_CONFIRMRECEIVED);
				
				request.setAttribute("result", "SUCCESS"); //�ɹ����
			}
			else
				request.setAttribute("result", "INVALID"); //ʧ�ܱ��
			
			//��ת���ջ���ʾ����
			request.getRequestDispatcher("paysuccess_page.jsp").forward(request, response);
			return;	
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	//ɾ������session�е���ʱ������
	void clearOrderListInSession(String username,HttpSession session)
	{
		if(username != null)
			session.removeAttribute(username+"_orderBeanList"); //ɾ����ʱ�Ķ�����
	}
}
