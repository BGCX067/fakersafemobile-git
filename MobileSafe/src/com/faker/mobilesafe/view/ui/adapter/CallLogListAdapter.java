package com.faker.mobilesafe.view.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.faker.mobilesafe.R;
import com.faker.mobilesafe.bean.CallLogBean;

public class CallLogListAdapter extends BaseAdapter {

	private List<CallLogBean> callLogList;
	private LayoutInflater inflater;

	public CallLogListAdapter(Context context, List<CallLogBean> callLogList) {
		// TODO Auto-generated constructor stub
		this.callLogList = callLogList;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return callLogList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return callLogList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		CallLogBean bean = callLogList.get(position);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.call_log_item, null);
			holder = new ViewHolder();
			holder.phone_number = (TextView) convertView
					.findViewById(R.id.phone_number);
			holder.call_time = (TextView) convertView
					.findViewById(R.id.call_time);
			holder.call_address = (TextView) convertView
					.findViewById(R.id.call_address);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String name = bean.getName();
		if ("".equals(name) || name == null) {
			holder.phone_number.setText(bean.getNumber());
		} else {
			holder.phone_number.setText(name);
		}

		holder.call_time.setText(bean.getTime());
		holder.call_address.setText(bean.getAddress());
		return convertView;
	}

	private final class ViewHolder {
		private TextView phone_number;
		private TextView call_time;
		private TextView call_address;
	}
}
