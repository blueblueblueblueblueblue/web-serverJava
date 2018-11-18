package com.nuaa.model;

public class ShopBean {

	private int Sid=0;
	private String Sname="";
	private String Uid="";
	private String Sicon="";
	private String Ssummary="";
	private String Sactivity="";
	private float Stransprice=0.0f;

	public ShopBean(int sid, String sname, String uid, String sicon, String ssummary, String sactivity,
			float stransprice) {
		super();
		Sid = sid;
		Sname = sname;
		Uid = uid;
		Sicon = sicon;
		Ssummary = ssummary;
		Sactivity = sactivity;
		Stransprice = stransprice;
	}
	
	public int getSid() {
		return Sid;
	}
	public void setSid(int sid) {
		Sid = sid;
	}
	public String getUid() {
		return Uid;
	}

	public void setUid(String uid) {
		Uid = uid;
	}
	public String getSname() {
		return Sname;
	}
	public void setSname(String sname) {
		Sname = sname;
	}

	public String getSicon() {
		return "images/shopicons/" + Sicon;
	}
	public void setSicon(String sicon) {
		Sicon = sicon;
	}
	public String getSsummary() {
		return Ssummary;
	}
	public void setSsummary(String ssummary) {
		Ssummary = ssummary;
	}
	public String getSactivity() {
		return Sactivity;
	}
	public void setSactivity(String sactivity) {
		Sactivity = sactivity;
	}
	public float getStransprice() {
		return Stransprice;
	}
	public void setStransprice(float stransprice) {
		Stransprice = stransprice;
	}
}
