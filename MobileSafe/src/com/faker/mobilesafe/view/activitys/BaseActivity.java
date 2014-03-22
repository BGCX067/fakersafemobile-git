package com.faker.mobilesafe.view.activitys;

import android.app.Activity;
import android.content.Context;
import android.view.View;

public class BaseActivity extends Activity {

	/**
	 * 通过id获得控件对象，对View控件不适用
	 * @param id 控件id
	 * @return 控件对象
	 */
	protected <T extends View> T getView(int id) {
		return (T) findViewById(id);
	}
	
	/**
	 * 吐西提示
	 * @param context
	 * @param text
	 * @param duration
	 */
	protected void Toast(Context context,CharSequence text,int duration) {
		android.widget.Toast.makeText(context, text, duration).show();
	}
	
	/**
	 * 
	 * @param context
	 * @param resId
	 * @param duration
	 */
	protected void Toast(Context context,int resId,int duration) {
		android.widget.Toast.makeText(context, resId, duration).show();
	}
}
