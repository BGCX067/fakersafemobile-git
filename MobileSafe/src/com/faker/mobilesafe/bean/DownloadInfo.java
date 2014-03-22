package com.faker.mobilesafe.bean;

public class DownloadInfo {

	private int _id;
	private int threadid;
	private String path;
	private int downloadlength;
	
	
	
	public DownloadInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	public DownloadInfo(int threadid, String path, int downloadlength) {
		super();
		this.threadid = threadid;
		this.path = path;
		this.downloadlength = downloadlength;
	}




	public DownloadInfo(int _id, int threadid, String path, int downloadlength) {
		super();
		this._id = _id;
		this.threadid = threadid;
		this.path = path;
		this.downloadlength = downloadlength;
	}


	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public int getThreadid() {
		return threadid;
	}
	public void setThreadid(int threadid) {
		this.threadid = threadid;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getDownloadlength() {
		return downloadlength;
	}
	public void setDownloadlength(int downloadlength) {
		this.downloadlength = downloadlength;
	}
	
	
}
