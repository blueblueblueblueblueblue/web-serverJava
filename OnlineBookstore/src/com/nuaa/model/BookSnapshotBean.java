package com.nuaa.model;

/**
 * Book中提取出的书籍概览信息记录
 * 
 * @author chicken
 *
 */
public class BookSnapshotBean extends Json{
		
	private String Bid = "";
	private String Bimage = "";
	private float Bprice = 0.0f;
	private String Bname = "";
	private String Sname = "";
	private int Bsalednum = 0;
	private int Bcommentnum = 0;
	
	public BookSnapshotBean(String Bid, String Bimage, float Bprice, String Bname, String Sname, int Bsalednum,
			int Bcommentnum) {
	
		super();
		jsonObject.put("Bid", this.Bid=Bid);
		jsonObject.put("Bimage", this.Bimage="images/books/"+Bimage);
		jsonObject.put("Bprice", this.Bprice=Bprice);
		jsonObject.put("Sname", this.Sname=Sname);
		jsonObject.put("Bname", this.Bname=Bname);
		jsonObject.put("Bsalednum", this.Bsalednum=Bsalednum);
		jsonObject.put("Bcommentnum", this.Bcommentnum=Bcommentnum);
	}

	public String getBid() {
		return Bid;
	}

	public void setBid(String bid) {
		Bid = bid;
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

	public String getSname() {
		return Sname;
	}

	public void setSname(String sname) {
		Sname = sname;
	}

	public int getBsalednum() {
		return Bsalednum;
	}

	public void setBsalednum(int bsalednum) {
		Bsalednum = bsalednum;
	}

	public int getBcommentnum() {
		return Bcommentnum;
	}

	public void setBcommentnum(int bcommentnum) {
		Bcommentnum = bcommentnum;
	}
}
