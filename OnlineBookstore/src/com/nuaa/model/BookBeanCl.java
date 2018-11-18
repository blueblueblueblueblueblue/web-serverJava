package com.nuaa.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.nuaa.util.C;

public class BookBeanCl extends OperDB {

	
	//���ҷ������鼮
	public void addBook(BookDetailBean book)
	{
		try {
			//����
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();
			
			//�������
			String sql = "insert into Book values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, book.getBid());
			statement.setString(2, book.getBimage());
			statement.setFloat (3, book.getBprice());
			statement.setString(4, book.getBname());
			statement.setInt(5, book.getSid());
			statement.setString(6, book.getBauthor());
			statement.setString(7, book.getBpublisher());
			statement.setString(8, book.getBpublishdate());
			statement.setFloat(9, book.getBsalednum());
			statement.setFloat(10, book.getBcommentnum());
			statement.setFloat(11, book.getBoriprice());
			statement.setInt(12, book.getBversion());
			statement.setInt(13, book.getBpagenum());
			statement.setInt(14, book.getBwordnum());
			statement.setString(15, book.getBprintdate());
			statement.setInt(16, book.getBsize());
			statement.setString(17, book.getBpaperstyle());
			statement.setInt(18, book.getBprintnum());
			statement.setString(19, book.getBpackage());
			statement.setString(20, book.getBisbn());
			statement.setString(21, book.getBcontentsummary());
			statement.setString(22, book.getBauthorsummary());
			statement.setString(23, book.getBmediacomment());
			statement.setString(24, book.getBtastecontent());
			statement.setInt(25, book.getBstocknum());
			statement.setString(26, getCurTime());
			statement.setString(27, book.getBtype());
		
			statement.executeUpdate();//ִ�и���
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	
	/**
	 * �鼮Ԥ����������
	 */
	
	// ��ȡ���8����
	public ArrayList<BookSnapshotBean> getRandom8Books() {
		// sql�������ѯ8����¼
		String sql = "select Bid,Bimage,Bprice,Bname,Sname,Bsalednum,Bcommentnum from Book,Shop where Book.Sid=Shop.Sid order by rand() limit 8";

		return selectBookSnapshotBeanResultSet(sql);
	}

	// ���ؼ���key��ȡlike���鼮�б�
	public ArrayList<BookSnapshotBean> getBookSnapshotsByKey(String key, int pageNow,String refer) {
		// sql��ͨ��Sid���Ӳ�ѯBook��Shop����like key�ļ�¼	
		String sql = "select Bid,Bimage,Bprice,Bname,Sname,Bsalednum,Bcommentnum from Book,Shop where Book.Sid=Shop.Sid and (Bname like '%"
					+ key + "%' or Bauthor like '%" + key +"%' or Bpublisher like '%"+ key +"%' or Sname like '%" + key +"%') order by "+ refer +" limit " + C.bookSnapshotPageSize * (pageNow - 1) + "," + C.bookSnapshotPageSize;			

		return selectBookSnapshotBeanResultSet(sql);
	}
	
	//�������type��ȡ���鼮�б�
	public ArrayList<BookSnapshotBean> getBookSnapshotsByType(String type, int pageNow,String refer) {
		// sql��ͨ��Sid���Ӳ�ѯBook��Shop����= type�ļ�¼	
		String sql = "select Bid,Bimage,Bprice,Bname,Sname,Bsalednum,Bcommentnum from Book,Shop where Btype = '"+ type +"' and Book.Sid=Shop.Sid order by "+ refer +" limit " + C.bookSnapshotPageSize * (pageNow - 1) + "," + C.bookSnapshotPageSize;			

		return selectBookSnapshotBeanResultSet(sql);
	}	
	
	/**
	 * �鼮���鲿��
	 */
	
	//��ȡ�鼮����bean
	public BookDetailBean getBookDetailById(String bid){
		
		BookDetailBean bookDetailBean = null; // �洢�����鼮�����Ҫ����Ϣ
		try {

			// ����
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();

			// �������
			String sql = "select Book.Bid, Bimage, Bprice, Bname, Shop.Sid, Sname, Bsalednum, Bcommentnum, Bauthor, Bpublisher, Bpublishdate, Boriprice, Shop.Stransprice, Sactivity, AVG(Tmark), Bversion, Bpagenum, Bwordnum, Bprintdate, Bsize, Bpaperstyle, Bprintnum, Bpackage, Bisbn, Bcontentsummary, Bauthorsummary, Bmediacomment, Btastecontent,Shop.Sicon from Book,Shop,Transaction where Book.Bid ='"+bid+"' and Book.Bid = Transaction.Bid and Tstatus >= "+ C.TRADESTATUS_WAITCOMMENT +" and Book.Sid=Shop.Sid limit 1";
			statement = (PreparedStatement) connection.prepareStatement(sql);

			// ִ��
			// ����ѯ�õ��ļ�¼������鼮������Ϣ��
			resultSet = statement.executeQuery();
			if (resultSet.next())
				bookDetailBean=new BookDetailBean(resultSet.getString(1), resultSet.getString(2),
						resultSet.getFloat(3), resultSet.getString(4), resultSet.getInt(5), 
						resultSet.getString(6),resultSet.getInt(7),resultSet.getInt(8),
						resultSet.getString(9),resultSet.getString(10),
						resultSet.getString(11),resultSet.getFloat(12),resultSet.getFloat(13),
						resultSet.getString(14),resultSet.getInt(15),resultSet.getInt(16),resultSet.getInt(17),
						resultSet.getInt(18),resultSet.getString(19),resultSet.getInt(20),
						resultSet.getString(21),resultSet.getInt(22),resultSet.getString(23),
						resultSet.getString(24),resultSet.getString(25),resultSet.getString(26),
						resultSet.getString(27),resultSet.getString(28),resultSet.getString(29),0,"","");
	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return bookDetailBean;	
	}
	
	//��ȡ������Ϣ��
	public ArrayList<BookCommentBean> getBookCommentByIdAndPage(String Bid,int pageNow)
	{
		ArrayList<BookCommentBean> bookCommentBeanList = new ArrayList<BookCommentBean>();
		try {

			// ����
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();

			// �������
			String sql="select Uavatar,Unickname,Tcomment,date_format(Tcommenttime,'%Y-%c-%d %H:%i:%s') from Transaction,User where Bid='"+Bid+"' and Transaction.Uid=User.Uid and Tcomment != '' order by Tcommenttime DESC limit " + C.bookCommentPageSize * (pageNow - 1) + "," + C.bookCommentPageSize;
			statement = (PreparedStatement) connection.prepareStatement(sql);

			// ִ��
			// ����ѯ�õ��ļ�¼������鼮������Ϣ��
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				bookCommentBeanList.add(new BookCommentBean(resultSet.getString(1),resultSet.getString(2)
						,resultSet.getString(3),resultSet.getString(4)));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return bookCommentBeanList;
	}
	
	//��ȡ�ɽ���¼��Ϣ��
	public ArrayList<BookTradeRecordBean> getBookTradeRecordByIdAndPage(String Bid,int pageNow)
	{
		ArrayList<BookTradeRecordBean> bookTradeRecordBeanList = new ArrayList<BookTradeRecordBean>();
		try {

			// ����
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();

			// �������
			String sql="select Uavatar,Unickname,Tboughtnum,date_format(Treceivetime,'%Y-%c-%d %H:%i:%s') from Transaction,User where Bid='"+Bid+"' and Tstatus >= "+ C.TRADESTATUS_WAITCOMMENT +" and Transaction.Uid = User.Uid order by Treceivetime DESC limit " + C.bookTradeRecordPageSize * (pageNow - 1) + "," + C.bookTradeRecordPageSize;
			statement = (PreparedStatement) connection.prepareStatement(sql);

			// ִ��
			// ����ѯ�õ��ļ�¼������鼮������Ϣ��
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				bookTradeRecordBeanList.add(new BookTradeRecordBean(resultSet.getString(1),resultSet.getString(2)
						,resultSet.getInt(3),resultSet.getString(4)));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return bookTradeRecordBeanList;
	}
	
	//��ȡuid���������ѷ������鼮�������
	public String getMaxBidBySid(int sid)
	{
		String maxBid = null;
		try {
			//����
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();
			
			//�������
			String sql = "select max(Bid) from Book where Sid=?";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setInt(1, sid);

			//ִ��
			resultSet = statement.executeQuery();
			if(resultSet.next())
				maxBid=resultSet.getString(1);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
		return maxBid;	
	}
	
	//oid�����е��鼮�ɽ���ȫ��+1
	public void incSaledNum(String oid)
	{
		try {
			//����
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();
			
			//�������
			String sql = "update Book,Transaction set Bsalednum = Bsalednum + 1 where Book.Bid in (select Transaction.Bid from Transaction where Transaction.Oid = ?)";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, oid);
			statement.executeUpdate();//ִ�и���

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}	
	}
	
	public void incCommentNum(String bid)
	{
		try {
			//����
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();
			
			//�������
			String sql = "update Book set Bcommentnum = Bcommentnum + 1 where Bid = ?";
			statement = (PreparedStatement) connection.prepareStatement(sql);
			statement.setString(1, bid);
			statement.executeUpdate();//ִ�и���

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}	
	}
	
	
	
	
	/**
	 * ��ѯͨ�÷���
	 * 
	 */

	
	//��ѯBookSnapshotBean��Ľ�����ϵķ���
	public ArrayList<BookSnapshotBean> selectBookSnapshotBeanResultSet(String sql) {
		ArrayList<BookSnapshotBean> bookSnapshotList = new ArrayList<BookSnapshotBean>();
		try {

			// ����
			ConnDB connDB = new ConnDB();
			connection = (Connection) connDB.getConnection();

			// �������
			statement = (PreparedStatement) connection.prepareStatement(sql);

			// ִ��
			// ����ѯ�õ��ļ�¼������鼮������Ϣ��
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				bookSnapshotList.add(new BookSnapshotBean(resultSet.getString(1), resultSet.getString(2),
						resultSet.getFloat(3), resultSet.getString(4), resultSet.getString(5), resultSet.getInt(6), resultSet.getInt(7)));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}

		return bookSnapshotList;
	}
	
	
	/**
	 * ��ҳ���
	 */
	
	/*�鼮Ԥ������*/
	
	//����sql��ѯBook���ж�Ӧ�ļ�¼��Ŀ
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
	
	//���ݼ�¼��rowCount��pageSize����Ӧ�ñ��ֳɵ���ҳ��
	public int getPageCount(int rowCount,int pageSize){

		if (rowCount % pageSize== 0) //û�ж���
			return rowCount / pageSize;
		else
			return rowCount / pageSize + 1;
	}
	
	//��ȡkey�鼮snapshotӦ�÷ֳɵ���ҳ��
	public int getBookSnapshotsPageCountByKey(String key) {
		
		String sql = "select count(*) from Book,Shop where Book.Sid = Shop.Sid and (Bname like '%"+key+"%' or Bauthor like '%" + key +"%' or Bpublisher like '%"+ key +"%' or Sname like '%"+ key +"%')";
		int rowCount=getRowCount(sql);

		return getPageCount(rowCount, C.bookSnapshotPageSize);
	}
	
	//��ȡtype�鼮snapshotӦ�÷ֳɵ���ҳ��
	public int getBookSnapshotsPageCountByType(String type){
		
		String sql = "select count(*) from Book where Btype = '"+ type +"'";
		int rowCount=getRowCount(sql);
		
		return getPageCount(rowCount, C.bookSnapshotPageSize);
	}

	/*�鼮���鲿��*/

	//��ȡ��ǰϵͳʱ��,��ʽΪ     ��-��-�� ʱ:��:�루submittime paytime receivetimeʱ��¼��
	String getCurTime()
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(new Date());
	}
}
