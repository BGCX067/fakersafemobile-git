package com.faker.mobilesafe.deal;

import com.faker.mobilesafe.receiver.ConnectivityChangeReceiver;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.net.ConnectivityManagerCompat;

public class NetworkHelper {

	private Context context;
	// 当前连接网络的类型
	private int nNettype;

	public NetworkHelper(Context context) {
		nNettype = -1;
		this.context = context;
	}

	/**
	 * 获得当前连接网络的类型
	 * 
	 * @return
	 */
	public int getNettype() {
		return nNettype;
	}

	/**
	 * 判断是否有网络连接
	 * 
	 * @return
	 */
	public boolean isNetworkConnected() {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
		if (mNetworkInfo != null) {
			nNettype = mNetworkInfo.getType();
			return mNetworkInfo.isAvailable();
		}
		return false;
	}

	/**
	 * 调用系统网络设置界面，进行网络设置
	 */
	public void setNetwork() {

	}

	/**
	 * 对大数据传输时，需要调用该方法做出判断，如果流量敏感，应该提示用户
	 * 
	 * @param context
	 * @return true表示流量敏感，false表示不敏感
	 */
	public static boolean isActiveNetworkMetered(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		return ConnectivityManagerCompat.isActiveNetworkMetered(cm);
	}

	/**
	 * 注册监听网络状态的广播接收者
	 * 
	 * @param context
	 * @param receiver
	 * @return
	 */
	public static Intent registerReceiver(Context context,
			ConnectivityChangeReceiver receiver) {
		return context.registerReceiver(receiver,
				ConnectivityChangeReceiver.FILTER);
	}

	/**
	 * 取消注册监听网络状态的广播接收者
	 * 
	 * @param context
	 * @param receiver
	 */
	public static void unregisterReceiver(Context context,
			ConnectivityChangeReceiver receiver) {
		context.unregisterReceiver(receiver);
	}
}
