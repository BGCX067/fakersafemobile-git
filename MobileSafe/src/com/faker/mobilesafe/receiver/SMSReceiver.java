package com.faker.mobilesafe.receiver;

import com.faker.mobilesafe.R;
import com.faker.mobilesafe.dao.BlackNumberDao;
import com.faker.mobilesafe.deal.AdminService;
import com.faker.mobilesafe.deal.ConstConfig;
import com.faker.mobilesafe.deal.GPSService;
import com.faker.mobilesafe.deal.SafeSharedpreference;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("error", "成功拦截了短信");
		mobileIntecpter(context, intent);
		mobileProtected(context, intent);
	}

	/**
	 * 拦截黑名单短信
	 * 
	 * @param context
	 * @param intent
	 */
	private void mobileIntecpter(Context context, Intent intent) {
		// 是否开启拦截服务
		boolean b = SafeSharedpreference.getBoolean(context,
				ConstConfig.ISINTECPTER, true);
		if (b) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				Object[] pdus = (Object[]) bundle.get("pdus");
				BlackNumberDao dao = new BlackNumberDao(context);
				for (Object pdu : pdus) {
					SmsMessage message = SmsMessage.createFromPdu((byte[]) pdu);
					String number = message.getOriginatingAddress();
					Log.i("test", number);
					if (dao.isBlackNumber(number)) {
						// 是黑名单号码，则拦截,并将拦截信息存到数据库中
						setResultData(null);
						abortBroadcast();
					}
				}
			}
		}
	}

	/**
	 * 防盗保护功能的实现
	 * 
	 * @param context
	 * @param intent
	 */
	private void mobileProtected(Context context, Intent intent) {
		// 是否开启防盗保护
		boolean bProtected = SafeSharedpreference.getBoolean(context,
				ConstConfig.ISOPENLOST, false);
		if (bProtected) {
			// 获取短信内容
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				Object[] pdus = (Object[]) bundle.get("pdus");
				String location_command = SafeSharedpreference.getString(
						context, ConstConfig.LOCATION_COMMAND,
						ConstConfig.LOCATION_COMMAND);
				String lock_command = SafeSharedpreference.getString(context,
						ConstConfig.LOCK_COMMAND, ConstConfig.LOCK_COMMAND);
				String delete_command = SafeSharedpreference.getString(context,
						ConstConfig.DELETE_COMMAND, ConstConfig.DELETE_COMMAND);
				String alarm_command = SafeSharedpreference.getString(context,
						ConstConfig.ALARM_COMMAND, ConstConfig.ALARM_COMMAND);
				for (Object pdu : pdus) {
					SmsMessage message = SmsMessage.createFromPdu((byte[]) pdu);
					// 获取短信内容
					String body = message.getDisplayMessageBody();
					if (body.equals(location_command)) {
						// 获取手机位置并发送
						GPSService gpsService = GPSService.getInstance(context);
						gpsService.registLocationChangeListener();
						sendMessage(context, gpsService.getLocation());
						abortBroadcast();
					} else if (body.equals(lock_command)) {
						// 手机锁屏
						AdminService adminService = AdminService
								.getInstance(context);
						if (adminService.lockScreen(context)) {
							sendMessage(context, "LockScreen sucess!");
						} else {
							sendMessage(context,
									"LockScreen failed because you haven't actived it");
						}
						abortBroadcast();
					} else if (body.equals(delete_command)) {
						// 恢复出厂设置
						AdminService adminService = AdminService
								.getInstance(context);
						if (adminService.wipeData()) {
							sendMessage(context, "Wipedata sucess!");
						} else {
							sendMessage(context,
									"Wipedata failed because you haven't actived it");
						}
						abortBroadcast();
					} else if (body.equals(alarm_command)) {
						// 发出警报
						MediaPlayer mediaPlayer = MediaPlayer.create(context,
								R.raw.alarm);
						mediaPlayer.setVolume(1.0f, 1.0f);
						mediaPlayer.start();
						abortBroadcast();
					}
				}

			}
		}
	}

	private void sendMessage(Context context, String text) {
		SmsManager smsManager = SmsManager.getDefault();
		String safenum = SafeSharedpreference.getString(context,
				ConstConfig.SAFENUMBER);
		smsManager.sendTextMessage(safenum, null, text, null, null);
	}

}
