package com.faker.mobilesafe.view.activitys;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.faker.mobilesafe.R;
import com.faker.mobilesafe.deal.ConstConfig;
import com.faker.mobilesafe.deal.SafeSharedpreference;

public class AddIPActivity extends BaseActivity implements OnClickListener {

	private TextView tv_title;
	private TextView tv_ip_state;
	private ImageView iv_ip_switch;
	private EditText ip_number;

	private boolean is_autoIp;
	private String number;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_ip);
		initData();
		initUI();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
	}

	/**
	 * 保存数据
	 * 
	 * @param v
	 */
	public void save(View v) {
		SafeSharedpreference.save(this, ConstConfig.ISAUTOIP, is_autoIp);
		SafeSharedpreference.save(this, ConstConfig.IPNUMBER, ip_number
				.getText().toString());
		onBackPressed();
	}

	/**
	 * 返回上一界面
	 * 
	 * @param v
	 */
	public void onBack(View v) {
		onBackPressed();
	}

	private void initData() {
		is_autoIp = SafeSharedpreference.getBoolean(this, ConstConfig.ISAUTOIP,
				false);
		number = SafeSharedpreference.getString(this, ConstConfig.IPNUMBER);
	}

	private void initUI() {
		tv_title = getView(R.id.header_title);
		tv_title.setText(R.string.hightools_addip);
		tv_ip_state = getView(R.id.ip_state);
		iv_ip_switch = getView(R.id.ip_switch);
		iv_ip_switch.setOnClickListener(this);
		if (is_autoIp) {
			tv_ip_state.setText("已开启");
			iv_ip_switch.setImageResource(R.drawable.switch_on_normal);
		} else {
			tv_ip_state.setText("未开启");
			iv_ip_switch.setImageResource(R.drawable.switch_off_normal);
		}
		ip_number = getView(R.id.ip_number);
		ip_number.setText(number);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.ip_switch:
			if (is_autoIp) {
				tv_ip_state.setText("未开启");
				iv_ip_switch.setImageResource(R.drawable.switch_off_normal);
				is_autoIp = false;
			} else {
				tv_ip_state.setText("已开启");
				iv_ip_switch.setImageResource(R.drawable.switch_on_normal);
				is_autoIp = true;
			}
			break;

		default:
			break;
		}
	}

}
