package com.faker.mobilesafe.view.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.faker.mobilesafe.bean.AppInfoBean;
import com.faker.mobilesafe.view.activitys.AppLockActivity;

import java.util.List;

public class AppLockedAdapter extends BaseAdapter {

    private List<AppInfoBean> lockedAppInfos;
    private LayoutInflater inflater;

    public AppLockedAdapter(Context context, List<AppInfoBean> lockedAppInfos) {
        this.lockedAppInfos = lockedAppInfos;
        inflater = LayoutInflater.from(context);
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
		return null;
	}

}
