package com.faker.mobilesafe.view.activitys;

import android.view.WindowManager;
import com.faker.mobilesafe.R;
import com.faker.mobilesafe.deal.ConstConfig;
import com.faker.mobilesafe.deal.SafeSharedpreference;
import com.faker.mobilesafe.util.MD5;
import com.faker.mobilesafe.view.ui.ClearEditText;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class LostProtectedActivity extends BaseActivity implements TextWatcher,
		OnClickListener {

	private ClearEditText login_name;
	private ClearEditText login_pwd;
	private ClearEditText login_pwd_confirm;
	private Button registerButton;
	private Button loginButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFirst()) {
			setContentView(R.layout.activity_lost_register);
			login_pwd_confirm = getView(R.id.lost_procted_password_confirm);
			registerButton = getView(R.id.lost_protected_register);
			login_pwd_confirm.addTextChangedListener(this);
			registerButton.setOnClickListener(this);
		} else {
			setContentView(R.layout.activity_lost_login);
			loginButton = getView(R.id.lost_protected_login);
			loginButton.setOnClickListener(this);
		}
		login_name = getView(R.id.lost_procted_login_name);
		login_pwd = getView(R.id.lost_procted_login_password);
		login_name.addTextChangedListener(this);
		login_pwd.addTextChangedListener(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (isFirst()) {
			if (login_name.getText().length() > 0
					&& login_pwd.getText().length() > 0
					&& login_pwd_confirm.getText().length() > 0) {
				registerButton.setEnabled(true);
			} else {
				registerButton.setEnabled(false);
			}
		} else {
			if (login_name.getText().length() > 0
					&& login_pwd.getText().length() > 0) {
				loginButton.setEnabled(true);
			} else {
				loginButton.setEnabled(false);
			}
		}

	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.lost_protected_register) {
			regeister();
		} else {
			login();
		}

	}

	/**
	 * 登录处理逻辑
	 */
	private void login() {
		// 获取SharedPreference中保存的用户名和密码
		String username = SafeSharedpreference.getString(this,
				ConstConfig.USERNAME);
		String password = SafeSharedpreference.getString(this,
				ConstConfig.PASSWORD);
		if (username.equals(MD5.getMd5String(login_name.getTextString()))
				&& password.equals(MD5.getMd5String(login_pwd.getTextString()))) {
			// 登录成功，进行跳转
			Intent intent = null;
			// 判断是否设置完成
			boolean b = SafeSharedpreference.getBoolean(this,
					ConstConfig.ISLOSTSETUP, false);
			if (b) {
				intent = new Intent(LostProtectedActivity.this,
						LostSetupResultActivity.class);
			} else {
				intent = new Intent(LostProtectedActivity.this,
						LostSetupActivity.class);
			}
			startActivity(intent);
			LostProtectedActivity.this.finish();
		} else {
			Toast(this, R.string.login_error, Toast.LENGTH_LONG);
		}
	}

	/**
	 * 注册的处理逻辑
	 */
	private void regeister() {
		if (login_pwd.equal(login_pwd_confirm)) {
			// 注册...
			// 1.对用户名和密码进行md5加密
			String username = MD5.getMd5String(login_name.getTextString());
			String password = MD5.getMd5String(login_pwd.getTextString());
			// 2.数据持久化，存入sharedpreference
			SafeSharedpreference.save(this, ConstConfig.USERNAME, username);
			SafeSharedpreference.save(this, ConstConfig.PASSWORD, password);
			SafeSharedpreference.save(this, ConstConfig.ISLOSTFIRST, false);
			// 3.跳转设置界面
			Intent intent = new Intent(LostProtectedActivity.this,
					LostSetupActivity.class);
			startActivity(intent);
			LostProtectedActivity.this.finish();
		} else {
			Toast(this, R.string.register_error, Toast.LENGTH_LONG);
		}
	}

	/**
	 * 是否第一次登陆手机防盗
	 * 
	 * @return
	 */
	private boolean isFirst() {
		return SafeSharedpreference.getBoolean(this, ConstConfig.ISLOSTFIRST,
				true);
	}
}
