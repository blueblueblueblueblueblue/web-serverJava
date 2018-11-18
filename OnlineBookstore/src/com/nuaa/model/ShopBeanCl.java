package com.nuaa.model;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.nuaa.util.C;

public class ShopBeanCl extends OperDB{

	//添加一个uid开设的店铺,sid为自动增长
	public void addShop(String uid)
	{
		try {
			//连接
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();
			
			//构建语句
			String sql = "insert into Shop (Uid) values (?)";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, uid);
			
			statement.executeUpdate();//执行更新
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	
	//通过sid查询店铺信息
	public ShopBean findShopBySid(int sid)
	{
		ShopBean shopBean = null;
		try {

			// 连接
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();

			// 构建语句
			String sql = "select * from Shop where Sid = " + sid + " limit 1";
			statement = (PreparedStatement) connection.prepareStatement(sql);

			// 执行
			resultSet = statement.executeQuery();
			if (resultSet.next())
				shopBean=new ShopBean(resultSet.getInt(1), resultSet.getString(2),
						resultSet.getString(3),resultSet.getString(4),resultSet.getString(5),
						resultSet.getString(6),resultSet.getFloat(7));
				
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return shopBean;		
	}
	
	//查找uid用户开的店铺号sid
	public int getSidByUid(String uid)
	{
		int sid = C.OPENED_SHOP_NO;
		try {

			// 连接
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();

			// 构建语句
			String sql = "select Sid from Shop where Uid = ?";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, uid);

			// 执行
			resultSet = statement.executeQuery();
			if (resultSet.next())
				sid = resultSet.getInt(1);
				
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return sid;	
	}
	
	//用shop中的sid找到并更新其信息
	public void updateShop(ShopBean shop,String sicon)
	{

		try {
			//连接
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();
			
			//构建语句
			String sql = "update Shop set Sname = ? , Sicon = ? , Ssummary = ? ,Sactivity = ? ,Stransprice = ? where Sid = ?";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, shop.getSname());
			statement.setString(2, sicon);
			statement.setString(3, shop.getSsummary());
			statement.setString(4, shop.getSactivity());
			statement.setFloat(5, shop.getStransprice());
			statement.setInt(6, shop.getSid());
			statement.executeUpdate();//执行更新
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	public String getSiconBySid(int sid)
	{
		String sicon = "shopicon.jpg";
		try {

			// 连接
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();

			// 构建语句
			String sql = "select Sicon from Shop where Sid = ?";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setInt(1, sid);

			// 执行
			resultSet = statement.executeQuery();
			if (resultSet.next())
				sicon = resultSet.getString(1);
				
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return sicon;	
	}
}
