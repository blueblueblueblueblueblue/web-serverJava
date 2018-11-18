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
 * 处理与订单相关请求
 * 1.点击结算，进入填写订单页面时，获取session中购物车选中的书籍信息，整理后返回前台
 * 2.点击我的订单时，读取Transaction数据库中关于该用户的订单信息，整理后返回前台
 */

@WebServlet("/OrderClServlet")
public class OrderClServlet extends HttpServlet{
	
	/**
	 * 处理与订单相关请求
	 * 点击结算，进入填写订单页面时，获取session中购物车选中的书籍信息，整理后返回前台
	 */
	private static final long serialVersionUID = 1L;

	public OrderClServlet() {
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
			if(dowhat.equals("buyatonce")) //点击“立即购买”，则获得书籍的信息后，跳过购物车，直接生成订单
			{
				session.removeAttribute(C.CART_TOKEN);
				
				//获取书籍信息
				BookInCartBean bookInCartBean=new BookInCartBean(request.getParameter("Bid"),request.getParameter("Bimage"), 
						Float.parseFloat(request.getParameter("Bprice")),request.getParameter("Bname"),Integer.parseInt(request.getParameter("Sid")),
						request.getParameter("Sicon"),request.getParameter("Sname"),Float.parseFloat(request.getParameter("Boriprice")),
						Integer.parseInt(request.getParameter("Tboughtnum")));
				
				//生成订单
				List<OrderFormBean> orderBeanList=new ArrayList<OrderFormBean>();
				addToOrderList(bookInCartBean,orderBeanList);  //把这本书加入订单
				
				//计算所有订单的小计价格+运费
				calOrderPrice(orderBeanList);
				
				//转发
				session.setAttribute(username+"_orderBeanList", orderBeanList);
				request.getRequestDispatcher("writeorder_page.jsp").forward(request, response);
				return;
				
			}

			if(session.getAttribute(C.CART_TOKEN) != null) //点击“结算”
			{
				session.removeAttribute(C.CART_TOKEN);
				if(dowhat.equals("writeorder")) //点击结算，对购物车打勾商品串成一家店铺一个列表的数据结构（表中有表形式）
				{
					//获取当前购物车
					ArrayList<BookInCartBean> bookInCartBeanList=(ArrayList<BookInCartBean>) session.getAttribute(username+"_bookInCartBeanList");
					
					//将"打勾"商品按“店铺”归到同一订单中
					List<OrderFormBean> orderBeanList=new ArrayList<OrderFormBean>();
					for(int i=0;i<bookInCartBeanList.size();i++)
					{
						BookInCartBean bookInCartBean=bookInCartBeanList.get(i);
						if(bookInCartBean.isChecked() == true) //打勾的才加入
							addToOrderList(bookInCartBean,orderBeanList); 
					}
					
					//计算所有订单的小计价格+运费
					calOrderPrice(orderBeanList);
					
					//转发
					session.setAttribute(username+"_orderBeanList", orderBeanList);
					request.getRequestDispatcher("writeorder_page.jsp").forward(request, response);
					return;
				}
			}
			else  
			{
				if(dowhat.equals("sendBookBackToCart")) //点击“放回购物车”
				{
					int orderIndex=Integer.parseInt(request.getParameter("orderIndex")); //第orderIndex个订单
					int bookIndex=Integer.parseInt(request.getParameter("bookIndex")); //第bookIndex本书
					
					//获取订单表
					List<OrderFormBean> orderBeanList=(ArrayList<OrderFormBean>)session.getAttribute(username+"_orderBeanList");
					OrderFormBean orderBean = orderBeanList.get(orderIndex);
					orderBean.getBookInCartBeanList().remove(bookIndex);
					
					if(orderBean.getBookInCartBeanList().size() == 0) //订单orderIndex中的书已被删完
					{
						orderBeanList.remove(orderIndex); //删去订单
						if(orderBeanList.size() == 0) //订单删光了
							session.removeAttribute(username+"_orderBeanList");
						return;
					}
					else //没被删完，运费则可能会变，重新计算
					{
						orderBean.calOrderPrice();
						orderBean.calOrderTotalTransPrice();
						response.getWriter().print(new JSONObject().put("totalTransPrice", orderBean.getOtotaltransprice()).toString());
						return;
					}
				}
				else if(dowhat.equals("submitOrder")) //点击“提交订单”
				{
					//将session中的orderBeanList存入数据库
					//同时删除session中购物车对应商品
					
					if(session.getAttribute(C.WRITEORDER_TOKEN) != null) //最正常地提交订单
					{	
						session.removeAttribute(C.WRITEORDER_TOKEN);
						
						//获取订单表、购物车、地址号
						List<OrderFormBean> orderBeanList=(ArrayList<OrderFormBean>)session.getAttribute(username+"_orderBeanList");
						List<BookInCartBean> bookInCartBeanList=(ArrayList<BookInCartBean>) session.getAttribute(username+"_bookInCartBeanList");
						String aid=request.getParameter("Aid");
						
						//将订单从session中购物车中删除+写入数据库
						removeSubmittedBookInCartAndAddOrder(username,aid,orderBeanList,bookInCartBeanList);
						request.setAttribute("result", "SUCCESS"); //成功标记
					}
					else
					{
						request.setAttribute("result", "INVALID"); //失败标记
					}
					
					//跳转到支付界面
					request.getRequestDispatcher("pay_page.jsp").forward(request, response);
					return;	
				}
			}
			
			//重复刷新提交
			System.out.println("刷新");
			request.getRequestDispatcher("writeorder_page.jsp").forward(request, response);
			return;	
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	//将书籍添加到订单表中
	//查找目前的订单里是否有与bookInCartBean.sid相同的店铺
	//若相同，追加进去
	//若未找到，新增至orderList尾
	void addToOrderList(BookInCartBean bookInCartBean,List<OrderFormBean> orderBeanList)
	{
		int j;
		for(j=0;j<orderBeanList.size();j++)
		{
			OrderFormBean oldOrder= orderBeanList.get(j); //j号订单
			
			if(bookInCartBean.getSid() == oldOrder.getBookInCartBeanList().get(0).getSid())
			{
				oldOrder.getBookInCartBeanList().add(bookInCartBean);
				break;
			}
		}
		if(j >= orderBeanList.size()) //未找到
		{
			//构建订单表（初始只含一本书）
			OrderFormBean newOrder=new OrderFormBean();
			newOrder.getBookInCartBeanList().add(bookInCartBean);
			
			//添加第一本书时获取店铺(订单)的id、图标、名称、单位运费
			ShopBeanCl shopBeanCl = new ShopBeanCl(); //以sid从店铺表中获取运费
			ShopBean shopBean = shopBeanCl.findShopBySid(bookInCartBean.getSid()); 
			newOrder.setSid(shopBean.getSid());
			newOrder.setSicon(shopBean.getSicon());
			newOrder.setSname(shopBean.getSname());
			newOrder.setStransprice(shopBean.getStransprice());
			
			//新增到orderList
			orderBeanList.add(newOrder);
		}	
	}
	
	//删除购物车中已提交过的商品、将订单写入到数据库
	void removeSubmittedBookInCartAndAddOrder(String username,String aid,
			List<OrderFormBean> orderBeanList,List<BookInCartBean> bookInCartBeanList){
		
		TransactionBeanCl transactionBeanCl=new TransactionBeanCl();
		OrderFormBeanCl orderBeanCl=new OrderFormBeanCl();
		
		if(orderBeanList != null && orderBeanList.size() > 0)
		{
			for(int orderIndex=0;orderIndex < orderBeanList.size();orderIndex++)
			{
				OrderFormBean orderBean=orderBeanList.get(orderIndex);
				List<BookInCartBean> submittedBookList=orderBean.getBookInCartBeanList(); //订单orderIndex
				String oid = createOrderNumber(username,orderIndex);
				
				//将该订单写入订单表
				orderBean.setOid(oid);
				orderBean.setUid(username);
				orderBean.setAid(aid);
				orderBeanCl.addOrder(orderBean);
				
				for(int sIndex=0;sIndex < submittedBookList.size();sIndex++) 
				{
					BookInCartBean submittedBook=submittedBookList.get(sIndex); //第sIndex本订了的书

					//将交易信息写入交易表
					TransactionBean transactionBean=new TransactionBean(C.RAND_NUM, submittedBook.getBid() ,oid,username, C.TRADESTATUS_WAITPAY, submittedBook.getTboughtnum(), 0,null, null, null,null,null);
					transactionBeanCl.addTrade(transactionBean);
					
					//从购物车中删除
					if(bookInCartBeanList != null && bookInCartBeanList.size() > 0)
					{
						for(int cIndex=0;cIndex < bookInCartBeanList.size();cIndex++)
						{
							BookInCartBean cartBook=bookInCartBeanList.get(cIndex);//购物车中的第cIndex本书
							if(submittedBook.getBid() == cartBook.getBid())
							{
								bookInCartBeanList.remove(cIndex); //从购物车中删除
								break;
							}
						}
					}
				}
			}
		}
	}
	
	//获取以username+orderIndex+当前时间作为的“订单号”
	String createOrderNumber(String username,int orderIndex)
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		return username + "_" + df.format(new Date()) + "_" + orderIndex;
	}
	
	//计算订单列表的运费+小计运费
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
