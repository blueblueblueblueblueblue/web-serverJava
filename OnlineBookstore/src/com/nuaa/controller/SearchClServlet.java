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
	 * 搜索相关处理（点击搜索+首页）
	 */
	private static final long serialVersionUID = 1L;

	public SearchClServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//清空订单表
		clearOrderListInSession(request);
		
		String dowhat = request.getParameter("dowhat");
		if(dowhat.equals("searchByRand")) //首次登录或返回首页
		{
			// 随机产生包含8本书籍信息的ArrayList
			BookBeanCl bookBeanCl = new BookBeanCl();
			ArrayList<BookSnapshotBean> bookSnapshotList = bookBeanCl.getRandom8Books();

			//转发
			new CookieUtil().addCookie(request, response, "curPageUrl", request.getServletPath()+"?"+request.getQueryString());//将来源页面的url写入cookie，方便登录或注册以后读取url并返回之前页面
			
			request.setAttribute("bookSnapshotList", bookSnapshotList);
			request.getRequestDispatcher("index_page.jsp").forward(request, response);
			return;
		}
		else //关键字搜索
		{
			//获取参数
			String key = request.getParameter("key"); //关键字
			String type = request.getParameter("type"); //类别码
			String refer = request.getParameter("refer"); //排序参照列
			String way = request.getParameter("way"); //页码搜索方式
			int pageCount=Integer.parseInt((request.getParameter("pageCount")));
			int pageNow=Integer.parseInt(request.getParameter("pageNow"));
			int pageFrom=Integer.parseInt(request.getParameter("pageFrom"));
			int pageTo=Integer.parseInt(request.getParameter("pageTo"));
			
			BookBeanCl bookBeanCl = new BookBeanCl();
			
			//判断是那种检索方式（按关键字？按类别码）
			if(dowhat.equals("searchByKey"))	
			{
				pageCount=bookBeanCl.getBookSnapshotsPageCountByKey(key);
			}
			else if(dowhat.equals("searchByType"))
			{
				pageCount=bookBeanCl.getBookSnapshotsPageCountByType(type);
			}
			
			//判断是那种页码检索方式（第一次？上一页？下一页？点击某页？跳转到某页？）
			//计算好下一次的分页参数
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
			//else byPageFirst\byPageNow不作任何处理
			pageTo = pageCount <= C.pageRange ? pageCount : pageFrom+C.pageRange-1;

			// 按关键字检索数据库中书籍，获取ArrayList
			ArrayList<BookSnapshotBean> bookSnapshotList=null;
			if(dowhat.equals("searchByKey"))
				bookSnapshotList = bookBeanCl.getBookSnapshotsByKey(key,pageNow,refer);
			else if(dowhat.equals("searchByType"))
				bookSnapshotList = bookBeanCl.getBookSnapshotsByType(type,pageNow,refer);
			
			//转发参数
			new CookieUtil().addCookie(request, response, "curPageUrl",request.getServletPath()+"?"+request.getQueryString());//将来源页面的url写入cookie，方便登录或注册以后读取url并返回之前页面
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
	
	//删除存在session中的临时订单表
	void clearOrderListInSession(HttpServletRequest request)
	{
		HttpSession session=request.getSession(true);
		String username=(String)session.getAttribute("username");
		if(username != null)
			session.removeAttribute(username+"_orderBeanList"); //删除临时的订单表	
	}
}
