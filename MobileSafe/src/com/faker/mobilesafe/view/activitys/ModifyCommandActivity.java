package com.faker.mobilesafe.view.activitys;

import com.faker.mobilesafe.R;
import com.faker.mobilesafe.deal.ConstConfig;
import com.faker.mobilesafe.deal.SafeSharedpreference;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

public class ModifyCommandActivity extends BaseActivity {

	private EditText location_command_edit;
	private EditText delete_command_edit;
	private EditText lock_command_edit;
	private EditText lockpwd_command_edit;
	private EditText alarm_command_edit;
	private LinearLayout linearLayout;

	private String location; // 获取手机位置指令
	private String delete; // 恢复出厂设置指令
	private String lockscreen; // 锁定手机屏幕指令
	private String alarm; // 发出警报指令
	private String lock_pwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modify_command);
		initData();
		initUI();
	}

	public void onBack(View v) {
		onBackPressed();
	}

	public void save(View v) {
		SafeSharedpreference.save(this, ConstConfig.LOCATION_COMMAND,
				location_command_edit.getText().toString());
		SafeSharedpreference.save(this, ConstConfig.DELETE_COMMAND,
				delete_command_edit.getText().toString());
		SafeSharedpreference.save(this, ConstConfig.LOCK_COMMAND,
				lock_command_edit.getText().toString());
		SafeSharedpreference.save(this, ConstConfig.LOCKSCREEN_PWD,
				lockpwd_command_edit.getText().toString());
		SafeSharedpreference.save(this, ConstConfig.ALARM_COMMAND,
				alarm_command_edit.getText().toString());
		onBackPressed();
		finish();
	}

	private void initData() {
		location = SafeSharedpreference.getString(this,
				ConstConfig.LOCATION_COMMAND, ConstConfig.LOCATION_COMMAND);
		delete = SafeSharedpreference.getString(this,
				ConstConfig.DELETE_COMMAND, ConstConfig.DELETE_COMMAND);
		lockscreen = SafeSharedpreference.getString(this,
				ConstConfig.LOCK_COMMAND, ConstConfig.LOCK_COMMAND);
		alarm = SafeSharedpreference.getString(this, ConstConfig.ALARM_COMMAND,
				ConstConfig.ALARM_COMMAND);

		lock_pwd = SafeSharedpreference.getString(this,
				ConstConfig.LOCKSCREEN_PWD);
	}

	private void initUI() {
		location_command_edit = getView(R.id.location_command_edit);
		location_command_edit.setText(location);
		location_command_edit.addTextChangedListener(watcher);
		delete_command_edit = getView(R.id.delete_command_edit);
		delete_command_edit.setText(delete);
		delete_command_edit.addTextChangedListener(watcher);
		lock_command_edit = getView(R.id.lock_command_edit);
		lock_command_edit.setText(lockscreen);
		lock_command_edit.addTextChangedListener(watcher);
		lockpwd_command_edit = getView(R.id.lockpwd_command_edit);
		lockpwd_command_edit.setText(lock_pwd);
		alarm_command_edit = getView(R.id.alarm_command_edit);
		alarm_command_edit.setText(alarm);
		alarm_command_edit.addTextChangedListener(watcher);
		linearLayout = (LinearLayout) findViewById(R.id.save_click);
	}

	private commandEditTextWatcher watcher = new commandEditTextWatcher();

	private final class commandEditTextWatcher implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if ("".equals(location_command_edit.getText())
					&& "".equals(lock_command_edit.getText())
					&& "".equals(alarm_command_edit.getText())
					&& "".equals(delete_command_edit.getText())) {
				linearLayout.setEnabled(false);
			} else {
				linearLayout.setEnabled(true);
			}
		}

		@Override
		public void afterTextChanged(Editable s) {
		}

	}

}
