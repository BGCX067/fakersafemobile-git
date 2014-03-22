package com.faker.mobilesafe.view.activitys;

import java.util.List;

import com.faker.mobilesafe.R;
import com.faker.mobilesafe.dao.BlackNumberDao;
import com.faker.mobilesafe.view.ui.adapter.ShowBlacknumberAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class ShowBlacknumberListActivity extends Activity {

	private ListView listView;
	private TextView emptyView;
	private ShowBlacknumberAdapter adapter;
	private List<String> blackList;
	private BlackNumberDao dao;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_blacknumber);
		dao = new BlackNumberDao(this);
		blackList = dao.findAll();
		adapter = new ShowBlacknumberAdapter(this,blackList);
		listView = (ListView) findViewById(R.id.lv_blacknumber);
		listView.setAdapter(adapter);
		emptyView = (TextView) findViewById(R.id.empty);
		listView.setEmptyView(emptyView);
	}
	
	public void onBack(View v){
		onBackPressed();
		finish();
	}
}
