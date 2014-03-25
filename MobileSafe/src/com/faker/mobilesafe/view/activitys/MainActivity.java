package com.faker.mobilesafe.view.activitys;

import com.faker.mobilesafe.R;
import com.faker.mobilesafe.deal.CheckupdateThread;
import com.faker.mobilesafe.deal.ConstConfig;
import com.faker.mobilesafe.deal.SafeSharedpreference;
import com.faker.mobilesafe.service.BlackNumberService;
import com.faker.mobilesafe.service.ShowAddressService;
import com.faker.mobilesafe.view.ui.adapter.GVAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class MainActivity extends BaseActivity implements OnItemClickListener {

	private boolean isExit = false;
	private GVAdapter adapter;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isExit = false;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 绑定GridView的数据
		GridView main_gv = getView(R.id.main_gridview);
		adapter = new GVAdapter(this);
		main_gv.setAdapter(adapter);
		main_gv.setOnItemClickListener(this);
		// 是否开启更新检测
		boolean isUpdate = SafeSharedpreference.getBoolean(this,
				ConstConfig.ISUPDATE, true);
		boolean isAdress = SafeSharedpreference.getBoolean(this,
				ConstConfig.ISOPENADRESS, true);
		boolean isIntecpter = SafeSharedpreference.getBoolean(this,
				ConstConfig.ISINTECPTER, true);
		if (isUpdate) {
			// 开启版本更新检测后台服务
			new CheckupdateThread(this).connect();
		}
		// 如果开启归属地服务，则启动电话状态监听服务
		if (isAdress) {
			Intent intent = new Intent(this, ShowAddressService.class);
			startService(intent);
		}
		// 如果拦截服务，则启动黑名单号码拦截服务
		if (isIntecpter) {
			Intent intent = new Intent(this, BlackNumberService.class);
			startService(intent);
		}
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = null;
		switch (position) {
		case 0:// 软件管理
			intent = new Intent(this,AppManagerActivity.class);
			startActivity(intent);
			break;
		case 1:// 通讯卫士
			intent = new Intent(this, CommunicateActivity.class);
			startActivity(intent);
			break;
		case 2:// 高级工具
			intent = new Intent(this, HighToolsActivity.class);
			startActivity(intent);
			break;
		case 3:// 手机杀毒
			Toast(this, "3", 1);
			break;
		case 4:// 流量管理
			Toast(this, "4", 1);
			break;
		case 5:// 系统优化
			Toast(this, "5", 1);
			break;
		case 6:// 任务管理
			intent = new Intent(this, TaskManagerActivity.class);
			startActivity(intent);
			break;
		case 7:// 设置中心
			intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			break;
		case 8:// 手机防盗
			intent = new Intent(MainActivity.this, LostProtectedActivity.class);
			startActivity(intent);
			break;
		default:
			break;

		}
	}

	@Override
	public void onBackPressed() {
		if (!isExit) {
			Toast(this, "再按一次即可退出", android.widget.Toast.LENGTH_SHORT);
			isExit = true;
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			finish();
		}
	}
}
