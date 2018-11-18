package com.nuaa.model;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * �������ݿ�
 * 
 * @author chicken
 *
 */
public class ConnDB {

	private final String driverName = "com.mysql.jdbc.Driver";// ���ݿ���������
	private final String urlPre = "jdbc:mysql://localhost:3306/";// ��ַǰ׺
	private final String databaseName = "online_bookstore"; // ���ݿ�����
	private final String urlSuff = "?characterEncoding=UTF-8"; // �����ʽ
	private final String userName = "root"; // �û���
	private final String password = "admin"; // ����
	private Connection conn = null; // ���ݿ�����

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
