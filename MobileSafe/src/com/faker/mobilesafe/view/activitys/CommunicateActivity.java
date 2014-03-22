package com.faker.mobilesafe.view.activitys;

import com.faker.mobilesafe.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class CommunicateActivity extends TabActivity implements
		OnCheckedChangeListener {

	private TabHost mHost;
	private RadioButton intercept_call;
	private RadioButton intercept_message;
	private RadioButton intercept_number;

	private final static String TAG_CALL_STRING = "TAG_CALL";
	private final static String TAG_MESSAFE_STRING = "TAG_MESSAGE";
	private final static String TAG_NUMBER_STRING = "TAG_NUMBER";

	private Intent call_intent;
	private Intent message_intent;
	private Intent number_intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_communicate);
		initUi();
		initIntent();
		initTabspec();
	}

	private void initTabspec() {
		mHost.addTab(buildTabSpec(TAG_CALL_STRING,
				R.string.communicate_call_history,
				R.drawable.intercept_calltab_bg, call_intent));
		mHost.addTab(buildTabSpec(TAG_MESSAFE_STRING,
				R.string.communicate_message_history,
				R.drawable.intercept_messagetab_bg, message_intent));
		mHost.addTab(buildTabSpec(TAG_NUMBER_STRING,
				R.string.communicate_blacknum_history,
				R.drawable.intercept_blacknumetab_bg, number_intent));
	}

	/**
	 * 
	 * @param string
	 *            tab的TAG标签
	 * @param label
	 *            文本信息
	 * @param icon
	 *            图片信息
	 * @param intent
	 *            选中后跳转的intent
	 * @return
	 */
	private TabSpec buildTabSpec(String string, int label, int icon,
			Intent intent) {
		return mHost
				.newTabSpec(string)
				.setContent(intent)
				.setIndicator(String.valueOf(label),
						getResources().getDrawable(icon));
	}

	/**
	 * 初始化跳转意图
	 */
	private void initIntent() {
		call_intent = new Intent(this, InterceptCallrecordActivity.class);
		message_intent = new Intent(this, InterceptMessagerecordActivity.class);
		number_intent = new Intent(this, InterceptBlacknumberActivity.class);
	}

	/**
	 * 初始化界面UI对象
	 */
	private void initUi() {
//		mHost = (TabHost) findViewById(android.R.id.tabhost);
		mHost = getTabHost();
		intercept_call = (RadioButton) findViewById(R.id.radio_button0);
		intercept_call.setOnCheckedChangeListener(this);
		intercept_message = (RadioButton) findViewById(R.id.radio_button1);
		intercept_message.setOnCheckedChangeListener(this);
		intercept_number = (RadioButton) findViewById(R.id.radio_button2);
		intercept_number.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			switch (buttonView.getId()) {
			case R.id.radio_button0:
				mHost.setCurrentTabByTag(TAG_CALL_STRING);
				break;
			case R.id.radio_button1:
				mHost.setCurrentTabByTag(TAG_MESSAFE_STRING);
				break;
			case R.id.radio_button2:
				mHost.setCurrentTabByTag(TAG_NUMBER_STRING);
				break;

			default:
				break;
			}
		}
	}

}
