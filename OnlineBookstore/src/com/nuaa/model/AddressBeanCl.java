package com.nuaa.model;

import java.util.ArrayList;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.nuaa.util.C;

public class AddressBeanCl extends OperDB{
	
	//ͨ��Aid��ȡ��ַ
	public AddressBean getAddressByAid(String aid){
		
		AddressBean addressBean = null;
		try {

			// ����
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();

			// �������
			String sql="select * from Address where Aid=?";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, aid);
			
			// ִ��
			// ����ѯ�õ��ļ�¼������鼮������Ϣ��
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
	
	
	//ͨ���û���uid��ȡ����û������е�ַ
	public ArrayList<AddressBean> getAddressByUid(String uid){
		
		ArrayList<AddressBean> addressBeanList = new ArrayList<AddressBean>();
		try {

			// ����
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();

			// �������
			String sql="select * from Address where Uid=?";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, uid);
			
			// ִ��
			// ����ѯ�õ��ļ�¼������鼮������Ϣ��
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
	
	
	//����µ�ַ
	public void addAddress(AddressBean addressBean){
		
		try {
			//����
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();
			
			//�������
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
			statement.executeUpdate();//ִ�и���
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	//�޸ĵ�ַ��������ԭ�е�ַĿǰ���ڵ�ַ����λ��
	public int modifyAddress(AddressBean addressBean){
		
		int index=0;
		try {
			//����
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();
			
			//�������
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
			statement.executeUpdate();//ִ�и���
			
			//��ȡ�ղ����������aid
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
	
	
	
	//���username��ǰ��ַ��������<8����success,��>=8����address_overflow
	public int getAddressCount(String uid){
		
		int count=C.maxAddressNum;
		try {
			//����
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();
			
			//�������
			String sql = "select count(*) from Address where Uid=?";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, uid);

			//ִ��
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
	
	//���username��ַ�����һ����ַ��id
	public String getMaxAidByUid(String uid)
	{
		String maxAid = null;
		try {
			//����
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();
			
			//�������
			String sql = "select max(Aid) from Address where Uid=?";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, uid);

			//ִ��
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
	
	
	//ɾ��aid��ַ��ͬʱ��õ�һ����ַ
	public String deleteAddress(String username,String aid){
		
		String newCheckedAid=null;
		try {
			//����
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();
			
			//�������
			String sql = "delete from Address where Aid=?";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, aid);
			statement.executeUpdate();//ִ�и���
			
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
	
	
	//ѡ��aid��ַ�����Ȱ�֮ǰѡ�е���Ϊ��ѡ��
	public void checkAddress(String username,String aid){
		
		try {
			//����
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();
			
			//�������
			String sql = "update Address set Acheck=? where Uid=? and Acheck=?";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setInt(1, C.addressUnchecked);
			statement.setString(2, username);
			statement.setInt(3, C.addressChecked);
			statement.executeUpdate();//ִ�и���
			
			sql = "update Address set Acheck=? where Aid=?";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setInt(1, C.addressChecked);
			statement.setString(2, aid);
			statement.executeUpdate();//ִ�и���
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
	}
}
