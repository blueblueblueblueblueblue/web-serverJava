package com.nuaa.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.nuaa.util.C;

/**
 * �������ױ��ύ����ʱ���Ӽ�¼����ɾ������ʱɾ�����׼�¼�ȣ�
 */

public class TransactionBeanCl extends OperDB{

	//��transactionBean�������׼�¼�����transaction���ױ� 
	public void addTrade(TransactionBean transactionBean){
		
		try {
			//����
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();
			
			//�������
			String sql = "insert into Transaction (Bid,Oid,Uid,Tstatus,Tboughtnum,Tsubmittime) values (?,?,?,?,?,?)";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, transactionBean.getBid());
			statement.setString(2, transactionBean.getOid());
			statement.setString(3, transactionBean.getUid());
			statement.setInt(4, transactionBean.getTstatus());
			statement.setInt(5, transactionBean.getTboughtnum());
			statement.setString(6, getCurTime());
			statement.executeUpdate();//ִ�и���
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}	
	}
	
	//�޸��Ѷ��鼮�Ľ���״̬(�ȴ�֧�����ȴ��ջ����ȴ�����)
	public void modifyTradeStatusByOids(List<OrderFormBean> orderFormBeanList, int status)
	{
		//�ж�status���ĸ�״̬����������ʱ��
		String Ttimename = "";
		switch(status)
		{
			case C.TRADESTATUS_CONFIRMRECEIVED:Ttimename="Tpaytime";break;
			case C.TRADESTATUS_WAITCOMMENT:Ttimename="Treceivetime";break;
			case C.TRADESTATUS_FINISHED:Ttimename="Tcommenttime";break;
		}

		String sql = "update Transaction set Tstatus = "+ status +","+ Ttimename + "=? where Oid = ?";	
		try {
			//����
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();
			
			//�������
			for(int i=0;i<orderFormBeanList.size();i++)
			{
				OrderFormBean orderBean = orderFormBeanList.get(i);

				statement = (PreparedStatement) connection.prepareStatement(sql);
				statement.setString(1, getCurTime());
				statement.setString(2, orderBean.getOid());
				statement.executeUpdate();//ִ�и���
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}	
	}
	
	public void modifyTradeStatusByOid(String oid, int status)
	{
		//�ж�status���ĸ�״̬����������ʱ��
		String Ttimename = "";
		switch(status)
		{
			case C.TRADESTATUS_CONFIRMRECEIVED:Ttimename="Tpaytime";break;
			case C.TRADESTATUS_WAITCOMMENT:Ttimename="Treceivetime";break;
			case C.TRADESTATUS_FINISHED:Ttimename="Tcommenttime";break;
		}
		
		
		try {
			//����
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();
			
			//�������
			String sql = "update Transaction set Tstatus = ? ,"+ Ttimename +"=? where Oid = ?";
			
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setInt(1, status);
			statement.setString(2, getCurTime());
			statement.setString(3, oid);
			statement.executeUpdate();//ִ�и���
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}	
	}
	
	//ͨ��oid bid�鵽��Ӧ���ף�Ȼ���޸ĸý��׵�״̬�����ǡ����ۡ�����ʱ��
	public void updateTransactionByOidByBid(String oid, String bid, int status, int mark, String comment)
	{
		try {
			//����
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();
			
			//�������
			String sql = "update Transaction set Tstatus = ?, Tmark = ?, Tcomment = ?,Tcommenttime = ? where Oid = ? and Bid = ?";
			
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setInt(1, status);
			statement.setInt(2, mark);
			statement.setString(3, comment);
			statement.setString(4, getCurTime());
			statement.setString(5, oid);
			statement.setString(6, bid);
			
			statement.executeUpdate();//ִ�и���
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	//ͨ��oid��ѯ��������������鼮��״̬
	public ArrayList<Integer> findTransactionStatusListByOid(String oid)
	{
		ArrayList<Integer> transactionStatusList = new ArrayList<Integer>();
		
		try {
			//����
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();
			
			//�������
			String sql = "select Tstatus from Transaction where Oid = ?";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, oid);
			resultSet = statement.executeQuery();//ִ�и���
			
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
	
	//��ȡ��ǰϵͳʱ��,��ʽΪ     ��-��-�� ʱ:��:�루submittime paytime receivetimeʱ��¼��
	String getCurTime()
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(new Date());
	}
}
