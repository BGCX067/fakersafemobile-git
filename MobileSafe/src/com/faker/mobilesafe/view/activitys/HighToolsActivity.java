package com.faker.mobilesafe.view.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.faker.mobilesafe.R;
import com.faker.mobilesafe.deal.ConstConfig;
import com.faker.mobilesafe.deal.SafeSharedpreference;

public class HighToolsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hightools);
	}

	public void onBack(View v) {
		onBackPressed();
		finish();
	}

	/**
	 * 添加IP
	 * 
	 * @param v
	 */
	public void addIp(View v) {
		Intent intent = new Intent(this, AddIPActivity.class);
		startActivity(intent);
	}

	/**
	 * 号码归属地查询
	 * 
	 * @param v
	 */
	public void phone_query(View v) {
		Intent intent = new Intent(this, PhoneQueryActivity.class);
		startActivity(intent);
	}

	/**
	 * 常用号码
	 * 
	 * @param v
	 */
	public void common_number(View v) {
		Intent intent = new Intent(this, CommonNumberActivity.class);
		startActivity(intent);
	}

	/**
	 * 程序锁
	 * 
	 * @param v
	 */
	public void lock_pattern(View v) {
		boolean is_lock = SafeSharedpreference.getBoolean(this, ConstConfig.LOCK_PATTERN, false);
		if(!is_lock){
			Intent intent = new Intent(this, GuideGesturePasswordActivity.class);
			startActivity(intent);
		}else{
			Intent intent = new Intent(this, UnlockGesturePasswordActivity.class);
			startActivity(intent);
		}
	}
}
