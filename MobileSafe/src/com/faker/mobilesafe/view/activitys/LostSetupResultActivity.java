package com.faker.mobilesafe.view.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.faker.mobilesafe.R;
import com.faker.mobilesafe.deal.ConstConfig;
import com.faker.mobilesafe.deal.SafeSharedpreference;

public class LostSetupResultActivity extends BaseActivity {

	private String safeNumber; // 安全号码
	private boolean bOpen; // 防盗保护是否开启
	private boolean bShow; // 防盗模块是否隐藏
	private String location; // 获取手机位置指令
	private String delete; // 恢复出厂设置指令
	private String lockscreen; // 锁定手机屏幕指令
	private String alarm; // 发出警报指令

	private TextView tv_safenumber;
	private TextView tv_openlost_hint; // 是否打开手机防盗的提示
	private ImageView iv_lost_switch;
	private TextView tv_hide_hint;
	private ImageView iv_hide_switch;
	private TextView tv_command_hint;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result_main);
		initData();
		initUI();
	}

	/**
	 * 重新进入设置向导
	 * 
	 * @param v
	 */
	public void reSetup(View v) {
		Intent intent = new Intent(this, LostSetupActivity.class);
		startActivity(intent);
		finish();
	}
	
	public void onBack(View v){
		onBackPressed();
		finish();
	}

	/**
	 * 是否开启手机防盗
	 * 
	 * @param v
	 */
	public void openLost(View v) {
		if (bOpen) {
			tv_openlost_hint.setText(R.string.setupresult_state_off);
			iv_lost_switch.setImageResource(R.drawable.switch_off_normal);
			bOpen = false;
		} else {
			tv_openlost_hint.setText(R.string.setupresult_state_on);
			iv_lost_switch.setImageResource(R.drawable.switch_on_normal);
			bOpen = true;
		}
		SafeSharedpreference.save(this, ConstConfig.ISOPENLOST, bOpen);
	}

	/**
	 * 修改防盗模块名称
	 * 
	 * @param v
	 */
	public void modifyName(View v) {
		Intent intent = new Intent(this, ModifyNameActivety.class);
		startActivity(intent);
	}

	/**
	 * 是否隐藏手机防盗模块
	 * 
	 * @param v
	 */
	public void hideLost(View v) {
		if (bShow) {
			tv_hide_hint.setText(R.string.setupresult_moudle_off);
			iv_hide_switch.setImageResource(R.drawable.switch_off_normal);
			bShow = false;
		} else {
			tv_hide_hint.setText(R.string.setupresult_moudle_on);
			iv_hide_switch.setImageResource(R.drawable.switch_on_normal);
			bShow = true;
		}
		SafeSharedpreference.save(this, ConstConfig.ISSHOW, bShow);
	}

	/**
	 * 修改指令
	 * 
	 * @param v
	 */
	public void modify_command(View v) {
		Intent intent = new Intent(this,ModifyCommandActivity.class);
		startActivity(intent);
	}

	private void initData() {
		safeNumber = SafeSharedpreference.getString(this,
				ConstConfig.SAFENUMBER);
		bOpen = SafeSharedpreference.getBoolean(this, ConstConfig.ISOPENLOST,
				false);
		bShow = SafeSharedpreference.getBoolean(this, ConstConfig.ISSHOW, true);
		location = SafeSharedpreference.getString(this,
				ConstConfig.LOCATION_COMMAND, ConstConfig.LOCATION_COMMAND);
		delete = SafeSharedpreference.getString(this,
				ConstConfig.DELETE_COMMAND, ConstConfig.DELETE_COMMAND);
		lockscreen = SafeSharedpreference.getString(this,
				ConstConfig.LOCK_COMMAND, ConstConfig.LOCK_COMMAND);
		alarm = SafeSharedpreference.getString(this, ConstConfig.ALARM_COMMAND,
				ConstConfig.ALARM_COMMAND);
	}

	private void initUI() {
		// 设置安全号码
		tv_safenumber = getView(R.id.safe_number);
		tv_safenumber.setText(safeNumber);
		// 防盗保护设置
		tv_openlost_hint = getView(R.id.open_lost);
		iv_lost_switch = getView(R.id.open_lost_switch);
		if (bOpen) {
			tv_openlost_hint.setText(R.string.setupresult_state_on);
			iv_lost_switch.setImageResource(R.drawable.switch_on_normal);
		} else {
			tv_openlost_hint.setText(R.string.setupresult_state_off);
			iv_lost_switch.setImageResource(R.drawable.switch_off_normal);
		}
		tv_hide_hint = getView(R.id.hide_moudle_lost);
		iv_hide_switch = getView(R.id.hide_lost_switch);
		if (bShow) {
			tv_hide_hint.setText(R.string.setupresult_moudle_on);
			iv_hide_switch.setImageResource(R.drawable.switch_on_normal);
		} else {
			tv_hide_hint.setText(R.string.setupresult_moudle_off);
			iv_hide_switch.setImageResource(R.drawable.switch_off_normal);
		}
		tv_command_hint = getView(R.id.command_hint);
		StringBuilder sb = new StringBuilder();
		sb.append(location);
		sb.append("   获取手机位置\n");
		sb.append(delete);
		sb.append("   恢复出厂设置\n");
		sb.append(lockscreen);
		sb.append("   锁定手机屏幕\n");
		sb.append(alarm);
		sb.append("   发出警报音乐\n");
		tv_command_hint.setText(sb.toString());

	}

}
