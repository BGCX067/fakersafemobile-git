package com.faker.mobilesafe.view.ui.adapter;

import java.util.List;

import com.faker.mobilesafe.R;
import com.faker.mobilesafe.bean.TaskInfoBean;
import com.faker.mobilesafe.util.FormatUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TaskManagerAdapter extends BaseAdapter {

	private List<TaskInfoBean> unKillTasks;
	private List<TaskInfoBean> adviceKillTasks;

	private LayoutInflater inflater;
	private Context context;

	public TaskManagerAdapter(Context context, List<TaskInfoBean> unKillTasks,
			List<TaskInfoBean> adviceKillTasks) {
		// TODO Auto-generated constructor stub
		this.unKillTasks = unKillTasks;
		this.adviceKillTasks = adviceKillTasks;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	public void update(List<TaskInfoBean> unKillTasks,
			List<TaskInfoBean> adviceKillTasks) {
		this.unKillTasks = unKillTasks;
		this.adviceKillTasks = adviceKillTasks;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (adviceKillTasks.size() > 0) {
			return unKillTasks.size() + adviceKillTasks.size() + 2;
		} else {
			return unKillTasks.size() + 1;
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (position == 0)
			return null;
		int adviceCount = adviceKillTasks.size();
		if (adviceCount == 0) {
			return unKillTasks.get(position - 1);
		}
		if (position <= adviceCount) {
			position = position - 1;
			return adviceKillTasks.get(position);
		} else {
			position = position - 2 - adviceCount;
			return unKillTasks.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		int adviceCount = adviceKillTasks.size();
		if (position <= adviceCount) {
			position = position - 1;
		} else {
			position = position - 2;
		}
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		int adviceCount = adviceKillTasks.size();
		int unkillCount = unKillTasks.size();
		TaskInfoBean bean;
		if (adviceCount == 0) {
			if (position == 0) {
				TextView view = new TextView(context);
				view.setText("建议保留的进程项: " + unkillCount + " 个");
				view.setBackgroundColor(Color.GRAY);
				return view;
			} else {
				position = position - 1;
				bean = unKillTasks.get(position);
			}
		} else {
			if (position == 0) {
				TextView view = new TextView(context);
				view.setText("建议结束的进程项: " + adviceCount + " 个");
				view.setBackgroundColor(Color.GRAY);
				return view;
			} else if (position <= adviceCount) {
				position = position - 1;
				bean = adviceKillTasks.get(position);
			} else if (position == adviceCount + 1) {
				TextView view = new TextView(context);
				view.setText("建议保留的进程项: " + unkillCount + " 个");
				view.setBackgroundColor(Color.GRAY);
				return view;
			} else {
				position = position - 2 - adviceCount;
				bean = unKillTasks.get(position);
			}
		}
		String appName = bean.getAppName();
		long memorySize = bean.getMemorySize();
		Bitmap appIcon = bean.getAppIcon();

		ViewHolder holder;
		if (convertView != null && !(convertView instanceof TextView)) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			convertView = inflater.inflate(R.layout.task_manager_item, null);
			holder = new ViewHolder();
			holder.app_icon = (ImageView) convertView
					.findViewById(R.id.appicon);
			holder.app_name = (TextView) convertView.findViewById(R.id.appname);
			holder.memorySize = (TextView) convertView
					.findViewById(R.id.memorysize);
			holder.checkBox = (ImageView) convertView
					.findViewById(R.id.clear_check);
			convertView.setTag(holder);
		}
		holder.app_icon.setImageBitmap(appIcon);
		holder.app_name.setText(appName);
		holder.memorySize.setText("占用内存:"
				+ FormatUtil.formatByte(memorySize * 1024));
		if (bean.isChecked()
				&& !bean.getPackageName().equals(context.getPackageName())) {
			holder.checkBox.setImageResource(R.drawable.checkbox_checked);
		} else {
			holder.checkBox.setImageResource(R.drawable.checkbox_unchecked);
		}
		return convertView;
	}

	private final class ViewHolder {
		private ImageView app_icon;
		private TextView app_name;
		private TextView memorySize;
		private ImageView checkBox;
	}
}
