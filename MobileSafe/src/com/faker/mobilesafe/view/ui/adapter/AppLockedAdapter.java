package com.faker.mobilesafe.view.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.faker.mobilesafe.MobilesafeApplication;
import com.faker.mobilesafe.R;
import com.faker.mobilesafe.bean.AppInfoBean;
import com.faker.mobilesafe.dao.AppLockDao;

import java.util.List;

public class AppLockedAdapter extends BaseAdapter {

    private List<AppInfoBean> lockedAppInfos;
    private LayoutInflater inflater;
    private AppLockDao dao;

    public AppLockedAdapter(Context context, List<AppInfoBean> lockedAppInfos) {
        this.lockedAppInfos = lockedAppInfos;
        inflater = LayoutInflater.from(context);
        dao = MobilesafeApplication.getInstance(context).getApplockDao();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return lockedAppInfos.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return lockedAppInfos.get(position);
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
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.app_locked_item, null);
            holder.appIcon = (ImageView) convertView.findViewById(R.id.appicon);
            holder.appName = (TextView) convertView.findViewById(R.id.appname);
            holder.delete = (Button) convertView.findViewById(R.id.delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final AppInfoBean bean = lockedAppInfos.get(position);
        holder.appIcon.setImageBitmap(bean.getAppIcon());
        holder.appName.setText(bean.getAppName());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dao.delete(bean.getPackageName());
                lockedAppInfos.remove(bean);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    public void update(List<AppInfoBean> lockedAppInfos) {
        this.lockedAppInfos = lockedAppInfos;
        notifyDataSetChanged();
    }

    private final class ViewHolder {
        private ImageView appIcon;
        private TextView appName;
        private Button delete;
    }

}
