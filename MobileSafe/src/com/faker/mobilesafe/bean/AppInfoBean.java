package com.faker.mobilesafe.bean;

import android.graphics.Bitmap;

public class AppInfoBean {

	private Bitmap appIcon;
	private String appName;
	private String appVersion;
	private String packageName;
	private boolean isUserApp;

	public AppInfoBean(Bitmap appIcon, String appName, String appVersion,
			String packageName, boolean isUserApp) {
		super();
		this.appIcon = appIcon;
		this.appName = appName;
		this.appVersion = appVersion;
		this.packageName = packageName;
		this.isUserApp = isUserApp;
	}

	public AppInfoBean() {
		super();
		// TODO Auto-generated constructor stub
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

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public boolean isUserApp() {
		return isUserApp;
	}

	public void setUserApp(boolean isUserApp) {
		this.isUserApp = isUserApp;
	}

	@Override
	public String toString() {
		return "AppInfoBean [appIcon=" + appIcon + ", appName=" + appName
				+ ", appVersion=" + appVersion + ", packageName=" + packageName
				+ ", isUserApp=" + isUserApp + "]";
	}

}
