package com.nuaa.util;

/**
 * �־ó�������
 * @author chicken
 *
 */
public class C {
	public static final int bookSnapshotPageSize = 20;// һҳ��Ʒ��ʾ��Ŀ
	public static final int bookCommentPageSize =5;// һҳ������ʾ��Ŀ
	public static final int bookTradeRecordPageSize = 10;// һҳ�ɽ���¼��ʾ��Ŀ
	public static final int ORDER_PAGESIZE = 4;// һҳ������¼��ʾ��Ŀ
	
	public static final int pageRange=5;//�����ʾ��ҳ�������1-5 6-11
	public static final int maxAddressNum=8;//ÿ���û����8����ַ
	
	public static final int addressUnchecked=0;//��ַδѡ��
	public static final int addressChecked=1;//��ַѡ��
	
	public static final int TRANS_UNIT_NUM=3; //�˷�ÿTRANS_UNIT_NUM������һ��
	
	public static final int RAND_NUM=-1;
	
	//����״̬��Order��
	public static final int ORDERSTATUS_WAITPAY=1; //�ȴ�֧��
	public static final int ORDERSTATUS_CONFIRMRECEIVED=2;//ȷ���ջ�
	public static final int ORDERSTATUS_WAITCOMMENT=3;//�ȴ�����
	public static final int ORDERSTATUS_FINISHED=4; //������ɣ���Ҫ���û�����֮�󣬲����������׵���ɣ�
	
	//����״̬(Transaction)
	public static final int TRADESTATUS_WAITPAY=1; //�ȴ�֧��
	public static final int TRADESTATUS_CONFIRMRECEIVED=2; //ȷ���ջ�
	public static final int TRADESTATUS_WAITCOMMENT=3;//�ȴ�����
	public static final int TRADESTATUS_FINISHED=4;//���ս������
	
	//����KEY����ֹˢ�¡�����ʱ���������ظ��ύ��
	public static final String LOGIN_TOKEN="LOGIN_TOKEN";
	public static final String REGISTER_TOKEN="REGISTER_TOKEN";
	public static final String CART_TOKEN="CART_TOKEN";
	public static final String DETAIL_TOKEN="DETAIL_TOKEN";
	public static final String WRITEORDER_TOKEN="WRITEORDER_TOKEN";
	public static final String PAY_TOKEN="PAY_TOKEN";
	public static final String SHOPUPLOAD_TOKEN="SHOPUPLOAD_TOKEN";

	//SESSION�ʱЧ
	public static final int SESSION_MAX_INTERVAL=900;
	
	//�û��Ƿ񿪵�
	public static final int OPENED_SHOP_NO=0;
}
