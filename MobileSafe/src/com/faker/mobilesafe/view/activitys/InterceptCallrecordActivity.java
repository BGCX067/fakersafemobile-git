package com.faker.mobilesafe.view.activitys;

import java.util.ArrayList;
import java.util.List;

import com.faker.mobilesafe.R;
import com.faker.mobilesafe.bean.CallRecordBean;
import com.faker.mobilesafe.dao.CallRecordDao;
import com.faker.mobilesafe.view.ui.adapter.CallhistoryListAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class InterceptCallrecordActivity extends Activity implements
		OnClickListener {

	private ListView lv_call;
	private CallhistoryListAdapter adapter;
	private TextView empty_view;
	private Button delete_all;

	private List<CallRecordBean> call_list;

	private CallRecordDao dao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intecepter_callhistory);
		dao = new CallRecordDao(this);
		lv_call = (ListView) findViewById(R.id.callhistory_listview);
		empty_view = (TextView) findViewById(R.id.empty);
		call_list = dao.findAll();
		adapter = new CallhistoryListAdapter(this, call_list);
		lv_call.setAdapter(adapter);
		lv_call.setEmptyView(empty_view);
		delete_all = (Button) findViewById(R.id.delete_all);
		if (call_list.size() > 0) {
			delete_all.setEnabled(true);
		}
		delete_all.setOnClickListener(this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		call_list = dao.findAll();
		adapter.update(call_list);
		if (call_list.size() > 0) {
			delete_all.setEnabled(true);
		}
		super.onNewIntent(intent);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		call_list = dao.findAll();
		adapter.update(call_list);
		if (call_list.size() > 0) {
			delete_all.setEnabled(true);
		}
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		dao.deleteAll();
		delete_all.setEnabled(false);
		adapter.update(new ArrayList<CallRecordBean>());
	}
}
