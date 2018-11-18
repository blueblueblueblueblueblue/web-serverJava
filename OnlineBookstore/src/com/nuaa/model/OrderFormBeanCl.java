package com.nuaa.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.nuaa.util.C;

/**
 * 订单表操作（提交订单时添加、删除订单、修改订单）
 */
public class OrderFormBeanCl extends OperDB{

	//通过Oid获取订单对象
	public OrderFormBean findOrderFormByOid(String oid)
	{
		OrderFormBean orderBean = new OrderFormBean();
		try {

			// 连接
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();

			// 构建语句
			String sql="select Oid,Orderform.Uid,Orderform.Sid,Aid,Ototalbooksprice,Ototaltransprice,date_format(Osubmittime,'%Y-%c-%d %H:%i:%s'),date_format(Opaytime,'%Y-%c-%d %H:%i:%s'),date_format(Oreceivetime,'%Y-%c-%d %H:%i:%s'),date_format(Ofinishedtime,'%Y-%c-%d %H:%i:%s'),Ostatus,Shop.Sicon,Shop.Sname from Orderform,Shop where Orderform.Oid=? and Orderform.Sid=Shop.Sid limit 1";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, oid);
			
			// 执行
			// 将查询得到的记录添加至书籍快照信息表
			resultSet = statement.executeQuery();
			if(resultSet.next()) {
				
				orderBean.setOid(resultSet.getString(1));
				orderBean.setUid(resultSet.getString(2));
				orderBean.setSid(resultSet.getInt(3));
				orderBean.setAid(resultSet.getString(4));
				orderBean.setOtotalbooksprice(resultSet.getFloat(5));
				orderBean.setOtotaltransprice(resultSet.getFloat(6));
				orderBean.setOsubmittime(resultSet.getString(7));
				orderBean.setOpaytime(resultSet.getString(8));
				orderBean.setOreceivetime(resultSet.getString(9));
				orderBean.setOfinishedtime(resultSet.getString(10));
				orderBean.setOstatus(resultSet.getInt(11));
				orderBean.setSicon("images/shopicons/" + resultSet.getString(12));
				orderBean.setSname(resultSet.getString(13));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return orderBean;	
	}
	
	
	
	//通过Uid获取这个用户的订单记录总数
	public int getOrderPageCountByUid(String uid) {
		
		String sql = "select count(*) from Orderform where Uid='" + uid +"'";
		int rowCount=getRowCount(sql);
		
		return getPageCount(rowCount, C.ORDER_PAGESIZE);
	}
	
	//根据sql查询Orderform表中对应的记录数目
	public int getRowCount(String sql){
		
		int count=0;
		try {

			// 连接
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();

			// 构建语句
			statement = (PreparedStatement) connection.prepareStatement(sql);

			// 执行
			// 将查询得到的记录添加至书籍快照信息表
			resultSet = statement.executeQuery();
			if(resultSet.next())
				count=resultSet.getInt(1);
		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return count;
	}
	
	
	
	//通过username查询其所有订单(只查询订单信息，不查询订单中的书籍信息BookInCartBean，要查BookInCartBean，在BookBeanCl里面，通过这个函数得到的Oid与Transaction的Oid做连接得到Bid,再用Bid去取得书籍的Bname Boriprice Bprice Tboughtnum等，总之最后反回一个最小化（像图标这些就不用了）的BookInCartBean类型对象)
	public ArrayList<OrderFormBean> findOrderFormsByUidByPage(String username,int pageNow)
	{
		ArrayList<OrderFormBean> orderFormBeanList = new ArrayList<OrderFormBean>();
		try {

			// 连接
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();

			// 构建语句
			String sql="select Oid,Orderform.Uid,Orderform.Sid,Aid,Ototalbooksprice,Ototaltransprice,date_format(Osubmittime,'%Y-%c-%d %H:%i:%s'),date_format(Opaytime,'%Y-%c-%d %H:%i:%s'),date_format(Oreceivetime,'%Y-%c-%d %H:%i:%s'),date_format(Ofinishedtime,'%Y-%c-%d %H:%i:%s'),Ostatus,Shop.Sicon,Shop.Sname from Orderform,Shop where Orderform.Uid=? and Orderform.Sid=Shop.Sid order by Osubmittime DESC limit " + C.ORDER_PAGESIZE * (pageNow - 1) + "," + C.ORDER_PAGESIZE;
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, username);
			
			// 执行
			// 将查询得到的记录添加至书籍快照信息表
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				
				OrderFormBean orderFormBean = new OrderFormBean();
				orderFormBean.setOid(resultSet.getString(1));
				orderFormBean.setUid(resultSet.getString(2));
				orderFormBean.setSid(resultSet.getInt(3));
				orderFormBean.setAid(resultSet.getString(4));
				orderFormBean.setOtotalbooksprice(resultSet.getFloat(5));
				orderFormBean.setOtotaltransprice(resultSet.getFloat(6));
				orderFormBean.setOsubmittime(resultSet.getString(7));
				orderFormBean.setOpaytime(resultSet.getString(8));
				orderFormBean.setOreceivetime(resultSet.getString(9));
				orderFormBean.setOfinishedtime(resultSet.getString(10));
				orderFormBean.setOstatus(resultSet.getInt(11));
				orderFormBean.setSicon("images/shopicons/" + resultSet.getString(12));
				orderFormBean.setSname(resultSet.getString(13));
				
				orderFormBeanList.add(orderFormBean);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return orderFormBeanList;
	}
	
	//通过OrderList中的Oid查询这个订单所购买的所有书籍，最后形成书籍列表，接入orderList中的bookList中去（群体）
	public void findBookListInOrderList(ArrayList<OrderFormBean> orderList)
	{
		try {

			// 连接
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();

			// 构建语句
			String sql="select Book.Bid,Bimage,Bname,Boriprice,Bprice,Tboughtnum,Tstatus from Orderform,Transaction,Book where Orderform.Oid = ? and Orderform.Oid=Transaction.Oid and Transaction.Bid=Book.Bid";
			
			for(int i=0;i<orderList.size();i++)
			{
				OrderFormBean orderBean = orderList.get(i); //获取订单
				ArrayList<BookInCartBean> bookList = new ArrayList<BookInCartBean>(); //生成该订单所购书籍容器列表
				
				statement = (PreparedStatement) connection.prepareStatement(sql);
				statement.setString(1, orderBean.getOid());
				
				// 执行
				resultSet = statement.executeQuery();
				while (resultSet.next()) {
					
					BookInCartBean book = new BookInCartBean(); //生成书籍
					book.setBid(resultSet.getString(1));
					book.setBimage("images/books/" + resultSet.getString(2));
					book.setBname(resultSet.getString(3));
					book.setBoriprice(resultSet.getFloat(4));
					book.setBprice(resultSet.getFloat(5));
					book.setTboughtnum(resultSet.getInt(6));
					book.setTstatus(resultSet.getInt(7));

					bookList.add(book);
				}
				
				orderBean.setBookInCartBeanList(bookList); //将书籍列表加入该订单中
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	//通过Oid查询这个订单所购买的所有书籍，最后形成书籍列表，接入order中的bookList中去（单体）
	public void findBookListInOrder(OrderFormBean orderBean)
	{
		try {

			// 连接
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();

			// 构建语句
			String sql="select Book.Bid,Bimage,Bname,Boriprice,Bprice,Tboughtnum,Tstatus from Orderform,Transaction,Book where Orderform.Oid = ? and Orderform.Oid=Transaction.Oid and Transaction.Bid=Book.Bid";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, orderBean.getOid());
				
			// 执行
			ArrayList<BookInCartBean> bookList = new ArrayList<BookInCartBean>(); //生成该订单所购书籍容器列表
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
					
					BookInCartBean book = new BookInCartBean(); //生成书籍
					book.setBid(resultSet.getString(1));
					book.setBimage("images/books/" + resultSet.getString(2));
					book.setBname(resultSet.getString(3));
					book.setBoriprice(resultSet.getFloat(4));
					book.setBprice(resultSet.getFloat(5));
					book.setTboughtnum(resultSet.getInt(6));
					book.setTstatus(resultSet.getInt(7));

					bookList.add(book);
				}
				
				orderBean.setBookInCartBeanList(bookList); //将书籍列表接入该订单中

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	
	//添加新订单记录
	public void addOrder(OrderFormBean orderBean){
		
		try {
			//连接
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();
		
			//构建语句
			String sql = "insert into orderform (Oid,Uid,Sid,Aid,Ototalbooksprice,Ototaltransprice,Osubmittime,Ostatus) values (?,?,?,?,?,?,?,?)";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, orderBean.getOid());
			statement.setString(2, orderBean.getUid());
			statement.setInt(3, orderBean.getSid());
			statement.setString(4, orderBean.getAid());
			statement.setFloat(5, orderBean.getOtotalbooksprice());
			statement.setFloat(6, orderBean.getOtotaltransprice());
			statement.setString(7, getCurTime());
			statement.setInt(8, orderBean.getOstatus());
			statement.executeUpdate();//执行更新
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}	
	}
	
	//修改订单状态(status,同时捕获当前时间)(群)
	public void modifyOrderListStatusByOids(List<OrderFormBean> orderFormBeanList, int status)
	{
		//判断status是哪个状态，进而赋予时间
		String Otimename = "";
		switch(status)
		{
			case C.ORDERSTATUS_CONFIRMRECEIVED:Otimename="Opaytime";break;
			case C.ORDERSTATUS_WAITCOMMENT:Otimename="Oreceivetime";break;
			case C.ORDERSTATUS_FINISHED:Otimename="Ofinishedtime";break;
		}
		
		String sql = "update Orderform set Ostatus = "+ status +", " + Otimename + "=?"+" where Oid = ?";
		
		try {
			
			//连接
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();
			
			//构建语句
			for(int i=0;i<orderFormBeanList.size();i++)
			{
				OrderFormBean orderBean = orderFormBeanList.get(i);
				
				statement = (PreparedStatement) connection.prepareStatement(sql);
				statement.setString(1, getCurTime());
				statement.setString(2, orderBean.getOid());
				statement.executeUpdate();//执行更新
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}	
	}
	
	//修改订单状态(status,同时捕获当前时间)（单）
	public void modifyOrderStatusByOid(String oid, int status)
	{
		//判断status是哪个状态，进而赋予时间
		String Otimename = "";
		switch(status)
		{
			case C.ORDERSTATUS_CONFIRMRECEIVED:Otimename="Opaytime";break;
			case C.ORDERSTATUS_WAITCOMMENT:Otimename="Oreceivetime";break;
			case C.ORDERSTATUS_FINISHED:Otimename="Ofinishedtime";break;
		}

		try {
			
			//连接
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();

			String sql = "update Orderform set Ostatus = ?," + Otimename + "=?"+" where Oid = ?";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setInt(1, status);
			statement.setString(2, getCurTime());
			statement.setString(3, oid);
			statement.executeUpdate();//执行更新
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}	
	}
	
	
	//删除订单号为oid的订单
	public void deleteOrder(String oid)
	{
		try {

			// 连接
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();

			// 构建语句
			String sql = "delete from Orderform where Oid = '" + oid + "'";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.executeUpdate();
		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	//获取当前系统时间,格式为     年-月-日 时:分:秒（submittime paytime receivetime时记录）
	String getCurTime()
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(new Date());
	}
	
	//通过rowCount、pageSize计算总分页页数
	public int getPageCount(int rowCount,int pageSize){
		
		if (rowCount % pageSize== 0) //没有多余
			return rowCount / pageSize;
		else
			return rowCount / pageSize + 1;
	}
}
