package com.faker.mobilesafe.service;

import java.lang.reflect.Method;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CallLog.Calls;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.faker.mobilesafe.dao.BlackNumberDao;
import com.faker.mobilesafe.dao.CallRecordDao;
import com.faker.mobilesafe.dao.PhoneAddressQueryDao;
import com.faker.mobilesafe.deal.ConstConfig;
import com.faker.mobilesafe.deal.SafeSharedpreference;
import com.faker.mobilesafe.view.activitys.CommunicateActivity;

public class BlackNumberService extends Service {

	private TelephonyManager tm;
	private MyPhoneStateListener listener;
	private BlackNumberDao daoBlacknumber;
	private CallRecordDao callRecordDao;

	private NotificationManager nm;

	private PhoneAddressQueryDao phoneQuery;

	@Override
	public void onCreate() {
		super.onCreate();
		listener = new MyPhoneStateListener();
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

		daoBlacknumber = new BlackNumberDao(getApplicationContext());

		callRecordDao = new CallRecordDao(getApplicationContext());

		phoneQuery = new PhoneAddressQueryDao();

		nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private final class MyPhoneStateListener extends PhoneStateListener {

		private long startTime = 0;

		@SuppressWarnings("deprecation")
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				// 判断拦截服务是否开启
				boolean isStart = SafeSharedpreference.getBoolean(
						getApplicationContext(), ConstConfig.ISINTECPTER, true);
				if (isStart) {
					boolean isBlack = daoBlacknumber
							.isBlackNumber(incomingNumber);
					if (isBlack) {
						// 如果是黑名单，则挂断电话
						Log.i("test", "拦截到电话");
						endCall(incomingNumber);
						// 保存拦截记录
						String address = phoneQuery
								.queryAddress(incomingNumber);
						if (address.indexOf("黑龙江") > 0) {
							address = address.substring(0, 5);
						} else {
							address = address.substring(0, 4);
						}
						callRecordDao.addRecord(incomingNumber, address, "黑名单",
								System.currentTimeMillis());
						return;
					} else {
						startTime = System.currentTimeMillis();
					}
				}
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:

				break;
			case TelephonyManager.CALL_STATE_IDLE:

				long endTime = System.currentTimeMillis();
				if (startTime != 0 && endTime - startTime < 2000) {
					clearCallHistory(incomingNumber);
					// 拦截到来电一声响,发出通知
					Notification notification = new Notification(
							android.R.drawable.stat_notify_missed_call,
							"拦截到来电一声响", endTime);
					Intent intent = new Intent(getApplicationContext(),
							CommunicateActivity.class);
					PendingIntent contentIntent = PendingIntent.getActivity(
							getApplicationContext(), 1000, intent, 0);
					notification.setLatestEventInfo(getApplicationContext(),
							"来电一声响", "拦截到来电一声响", contentIntent);
					// 点击后通知自动消失
					notification.flags = Notification.FLAG_AUTO_CANCEL;
					nm.notify(1000, notification);
					// 保存拦截记录
					String address = phoneQuery.queryAddress(incomingNumber);
					if (address.indexOf("黑龙江") >= 0) {
						address = address.substring(0, 5);
					} else {
						address = address.substring(0, 4);
					}
					callRecordDao.addRecord(incomingNumber, address, "一声响",
							System.currentTimeMillis());
				}
				break;

			default:
				break;
			}
		}

		/**
		 * 挂断电话
		 * 
		 * @param incomingNumber
		 */
		private void endCall(String incomingNumber) {
			try {
				Class clazz = Class.forName("android.os.ServiceManager");
				Method method = clazz.getMethod("getService", String.class);
				IBinder ibinder = (IBinder) method.invoke(null,
						Context.TELEPHONY_SERVICE);
				ITelephony iTelephony = ITelephony.Stub.asInterface(ibinder);
				iTelephony.endCall();
				// 删除通话记录 通话记录的保存是一个异步的操作，需要使用ContentObserver技术来实现
				Uri uri = Calls.CONTENT_URI;
				getContentResolver().registerContentObserver(uri, true,
						new MyContentObserver(new Handler(), incomingNumber));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 * 删除来电一声响未接来电通知
		 * 
		 * @param incomingNumber
		 */
		private void clearCallHistory(String incomingNumber) {
			Class clazz;
			try {
				clazz = Class.forName("android.os.ServiceManager");
				Method method = clazz.getMethod("getService", String.class);
				IBinder ibinder = (IBinder) method.invoke(null,
						Context.TELEPHONY_SERVICE);
				ITelephony iTelephony = ITelephony.Stub.asInterface(ibinder);
				// 需要MODIFY_PHONE_STATE权限
				iTelephony.cancelMissedCallsNotification();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		private final class MyContentObserver extends ContentObserver {

			private String incomingNumber;

			public MyContentObserver(Handler handler, String incomingNumber) {
				super(handler);
				// TODO Auto-generated constructor stub
				this.incomingNumber = incomingNumber;
			}

			@Override
			public void onChange(boolean selfChange) {
				// TODO Auto-generated method stub
				super.onChange(selfChange);
				Uri uri = Calls.CONTENT_URI;
				String where = Calls.NUMBER + " = ?";
				String[] selectionArgs = new String[] { incomingNumber };
				getContentResolver().delete(uri, where, selectionArgs);
				// 解除监听
				getContentResolver().unregisterContentObserver(this);
			}
		}

	}

}
