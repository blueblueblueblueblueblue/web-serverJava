package com.nuaa.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.nuaa.util.C;

/**
 * ������������ύ����ʱ��ӡ�ɾ���������޸Ķ�����
 */
public class OrderFormBeanCl extends OperDB{

	//ͨ��Oid��ȡ��������
	public OrderFormBean findOrderFormByOid(String oid)
	{
		OrderFormBean orderBean = new OrderFormBean();
		try {

			// ����
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();

			// �������
			String sql="select Oid,Orderform.Uid,Orderform.Sid,Aid,Ototalbooksprice,Ototaltransprice,date_format(Osubmittime,'%Y-%c-%d %H:%i:%s'),date_format(Opaytime,'%Y-%c-%d %H:%i:%s'),date_format(Oreceivetime,'%Y-%c-%d %H:%i:%s'),date_format(Ofinishedtime,'%Y-%c-%d %H:%i:%s'),Ostatus,Shop.Sicon,Shop.Sname from Orderform,Shop where Orderform.Oid=? and Orderform.Sid=Shop.Sid limit 1";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, oid);
			
			// ִ��
			// ����ѯ�õ��ļ�¼������鼮������Ϣ��
			resultSet = statement.executeQuery();
			if(resultSet.next()) {
				
				orderBean.setOid(resultSet.getString(1));
				orderBean.setUid(resultSet.getString(2));
				orderBean.setSid(resultSet.getInt(3));
				orderBean.setAid(resultSet.getString(4));
				orderBean.setOtotalbooksprice(resultSet.getFloat(5));
				orderBean.setOtotaltransprice(resultSet.getFloat(6));
				orderBean.setOsubmittime(resultSet.getString(7));
				orderBean.setOpaytime(resultSet.getString(8));
				orderBean.setOreceivetime(resultSet.getString(9));
				orderBean.setOfinishedtime(resultSet.getString(10));
				orderBean.setOstatus(resultSet.getInt(11));
				orderBean.setSicon("images/shopicons/" + resultSet.getString(12));
				orderBean.setSname(resultSet.getString(13));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return orderBean;	
	}
	
	
	
	//ͨ��Uid��ȡ����û��Ķ�����¼����
	public int getOrderPageCountByUid(String uid) {
		
		String sql = "select count(*) from Orderform where Uid='" + uid +"'";
		int rowCount=getRowCount(sql);
		
		return getPageCount(rowCount, C.ORDER_PAGESIZE);
	}
	
	//����sql��ѯOrderform���ж�Ӧ�ļ�¼��Ŀ
	public int getRowCount(String sql){
		
		int count=0;
		try {

			// ����
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();

			// �������
			statement = (PreparedStatement) connection.prepareStatement(sql);

			// ִ��
			// ����ѯ�õ��ļ�¼������鼮������Ϣ��
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
	
	
	
	//ͨ��username��ѯ�����ж���(ֻ��ѯ������Ϣ������ѯ�����е��鼮��ϢBookInCartBean��Ҫ��BookInCartBean����BookBeanCl���棬ͨ����������õ���Oid��Transaction��Oid�����ӵõ�Bid,����Bidȥȡ���鼮��Bname Boriprice Bprice Tboughtnum�ȣ���֮��󷴻�һ����С������ͼ����Щ�Ͳ����ˣ���BookInCartBean���Ͷ���)
	public ArrayList<OrderFormBean> findOrderFormsByUidByPage(String username,int pageNow)
	{
		ArrayList<OrderFormBean> orderFormBeanList = new ArrayList<OrderFormBean>();
		try {

			// ����
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();

			// �������
			String sql="select Oid,Orderform.Uid,Orderform.Sid,Aid,Ototalbooksprice,Ototaltransprice,date_format(Osubmittime,'%Y-%c-%d %H:%i:%s'),date_format(Opaytime,'%Y-%c-%d %H:%i:%s'),date_format(Oreceivetime,'%Y-%c-%d %H:%i:%s'),date_format(Ofinishedtime,'%Y-%c-%d %H:%i:%s'),Ostatus,Shop.Sicon,Shop.Sname from Orderform,Shop where Orderform.Uid=? and Orderform.Sid=Shop.Sid order by Osubmittime DESC limit " + C.ORDER_PAGESIZE * (pageNow - 1) + "," + C.ORDER_PAGESIZE;
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, username);
			
			// ִ��
			// ����ѯ�õ��ļ�¼������鼮������Ϣ��
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				
				OrderFormBean orderFormBean = new OrderFormBean();
				orderFormBean.setOid(resultSet.getString(1));
				orderFormBean.setUid(resultSet.getString(2));
				orderFormBean.setSid(resultSet.getInt(3));
				orderFormBean.setAid(resultSet.getString(4));
				orderFormBean.setOtotalbooksprice(resultSet.getFloat(5));
				orderFormBean.setOtotaltransprice(resultSet.getFloat(6));
				orderFormBean.setOsubmittime(resultSet.getString(7));
				orderFormBean.setOpaytime(resultSet.getString(8));
				orderFormBean.setOreceivetime(resultSet.getString(9));
				orderFormBean.setOfinishedtime(resultSet.getString(10));
				orderFormBean.setOstatus(resultSet.getInt(11));
				orderFormBean.setSicon("images/shopicons/" + resultSet.getString(12));
				orderFormBean.setSname(resultSet.getString(13));
				
				orderFormBeanList.add(orderFormBean);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return orderFormBeanList;
	}
	
	//ͨ��OrderList�е�Oid��ѯ�������������������鼮������γ��鼮�б�����orderList�е�bookList��ȥ��Ⱥ�壩
	public void findBookListInOrderList(ArrayList<OrderFormBean> orderList)
	{
		try {

			// ����
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();

			// �������
			String sql="select Book.Bid,Bimage,Bname,Boriprice,Bprice,Tboughtnum,Tstatus from Orderform,Transaction,Book where Orderform.Oid = ? and Orderform.Oid=Transaction.Oid and Transaction.Bid=Book.Bid";
			
			for(int i=0;i<orderList.size();i++)
			{
				OrderFormBean orderBean = orderList.get(i); //��ȡ����
				ArrayList<BookInCartBean> bookList = new ArrayList<BookInCartBean>(); //���ɸö��������鼮�����б�
				
				statement = (PreparedStatement) connection.prepareStatement(sql);
				statement.setString(1, orderBean.getOid());
				
				// ִ��
				resultSet = statement.executeQuery();
				while (resultSet.next()) {
					
					BookInCartBean book = new BookInCartBean(); //�����鼮
					book.setBid(resultSet.getString(1));
					book.setBimage("images/books/" + resultSet.getString(2));
					book.setBname(resultSet.getString(3));
					book.setBoriprice(resultSet.getFloat(4));
					book.setBprice(resultSet.getFloat(5));
					book.setTboughtnum(resultSet.getInt(6));
					book.setTstatus(resultSet.getInt(7));

					bookList.add(book);
				}
				
				orderBean.setBookInCartBeanList(bookList); //���鼮�б����ö�����
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	//ͨ��Oid��ѯ�������������������鼮������γ��鼮�б�����order�е�bookList��ȥ�����壩
	public void findBookListInOrder(OrderFormBean orderBean)
	{
		try {

			// ����
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();

			// �������
			String sql="select Book.Bid,Bimage,Bname,Boriprice,Bprice,Tboughtnum,Tstatus from Orderform,Transaction,Book where Orderform.Oid = ? and Orderform.Oid=Transaction.Oid and Transaction.Bid=Book.Bid";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, orderBean.getOid());
				
			// ִ��
			ArrayList<BookInCartBean> bookList = new ArrayList<BookInCartBean>(); //���ɸö��������鼮�����б�
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
					
					BookInCartBean book = new BookInCartBean(); //�����鼮
					book.setBid(resultSet.getString(1));
					book.setBimage("images/books/" + resultSet.getString(2));
					book.setBname(resultSet.getString(3));
					book.setBoriprice(resultSet.getFloat(4));
					book.setBprice(resultSet.getFloat(5));
					book.setTboughtnum(resultSet.getInt(6));
					book.setTstatus(resultSet.getInt(7));

					bookList.add(book);
				}
				
				orderBean.setBookInCartBeanList(bookList); //���鼮�б����ö�����

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	
	//����¶�����¼
	public void addOrder(OrderFormBean orderBean){
		
		try {
			//����
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();
		
			//�������
			String sql = "insert into orderform (Oid,Uid,Sid,Aid,Ototalbooksprice,Ototaltransprice,Osubmittime,Ostatus) values (?,?,?,?,?,?,?,?)";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, orderBean.getOid());
			statement.setString(2, orderBean.getUid());
			statement.setInt(3, orderBean.getSid());
			statement.setString(4, orderBean.getAid());
			statement.setFloat(5, orderBean.getOtotalbooksprice());
			statement.setFloat(6, orderBean.getOtotaltransprice());
			statement.setString(7, getCurTime());
			statement.setInt(8, orderBean.getOstatus());
			statement.executeUpdate();//ִ�и���
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}	
	}
	
	//�޸Ķ���״̬(status,ͬʱ����ǰʱ��)(Ⱥ)
	public void modifyOrderListStatusByOids(List<OrderFormBean> orderFormBeanList, int status)
	{
		//�ж�status���ĸ�״̬����������ʱ��
		String Otimename = "";
		switch(status)
		{
			case C.ORDERSTATUS_CONFIRMRECEIVED:Otimename="Opaytime";break;
			case C.ORDERSTATUS_WAITCOMMENT:Otimename="Oreceivetime";break;
			case C.ORDERSTATUS_FINISHED:Otimename="Ofinishedtime";break;
		}
		
		String sql = "update Orderform set Ostatus = "+ status +", " + Otimename + "=?"+" where Oid = ?";
		
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
	
	//�޸Ķ���״̬(status,ͬʱ����ǰʱ��)������
	public void modifyOrderStatusByOid(String oid, int status)
	{
		//�ж�status���ĸ�״̬����������ʱ��
		String Otimename = "";
		switch(status)
		{
			case C.ORDERSTATUS_CONFIRMRECEIVED:Otimename="Opaytime";break;
			case C.ORDERSTATUS_WAITCOMMENT:Otimename="Oreceivetime";break;
			case C.ORDERSTATUS_FINISHED:Otimename="Ofinishedtime";break;
		}

		try {
			
			//����
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();

			String sql = "update Orderform set Ostatus = ?," + Otimename + "=?"+" where Oid = ?";
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
	
	
	//ɾ��������Ϊoid�Ķ���
	public void deleteOrder(String oid)
	{
		try {

			// ����
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();

			// �������
			String sql = "delete from Orderform where Oid = '" + oid + "'";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.executeUpdate();
		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	//��ȡ��ǰϵͳʱ��,��ʽΪ     ��-��-�� ʱ:��:�루submittime paytime receivetimeʱ��¼��
	String getCurTime()
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(new Date());
	}
	
	//ͨ��rowCount��pageSize�����ܷ�ҳҳ��
	public int getPageCount(int rowCount,int pageSize){
		
		if (rowCount % pageSize== 0) //û�ж���
			return rowCount / pageSize;
		else
			return rowCount / pageSize + 1;
	}
}
