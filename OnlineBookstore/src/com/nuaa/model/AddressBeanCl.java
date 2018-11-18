package com.nuaa.model;

import java.util.ArrayList;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.nuaa.util.C;

public class AddressBeanCl extends OperDB{
	
	//通过Aid获取地址
	public AddressBean getAddressByAid(String aid){
		
		AddressBean addressBean = null;
		try {

			// 连接
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();

			// 构建语句
			String sql="select * from Address where Aid=?";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, aid);
			
			// 执行
			// 将查询得到的记录添加至书籍快照信息表
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				addressBean = new AddressBean(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),
						resultSet.getString(4),resultSet.getString(5),resultSet.getString(6),resultSet.getString(7),
						resultSet.getString(8),resultSet.getString(9),resultSet.getString(10),resultSet.getInt(11));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return addressBean;
	}
	
	
	//通过用户名uid获取这个用户的所有地址
	public ArrayList<AddressBean> getAddressByUid(String uid){
		
		ArrayList<AddressBean> addressBeanList = new ArrayList<AddressBean>();
		try {

			// 连接
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();

			// 构建语句
			String sql="select * from Address where Uid=?";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, uid);
			
			// 执行
			// 将查询得到的记录添加至书籍快照信息表
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				addressBeanList.add(new AddressBean(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),
						resultSet.getString(4),resultSet.getString(5),resultSet.getString(6),resultSet.getString(7),
						resultSet.getString(8),resultSet.getString(9),resultSet.getString(10),resultSet.getInt(11)));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return addressBeanList;
	}	
	
	
	//添加新地址
	public void addAddress(AddressBean addressBean){
		
		try {
			//连接
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();
			
			//构建语句
			String sql = "insert into Address (Aid,Uid,Areceivername,Aaddress,Acode,Aphone,Afixphone,Aprovince,Acity,Atown,Acheck) values (?,?,?,?,?,?,?,?,?,?,?)";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, addressBean.getAid());
			statement.setString(2, addressBean.getUid());
			statement.setString(3, addressBean.getAreceivername());
			statement.setString(4, addressBean.getAaddress());
			statement.setString(5, addressBean.getAcode());
			statement.setString(6, addressBean.getAphone());
			statement.setString(7, addressBean.getAfixphone());
			statement.setString(8, addressBean.getAprovince());
			statement.setString(9, addressBean.getAcity());
			statement.setString(10, addressBean.getAtown());
			statement.setInt(11, addressBean.getAcheck());
			statement.executeUpdate();//执行更新
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	//修改地址，并返回原有地址目前处在地址栏的位置
	public int modifyAddress(AddressBean addressBean){
		
		int index=0;
		try {
			//连接
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();
			
			//构建语句
			String sql = "update Address set Areceivername = ? , Aaddress = ? , Acode = ? ,Aphone = ? ,Afixphone = ? ,Aprovince = ? ,Acity = ? ,Atown = ? where Aid = ?";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, addressBean.getAreceivername());
			statement.setString(2, addressBean.getAaddress());
			statement.setString(3, addressBean.getAcode());
			statement.setString(4, addressBean.getAphone());
			statement.setString(5, addressBean.getAfixphone());
			statement.setString(6, addressBean.getAprovince());
			statement.setString(7, addressBean.getAcity());
			statement.setString(8, addressBean.getAtown());
			statement.setString(9, addressBean.getAid());
			statement.executeUpdate();//执行更新
			
			//获取刚插入的自增长aid
			sql="select count(*) from Address where Uid = ? and Aid < ?";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, addressBean.getUid());
			statement.setString(2, addressBean.getAid());
			
			resultSet = statement.executeQuery();
			if(resultSet.next())
				index=resultSet.getInt(1);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return index;	
	}
	
	
	
	//获得username当前地址数量，若<8返回success,若>=8返回address_overflow
	public int getAddressCount(String uid){
		
		int count=C.maxAddressNum;
		try {
			//连接
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();
			
			//构建语句
			String sql = "select count(*) from Address where Uid=?";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, uid);

			//执行
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
	
	//获得username地址中最后一个地址的id
	public String getMaxAidByUid(String uid)
	{
		String maxAid = null;
		try {
			//连接
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();
			
			//构建语句
			String sql = "select max(Aid) from Address where Uid=?";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, uid);

			//执行
			resultSet = statement.executeQuery();
			if(resultSet.next())
				maxAid=resultSet.getString(1);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return maxAid;	
	}
	
	
	//删除aid地址，同时获得第一个地址
	public String deleteAddress(String username,String aid){
		
		String newCheckedAid=null;
		try {
			//连接
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();
			
			//构建语句
			String sql = "delete from Address where Aid=?";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, aid);
			statement.executeUpdate();//执行更新
			
			sql = "select Aid from Address where Uid=? limit 1";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, username);
			resultSet = statement.executeQuery();
			if(resultSet.next())
				newCheckedAid=resultSet.getString(1);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return newCheckedAid;
	}
	
	
	//选中aid地址，但先把之前选中的置为不选中
	public void checkAddress(String username,String aid){
		
		try {
			//连接
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();
			
			//构建语句
			String sql = "update Address set Acheck=? where Uid=? and Acheck=?";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setInt(1, C.addressUnchecked);
			statement.setString(2, username);
			statement.setInt(3, C.addressChecked);
			statement.executeUpdate();//执行更新
			
			sql = "update Address set Acheck=? where Aid=?";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setInt(1, C.addressChecked);
			statement.setString(2, aid);
			statement.executeUpdate();//执行更新
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
	}
}
