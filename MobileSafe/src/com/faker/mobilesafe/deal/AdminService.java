package com.faker.mobilesafe.deal;

import com.faker.mobilesafe.receiver.AdminReceiver;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class AdminService {

	private DevicePolicyManager policyManager;
	private ComponentName componentName;
	private static AdminService mInstance;

	private AdminService(Context context) {
		policyManager = (DevicePolicyManager) context
				.getSystemService(Context.DEVICE_POLICY_SERVICE);
		componentName = new ComponentName(context, AdminReceiver.class);
	}

	public synchronized static AdminService getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new AdminService(context);
		}
		return mInstance;
	}

	/**
	 * 设备管理授权
	 */
	public void activeAdmin(Context context) {
		if (isActive()) {
			return;
		}
		Intent intent = new Intent();
		intent.setAction(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
		context.startActivity(intent);
	}

	/**
	 * 锁定屏幕
	 * 
	 * @return
	 */
	public boolean lockScreen(Context context) {
		if (isActive()) {
			String password = SafeSharedpreference.getString(context,
					ConstConfig.LOCKSCREEN_PWD);
			policyManager.lockNow();
			if (!password.equals("")) {
				policyManager.resetPassword(password, 0);
			}
			return true;
		}
		return false;
	}

	/**
	 * 恢复出厂设置
	 * 
	 * @return
	 */
	public boolean wipeData() {
		if (isActive()) {
			policyManager.wipeData(0);
		}
		return false;
	}

	public boolean isActive() {
		return policyManager.isAdminActive(componentName);
	}
}
