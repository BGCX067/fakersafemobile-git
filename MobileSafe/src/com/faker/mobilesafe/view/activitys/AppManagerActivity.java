package com.faker.mobilesafe.view.activitys;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.faker.mobilesafe.R;
import com.faker.mobilesafe.bean.AppInfoBean;
import com.faker.mobilesafe.deal.AppInfoService;
import com.faker.mobilesafe.view.ui.adapter.AppManagerAdapter;

public class AppManagerActivity extends Activity implements OnClickListener,
		OnItemClickListener {

	protected static final int INIT_DATA_SUCCESS = 0;
	private TextView asset_title;
	private ListView lv_appmanage;
	private RelativeLayout rl_loading;
	private PopupWindow popupWindow;

	private List<AppInfoBean> allAppInfos;
	private List<AppInfoBean> userAppInfos;

	private AppInfoService appService;

	private AppManagerAdapter adapter;

	private boolean isUserApp = false;

	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.applationinstall);
		// 初始化UI控件
		asset_title = (TextView) findViewById(R.id.header_title);
		lv_appmanage = (ListView) findViewById(R.id.lv_appmanage);
		rl_loading = (RelativeLayout) findViewById(R.id.rl_loading);

		appService = new AppInfoService(this);
		context = this;

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				allAppInfos = appService.getAppInfo();
				userAppInfos = new ArrayList<AppInfoBean>();
				for (AppInfoBean bean : allAppInfos) {
					if (bean.isUserApp()) {
						userAppInfos.add(bean);
					}
				}
				Message msg = new Message();
				msg.what = INIT_DATA_SUCCESS;
				handler.sendMessage(msg);
			}
		}).start();

		asset_title.setOnClickListener(this);
		lv_appmanage.setOnItemClickListener(this);
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case INIT_DATA_SUCCESS:
				if(adapter == null){
					adapter = new AppManagerAdapter(context, allAppInfos);
					lv_appmanage.setAdapter(adapter);
				}else{
					if(isUserApp){
						adapter.update(userAppInfos);
					}else{
						adapter.update(allAppInfos);
					}
				}
				rl_loading.setVisibility(View.GONE);
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}

	};
	
	public void onBack(View v){
		onBackPressed();
		finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.header_title:
			if (adapter != null) {
				if (isUserApp) {
					adapter.update(allAppInfos);
					isUserApp = false;
					asset_title.setText("所   有  程   序");
				} else {
					adapter.update(userAppInfos);
					isUserApp = true;
					asset_title.setText("用  户  程   序");
				}
			}
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("static-access")
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		View contentView = view.inflate(context, R.layout.popup_window, null);
		LinearLayout ll_uninstall = (LinearLayout) contentView
				.findViewById(R.id.ll_uninstall);
		LinearLayout ll_start = (LinearLayout) contentView
				.findViewById(R.id.ll_start);
		LinearLayout ll_share = (LinearLayout) contentView
				.findViewById(R.id.ll_share);
		LinearLayout ll_popup_window = (LinearLayout) contentView
				.findViewById(R.id.ll_popupwindow);

		MyOnclickListener listener = new MyOnclickListener(position);
		ll_uninstall.setOnClickListener(listener);
		ll_start.setOnClickListener(listener);
		ll_share.setOnClickListener(listener);

		popupWindow = new PopupWindow(contentView,
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		ScaleAnimation animation = new ScaleAnimation(0, 1.0f, 0.5f, 1.0f,
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setDuration(100);
		ll_popup_window.startAnimation(animation);

		int[] location = new int[2];
		view.getLocationInWindow(location);
		int x = location[0] + 70;
		int y = location[1];

		// 1 指定popupwindow的背景 2 popupwindow能够获得焦点
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setFocusable(true);
		popupWindow.showAtLocation(view, Gravity.LEFT | Gravity.TOP, x, y);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode == 100){
			rl_loading.setVisibility(View.VISIBLE);
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					allAppInfos = appService.getAppInfo();
					userAppInfos = new ArrayList<AppInfoBean>();
					for (AppInfoBean bean : allAppInfos) {
						if (bean.isUserApp()) {
							userAppInfos.add(bean);
						}
					}
					Message msg = new Message();
					msg.what = INIT_DATA_SUCCESS;
					handler.sendMessage(msg);
				}
			}).start();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if(popupWindow != null)
			popupWindow.dismiss();
		super.onResume();
	}
	
	private final class MyOnclickListener implements OnClickListener {

		private int position;

		public MyOnclickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int id = v.getId();
			AppInfoBean bean = (AppInfoBean) adapter.getItem(position);
			switch (id) {
			case R.id.ll_uninstall: // 卸载
				Intent uninstallIntent = new Intent();
				uninstallIntent.setAction(Intent.ACTION_DELETE);
				String packName = bean.getPackageName();
				uninstallIntent.setData(Uri.parse("package:"+packName));
				startActivityForResult(uninstallIntent, 100);
				break;
			case R.id.ll_start: // 启动
				appStart(bean);
				break;
			case R.id.ll_share: // 分享
				Intent shareIntent = new Intent();
				shareIntent.setAction(Intent.ACTION_SEND);
				shareIntent.setType("text/plain");
				shareIntent.putExtra(Intent.EXTRA_SUBJECT, "f分享");
				shareIntent.putExtra(Intent.EXTRA_TEXT, "HI 推荐您使用一款软件：" + bean.getAppName());
				shareIntent = Intent.createChooser(shareIntent, "分享");
				startActivity(shareIntent);
				break;

			default:
				break;
			}
		}

		private void appStart(AppInfoBean bean) {
			// TODO Auto-generated method stub
			String packageName = bean.getPackageName();
			PackageManager pm = context.getPackageManager();
			try {
				PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
				ActivityInfo[] activityInfos = packageInfo.activities;
				if(activityInfos == null || activityInfos.length == 0){
					throw new Exception();
				}else{
					ActivityInfo activityInfo = activityInfos[0];
					String name = activityInfo.name;
					ComponentName component = new ComponentName(packageName, name);
					Intent intent = new Intent();
					intent.setComponent(component);
					startActivity(intent);
					
				}
			} catch (Exception e) {
				Toast.makeText(context, "无法启动该应用", Toast.LENGTH_SHORT).show();
			}
		}

	}
}
