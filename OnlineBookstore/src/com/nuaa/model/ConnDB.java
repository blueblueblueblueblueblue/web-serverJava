package com.nuaa.model;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * 连接数据库
 * 
 * @author chicken
 *
 */
public class ConnDB {

	private final String driverName = "com.mysql.jdbc.Driver";// 数据库驱动类名
	private final String urlPre = "jdbc:mysql://localhost:3306/";// 地址前缀
	private final String databaseName = "online_bookstore"; // 数据库名称
	private final String urlSuff = "?characterEncoding=UTF-8"; // 编码格式
	private final String userName = "root"; // 用户名
	private final String password = "admin"; // 密码
	private Connection conn = null; // 数据库连接

	public Connection getConnection() {
		try {
			Class.forName(driverName);
			conn = DriverManager.getConnection(urlPre + databaseName + urlSuff, userName, password);
		} catch (Exception e) {
			e.getMessage();
		}
		return conn;
	}
}
