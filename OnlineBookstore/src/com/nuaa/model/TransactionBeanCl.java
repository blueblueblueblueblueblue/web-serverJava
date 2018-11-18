package com.nuaa.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.nuaa.util.C;

/**
 * 操作交易表，提交订单时增加记录（或删除订单时删除交易记录等）
 */

public class TransactionBeanCl extends OperDB{

	//将transactionBean所含交易记录添加至transaction交易表 
	public void addTrade(TransactionBean transactionBean){
		
		try {
			//连接
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();
			
			//构建语句
			String sql = "insert into Transaction (Bid,Oid,Uid,Tstatus,Tboughtnum,Tsubmittime) values (?,?,?,?,?,?)";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, transactionBean.getBid());
			statement.setString(2, transactionBean.getOid());
			statement.setString(3, transactionBean.getUid());
			statement.setInt(4, transactionBean.getTstatus());
			statement.setInt(5, transactionBean.getTboughtnum());
			statement.setString(6, getCurTime());
			statement.executeUpdate();//执行更新
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}	
	}
	
	//修改已订书籍的交易状态(等待支付、等待收货、等待评价)
	public void modifyTradeStatusByOids(List<OrderFormBean> orderFormBeanList, int status)
	{
		//判断status是哪个状态，进而赋予时间
		String Ttimename = "";
		switch(status)
		{
			case C.TRADESTATUS_CONFIRMRECEIVED:Ttimename="Tpaytime";break;
			case C.TRADESTATUS_WAITCOMMENT:Ttimename="Treceivetime";break;
			case C.TRADESTATUS_FINISHED:Ttimename="Tcommenttime";break;
		}

		String sql = "update Transaction set Tstatus = "+ status +","+ Ttimename + "=? where Oid = ?";	
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
	
	public void modifyTradeStatusByOid(String oid, int status)
	{
		//判断status是哪个状态，进而赋予时间
		String Ttimename = "";
		switch(status)
		{
			case C.TRADESTATUS_CONFIRMRECEIVED:Ttimename="Tpaytime";break;
			case C.TRADESTATUS_WAITCOMMENT:Ttimename="Treceivetime";break;
			case C.TRADESTATUS_FINISHED:Ttimename="Tcommenttime";break;
		}
		
		
		try {
			//连接
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();
			
			//构建语句
			String sql = "update Transaction set Tstatus = ? ,"+ Ttimename +"=? where Oid = ?";
			
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
	
	//通过oid bid查到对应交易，然后修改该交易的状态、评星、评价、评价时间
	public void updateTransactionByOidByBid(String oid, String bid, int status, int mark, String comment)
	{
		try {
			//连接
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();
			
			//构建语句
			String sql = "update Transaction set Tstatus = ?, Tmark = ?, Tcomment = ?,Tcommenttime = ? where Oid = ? and Bid = ?";
			
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setInt(1, status);
			statement.setInt(2, mark);
			statement.setString(3, comment);
			statement.setString(4, getCurTime());
			statement.setString(5, oid);
			statement.setString(6, bid);
			
			statement.executeUpdate();//执行更新
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	//通过oid查询这个订单下所购书籍的状态
	public ArrayList<Integer> findTransactionStatusListByOid(String oid)
	{
		ArrayList<Integer> transactionStatusList = new ArrayList<Integer>();
		
		try {
			//连接
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();
			
			//构建语句
			String sql = "select Tstatus from Transaction where Oid = ?";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, oid);
			resultSet = statement.executeQuery();//执行更新
			
			while (resultSet.next()) {

				transactionStatusList.add(resultSet.getInt(1));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return transactionStatusList;
	}
	
	//获取当前系统时间,格式为     年-月-日 时:分:秒（submittime paytime receivetime时记录）
	String getCurTime()
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(new Date());
	}
}
