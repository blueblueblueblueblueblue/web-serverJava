package com.nuaa.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.nuaa.model.BookInCartBean;
import com.nuaa.model.OrderFormBean;
import com.nuaa.model.OrderFormBeanCl;
import com.nuaa.model.ShopBean;
import com.nuaa.model.ShopBeanCl;
import com.nuaa.model.TransactionBean;
import com.nuaa.model.TransactionBeanCl;
import com.nuaa.util.C;

import net.sf.json.JSONObject;

/**
 * �����붩���������
 * 1.������㣬������д����ҳ��ʱ����ȡsession�й��ﳵѡ�е��鼮��Ϣ������󷵻�ǰ̨
 * 2.����ҵĶ���ʱ����ȡTransaction���ݿ��й��ڸ��û��Ķ�����Ϣ������󷵻�ǰ̨
 */

@WebServlet("/OrderClServlet")
public class OrderClServlet extends HttpServlet{
	
	/**
	 * �����붩���������
	 * ������㣬������д����ҳ��ʱ����ȡsession�й��ﳵѡ�е��鼮��Ϣ������󷵻�ǰ̨
	 */
	private static final long serialVersionUID = 1L;

	public OrderClServlet() {
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
			if(dowhat.equals("buyatonce")) //������������򡱣������鼮����Ϣ���������ﳵ��ֱ�����ɶ���
			{
				session.removeAttribute(C.CART_TOKEN);
				
				//��ȡ�鼮��Ϣ
				BookInCartBean bookInCartBean=new BookInCartBean(request.getParameter("Bid"),request.getParameter("Bimage"), 
						Float.parseFloat(request.getParameter("Bprice")),request.getParameter("Bname"),Integer.parseInt(request.getParameter("Sid")),
						request.getParameter("Sicon"),request.getParameter("Sname"),Float.parseFloat(request.getParameter("Boriprice")),
						Integer.parseInt(request.getParameter("Tboughtnum")));
				
				//���ɶ���
				List<OrderFormBean> orderBeanList=new ArrayList<OrderFormBean>();
				addToOrderList(bookInCartBean,orderBeanList);  //���Ȿ����붩��
				
				//�������ж�����С�Ƽ۸�+�˷�
				calOrderPrice(orderBeanList);
				
				//ת��
				session.setAttribute(username+"_orderBeanList", orderBeanList);
				request.getRequestDispatcher("writeorder_page.jsp").forward(request, response);
				return;
				
			}

			if(session.getAttribute(C.CART_TOKEN) != null) //��������㡱
			{
				session.removeAttribute(C.CART_TOKEN);
				if(dowhat.equals("writeorder")) //������㣬�Թ��ﳵ����Ʒ����һ�ҵ���һ���б�����ݽṹ�������б���ʽ��
				{
					//��ȡ��ǰ���ﳵ
					ArrayList<BookInCartBean> bookInCartBeanList=(ArrayList<BookInCartBean>) session.getAttribute(username+"_bookInCartBeanList");
					
					//��"��"��Ʒ�������̡��鵽ͬһ������
					List<OrderFormBean> orderBeanList=new ArrayList<OrderFormBean>();
					for(int i=0;i<bookInCartBeanList.size();i++)
					{
						BookInCartBean bookInCartBean=bookInCartBeanList.get(i);
						if(bookInCartBean.isChecked() == true) //�򹴵Ĳż���
							addToOrderList(bookInCartBean,orderBeanList); 
					}
					
					//�������ж�����С�Ƽ۸�+�˷�
					calOrderPrice(orderBeanList);
					
					//ת��
					session.setAttribute(username+"_orderBeanList", orderBeanList);
					request.getRequestDispatcher("writeorder_page.jsp").forward(request, response);
					return;
				}
			}
			else  
			{
				if(dowhat.equals("sendBookBackToCart")) //������Żع��ﳵ��
				{
					int orderIndex=Integer.parseInt(request.getParameter("orderIndex")); //��orderIndex������
					int bookIndex=Integer.parseInt(request.getParameter("bookIndex")); //��bookIndex����
					
					//��ȡ������
					List<OrderFormBean> orderBeanList=(ArrayList<OrderFormBean>)session.getAttribute(username+"_orderBeanList");
					OrderFormBean orderBean = orderBeanList.get(orderIndex);
					orderBean.getBookInCartBeanList().remove(bookIndex);
					
					if(orderBean.getBookInCartBeanList().size() == 0) //����orderIndex�е����ѱ�ɾ��
					{
						orderBeanList.remove(orderIndex); //ɾȥ����
						if(orderBeanList.size() == 0) //����ɾ����
							session.removeAttribute(username+"_orderBeanList");
						return;
					}
					else //û��ɾ�꣬�˷�����ܻ�䣬���¼���
					{
						orderBean.calOrderPrice();
						orderBean.calOrderTotalTransPrice();
						response.getWriter().print(new JSONObject().put("totalTransPrice", orderBean.getOtotaltransprice()).toString());
						return;
					}
				}
				else if(dowhat.equals("submitOrder")) //������ύ������
				{
					//��session�е�orderBeanList�������ݿ�
					//ͬʱɾ��session�й��ﳵ��Ӧ��Ʒ
					
					if(session.getAttribute(C.WRITEORDER_TOKEN) != null) //���������ύ����
					{	
						session.removeAttribute(C.WRITEORDER_TOKEN);
						
						//��ȡ���������ﳵ����ַ��
						List<OrderFormBean> orderBeanList=(ArrayList<OrderFormBean>)session.getAttribute(username+"_orderBeanList");
						List<BookInCartBean> bookInCartBeanList=(ArrayList<BookInCartBean>) session.getAttribute(username+"_bookInCartBeanList");
						String aid=request.getParameter("Aid");
						
						//��������session�й��ﳵ��ɾ��+д�����ݿ�
						removeSubmittedBookInCartAndAddOrder(username,aid,orderBeanList,bookInCartBeanList);
						request.setAttribute("result", "SUCCESS"); //�ɹ����
					}
					else
					{
						request.setAttribute("result", "INVALID"); //ʧ�ܱ��
					}
					
					//��ת��֧������
					request.getRequestDispatcher("pay_page.jsp").forward(request, response);
					return;	
				}
			}
			
			//�ظ�ˢ���ύ
			System.out.println("ˢ��");
			request.getRequestDispatcher("writeorder_page.jsp").forward(request, response);
			return;	
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	//���鼮��ӵ���������
	//����Ŀǰ�Ķ������Ƿ�����bookInCartBean.sid��ͬ�ĵ���
	//����ͬ��׷�ӽ�ȥ
	//��δ�ҵ���������orderListβ
	void addToOrderList(BookInCartBean bookInCartBean,List<OrderFormBean> orderBeanList)
	{
		int j;
		for(j=0;j<orderBeanList.size();j++)
		{
			OrderFormBean oldOrder= orderBeanList.get(j); //j�Ŷ���
			
			if(bookInCartBean.getSid() == oldOrder.getBookInCartBeanList().get(0).getSid())
			{
				oldOrder.getBookInCartBeanList().add(bookInCartBean);
				break;
			}
		}
		if(j >= orderBeanList.size()) //δ�ҵ�
		{
			//������������ʼֻ��һ���飩
			OrderFormBean newOrder=new OrderFormBean();
			newOrder.getBookInCartBeanList().add(bookInCartBean);
			
			//��ӵ�һ����ʱ��ȡ����(����)��id��ͼ�ꡢ���ơ���λ�˷�
			ShopBeanCl shopBeanCl = new ShopBeanCl(); //��sid�ӵ��̱��л�ȡ�˷�
			ShopBean shopBean = shopBeanCl.findShopBySid(bookInCartBean.getSid()); 
			newOrder.setSid(shopBean.getSid());
			newOrder.setSicon(shopBean.getSicon());
			newOrder.setSname(shopBean.getSname());
			newOrder.setStransprice(shopBean.getStransprice());
			
			//������orderList
			orderBeanList.add(newOrder);
		}	
	}
	
	//ɾ�����ﳵ�����ύ������Ʒ��������д�뵽���ݿ�
	void removeSubmittedBookInCartAndAddOrder(String username,String aid,
			List<OrderFormBean> orderBeanList,List<BookInCartBean> bookInCartBeanList){
		
		TransactionBeanCl transactionBeanCl=new TransactionBeanCl();
		OrderFormBeanCl orderBeanCl=new OrderFormBeanCl();
		
		if(orderBeanList != null && orderBeanList.size() > 0)
		{
			for(int orderIndex=0;orderIndex < orderBeanList.size();orderIndex++)
			{
				OrderFormBean orderBean=orderBeanList.get(orderIndex);
				List<BookInCartBean> submittedBookList=orderBean.getBookInCartBeanList(); //����orderIndex
				String oid = createOrderNumber(username,orderIndex);
				
				//���ö���д�붩����
				orderBean.setOid(oid);
				orderBean.setUid(username);
				orderBean.setAid(aid);
				orderBeanCl.addOrder(orderBean);
				
				for(int sIndex=0;sIndex < submittedBookList.size();sIndex++) 
				{
					BookInCartBean submittedBook=submittedBookList.get(sIndex); //��sIndex�����˵���

					//��������Ϣд�뽻�ױ�
					TransactionBean transactionBean=new TransactionBean(C.RAND_NUM, submittedBook.getBid() ,oid,username, C.TRADESTATUS_WAITPAY, submittedBook.getTboughtnum(), 0,null, null, null,null,null);
					transactionBeanCl.addTrade(transactionBean);
					
					//�ӹ��ﳵ��ɾ��
					if(bookInCartBeanList != null && bookInCartBeanList.size() > 0)
					{
						for(int cIndex=0;cIndex < bookInCartBeanList.size();cIndex++)
						{
							BookInCartBean cartBook=bookInCartBeanList.get(cIndex);//���ﳵ�еĵ�cIndex����
							if(submittedBook.getBid() == cartBook.getBid())
							{
								bookInCartBeanList.remove(cIndex); //�ӹ��ﳵ��ɾ��
								break;
							}
						}
					}
				}
			}
		}
	}
	
	//��ȡ��username+orderIndex+��ǰʱ����Ϊ�ġ������š�
	String createOrderNumber(String username,int orderIndex)
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		return username + "_" + df.format(new Date()) + "_" + orderIndex;
	}
	
	//���㶩���б���˷�+С���˷�
	void calOrderPrice(List<OrderFormBean> orderBeanList)
	{
		for(int i=0;i<orderBeanList.size();i++)
		{
			OrderFormBean orderFormBean = orderBeanList.get(i);
			orderFormBean.calOrderPrice();
			orderFormBean.calOrderTotalTransPrice();
		}
	}
}
