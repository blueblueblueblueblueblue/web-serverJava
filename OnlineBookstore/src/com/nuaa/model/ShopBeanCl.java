package com.nuaa.model;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.nuaa.util.C;

public class ShopBeanCl extends OperDB{

	//���һ��uid����ĵ���,sidΪ�Զ�����
	public void addShop(String uid)
	{
		try {
			//����
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();
			
			//�������
			String sql = "insert into Shop (Uid) values (?)";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, uid);
			
			statement.executeUpdate();//ִ�и���
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	
	//ͨ��sid��ѯ������Ϣ
	public ShopBean findShopBySid(int sid)
	{
		ShopBean shopBean = null;
		try {

			// ����
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();

			// �������
			String sql = "select * from Shop where Sid = " + sid + " limit 1";
			statement = (PreparedStatement) connection.prepareStatement(sql);

			// ִ��
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
	
	//����uid�û����ĵ��̺�sid
	public int getSidByUid(String uid)
	{
		int sid = C.OPENED_SHOP_NO;
		try {

			// ����
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();

			// �������
			String sql = "select Sid from Shop where Uid = ?";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, uid);

			// ִ��
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
	
	//��shop�е�sid�ҵ�����������Ϣ
	public void updateShop(ShopBean shop,String sicon)
	{

		try {
			//����
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();
			
			//�������
			String sql = "update Shop set Sname = ? , Sicon = ? , Ssummary = ? ,Sactivity = ? ,Stransprice = ? where Sid = ?";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, shop.getSname());
			statement.setString(2, sicon);
			statement.setString(3, shop.getSsummary());
			statement.setString(4, shop.getSactivity());
			statement.setFloat(5, shop.getStransprice());
			statement.setInt(6, shop.getSid());
			statement.executeUpdate();//ִ�и���
			
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

			// ����
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();

			// �������
			String sql = "select Sicon from Shop where Sid = ?";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setInt(1, sid);

			// ִ��
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
