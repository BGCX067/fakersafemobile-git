package com.faker.mobilesafe.view.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.*;
import com.faker.mobilesafe.MobilesafeApplication;
import com.faker.mobilesafe.R;
import com.faker.mobilesafe.bean.AppInfoBean;
import com.faker.mobilesafe.dao.AppLockDao;
import com.faker.mobilesafe.deal.AppInfoService;
import com.faker.mobilesafe.view.ui.adapter.AppUnlockAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 14-3-31.
 */
public class AppUnlockActivity extends Activity implements AdapterView.OnItemClickListener {
    private static final int FINISH = 0;

    private ListView lv_unlock_app;
    private Button addlockapp;
    private RelativeLayout rlloading;

    private AppInfoService appInfoService;
    private AppLockDao dao;

    private List<AppInfoBean> totalApps;
    private List<String> lockedApps;

    private AppUnlockAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_unlock);
        initialize();
        new Thread(new Runnable() {
            @Override
            public void run() {
                totalApps = appInfoService.getUnlockAppInfo();
                Message msg = new Message();
                msg.what = FINISH;
                handler.sendMessage(msg);
            }
        }).start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FINISH:
                    rlloading.setVisibility(View.GONE);
                    adapter = new AppUnlockAdapter(getApplicationContext(), totalApps);
                    lv_unlock_app.setAdapter(adapter);
                    lv_unlock_app.setOnItemClickListener(AppUnlockActivity.this);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public void onBack(View v) {
        onBackPressed();
        finish();
    }

    private void initialize() {

        lv_unlock_app = (ListView) findViewById(R.id.lv_unlock_app);
        addlockapp = (Button) findViewById(R.id.add_lock_app);
        rlloading = (RelativeLayout) findViewById(R.id.rl_loading);

        appInfoService = new AppInfoService(this);
        dao = MobilesafeApplication.getInstance(this).getApplockDao();
        lockedApps = new ArrayList<String>();

        addlockapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dao.add(lockedApps);
                for (int i = totalApps.size() - 1; i >= 0; i--) {
                    AppInfoBean bean = totalApps.get(i);
                    if (dao.isLocked(bean.getPackageName())) {
                        totalApps.remove(i);
                    }
                }
                lockedApps.clear();
                adapter.update(totalApps);
                addlockapp.setEnabled(false);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AppInfoBean bean = (AppInfoBean) adapter.getItem(position);
        String packageName = bean.getPackageName();
        ImageView checkBox = (ImageView) view.findViewById(R.id.unlock_check);
        if (lockedApps.contains(packageName)) {
            checkBox.setImageResource(R.drawable.checkbox_unchecked);
            bean.setChecked(false);
            lockedApps.remove(packageName);
        } else {
            checkBox.setImageResource(R.drawable.checkbox_checked);
            bean.setChecked(true);
            lockedApps.add(packageName);
        }
        if (lockedApps.size() > 0) {
            addlockapp.setEnabled(true);
        } else {
            addlockapp.setEnabled(false);
        }
    }
}