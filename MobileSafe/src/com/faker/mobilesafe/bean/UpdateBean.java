package com.faker.mobilesafe.bean;

public class UpdateBean {

	private static UpdateBean instance = null;
	private String version;
	private String descripte;
	private String apkurl;
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDescripte() {
		return descripte;
	}

	public void setDescripte(String descripte) {
		this.descripte = descripte;
	}

	public String getApkurl() {
		return apkurl;
	}

	public void setApkurl(String apkurl) {
		this.apkurl = apkurl;
	}

	private UpdateBean(){
		
	}
	
	public static UpdateBean getInstance(){
		if(instance == null){
			instance = new UpdateBean();
		}
		return instance;
	}
}
