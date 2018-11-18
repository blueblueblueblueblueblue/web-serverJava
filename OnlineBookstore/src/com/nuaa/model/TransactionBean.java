package com.nuaa.model;

import com.nuaa.util.C;

public class TransactionBean {
	
	private int Tid=0;
	private String Bid="";
	private String Oid="";
	private String Uid= "";
	private int Tstatus=C.TRADESTATUS_WAITPAY;
	private int Tboughtnum=0;
	private int Tmark=0;
	private String Tcomment="";
	private String Tsubmittime="";
	private String Tpaytime="";
	private String Treceivetime="";
	private String Tcommenttime="";

	public TransactionBean(int tid, String bid, String oid, String uid, int tstatus, int tboughtnum, int tmark,
			String tcomment, String tsubmittime, String tpaytime, String treceivetime, String tcommenttime) {
		super();
		Tid = tid;
		Bid = bid;
		Oid = oid;
		Uid = uid;
		Tstatus = tstatus;
		Tboughtnum = tboughtnum;
		Tmark = tmark;
		Tcomment = tcomment;
		Tsubmittime = tsubmittime;
		Tpaytime = tpaytime;
		Treceivetime = treceivetime;
		Tcommenttime = tcommenttime;
	}
	
	public int getTid() {
		return Tid;
	}

	public void setTid(int tid) {
		Tid = tid;
	}
	public String getBid() {
		return Bid;
	}

	public void setBid(String bid) {
		Bid = bid;
	}
	
	public String getOid() {
		return Oid;
	}

	public void setOid(String oid) {
		Oid = oid;
	}
	public String getUid() {
		return Uid;
	}

	public void setUid(String uid) {
		Uid = uid;
	}
	public int getTstatus() {
		return Tstatus;
	}

	public void setTstatus(int tstatus) {
		Tstatus = tstatus;
	}

	public int getTboughtnum() {
		return Tboughtnum;
	}

	public void setTboughtnum(int tboughtnum) {
		Tboughtnum = tboughtnum;
	}

	public int getTmark() {
		return Tmark;
	}

	public void setTmark(int tmark) {
		Tmark = tmark;
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
	
	public String getTsubmittime() {
		return Tsubmittime;
	}

	public void setTsubmittime(String tsubmittime) {
		Tsubmittime = tsubmittime;
	}

	public String getTpaytime() {
		return Tpaytime;
	}

	public void setTpaytime(String tpaytime) {
		Tpaytime = tpaytime;
	}

	public String getTreceivetime() {
		return Treceivetime;
	}

	public void setTreceivetime(String treceivetime) {
		Treceivetime = treceivetime;
	}

}
