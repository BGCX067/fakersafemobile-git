package com.faker.mobilesafe.view.activitys;

import java.io.File;
import java.io.InputStream;

import com.faker.mobilesafe.R;
import com.faker.mobilesafe.dao.PhoneAddressQueryDao;
import com.faker.mobilesafe.deal.ConstConfig;
import com.faker.mobilesafe.deal.SafeSharedpreference;
import com.faker.mobilesafe.util.FileUtil;
import com.faker.mobilesafe.util.ViewHelper;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.view.Menu;
import android.widget.TextView;

public class SplashActivity extends BaseActivity {

	// 延迟跳转的时间，单位毫秒
	private final static long SPLASH_DELAY_TIME = 200;
	private Context context;
	private MyProgressDialog pd;
	private PhoneAddressQueryDao phone_query;
	private Thread thread;

	private final static int INIT_DATABASE_ERROR = 100;
	public static final int INIT_DATABASE_SUCCESS = 101;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		TextView tx_version = getView(R.id.textView_version);
		tx_version.setText("版本号:" + ViewHelper.getVersion(this));
		pd = new MyProgressDialog(this);
		phone_query = new PhoneAddressQueryDao();
		context = this;
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				if (!phone_query.isExist(context)) {
					updateDB();
				} else {
					goHome();
				}
			}
		}, SPLASH_DELAY_TIME);

	}

	/**
	 * 数据库文件更新
	 */
	private void updateDB() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			// 下载归属地数据库文件到SDcard
			pd.show();
			thread = new Thread(new DownloadDbTask());
			thread.start();
		} else {
			Toast(this, "SDCard不存在", android.widget.Toast.LENGTH_LONG);
			finish();
		}
	}

	/**
	 * 主界面跳转函数
	 */
	private void goHome() {
		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
		SplashActivity.this.startActivity(intent);
		SplashActivity.this.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case INIT_DATABASE_ERROR:
				Toast(context, "更新数据库失败，无法启动程序",
						android.widget.Toast.LENGTH_LONG);
				pd.dismiss();
				finish();
				break;
			case INIT_DATABASE_SUCCESS:
				pd.dismiss();
				SafeSharedpreference.save(context, ConstConfig.ISINITDB, true);
				goHome();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}

	};

	private final class DownloadDbTask implements Runnable {

		@Override
		public void run() {
			try {
				File dir = new File(phone_query.getDirpath());
				dir.mkdirs();
				AssetManager am = context.getAssets();
				String[] allFiles = am.list("");
				String[] partFileList = new String[] { allFiles[0],
						allFiles[1], allFiles[2] };
				FileUtil.mergeZipFile(context, partFileList, dir, dir,
						"address.db");
				InputStream is = getAssets().open("commonnum.db");
				FileUtil.moveFile(is, context.getFilesDir(), "commonnum.db");

				Message msg = new Message();
				msg.what = INIT_DATABASE_SUCCESS;
				handler.sendMessage(msg);

			} catch (Exception e) {
                e.printStackTrace();
				Message msg = new Message();
				msg.what = INIT_DATABASE_ERROR;
				handler.sendMessage(msg);
			}
		}

	}

	private final class MyProgressDialog extends ProgressDialog {

		public MyProgressDialog(Context context) {
			super(context);
			setTitle("正在更新数据库...");
			setProgressStyle(STYLE_SPINNER);
		}

		@Override
		public void onBackPressed() {
			// TODO Auto-generated method stub
			super.onBackPressed();
			finish();
		}

	}
}
