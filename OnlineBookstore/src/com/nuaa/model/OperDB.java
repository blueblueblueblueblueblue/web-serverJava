package com.nuaa.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

/**
 * 操纵数据库共性属性与方法
 * 
 * @author chicken
 *
 */
public class OperDB {
	protected Connection connection = null;
	protected PreparedStatement statement = null;
	protected ResultSet resultSet = null;

	public void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
				resultSet = null;
			}
			if (statement != null) {
				statement.close();
				statement = null;
			}

			if (connection != null) {
				connection.close();
				connection = null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
