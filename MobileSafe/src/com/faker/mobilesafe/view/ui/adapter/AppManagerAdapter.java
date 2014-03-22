package com.faker.mobilesafe.view.ui.adapter;

import java.util.List;

import com.faker.mobilesafe.R;
import com.faker.mobilesafe.bean.AppInfoBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AppManagerAdapter extends BaseAdapter {

	private List<AppInfoBean> appInfos;
	private LayoutInflater inflater;

	public AppManagerAdapter(Context context, List<AppInfoBean> appInfos) {
		// TODO Auto-generated constructor stub
		this.appInfos = appInfos;
		inflater = LayoutInflater.from(context);
	}

	public void update(List<AppInfoBean> appInfos) {
		this.appInfos = appInfos;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return appInfos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return appInfos.get(position);
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
			convertView = inflater
					.inflate(R.layout.applationinstall_item, null);
			holder = new ViewHolder();
			holder.appIcon = (ImageView) convertView.findViewById(R.id.appicon);
			holder.appName = (TextView) convertView.findViewById(R.id.appname);
			holder.appVersion = (TextView) convertView
					.findViewById(R.id.appversion);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		AppInfoBean bean = appInfos.get(position);

		holder.appIcon.setImageBitmap(bean.getAppIcon());
		holder.appName.setText(bean.getAppName());
		holder.appVersion.setText(bean.getAppVersion());

		return convertView;
	}

	private final class ViewHolder {
		private ImageView appIcon;
		private TextView appName;
		private TextView appVersion;
	}
}
