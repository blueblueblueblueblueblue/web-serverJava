package com.nuaa.model;

/*
 * 用户的收货地址实体类
 */

public class AddressBean extends Json{
	
	private String Aid="";
	private String Uid="";
	private String Areceivername="";
	private String Aaddress="";
	private String Acode="";
	private String Aphone="";
	private String Afixphone="";
	private String Aprovince="";
	private String Acity="";
	private String Atown="";
	private int Acheck=0;
	
	public AddressBean(String aid, String uid, String areceivername, String aaddress, String acode, String aphone,
			String afixphone, String aprovince, String acity, String atown, int acheck) {
		
		super();
		jsonObject.put("Aid", Aid=aid);
		jsonObject.put("Uid",Uid = uid);
		jsonObject.put("Areceivername",Areceivername = areceivername);
		jsonObject.put("Aaddress",Aaddress = aaddress);
		jsonObject.put("Acode",Acode = acode);
		jsonObject.put("Aphone",Aphone = aphone);
		jsonObject.put("Afixphone",Afixphone = afixphone);
		jsonObject.put("Aprovince",Aprovince = aprovince);
		jsonObject.put("Acity",Acity = acity);
		jsonObject.put("Atown",Atown = atown);
		jsonObject.put("Acheck",Acheck = acheck);
	}
	
	public String getAid() {
		return Aid;
	}
	public void setAid(String aid) {
		Aid = aid;
	}
	public String getUid() {
		return Uid;
	}
	public void setUid(String uid) {
		Uid = uid;
	}
	public String getAreceivername() {
		return Areceivername;
	}
	public void setAreceivername(String areceivername) {
		Areceivername = areceivername;
	}
	public String getAaddress() {
		return Aaddress;
	}
	public void setAaddress(String aaddress) {
		Aaddress = aaddress;
	}
	public String getAcode() {
		return Acode;
	}
	public void setAcode(String acode) {
		Acode = acode;
	}
	public String getAphone() {
		return Aphone;
	}
	public void setAphone(String aphone) {
		Aphone = aphone;
	}
	public String getAfixphone() {
		return Afixphone;
	}
	public void setAfixphone(String afixphone) {
		Afixphone = afixphone;
	}
	public String getAprovince() {
		return Aprovince;
	}
	public void setAprovince(String aprovince) {
		Aprovince = aprovince;
	}
	public String getAcity() {
		return Acity;
	}
	public void setAcity(String acity) {
		Acity = acity;
	}
	public String getAtown() {
		return Atown;
	}
	public void setAtown(String atown) {
		Atown = atown;
	}
	public int getAcheck() {
		return Acheck;
	}
	public void setAcheck(int acheck) {
		Acheck = acheck;
	}
}
