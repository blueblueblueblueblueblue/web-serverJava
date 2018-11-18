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

import com.nuaa.model.BookBeanCl;
import com.nuaa.model.OrderFormBeanCl;
import com.nuaa.model.TransactionBeanCl;
import com.nuaa.util.C;

import net.sf.json.JSONObject;

@WebServlet("/CommentClServlet")
public class CommentClServlet extends HttpServlet {
	
	/**
	 * ���������ۡ������������
	 * ���֧�����ı�Transaction��OrderForm���״̬λ����Ϣ��Tcomment��Tmark�ֶ���Ϣ
	 */
	private static final long serialVersionUID = 1L;

	public CommentClServlet() {
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
			if(dowhat.equals("commentBook")) //���������Ʒ������д�����ݿ⣩
			{
				//��ȡ������صĲ���
				String oid = request.getParameter("Oid"); //������
				String bid = request.getParameter("Bid"); //�鼮���
				int mark = Integer.parseInt(request.getParameter("Mark")); //������
				String comment = request.getParameter("Comment"); //����
				
				//ͨ��oid��bid��mark��comment����Transaction��
				TransactionBeanCl transactionBeanCl = new TransactionBeanCl();
				transactionBeanCl.updateTransactionByOidByBid(oid,bid,C.TRADESTATUS_FINISHED,mark,comment);
				
				//����bid�鼮��������+1
				BookBeanCl bookBeanCl = new BookBeanCl();
				bookBeanCl.incCommentNum(bid);
	
				//��ѯTransaction����oid�����µ��������Ƿ����������(�õ�һ��ArrayList<int>��״̬�б�)
				ArrayList<Integer> transactionStatusList = transactionBeanCl.findTransactionStatusListByOid(oid);

				if(isAllOidTransactionFinished(transactionStatusList)) //ȫ���鼮�������
				{
					//�޸�oid������״̬Ϊ����ɡ�����
					OrderFormBeanCl orderFormBeanCl = new OrderFormBeanCl(); 
					orderFormBeanCl.modifyOrderStatusByOid(oid, C.ORDERSTATUS_FINISHED);
				}

				return;
			}
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	//�ж�Transaction����oid�����µ��������Ƿ����������
	boolean isAllOidTransactionFinished(ArrayList<Integer> transactionStatusList)
	{
		for(int i=0;i<transactionStatusList.size();i++)
		{
			if(transactionStatusList.get(i) != C.TRADESTATUS_FINISHED) //ֻҪ��һ����û�����۾Ͳ���
				return false;
		}
		
		return true;
	}
}