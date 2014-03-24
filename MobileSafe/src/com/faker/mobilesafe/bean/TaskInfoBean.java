package com.faker.mobilesafe.bean;

import android.graphics.Bitmap;

public class TaskInfoBean {

	private Bitmap appIcon;
	private String appName;
	private String packageName;
	private int pid;
	private boolean isUserApp;
	private boolean isChecked;
	private long memorySize;

	public TaskInfoBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TaskInfoBean(Bitmap appIcon, String appName, String packageName,
			int pid, boolean isUserApp, long memorySize) {
		super();
		this.appIcon = appIcon;
		this.appName = appName;
		this.packageName = packageName;
		this.pid = pid;
		this.isUserApp = isUserApp;
		this.memorySize = memorySize;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public Bitmap getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(Bitmap appIcon) {
		this.appIcon = appIcon;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public boolean isUserApp() {
		return isUserApp;
	}

	public void setUserApp(boolean isUserApp) {
		this.isUserApp = isUserApp;
	}

	public long getMemorySize() {
		return memorySize;
	}

	public void setMemorySize(long memorySize) {
		this.memorySize = memorySize;
	}

	@Override
	public String toString() {
		return "TaskInfoBean [appIcon=" + appIcon + ", appName=" + appName
				+ ", packageName=" + packageName + ", pid=" + pid
				+ ", isUserApp=" + isUserApp + ", memorySize=" + memorySize
				+ "]";
	}

}
