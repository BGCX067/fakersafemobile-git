package com.faker.mobilesafe.view.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.faker.mobilesafe.R;

public class MsgLogActivity extends Activity implements OnClickListener{

	private ListView lv_msg_log;
	private TextView emptyView;
	private Button addButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_black_from_msglog);

		lv_msg_log = (ListView) findViewById(R.id.lv_msg_log);
		emptyView = (TextView) findViewById(R.id.empty);
		addButton = (Button) findViewById(R.id.add_comfirm);

		lv_msg_log.setEmptyView(emptyView);
		
		addButton.setOnClickListener(this);
	}

	public void onBack(View v) {
		onBackPressed();
		finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// 添加黑名单
		onBackPressed();
		finish();
	}
}
