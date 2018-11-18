package com.nuaa.model;

/**
 * User±í¼ÇÂ¼
 * 
 * @author chicken
 *
 */
public class UserBean {

	private String Uid = "";
	private String Upassword = "";
	private String Uavatar = "";
	private String Unickname = "";
	private String Urealname = "";
	private String Usex = "";
	private String Uaddress = "";
	private String Uphone = "";
	private String Uemail = "";
	private String Ubirthday = "";


	public UserBean(String Uid, String Upassword) {
		
		this.Uid=Uid;
		this.Upassword=Upassword;
	}
	
	public UserBean(String uid, String upassword, String uavatar, String unickname, String urealname, String usex,
			String uaddress, String uphone, String uemail, String ubirthday) {
		super();
		Uid = uid;
		Upassword = upassword;
		Uavatar = uavatar;
		Unickname = unickname;
		Urealname = urealname;
		Usex = usex;
		Uaddress = uaddress;
		Uphone = uphone;
		Uemail = uemail;
		Ubirthday = ubirthday;
	}
	
	public String getUphone() {
		return Uphone;
	}



	public void setUphone(String uphone) {
		Uphone = uphone;
	}



	public String getUemail() {
		return Uemail;
	}



	public void setUemail(String uemail) {
		Uemail = uemail;
	}
	
	public String getUid() {
		return Uid;
	}

	public void setUid(String uid) {
		Uid = uid;
	}

	public String getUpassword() {
		return Upassword;
	}

	public void setUpassword(String upassword) {
		Upassword = upassword;
	}

	public String getUaddress() {
		return Uaddress;
	}

	public void setUaddress(String uaddress) {
		Uaddress = uaddress;
	}

	public String getUnickname() {
		return Unickname;
	}

	public void setUnickname(String unickname) {
		Unickname = unickname;
	}

	public String getUavatar() {
		return "images/avatars/" + Uavatar;
	}

	public void setUavatar(String uavatar) {
		Uavatar = uavatar;
	}

	public String getUrealname() {
		return Urealname;
	}

	public void setUrealname(String urealname) {
		Urealname = urealname;
	}

	public String getUsex() {
		return Usex;
	}

	public void setUsex(String usex) {
		Usex = usex;
	}

	public String getUbirthday() {
		return Ubirthday;
	}

	public void setUbirthday(String ubirthday) {
		Ubirthday = ubirthday;
	}
}
