package com.faker.mobilesafe.view.ui.adapter;

import java.util.List;

import com.faker.mobilesafe.R;
import com.faker.mobilesafe.bean.MsgRecordBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MsgLogListAdapter extends BaseAdapter {

	private List<MsgRecordBean> msgLists;
	private LayoutInflater inflater;

	public MsgLogListAdapter(Context context, List<MsgRecordBean> msgLists) {
		// TODO Auto-generated constructor stub
		this.msgLists = msgLists;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return msgLists.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return msgLists.get(position);
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
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.msg_log_item, null);
			holder = new ViewHolder();
			holder.number = (TextView) convertView
					.findViewById(R.id.phone_number);
			holder.time = (TextView) convertView.findViewById(R.id.msg_time);
			holder.content = (TextView) convertView
					.findViewById(R.id.msg_content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MsgRecordBean bean = msgLists.get(position);
		holder.number.setText(bean.getNumber());
		holder.time.setText(bean.getTime());
		holder.content.setText(bean.getContent());
		return convertView;
	}

	private final class ViewHolder {
		private TextView number;
		private TextView time;
		private TextView content;
	}
}
