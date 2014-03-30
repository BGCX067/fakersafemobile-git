package com.faker.mobilesafe.view.activitys;

import android.app.ListActivity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.TextView;
import com.faker.mobilesafe.R;
import com.faker.mobilesafe.bean.AppInfoBean;
import com.faker.mobilesafe.dao.AppLockDao;
import com.faker.mobilesafe.util.ImageUtil;
import com.faker.mobilesafe.view.ui.adapter.AppLockedAdapter;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class AppLockActivity extends ListActivity {

    private Button addButton;
    private TextView emptyText;

    private AppLockedAdapter adapter;

    private AppLockDao dao;

    private PackageManager pm;

    private List<AppInfoBean> lockedAppInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_locked);
        // INIT UI
        addButton = (Button) findViewById(R.id.add_apptolock);
        emptyText = (TextView) findViewById(R.id.empty_text);
        // INIT DATA
        dao = new AppLockDao(this);
        pm = getPackageManager();
        lockedAppInfos = getLockedAppInfo();
        adapter = new AppLockedAdapter(this,lockedAppInfos);
        setListAdapter(adapter);

        emptyText.setText("添加之后，锁住软件里的小秘密");

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void onBack(View v) {
        onBackPressed();
        finish();
    }

    private List<AppInfoBean> getLockedAppInfo() {
        List<String> lockedPackageInfo = dao.findAll();
        List<AppInfoBean> appInfoBeanList = new ArrayList<AppInfoBean>();
        for (String packageName : lockedPackageInfo) {
            try {
                AppInfoBean bean = new AppInfoBean();
                bean.setPackageName(packageName);
                Drawable drawable = pm.getApplicationIcon(packageName);
                Bitmap appIcon = ((BitmapDrawable) drawable).getBitmap();
                bean.setAppIcon(ImageUtil.zoomImg(appIcon, 50, 50));
                ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName, 0);
                String appName = applicationInfo.loadLabel(pm).toString();
                bean.setAppName(appName);
                appInfoBeanList.add(bean);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return appInfoBeanList;
    }
}
