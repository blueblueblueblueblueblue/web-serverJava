package com.nuaa.model;

import com.nuaa.util.C;

/*
 * 购物车内书籍信息实体
 */
public class BookInCartBean {

	private String Bid="";
	private String Bimage="";
	private float Bprice=0.0f;
	private String Bname="";
	private int Sid=0;
	private String Sicon="";
	private String Sname="";
	private float Boriprice=0.0f;
	private int Tboughtnum=0;
	private int Tstatus=C.TRADESTATUS_WAITPAY;
	private boolean checked=true;

	public BookInCartBean(){
		
	}
	
	public BookInCartBean(String bid, String bimage, float bprice, String bname, int sid, String sicon, String sname, float boriprice,int tboughtnum) {
		super();
		Bid = bid;
		Bimage = bimage;
		Bprice = bprice;
		Bname = bname;
		Sid=sid;
		Sicon=sicon;
		Sname=sname;
		Boriprice = boriprice;
		Tboughtnum = tboughtnum;
	}
	
	public String getBid() {
		return Bid;
	}

	public void setBid(String bid) {
		Bid = bid;
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
	public int getSid() {
		return Sid;
	}

	public void setSid(int sid) {
		Sid = sid;
	}
	
	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	public String getBimage() {
		return Bimage;
	}
	public void setBimage(String bimage) {
		Bimage = bimage;
	}
	public float getBprice() {
		return Bprice;
	}
	public void setBprice(float bprice) {
		Bprice = bprice;
	}
	public String getBname() {
		return Bname;
	}
	public void setBname(String bname) {
		Bname = bname;
	}

	public float getBoriprice() {
		return Boriprice;
	}
	public void setBoriprice(float boriprice) {
		Boriprice = boriprice;
	}
	public int getTboughtnum() {
		return Tboughtnum;
	}

	public void setTboughtnum(int tboughtnum) {
		Tboughtnum = tboughtnum;
	}
	public int getTstatus() {
		return Tstatus;
	}

	public void setTstatus(int tstatus) {
		Tstatus = tstatus;
	}
}
