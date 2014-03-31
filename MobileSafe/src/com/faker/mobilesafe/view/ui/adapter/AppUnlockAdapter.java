package com.faker.mobilesafe.view.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.faker.mobilesafe.R;
import com.faker.mobilesafe.bean.AppInfoBean;

import java.util.List;

/**
 * Created by Administrator on 14-3-31.
 */
public class AppUnlockAdapter extends BaseAdapter {

    private List<AppInfoBean> totalApps;
    private LayoutInflater inflater;

    public AppUnlockAdapter(Context context, List<AppInfoBean> totalApps) {
        this.totalApps = totalApps;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return totalApps.size();
    }

    @Override
    public Object getItem(int position) {
        return totalApps.get(position);
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
            convertView = inflater.inflate(R.layout.app_unlock_item, null);
            holder.appIcon = (ImageView) convertView.findViewById(R.id.appicon);
            holder.appChecked = (ImageView) convertView.findViewById(R.id.unlock_check);
            holder.appName = (TextView) convertView.findViewById(R.id.appname);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AppInfoBean bean = totalApps.get(position);
        holder.appIcon.setImageBitmap(bean.getAppIcon());
        holder.appName.setText(bean.getAppName());
        if(bean.isChecked()){
            holder.appChecked.setImageResource(R.drawable.checkbox_checked);
        }else{
            holder.appChecked.setImageResource(R.drawable.checkbox_unchecked);
        }
        return convertView;
    }

    public void update(List<AppInfoBean> totalApps) {
        this.totalApps = totalApps;
        notifyDataSetChanged();
    }

    private final class ViewHolder {
        private ImageView appIcon;
        private TextView appName;
        private ImageView appChecked;
    }
}
