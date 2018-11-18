package com.nuaa.controller;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nuaa.model.BookBeanCl;
import com.nuaa.model.BookDetailBean;
import com.nuaa.model.Json;
import com.nuaa.util.C;
import com.nuaa.util.CookieUtil;

import net.sf.json.JSONArray;

@WebServlet("/DetailClServlet")
public class DetailClServlet extends HttpServlet {

    /**
	 * ����鼮����������ҳ��ʱ�Ĵ���
	 */
	private static final long serialVersionUID = 1L;

	public DetailClServlet() {
        super();
    }

	@SuppressWarnings("rawtypes")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String dowhat = request.getParameter("dowhat");
		String Bid=request.getParameter("Bid");//��ȡ����ѯ������鼮id
		if(dowhat.equals("findDetail"))
		{
			//��ȡ�鼮����
			BookBeanCl bookBeanCl=new BookBeanCl();
			BookDetailBean bookDetailBean=bookBeanCl.getBookDetailById(Bid); //��ȡ�����鼮��Ϣ

			//�������ۡ��ɽ���¼ѡ���Ҫ��ҳ��ҳ��
			int bookCommentPageCount=bookBeanCl.getPageCount(bookDetailBean.getBcommentnum(), C.bookCommentPageSize);//��ȡ���۵��ܷ�ҳ��
			int bookTradeRecordPageCount=bookBeanCl.getPageCount(bookDetailBean.getBsalednum(), C.bookTradeRecordPageSize);//��ȡ�ɽ���¼���ܷ�ҳ��
					
			//ת��
			new CookieUtil().addCookie(request, response, "curPageUrl", request.getServletPath()+"?"+request.getQueryString());//����Դҳ���urlд��cookie�������¼��ע���Ժ��ȡurl������֮ǰҳ��
			request.setAttribute("bookDetailBean", bookDetailBean);
			request.setAttribute("bookCommentPageCount", bookCommentPageCount);
			request.setAttribute("bookTradeRecordPageCount", bookTradeRecordPageCount);
			request.getRequestDispatcher("detail_page.jsp").forward(request, response);
			return;
		}
		else //ajax�����ѯ���ۡ��ɽ���¼��Ϣ
		{
			//����ajax��������ҳ������ݿ⣬ȡ�������б�д��Json�����ظ�ǰ�˻ص�����
			int pageNext = Integer.parseInt(request.getParameter("pageNext"));
			
			//����Bid��ѯ���ݿ��и�����pageҳ���������ۻ�ɽ���¼
			BookBeanCl bookBeanCl=new BookBeanCl();
			ArrayList beanList=null;
			if(dowhat.equals("findcommentByPage"))
				beanList=bookBeanCl.getBookCommentByIdAndPage(Bid,pageNext);
			else if(dowhat.equals("findtraderecordByPage"))
				beanList=bookBeanCl.getBookTradeRecordByIdAndPage(Bid,pageNext);
				
			//��bookCommentBeanListת��Ϊjson�ַ��������ͻ���		
			JSONArray jsonArray=new JSONArray();
			for(int i=0;i<beanList.size();i++)
				jsonArray.put(((Json) beanList.get(i)).getJsonObject());
			
			//��д���ͻ��˲鵽��json����
			response.setContentType("text/html;charset=utf-8"); 
			response.setHeader("Pragma","No-cache"); 
			response.setHeader("Cache-Control","no-cache");
			response.setHeader("Expires","0");
			response.getWriter().print(jsonArray.toString());
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
