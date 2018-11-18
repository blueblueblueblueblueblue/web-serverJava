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
	 * 处理与评价、评星相关请求
	 * 点击支付，改变Transaction、OrderForm表的状态位等信息、Tcomment、Tmark字段信息
	 */
	private static final long serialVersionUID = 1L;

	public CommentClServlet() {
        super();
    }
	
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//先验证用户是否登录
		HttpSession session=request.getSession(true);
		String username=(String) session.getAttribute("username");
		String dowhat = request.getParameter("dowhat");//获取操作类型
		
		if(username == null) //未登录，跳转至登录界面
		{
			request.getRequestDispatcher("login_page.jsp").forward(request, response);
			return;
		}
		else //已登录
		{	
			if(dowhat.equals("commentBook")) //买家评论商品（请求写入数据库）
			{
				//获取评价相关的参数
				String oid = request.getParameter("Oid"); //订单号
				String bid = request.getParameter("Bid"); //书籍编号
				int mark = Integer.parseInt(request.getParameter("Mark")); //评星数
				String comment = request.getParameter("Comment"); //评价
				
				//通过oid、bid、mark、comment更新Transaction表
				TransactionBeanCl transactionBeanCl = new TransactionBeanCl();
				transactionBeanCl.updateTransactionByOidByBid(oid,bid,C.TRADESTATUS_FINISHED,mark,comment);
				
				//更改bid书籍的评论量+1
				BookBeanCl bookBeanCl = new BookBeanCl();
				bookBeanCl.incCommentNum(bid);
	
				//查询Transaction表中oid订单下的所有书是否都已评论完毕(得到一个ArrayList<int>的状态列表)
				ArrayList<Integer> transactionStatusList = transactionBeanCl.findTransactionStatusListByOid(oid);

				if(isAllOidTransactionFinished(transactionStatusList)) //全部书籍评论完成
				{
					//修改oid订单的状态为“完成”！！
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
	
	//判断Transaction表中oid订单下的所有书是否都已评论完毕
	boolean isAllOidTransactionFinished(ArrayList<Integer> transactionStatusList)
	{
		for(int i=0;i<transactionStatusList.size();i++)
		{
			if(transactionStatusList.get(i) != C.TRADESTATUS_FINISHED) //只要有一本书没有评论就不行
				return false;
		}
		
		return true;
	}
}