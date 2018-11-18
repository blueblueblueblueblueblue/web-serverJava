package com.nuaa.model;

public class BookTradeRecordBean extends Json{
	
	private String Uavatar="";
	private String Unickname="";
	private int Tboughtnum=0;
	private String Treceivetime="";
	
	public BookTradeRecordBean(String uavatar, String unickname, int tboughtnum, String treceivetime) {
		super();
		jsonObject.put("Uavatar", this.Uavatar="images/avatars/"+uavatar);
		jsonObject.put("Unickname", this.Unickname=unickname);
		jsonObject.put("Tboughtnum", this.Tboughtnum=tboughtnum);
		jsonObject.put("Treceivetime", this.Treceivetime=treceivetime);
	}
	
	public String getUavatar() {
		return Uavatar;
	}
	public void setUavatar(String uavatar) {
		Uavatar = uavatar;
	}
	public String getUnickname() {
		return Unickname;
	}
	public void setUnickname(String unickname) {
		Unickname = unickname;
	}
	public int getTboughtnum() {
		return Tboughtnum;
	}
	public void setTboughtnum(int tboughtnum) {
		Tboughtnum = tboughtnum;
	}
	public String getTreceivetime() {
		return Treceivetime;
	}

	public void setTreceivetime(String treceivetime) {
		Treceivetime = treceivetime;
	}
}
