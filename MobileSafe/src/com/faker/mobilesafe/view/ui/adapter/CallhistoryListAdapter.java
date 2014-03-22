package com.faker.mobilesafe.view.ui.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import com.faker.mobilesafe.R;
import com.faker.mobilesafe.bean.CallRecordBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CallhistoryListAdapter extends BaseAdapter {

	private List<CallRecordBean> call_list;
	private LayoutInflater inflater;

	public void update(List<CallRecordBean> call_list) {
		this.call_list = call_list;
		notifyDataSetChanged();
	}

	public CallhistoryListAdapter(Context context,
			List<CallRecordBean> call_list) {
		// TODO Auto-generated constructor stub
		this.call_list = call_list;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return call_list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return call_list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.call_record_item, null);
		}

		CallRecordBean bean = call_list.get(position);

		TextView phone_number = (TextView) convertView
				.findViewById(R.id.phone_number);
		TextView call_time = (TextView) convertView
				.findViewById(R.id.call_time);
		TextView call_address = (TextView) convertView
				.findViewById(R.id.call_address);
		TextView call_type = (TextView) convertView
				.findViewById(R.id.call_type);

		phone_number.setText(bean.getNumber());
		SimpleDateFormat sinDateFormat = new SimpleDateFormat("MM/DD HH:MM");
		String date = sinDateFormat.format(Long.parseLong(bean.getTime()));
		call_time.setText(date);
		call_address.setText(bean.getAddress());
		call_type.setText(bean.getType());
		return convertView;
	}

}
