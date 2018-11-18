package com.nuaa.model;

import java.sql.ResultSet;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.nuaa.util.LoginResultEnum;
import com.nuaa.util.MailUtils;
import com.nuaa.util.RegisterResultEnum;

/**
 * User�����ݿ��������ʵ��
 * 
 * @author chicken
 *
 */
public class UserBeanCl extends OperDB{

	// �������û������ظ�����false��
	public RegisterResultEnum add(UserBean userBean){
		
		RegisterResultEnum result=null; //�Ƿ�ɹ�������û�
		try {
			//����
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();
			
			//�������
			String sql = "insert into User (Uid,Upassword) values (?,?)";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, userBean.getUid());
			statement.setString(2, userBean.getUpassword());
			MailUtils mail = new MailUtils();
			String href = "http://localhost:8080/UserActive?uid="+userBean.getUid();
			String text = " <a href=' "+href+" ' >������������˻�</a>";
			mail.send(userBean.getUid(),text);

			//ִ��
			result = statement.executeUpdate() != 0 ? RegisterResultEnum.SUCCESS : RegisterResultEnum.USERNAME_DUPLICATED;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return result;
	}

	//��ѯ�û��Ƿ���ڣ������������Ƿ�ƥ��
	public LoginResultEnum checkLogin(UserBean userBean)
	{
		LoginResultEnum result=null; //ƥ����
		try {
			//����
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();
			
			//�������
			String sql = "select * from User where Uid=? limit 1";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, userBean.getUid());

			//ִ��
			ResultSet resultSet=statement.executeQuery();
			
			if(!resultSet.next()) //�û���������
				result=LoginResultEnum.USERNAME_UNEXISTED;
			else if(!resultSet.getString(2).equals(userBean.getUpassword())||resultSet.getInt(3)==0)
				result=LoginResultEnum.PASSWORD_ERROR;
			else 
				result=LoginResultEnum.SUCCESS;	
				
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return result;	
	}
	
	//��user�е�uid��������Ϣ
	public void updateUser(UserBean user,String uavatar)
	{

		try {
			//����
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();
			
			//�������
			String sql = "update User set Uavatar = ? , Unickname = ? , Urealname = ? ,Usex = ? ,Uaddress = ? ,Uphone = ? ,Uemail = ? ,Ubirthday = ? where Uid = ?";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, uavatar);
			statement.setString(2, user.getUnickname());
			statement.setString(3, user.getUrealname());
			statement.setString(4, user.getUsex());
			statement.setString(5, user.getUaddress());
			statement.setString(6, user.getUphone());
			statement.setString(7, user.getUemail());
			statement.setString(8, user.getUbirthday());
			statement.setString(9, user.getUid());
			statement.executeUpdate();//ִ�и���
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	//��user�е�uid����
		public void activeUser(String uid)
		{

			try {
				//����
				ConnDB connDB = new ConnDB();
				connection = (Connection) connDB.getConnection();
				
				//�������
				String sql = "update User set Uactive = ?  where Uid = ?";
				statement = (PreparedStatement) connection.prepareStatement(sql);
				statement.setInt(1, 1);
				statement.setString(2, uid);
				statement.executeUpdate();//ִ�и���
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				close();
			}
		}
	
	//ͨ��uid�ҵ�User��������Ϣ
	public UserBean findUserByUid(String uid)
	{
		UserBean userBean = null;
		try {

			// ����
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();

			// �������
			String sql="select * from User where Uid=?";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, uid);
			
			// ִ��
			// ����ѯ�õ��ļ�¼������鼮������Ϣ��
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				userBean = new UserBean(resultSet.getString(1),null,resultSet.getString(4),
						resultSet.getString(5),resultSet.getString(6),resultSet.getString(7),resultSet.getString(8),
						resultSet.getString(9),resultSet.getString(10),resultSet.getString(11));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return userBean;	
	}
	
	public String getUavatarByUid(String uid)
	{
		String uavatar = "avatar.jpg";
		try {

			// ����
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();

			// �������
			String sql = "select Uavatar from User where Uid = ?";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, uid);

			// ִ��
			resultSet = statement.executeQuery();
			if (resultSet.next())
				uavatar = resultSet.getString(1);
				
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return uavatar;	
	}
}
