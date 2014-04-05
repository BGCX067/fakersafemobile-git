package com.faker.mobilesafe.view.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.Toast;
import com.faker.mobilesafe.R;
import com.faker.mobilesafe.deal.ConstConfig;
import com.faker.mobilesafe.deal.SafeSharedpreference;
import com.faker.mobilesafe.service.BackupService;
import com.faker.mobilesafe.service.SmsRecaverService;

public class HighToolsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hightools);
    }

    public void onBack(View v) {
        onBackPressed();
        finish();
    }

    /**
     * 添加IP
     *
     * @param v
     */
    public void addIp(View v) {
        Intent intent = new Intent(this, AddIPActivity.class);
        startActivity(intent);
    }

    /**
     * 号码归属地查询
     *
     * @param v
     */
    public void phone_query(View v) {
        Intent intent = new Intent(this, PhoneQueryActivity.class);
        startActivity(intent);
    }

    /**
     * 常用号码
     *
     * @param v
     */
    public void common_number(View v) {
        Intent intent = new Intent(this, CommonNumberActivity.class);
        startActivity(intent);
    }

    /**
     * 备份短信，将信息写入一个xml文件保存在SDcard
     *
     * @param v
     */
    public void msgBackup(View v) {
        // 备份短信,开启一个服务进行后台备份
        Intent intent = new Intent(this, BackupService.class);
        startService(intent);
        Toast.makeText(this, "正在备份短信...", Toast.LENGTH_LONG).show();
    }

    /**
     * 还原短信
     *
     * @param v
     */
    public void smsRecaver(View v) {
        Intent intent = new Intent(this, SmsRecaverService.class);
        startService(intent);
        Toast.makeText(this, "正在恢复短信...", Toast.LENGTH_LONG).show();
    }

    /**
     * 程序锁
     *
     * @param v
     */
    public void lock_pattern(View v) {
        boolean is_lock = SafeSharedpreference.getBoolean(this, ConstConfig.LOCK_PATTERN, false);
        if (!is_lock) {
            Intent intent = new Intent(this, GuideGesturePasswordActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, UnlockGesturePasswordActivity.class);
            startActivity(intent);
        }
    }
}
