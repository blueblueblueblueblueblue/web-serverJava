package com.nuaa.model;

public class BookCommentBean extends Json{
	
	private String Uavatar="";
	private String Unickname="";
	private String Tcomment="";
	private String Tcommenttime="";
		
	public BookCommentBean(String uavatar, String unickname, String tcomment, String tcommenttime) {
		
		super();
		jsonObject.put("Uavatar", this.Uavatar="images/avatars/"+uavatar);
		jsonObject.put("Unickname", this.Unickname=unickname);
		jsonObject.put("Tcomment", this.Tcomment=tcomment);
		jsonObject.put("Tcommenttime", this.Tcommenttime=tcommenttime);
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
	public String getTcomment() {
		return Tcomment;
	}
	public void setTcomment(String tcomment) {
		Tcomment = tcomment;
	}
	public String getTcommenttime() {
		return Tcommenttime;
	}

	public void setTcommenttime(String tcommenttime) {
		Tcommenttime = tcommenttime;
	}
}
