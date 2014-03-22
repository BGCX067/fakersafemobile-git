package com.faker.mobilesafe.view.ui.adapter;

import java.util.List;

import com.faker.mobilesafe.R;
import com.faker.mobilesafe.dao.BlackNumberDao;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class ShowBlacknumberAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<String> blackNumbers;
	private BlackNumberDao dao;

	public ShowBlacknumberAdapter(Context context, List<String> blackList) {
		inflater = LayoutInflater.from(context);
		blackNumbers = blackList;
		dao = new BlackNumberDao(context);
	}

	@Override
	public int getCount() {
		return blackNumbers.size();
	}

	@Override
	public Object getItem(int position) {
		return blackNumbers.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.show_blacknum_list_item, null);
		TextView number = (TextView) convertView
				.findViewById(R.id.contact_phone);
		Button delete = (Button) convertView
				.findViewById(R.id.delete_blacknumber);
		number.setText(blackNumbers.get(position));
		Log.i("test", blackNumbers.get(position));
		delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dao.delete(blackNumbers.get(position));
				blackNumbers.remove(position);
				notifyDataSetChanged();
			}
		});
		return convertView;
	}

}
