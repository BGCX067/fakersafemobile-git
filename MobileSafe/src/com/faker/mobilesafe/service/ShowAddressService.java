package com.faker.mobilesafe.service;

import com.faker.mobilesafe.R;
import com.faker.mobilesafe.dao.PhoneAddressQueryDao;
import com.faker.mobilesafe.deal.ConstConfig;
import com.faker.mobilesafe.deal.SafeSharedpreference;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ShowAddressService extends Service {

	private TelephonyManager tm;
	private MyPhoneStateListener listener;
	private WindowManager wManager;
	private FloatView view;
	private TextView call_address;
	private PhoneAddressQueryDao queryService;
	private WindowManager.LayoutParams params;

	private boolean flag = false; // 用来判断是否添加了view
	private SharedPreferences sp;

	@Override
	public void onCreate() {
		super.onCreate();
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new MyPhoneStateListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		wManager = (WindowManager) getApplicationContext().getSystemService(
				WINDOW_SERVICE);
		view = new FloatView(this);
		call_address = view.getTextView();
		queryService = new PhoneAddressQueryDao();
		params = new WindowManager.LayoutParams();
		sp = getSharedPreferences(ConstConfig.SPNAME, 0);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		if (flag) {
			wManager.removeView(view);
			flag = false;
		}
		super.onDestroy();
	}

	private final class MyPhoneStateListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE: // 电话闲置
				if (flag) {
					wManager.removeView(view);
					flag = false;
				}
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK: // 接听中
				break;
			case TelephonyManager.CALL_STATE_RINGING: // 响铃
				params.height = WindowManager.LayoutParams.WRAP_CONTENT;
				params.width = WindowManager.LayoutParams.WRAP_CONTENT;
				params.format = PixelFormat.TRANSLUCENT;
				params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
						| WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;

				params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
						| WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
				// 获取窗口位置信息
				int x = SafeSharedpreference.getInt(getApplicationContext(),
						ConstConfig.FLOATVIEW_X, 0);
				int y = SafeSharedpreference.getInt(getApplicationContext(),
						ConstConfig.FLOATVIEW_Y, 0);
				Log.i("test", "x:" + x);
				Log.i("test", "y:" + y);
				params.x = x;
				params.y = y;
				// 获取号码信息
				String string = queryService.queryAddress(incomingNumber);
				if (string.indexOf("黑龙江") >= 0) {
					string = string.substring(0, 5);
				} else {
					string = string.substring(0, 4);
				}
				call_address.setText(string);
				wManager.addView(view, params);
				flag = true;
				break;
			default:
				if (flag) {
					wManager.removeView(view);
					flag = false;
				}
				break;
			}
		}
	}

	private class FloatView extends LinearLayout {

		private TextView textView;
		private boolean isDown = false;
		private float currentX = 0;
		private float currentY = 0;
		private float dx;
		private float dy;

		public FloatView(Context context) {
			super(context);
			textView = new TextView(context);
			textView.setText("text");
			textView.setBackgroundResource(R.drawable.show_address_bg);
			addView(textView);
		}

		public TextView getTextView() {
			return textView;
		}

		@Override
		public boolean onTouchEvent(MotionEvent ev) {
			Log.i("test", "paramX:" + params.x);
			Log.i("test", "paramY:" + params.y);
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				Log.i("test", "down");
				currentX = ev.getX(0);
				currentY = ev.getY(0);
				Log.i("test", "currentX:" + currentX);
				Log.i("test", "currentY:" + currentY);
				isDown = true;
				break;
			case MotionEvent.ACTION_MOVE:
				Log.i("test", "move");
				if (isDown) {
					dx = ev.getX(0) - currentX;
					dy = ev.getY(0) - currentY;
					currentX = ev.getX(0);
					currentY = ev.getY(0);
					Log.i("test", "moveX:" + currentX);
					Log.i("test", "moveY:" + currentY);
					params.x += dx;
					params.y += dy;
					wManager.updateViewLayout(view, params);
				}
				break;
			case MotionEvent.ACTION_UP:
				if (isDown) {
					Log.i("test", "UPX:" + ev.getX(0));
					Log.i("test", "UPY:" + ev.getY(0));
					isDown = false;
					// 保存当前位置信息
					sp.edit().putInt(ConstConfig.FLOATVIEW_X, params.x)
							.commit();
					sp.edit().putInt(ConstConfig.FLOATVIEW_Y, params.y)
							.commit();
				}
				break;
			default:
				break;
			}
			return false;
		}
	}
}
