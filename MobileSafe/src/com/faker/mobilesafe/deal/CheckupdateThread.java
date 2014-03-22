package com.faker.mobilesafe.deal;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;

import com.faker.mobilesafe.R;
import com.faker.mobilesafe.bean.UpdateBean;
import com.faker.mobilesafe.util.DownloadHelper;
import com.faker.mobilesafe.util.ViewHelper;
import com.faker.mobilesafe.util.XmlParseUtil;
import com.faker.mobilesafe.view.ui.CustomTipsconfirmDialoog;
import com.faker.mobilesafe.view.ui.CustomTipsconfirmDialoog.CustomDialogListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

public class CheckupdateThread {

	private Activity context;
	private CustomTipsconfirmDialoog dialog;

	private final static int UPDATE = 100; // 有更新
	private final static int DIRERROR = 101; // SDcard不存在
	private final static int DOWNLOADERROR = 102;// 下载失败

	public CheckupdateThread(Activity context) {
		this.context = context;
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE:
				tipsDialog();
				break;
			case DIRERROR:
				Toast.makeText(context, "SDCard不存在", Toast.LENGTH_LONG).show();
				break;
			case DOWNLOADERROR:
				Toast.makeText(context, "下载失败", Toast.LENGTH_LONG).show();
				break;
			}
			super.handleMessage(msg);
		}
	};

	/**
	 * 更新提示对话框
	 */
	private void tipsDialog() {
		final UpdateBean bean = UpdateBean.getInstance();
		dialog = new CustomTipsconfirmDialoog(context, R.layout.notice_dialog,
				R.style.custom_dialog, new CustomDialogListener() {
					@Override
					public void onClick(View v) {
						if (v.getId() == R.id.dialog_confirm) {
							// 下载安装APK
							update(bean.getApkurl());
						}
						dialog.dismiss();
					}
				});
		dialog.setTitle("检测到更新").setInfo(bean.getDescripte())
				.setConfirm("立即下载").setCancle("下次再说");
		dialog.show();
	}

	/**
	 * 连接服务端，检测更新
	 */
	public void connect() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					URL url = new URL(ConstConfig.APKURL);
					HttpURLConnection httpURLConnection = (HttpURLConnection) url
							.openConnection();
					// 设置连接方式，方法名必须大写
					httpURLConnection.setRequestMethod("GET");
					httpURLConnection.setConnectTimeout(5000);
					if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
						// 解析XML文件，将结果存入bean中
						XmlParseUtil.setUpdateInfo(httpURLConnection
								.getInputStream());
						UpdateBean bean = UpdateBean.getInstance();
						// 如果版本不一致则提示更新
						if (!bean.getVersion().equals(
								ViewHelper.getVersion(context))) {
							Message msg = new Message();
							msg.what = UPDATE;
							handler.sendMessage(msg);
						}
					}
				} catch (Exception e) {
				}
			}
		}).start();
	}

	/**
	 * 下载安装
	 * 
	 * @param url
	 */
	private void update(final String url) {
		Message msg = new Message();
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			// 下载进度条
			final ProgressDialog pd = new ProgressDialog(context);
			pd.setTitle("下载中...");
			pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pd.show();
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						String dirPath = Environment.getExternalStorageDirectory().getPath()
								+ "/Download";
						File apkFile = DownloadHelper.getServerFile(url,dirPath,pd);
						pd.dismiss();
						if (apkFile == null) {
							Message msg = new Message();
							msg.what = DOWNLOADERROR;
							handler.sendMessage(msg);
						}
						/**
						 * <intent-filter> <action
						 * android:name="android.intent.action.VIEW" />
						 * <category
						 * android:name="android.intent.category.DEFAULT" />
						 * <data android:scheme="content" /> <data
						 * android:scheme="file" /> <data android:mimeType=
						 * "application/vnd.android.package-archive" />
						 * </intent-filter>
						 */
						Intent intent = new Intent();
						intent.setAction("android.intent.action.VIEW");
						intent.addCategory("android.intent.category.DEFAULT");
						intent.setDataAndType(Uri.fromFile(apkFile),
								"application/vnd.android.package-archive");
						context.startActivity(intent);
						context.finish();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		} else {
			// SDCard不存在
			msg.what = DIRERROR;
			handler.sendMessage(msg);
		}
	}
}
