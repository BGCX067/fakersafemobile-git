package com.faker.mobilesafe.view.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.faker.mobilesafe.R;

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
}
