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
	 * 点击书籍，进入详情页面时的处理
	 */
	private static final long serialVersionUID = 1L;

	public DetailClServlet() {
        super();
    }

	@SuppressWarnings("rawtypes")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String dowhat = request.getParameter("dowhat");
		String Bid=request.getParameter("Bid");//获取欲查询详情的书籍id
		if(dowhat.equals("findDetail"))
		{
			//获取书籍详情
			BookBeanCl bookBeanCl=new BookBeanCl();
			BookDetailBean bookDetailBean=bookBeanCl.getBookDetailById(Bid); //获取核心书籍信息

			//计算评论、成交记录选项卡需要分页的页数
			int bookCommentPageCount=bookBeanCl.getPageCount(bookDetailBean.getBcommentnum(), C.bookCommentPageSize);//获取评价的总分页数
			int bookTradeRecordPageCount=bookBeanCl.getPageCount(bookDetailBean.getBsalednum(), C.bookTradeRecordPageSize);//获取成交记录的总分页数
					
			//转发
			new CookieUtil().addCookie(request, response, "curPageUrl", request.getServletPath()+"?"+request.getQueryString());//将来源页面的url写入cookie，方便登录或注册以后读取url并返回之前页面
			request.setAttribute("bookDetailBean", bookDetailBean);
			request.setAttribute("bookCommentPageCount", bookCommentPageCount);
			request.setAttribute("bookTradeRecordPageCount", bookTradeRecordPageCount);
			request.getRequestDispatcher("detail_page.jsp").forward(request, response);
			return;
		}
		else //ajax请求查询评价、成交记录信息
		{
			//根据ajax请求传来的页码查数据库，取得评价列表写入Json，返回给前端回调函数
			int pageNext = Integer.parseInt(request.getParameter("pageNext"));
			
			//根据Bid查询数据库中该书在page页的所有评价或成交记录
			BookBeanCl bookBeanCl=new BookBeanCl();
			ArrayList beanList=null;
			if(dowhat.equals("findcommentByPage"))
				beanList=bookBeanCl.getBookCommentByIdAndPage(Bid,pageNext);
			else if(dowhat.equals("findtraderecordByPage"))
				beanList=bookBeanCl.getBookTradeRecordByIdAndPage(Bid,pageNext);
				
			//将bookCommentBeanList转换为json字符串传给客户端		
			JSONArray jsonArray=new JSONArray();
			for(int i=0;i<beanList.size();i++)
				jsonArray.put(((Json) beanList.get(i)).getJsonObject());
			
			//回写给客户端查到的json数据
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
