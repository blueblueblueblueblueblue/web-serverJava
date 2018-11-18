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
	 * ������빺�ﳵ�����빺�ﳵ����ʱ�Ĵ���
	 */
	private static final long serialVersionUID = 1L;

	public CartClServlet() {
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
			clearOrderListInSession(username,session);
			if(session.getAttribute(C.DETAIL_TOKEN) != null)
			{
				session.removeAttribute(C.DETAIL_TOKEN);

				if(dowhat.equals("addBookIntoCart")) //�����������ﳵ��ťʱ
				{
					//��ȡsession�еĹ�����BookInCartBeanList��
					ArrayList<BookInCartBean> bookInCartBeanList=(ArrayList<BookInCartBean>) session.getAttribute(username+"_bookInCartBeanList");
					if(bookInCartBeanList == null) //�����Ϊ�գ���һ��
						bookInCartBeanList=new ArrayList<BookInCartBean>();
					
					//����Bid�������Ƿ����ڹ����
					String Bid=request.getParameter("Bid");
					int i;
					for(i=0;i<bookInCartBeanList.size();i++)
					{
						BookInCartBean bookInCartBean=bookInCartBeanList.get(i);
						if(Bid.equals(bookInCartBean.getBid())) //�Ѵ��ڣ��ɵģ��ı������+1
						{
							int tboughtnum=bookInCartBean.getTboughtnum();
							bookInCartBean.setTboughtnum(++tboughtnum);
							break;
						}
					}
					if(i == bookInCartBeanList.size()) //�����ڣ��µ�,��װ�����
					{
						//�����������빺�ﳵ���鼮��Ϣ��װ��BookInCartBean
						BookInCartBean bookInCartBean=new BookInCartBean(Bid,request.getParameter("Bimage"), 
								Float.parseFloat(request.getParameter("Bprice")),request.getParameter("Bname"),Integer.parseInt(request.getParameter("Sid")),
								request.getParameter("Sicon"),request.getParameter("Sname"),Float.parseFloat(request.getParameter("Boriprice")),
								Integer.parseInt(request.getParameter("Tboughtnum")));	
						
						 //����������
						bookInCartBeanList.add(bookInCartBean);
					}				

					//ת��
					session.setAttribute(username+"_bookInCartBeanList", bookInCartBeanList);	//����session(ע��!!��username+_bookInCartBeanList����˻���)
					request.getRequestDispatcher("cart_page.jsp").forward(request, response);
					return;
				}
			}
			else //�ύajax���� || һζˢ�µ����ظ��ύ
			{
				List<BookInCartBean> bookInCartBeanList=(List<BookInCartBean>) session.getAttribute(username+"_bookInCartBeanList");
				
				if(dowhat.equals("checkABookInCart")) //ajax����ѡ��ĳ����
				{
					int index=Integer.parseInt(request.getParameter("index")); //���ﳵ�еڼ�����Ҫ���ı�
					boolean checked=Boolean.parseBoolean(request.getParameter("checked")); //�ı�ɼ���
					
					bookInCartBeanList.get(index).setChecked(checked);
					return;
				}
				else if(dowhat.equals("checkAllBooksInCart")) //ajax����ѡ��������
				{
					boolean checked=Boolean.parseBoolean(request.getParameter("checked")); //�ı�ɼ���
					if(bookInCartBeanList != null)
						for(int index=0;index<bookInCartBeanList.size();index++)
							bookInCartBeanList.get(index).setChecked(checked);
					return;
				}
				else if(dowhat.equals("updateBoughtNumInCart")) //ajax����ı��鼮��Ŀ
				{
					int index=Integer.parseInt(request.getParameter("index")); //���ﳵ�еڼ�����Ҫ���ı�
					int boughtNum=Integer.parseInt(request.getParameter("boughtNum")); //�ı�ɼ���
					
					bookInCartBeanList.get(index).setTboughtnum(boughtNum);
					return;
				}
				else if(dowhat.equals("delABookInCart")) //ajax����ɾ�����ﳵĳ����
				{
					int index=Integer.parseInt(request.getParameter("index")); //���ﳵ�еڼ�����Ҫ���ı�
					
					if(bookInCartBeanList != null && bookInCartBeanList.size() > 0)
						bookInCartBeanList.remove(index);
					return;
				}
				else if(dowhat.equals("delAllBooksInCart")) //ajax������������鼮
				{
					if(bookInCartBeanList != null)
						bookInCartBeanList.clear();
					return;
				}
			}
			
			//�ظ�ˢ���ύ�����ڵ�����ֱ�ӵ����������������������ʣ����ǲ������ʣ�
			request.getRequestDispatcher("cart_page.jsp").forward(request, response);
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
