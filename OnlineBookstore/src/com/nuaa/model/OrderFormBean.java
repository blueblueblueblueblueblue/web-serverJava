package com.nuaa.model;

import java.security.KeyStore.PrivateKeyEntry;
import java.util.ArrayList;

import com.nuaa.util.C;

/*
 * 订单类（书籍表+合计+运费）
 */

public class OrderFormBean {
	
	private String Oid="";
	private String Uid="";
	private int Sid=0;
	private String Aid="";
	private float Ototalbooksprice=0.0f;
	private float Ototaltransprice=0.0f; //总运费
	private float Stransprice=0.0f; //单位运费
	private String Sicon="";
	private String Sname="";
	private String Osubmittime="";
	private String Opaytime="";
	private String Oreceivetime="";
	private String Ofinishedtime="";
	private int Ostatus=C.ORDERSTATUS_WAITPAY;
	private ArrayList<BookInCartBean> bookInCartBeanList=new ArrayList<BookInCartBean>(); 
	
	public OrderFormBean(){
		
	}
	public OrderFormBean(String oid,String uid,int sid, String aid, float ototalbooksprice, float ototaltransprice, float stransprice,
			String sicon, String sname, String osubmittime, String opaytime, String oreceivetime, String ofinishedtime, int ostatus,
			ArrayList<BookInCartBean> bookInCartBeanList) {
		super();
		Oid = oid;
		Uid = uid;
		Sid = sid;
		Aid = aid;
		Ototalbooksprice = ototalbooksprice;
		Ototaltransprice = ototaltransprice;
		Stransprice = stransprice;
		Sicon = sicon;
		Sname = sname;
		Osubmittime = osubmittime;
		Opaytime = opaytime;
		Oreceivetime = oreceivetime;
		Ofinishedtime = ofinishedtime;
		Ostatus = ostatus;
		this.bookInCartBeanList = bookInCartBeanList;
	}
	
	public String getUid() {
		return Uid;
	}
	public void setUid(String uid) {
		Uid = uid;
	}
	public int getSid() {
		return Sid;
	}
	public void setSid(int sid) {
		Sid = sid;
	}
	public String getAid() {
		return Aid;
	}
	public void setAid(String aid) {
		Aid = aid;
	}
	public String getOid() {
		return Oid;
	}
	public void setOid(String oid) {
		Oid = oid;
	}
	public float getOtotalbooksprice() {
		return Ototalbooksprice;
	}
	public void setOtotalbooksprice(float ototalbooksprice) {
		Ototalbooksprice = ototalbooksprice;
	}
	public String getOsubmittime() {
		return Osubmittime;
	}
	public void setOsubmittime(String osubmittime) {
		Osubmittime = osubmittime;
	}
	public String getOpaytime() {
		return Opaytime;
	}
	public void setOpaytime(String opaytime) {
		Opaytime = opaytime;
	}
	public int getOstatus() {
		return Ostatus;
	}

	public void setOstatus(int ostatus) {
		Ostatus = ostatus;
	}
	
	public ArrayList<BookInCartBean> getBookInCartBeanList() {
		return bookInCartBeanList;
	}
	public void setBookInCartBeanList(ArrayList<BookInCartBean> bookInCartBeanList) {
		this.bookInCartBeanList = bookInCartBeanList;
	}
	public float getStransprice() {
		return Stransprice;
	}

	public void setStransprice(float stransprice) {
		Stransprice = stransprice;
	}	
	
	public float getOtotaltransprice() {
		return Ototaltransprice;
	}

	public void setOtotaltransprice(float ototaltransprice) {
		Ototaltransprice = ototaltransprice;
	}
	public String getSicon() {
		return Sicon;
	}

	public void setSicon(String sicon) {
		Sicon = sicon;
	}

	public String getSname() {
		return Sname;
	}

	public void setSname(String sname) {
		Sname = sname;
	}	
	
	public String getOreceivetime() {
		return Oreceivetime;
	}
	public void setOreceivetime(String oreceivetime) {
		Oreceivetime = oreceivetime;
	}
	public String getOfinishedtime() {
		return Ofinishedtime;
	}
	public void setOfinishedtime(String ofinishedtime) {
		Ofinishedtime = ofinishedtime;
	}
	
	//计算订单总价
	public void calOrderPrice(){
		
		Ototalbooksprice=0.0f;
		for(int i=0;i<bookInCartBeanList.size();i++)
		{
			BookInCartBean bookInCartBean=bookInCartBeanList.get(i);
			Ototalbooksprice += bookInCartBean.getBprice() * bookInCartBean.getTboughtnum();
		}
	}
	
	//计算运费（每3本加一次运费）
	public void calOrderTotalTransPrice(){
		
		//计算订单中书总数
		int bookNum=0;
		for(int i=0;i<bookInCartBeanList.size();i++)
		{
			BookInCartBean bookInCartBean = bookInCartBeanList.get(i);
			bookNum += bookInCartBean.getTboughtnum();
		}
		
		int circle=bookNum / C.TRANS_UNIT_NUM;
		if(bookNum % C.TRANS_UNIT_NUM != 0) //多余
			circle++;
		
		Ototaltransprice = circle * Stransprice;
	}
}
