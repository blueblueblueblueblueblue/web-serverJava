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
	 * ������"�ҵĶ���"��������޸�+�鿴+ɾ��+��֧����ȷ���ջ������ۣ���
	 */
	private static final long serialVersionUID = 1L;

	public MyOrderClServlet() {
        super();
    }

	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		//����֤�û��Ƿ��¼
		HttpSession session=request.getSession(true);
		String username=(String) session.getAttribute("username");
		String dowhat = request.getParameter("dowhat");//��ȡ��������
		
		//���session�е���ʱ������
		clearOrderListInSession(username,session);
		
		if(username == null) //δ��¼����ת����¼����
		{
			request.getRequestDispatcher("login_page.jsp").forward(request, response);
			return;
		}
		else //�ѵ�¼
		{
			if(dowhat.equals("queryOrder")) //��Ϊ�˲鿴����
			{
				//��ȡ��ѯ��ʽ��ҳ��Ȳ���
				String way = (String)request.getParameter("way");
				int pageCount=Integer.parseInt((request.getParameter("pageCount")));
				int pageNow=Integer.parseInt(request.getParameter("pageNow"));
				int pageFrom=Integer.parseInt(request.getParameter("pageFrom"));
				int pageTo=Integer.parseInt(request.getParameter("pageTo"));
				
				//��һ���ж������ֲ�ѯ��ʽ(����ҵĶ�������ҳ�룿)
				OrderFormBeanCl orderFormBeanCl = new OrderFormBeanCl();
				if(way.equals("byPageFirst")) //����ҵĶ�����֤���ǵ�һ�Σ���ȡҪ�ֵ���ҳ����
				{
					pageCount=orderFormBeanCl.getOrderPageCountByUid(username);
				}
				else if(way.equals("byPageLast")) //�����һҳ
				{
					pageFrom = pageNow == pageFrom ? pageFrom-1 : pageFrom;
					pageNow--;
				}
				else if(way.equals("byPageNext")) //�����һҳ
				{
					pageFrom = pageNow == pageTo ? pageFrom+1 : pageFrom;
					pageNow++;
				}
				else if(way.equals("byPageJump")) //�����ת
				{
					pageFrom = pageFrom+pageNow-pageTo < 1 ? 1 : pageFrom+pageNow-pageTo;
				}
				pageTo = pageCount <= C.pageRange ? pageCount : pageFrom+C.pageRange-1;
				
				
				//��ȡ���û�����ʷ���ж�����������Ϣ��
				ArrayList<OrderFormBean> orderList = orderFormBeanCl.findOrderFormsByUidByPage(username,pageNow);

				//��ȡ�����е��鼮�б���Ϣ
				orderFormBeanCl.findBookListInOrderList(orderList);

				//ת��
				request.setAttribute("pageCount", pageCount);
				request.setAttribute("pageNow", pageNow);
				request.setAttribute("pageFrom", pageFrom);
				request.setAttribute("pageTo", pageTo);
				request.setAttribute("orderList", orderList);
				request.getRequestDispatcher("myorder_page.jsp").forward(request, response);
				return;
			}
			else if(dowhat.equals("locateOrder")) //��λ�����ĵ�ǰ״̬���ڵ�ҳ�棨�鿴�������飩
			{
				String oid = (String)request.getParameter("Oid"); //������
				
				//�鶩����ͨ��Oid
				OrderFormBeanCl orderFormBeanCl = new OrderFormBeanCl();
				OrderFormBean orderBean = orderFormBeanCl.findOrderFormByOid(oid); //ȡ����������Ϣ
				orderFormBeanCl.findBookListInOrder(orderBean); //ȡ�����е��鼮�б�

				//���ַ��ͨ��order.Aid
				AddressBeanCl addressBeanCl = new AddressBeanCl();
				AddressBean addressBean = addressBeanCl.getAddressByAid(orderBean.getAid());
				
				//ת��
				String locateOrderUrl=""; //ת������ҳ���url
				request.setAttribute("orderBean", orderBean);
				request.setAttribute("addressBean", addressBean);
				switch(orderBean.getOstatus()) //���ݶ���Ostatus��λ�����ʵ�ҳ�棨֧�����ջ������ۡ��鿴��
				{
					case C.ORDERSTATUS_WAITPAY:locateOrderUrl="locateorder_pay_page.jsp";break; //�ȴ�֧��״̬ҳ��
					case C.ORDERSTATUS_CONFIRMRECEIVED:locateOrderUrl="locateorder_receive_page.jsp";break; //�ȴ��ջ�״̬ҳ��
					case C.ORDERSTATUS_WAITCOMMENT:locateOrderUrl="locateorder_comment_page.jsp";break; //�ȴ�����״̬ҳ��
					case C.ORDERSTATUS_FINISHED:locateOrderUrl="locateorder_finished_page.jsp";break; //�������ҳ��
				}

				request.getRequestDispatcher(locateOrderUrl).forward(request, response);
			}
			else if(dowhat.equals("deleteOrder")) //ɾ������
			{
				String oid=request.getParameter("Oid"); //Ҫɾ�Ķ�����
				OrderFormBeanCl orderFormBeanCl = new OrderFormBeanCl();
				orderFormBeanCl.deleteOrder(oid);

				return;
			}
			else if(dowhat.equals("modifyOrder")) //�޸Ķ���(�ݲ�ʵ��%%%%%%%%%%%%%%%%)
			{
				
			}
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
