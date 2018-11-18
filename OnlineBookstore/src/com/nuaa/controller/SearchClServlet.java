package com.nuaa.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.nuaa.model.BookBeanCl;
import com.nuaa.model.BookSnapshotBean;
import com.nuaa.util.C;
import com.nuaa.util.CookieUtil;

@WebServlet("/SearchClServlet")
public class SearchClServlet extends HttpServlet {

	/**
	 * ������ش����������+��ҳ��
	 */
	private static final long serialVersionUID = 1L;

	public SearchClServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//��ն�����
		clearOrderListInSession(request);
		
		String dowhat = request.getParameter("dowhat");
		if(dowhat.equals("searchByRand")) //�״ε�¼�򷵻���ҳ
		{
			// �����������8���鼮��Ϣ��ArrayList
			BookBeanCl bookBeanCl = new BookBeanCl();
			ArrayList<BookSnapshotBean> bookSnapshotList = bookBeanCl.getRandom8Books();

			//ת��
			new CookieUtil().addCookie(request, response, "curPageUrl", request.getServletPath()+"?"+request.getQueryString());//����Դҳ���urlд��cookie�������¼��ע���Ժ��ȡurl������֮ǰҳ��
			
			request.setAttribute("bookSnapshotList", bookSnapshotList);
			request.getRequestDispatcher("index_page.jsp").forward(request, response);
			return;
		}
		else //�ؼ�������
		{
			//��ȡ����
			String key = request.getParameter("key"); //�ؼ���
			String type = request.getParameter("type"); //�����
			String refer = request.getParameter("refer"); //���������
			String way = request.getParameter("way"); //ҳ��������ʽ
			int pageCount=Integer.parseInt((request.getParameter("pageCount")));
			int pageNow=Integer.parseInt(request.getParameter("pageNow"));
			int pageFrom=Integer.parseInt(request.getParameter("pageFrom"));
			int pageTo=Integer.parseInt(request.getParameter("pageTo"));
			
			BookBeanCl bookBeanCl = new BookBeanCl();
			
			//�ж������ּ�����ʽ�����ؼ��֣�������룩
			if(dowhat.equals("searchByKey"))	
			{
				pageCount=bookBeanCl.getBookSnapshotsPageCountByKey(key);
			}
			else if(dowhat.equals("searchByType"))
			{
				pageCount=bookBeanCl.getBookSnapshotsPageCountByType(type);
			}
			
			//�ж�������ҳ�������ʽ����һ�Σ���һҳ����һҳ�����ĳҳ����ת��ĳҳ����
			//�������һ�εķ�ҳ����
			if(way.equals("byPageLast"))
			{
				pageFrom = pageNow == pageFrom ? pageFrom-1 : pageFrom;
				pageNow--;
			}
			else if(way.equals("byPageNext"))
			{
				pageFrom = pageNow == pageTo ? pageFrom+1 : pageFrom;
				pageNow++;
			}
			else if(way.equals("byPageJump"))
			{
				pageFrom = pageFrom+pageNow-pageTo < 1 ? 1 : pageFrom+pageNow-pageTo;
			}
			//else byPageFirst\byPageNow�����κδ���
			pageTo = pageCount <= C.pageRange ? pageCount : pageFrom+C.pageRange-1;

			// ���ؼ��ּ������ݿ����鼮����ȡArrayList
			ArrayList<BookSnapshotBean> bookSnapshotList=null;
			if(dowhat.equals("searchByKey"))
				bookSnapshotList = bookBeanCl.getBookSnapshotsByKey(key,pageNow,refer);
			else if(dowhat.equals("searchByType"))
				bookSnapshotList = bookBeanCl.getBookSnapshotsByType(type,pageNow,refer);
			
			//ת������
			new CookieUtil().addCookie(request, response, "curPageUrl",request.getServletPath()+"?"+request.getQueryString());//����Դҳ���urlд��cookie�������¼��ע���Ժ��ȡurl������֮ǰҳ��
			request.setAttribute("dowhat", dowhat);
			request.setAttribute("way", way);
			request.setAttribute("key", key);
			request.setAttribute("type", type);
			request.setAttribute("refer", refer);
			request.setAttribute("pageCount", pageCount);
			request.setAttribute("pageNow", pageNow);
			request.setAttribute("pageFrom", pageFrom);
			request.setAttribute("pageTo", pageTo);
			request.setAttribute("bookSnapshotList", bookSnapshotList);
			request.getRequestDispatcher("search_page.jsp").forward(request, response);	
			return;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
	
	//ɾ������session�е���ʱ������
	void clearOrderListInSession(HttpServletRequest request)
	{
		HttpSession session=request.getSession(true);
		String username=(String)session.getAttribute("username");
		if(username != null)
			session.removeAttribute(username+"_orderBeanList"); //ɾ����ʱ�Ķ�����	
	}
}
