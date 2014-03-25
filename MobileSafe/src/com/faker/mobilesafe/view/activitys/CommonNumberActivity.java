package com.faker.mobilesafe.view.activitys;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;

import com.faker.mobilesafe.R;
import com.faker.mobilesafe.deal.CommonNumberService;

public class CommonNumberActivity extends Activity implements
		OnChildClickListener {

	private ExpandableListView elv_common_number;
	private SimpleExpandableListAdapter adapter;
	private CommonNumberService commonNumberService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_number);
		// INIT UI
		elv_common_number = (ExpandableListView) findViewById(R.id.elv_common_number);
		// INIT DATA
		commonNumberService = new CommonNumberService(this);
		try {
			commonNumberService.initDB();
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(this, "数据文件异常", Toast.LENGTH_SHORT).show();
			onBackPressed();
			finish();
		}
		List<Map<String, String>> groupData = commonNumberService
				.getGroundData();
		for (Map<String, String> map : groupData) {
			String idx = map.get("idx");
			Log.i("lichfaker", "idx:" + idx);
		}
		List<List<Map<String, String>>> childData = commonNumberService
				.getChildData();
		adapter = new SimpleExpandableListAdapter(this, groupData,
				android.R.layout.simple_expandable_list_item_1,
				new String[] { "name" }, new int[] { android.R.id.text1 },
				childData, android.R.layout.simple_expandable_list_item_2,
				new String[] { "name", "number" }, new int[] {
						android.R.id.text1, android.R.id.text2 });
		elv_common_number.setAdapter(adapter);
		elv_common_number.setOnChildClickListener(this);
	}

	public void onBack(View v) {
		onBackPressed();
		finish();
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		Map<String, String> child = (Map<String, String>) adapter.getChild(
				groupPosition, childPosition);
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_DIAL);
		intent.setData(Uri.parse("tel:" + child.get("number")));
		startActivity(intent);
		return false;
	}
}
