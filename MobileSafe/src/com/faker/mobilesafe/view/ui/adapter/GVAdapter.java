package com.faker.mobilesafe.view.ui.adapter;

import com.faker.mobilesafe.R;
import com.faker.mobilesafe.deal.ConstConfig;
import com.faker.mobilesafe.deal.SafeSharedpreference;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GVAdapter extends BaseAdapter {

	private int[] residArray; // gridview图片id数组
	private String[] iconStrings;// 图片对应的文字说明
	private Context context;

	/**
	 * 构造函数，传递Context对象
	 * 
	 * @param context
	 */
	public GVAdapter(Context context) {
		this.context = context;
		// residArray = context.getResources().getIntArray(R.array.main_gv);
		residArray = new int[] { R.drawable.widget01, R.drawable.widget02,
				R.drawable.widget03, R.drawable.widget04, R.drawable.widget05,
				R.drawable.widget06, R.drawable.widget07, R.drawable.widget09,
				R.drawable.widget08 };
		iconStrings = context.getResources().getStringArray(R.array.main_gv);
	}

	@Override
	public int getCount() {
		boolean b = SafeSharedpreference.getBoolean(context,
				ConstConfig.ISSHOW, true);
		return b ? residArray.length : residArray.length - 1;
	}

	@Override
	public Object getItem(int position) {
		return residArray[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.main_gv_item, null);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.main_gv_item_image);
			holder.text = (TextView) convertView
					.findViewById(R.id.main_gv_item_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(position == residArray.length - 1){
			String name = SafeSharedpreference.getString(context, ConstConfig.MODULE_NAME, "手机防盗");
			holder.text.setText(name);
		}else{
			holder.text.setText(iconStrings[position]);
		}
		holder.imageView.setImageResource(residArray[position]);
		return convertView;
	}

	private final static class ViewHolder {
		private ImageView imageView;
		private TextView text;
	}

}
