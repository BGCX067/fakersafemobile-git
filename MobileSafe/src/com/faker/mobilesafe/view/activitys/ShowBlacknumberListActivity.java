package com.faker.mobilesafe.view.activitys;

import java.util.List;

import com.faker.mobilesafe.R;
import com.faker.mobilesafe.dao.BlackNumberDao;
import com.faker.mobilesafe.view.ui.adapter.ShowBlacknumberAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

public class ShowBlacknumberListActivity extends Activity {

	private ListView listView;
	private LinearLayout emptyView;
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
		emptyView = (LinearLayout) findViewById(R.id.empty);
		listView.setEmptyView(emptyView);
	}
	
	public void onBack(View v){
		onBackPressed();
		finish();
	}
}
