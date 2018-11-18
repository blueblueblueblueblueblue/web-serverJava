package com.nuaa.model;

import java.sql.ResultSet;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.nuaa.util.LoginResultEnum;
import com.nuaa.util.MailUtils;
import com.nuaa.util.RegisterResultEnum;

/**
 * User表数据库操作具体实现
 * 
 * @author chicken
 *
 */
public class UserBeanCl extends OperDB{

	// 增加新用户（若重复返回false）
	public RegisterResultEnum add(UserBean userBean){
		
		RegisterResultEnum result=null; //是否成功添加新用户
		try {
			//连接
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();
			
			//构建语句
			String sql = "insert into User (Uid,Upassword) values (?,?)";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, userBean.getUid());
			statement.setString(2, userBean.getUpassword());
			MailUtils mail = new MailUtils();
			String href = "http://localhost:8080/UserActive?uid="+userBean.getUid();
			String text = " <a href=' "+href+" ' >点击激活您的账户</a>";
			mail.send(userBean.getUid(),text);

			//执行
			result = statement.executeUpdate() != 0 ? RegisterResultEnum.SUCCESS : RegisterResultEnum.USERNAME_DUPLICATED;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return result;
	}

	//查询用户是否存在，若存在密码是否匹配
	public LoginResultEnum checkLogin(UserBean userBean)
	{
		LoginResultEnum result=null; //匹配结果
		try {
			//连接
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();
			
			//构建语句
			String sql = "select * from User where Uid=? limit 1";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, userBean.getUid());

			//执行
			ResultSet resultSet=statement.executeQuery();
			
			if(!resultSet.next()) //用户名不存在
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
	
	//用user中的uid更新其信息
	public void updateUser(UserBean user,String uavatar)
	{

		try {
			//连接
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();
			
			//构建语句
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
			statement.executeUpdate();//执行更新
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	//用user中的uid激活
		public void activeUser(String uid)
		{

			try {
				//连接
				ConnDB connDB = new ConnDB();
				connection = (Connection) connDB.getConnection();
				
				//构建语句
				String sql = "update User set Uactive = ?  where Uid = ?";
				statement = (PreparedStatement) connection.prepareStatement(sql);
				statement.setInt(1, 1);
				statement.setString(2, uid);
				statement.executeUpdate();//执行更新
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				close();
			}
		}
	
	//通过uid找到User的所有信息
	public UserBean findUserByUid(String uid)
	{
		UserBean userBean = null;
		try {

			// 连接
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();

			// 构建语句
			String sql="select * from User where Uid=?";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, uid);
			
			// 执行
			// 将查询得到的记录添加至书籍快照信息表
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

			// 连接
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();

			// 构建语句
			String sql = "select Uavatar from User where Uid = ?";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, uid);

			// 执行
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
