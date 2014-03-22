package com.faker.mobilesafe.view.ui.adapter;

import java.util.List;

import com.faker.mobilesafe.R;
import com.faker.mobilesafe.bean.ContactBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BlackContactListAdapter extends BaseAdapter {

	private List<ContactBean> list_contacts;
	private LayoutInflater inflater;

	public BlackContactListAdapter(Context context,
			List<ContactBean> list_contacts) {
		this.list_contacts = list_contacts;
		inflater = LayoutInflater.from(context);
	}


	@Override
	public int getCount() {
		return list_contacts.size();
	}

	@Override
	public Object getItem(int position) {
		return list_contacts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ContactBean bean = list_contacts.get(position);
		convertView = inflater.inflate(R.layout.contact_list_item2, null);
		TextView contact_name = (TextView) convertView
				.findViewById(R.id.contact_name);
		TextView contact_number = (TextView) convertView
				.findViewById(R.id.contact_phone);
		contact_name.setText(bean.getName());
		contact_number.setText(bean.getPhoneNumber());
		return convertView;
	}

}
