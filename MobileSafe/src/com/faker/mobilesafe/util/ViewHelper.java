package com.faker.mobilesafe.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class ViewHelper {

	/**
	 * 返回版本号
	 * @param context 上下文对象
	 * @return
	 */
	public static String getVersion(Context context) {
		// 包管理对象
		PackageManager pm = context.getPackageManager();
		try {
			// 封装了功能清单中的数据
			PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
		}
		return null;
	}

}
