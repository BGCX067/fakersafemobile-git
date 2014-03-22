package com.faker.mobilesafe.view.activitys;

import java.util.List;

import com.faker.mobilesafe.R;
import com.faker.mobilesafe.bean.ContactBean;
import com.faker.mobilesafe.deal.ContactInfoService;
import com.faker.mobilesafe.view.ui.adapter.ContactListAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ContactListActivity extends BaseActivity implements
		OnItemClickListener {

	private ListView listView;
	private ContactListAdapter adapter;
	private TextView emptyView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_list);
		listView = getView(R.id.lv_contact);
		emptyView = getView(R.id.empty);
		List<ContactBean> list_contacts = new ContactInfoService(this)
				.getContacts();
		adapter = new ContactListAdapter(this, list_contacts);
		listView.setAdapter(adapter);
		listView.setEmptyView(emptyView);
		listView.setOnItemClickListener(this);
	}
	
	public void onBack(View v){
		onBackPressed();
		finish();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ContactBean info = (ContactBean) adapter.getItem(position);
		String number = info.getPhoneNumber();
		Intent data = new Intent();
		data.putExtra("number", number);
		setResult(200, data);// 往上一个activity返回数据
		finish();
	}
}
