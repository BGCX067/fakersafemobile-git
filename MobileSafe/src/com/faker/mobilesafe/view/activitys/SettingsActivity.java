package com.faker.mobilesafe.view.activitys;

import com.faker.mobilesafe.R;
import com.faker.mobilesafe.deal.ConstConfig;
import com.faker.mobilesafe.deal.SafeSharedpreference;
import com.faker.mobilesafe.service.ApplockService;
import com.faker.mobilesafe.service.BlackNumberService;
import com.faker.mobilesafe.service.ShowAddressService;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingsActivity extends BaseActivity implements OnClickListener {

    private ImageView iv_update_switch;
    private TextView tv_update_text;

    private ImageView iv_auto_ip;
    private TextView tv_auto_ip;

    private ImageView iv_address;
    private TextView tv_address;

    private ImageView iv_intecpter;
    private TextView tv_intecpter;

    private ImageView iv_applock;
    private TextView tv_applock;

    private boolean isUpdate;
    private boolean isAutoip;
    private boolean isAdress;
    private boolean isIntecpter;
    private boolean isApplock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initData();
        initUI();
    }

    private void initData() {
        isUpdate = SafeSharedpreference.getBoolean(this, ConstConfig.ISUPDATE,
                true);
        isAutoip = SafeSharedpreference.getBoolean(this, ConstConfig.ISAUTOIP,
                false);
        isAdress = SafeSharedpreference.getBoolean(this,
                ConstConfig.ISOPENADRESS, true);
        isIntecpter = SafeSharedpreference.getBoolean(this,
                ConstConfig.ISINTECPTER, true);
        isApplock = SafeSharedpreference.getBoolean(this,
                ConstConfig.ISAPPLOCK, true);
    }

    private void initUI() {
        // 自动更新
        iv_update_switch = getView(R.id.settings_update_switch);
        tv_update_text = getView(R.id.setting_update_text);
        if (isUpdate) {
            iv_update_switch.setImageResource(R.drawable.switch_on_normal);
            tv_update_text.setText(R.string.settings_state_on);
        } else {
            iv_update_switch.setImageResource(R.drawable.switch_off_normal);
            tv_update_text.setText(R.string.settings_state_off);
        }
        iv_update_switch.setOnClickListener(this);
        // IP 拨号
        iv_auto_ip = getView(R.id.settings_autoip_switch);
        tv_auto_ip = getView(R.id.setting_autoip_text);
        if (isAutoip) {
            iv_auto_ip.setImageResource(R.drawable.switch_on_normal);
            tv_auto_ip.setText(R.string.settings_state_on);
        } else {
            iv_auto_ip.setImageResource(R.drawable.switch_off_normal);
            tv_auto_ip.setText(R.string.settings_state_off);
        }
        iv_auto_ip.setOnClickListener(this);

        iv_address = getView(R.id.settings_address_switch);
        tv_address = getView(R.id.setting_address_text);
        if (isAdress) {
            iv_address.setImageResource(R.drawable.switch_on_normal);
            tv_address.setText(R.string.settings_state_on);
        } else {
            iv_address.setImageResource(R.drawable.switch_off_normal);
            tv_address.setText(R.string.settings_state_off);
        }
        iv_address.setOnClickListener(this);

        iv_intecpter = getView(R.id.settings_intecpter_switch);
        tv_intecpter = getView(R.id.setting_intecpter_text);
        if (isIntecpter) {
            iv_intecpter.setImageResource(R.drawable.switch_on_normal);
            tv_intecpter.setText(R.string.settings_state_on);
        } else {
            iv_intecpter.setImageResource(R.drawable.switch_off_normal);
            tv_intecpter.setText(R.string.settings_state_off);
        }
        iv_intecpter.setOnClickListener(this);

        iv_applock = getView(R.id.settings_applock_switch);
        tv_applock = getView(R.id.setting_app_lock);
        if (isApplock) {
            iv_applock.setImageResource(R.drawable.switch_on_normal);
            tv_applock.setText(R.string.settings_state_on);
        } else {
            iv_applock.setImageResource(R.drawable.switch_off_normal);
            tv_applock.setText(R.string.settings_state_off);
        }
        iv_applock.setOnClickListener(this);
    }

    public void onBack(View v) {
        onBackPressed();
        finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.settings_update_switch: // 是否开启自动更新检测
                if (isUpdate) {
                    iv_update_switch.setImageResource(R.drawable.switch_off_normal);
                    tv_update_text.setText(R.string.settings_state_off);
                    isUpdate = false;
                } else {
                    iv_update_switch.setImageResource(R.drawable.switch_on_normal);
                    tv_update_text.setText(R.string.settings_state_on);
                    isUpdate = true;
                }
                SafeSharedpreference.save(this, ConstConfig.ISUPDATE, isUpdate);
                break;
            case R.id.settings_autoip_switch: // IP 拨号
                if (isAutoip) {
                    iv_auto_ip.setImageResource(R.drawable.switch_off_normal);
                    tv_auto_ip.setText(R.string.settings_state_off);
                    isAutoip = false;
                } else {
                    iv_auto_ip.setImageResource(R.drawable.switch_on_normal);
                    tv_auto_ip.setText(R.string.settings_state_on);
                    isAutoip = true;
                }
                SafeSharedpreference.save(this, ConstConfig.ISAUTOIP, isAutoip);
                break;
            case R.id.settings_address_switch: // 归属地服务
                if (isAdress) {
                    iv_address.setImageResource(R.drawable.switch_off_normal);
                    tv_address.setText(R.string.settings_state_off);
                    isAdress = false;
                    // 停止归属地服务
                    Intent intent = new Intent(this, ShowAddressService.class);
                    stopService(intent);
                } else {
                    iv_address.setImageResource(R.drawable.switch_on_normal);
                    tv_address.setText(R.string.settings_state_on);
                    isAdress = true;
                    Intent intent = new Intent(this, ShowAddressService.class);
                    startService(intent);
                }
                SafeSharedpreference.save(this, ConstConfig.ISOPENADRESS, isAdress);
                break;
            case R.id.settings_intecpter_switch: // 拦截服务
                if (isIntecpter) {
                    iv_intecpter.setImageResource(R.drawable.switch_off_normal);
                    tv_intecpter.setText(R.string.settings_state_off);
                    isIntecpter = false;
                    Intent intent = new Intent(this, BlackNumberService.class);
                    stopService(intent);
                } else {
                    iv_intecpter.setImageResource(R.drawable.switch_on_normal);
                    tv_intecpter.setText(R.string.settings_state_on);
                    isIntecpter = true;
                    Intent intent = new Intent(this, BlackNumberService.class);
                    startService(intent);
                }
                SafeSharedpreference.save(this, ConstConfig.ISINTECPTER,
                        isIntecpter);
                break;
            case R.id.settings_applock_switch: // 程序锁服务
                if (isApplock) {
                    iv_applock.setImageResource(R.drawable.switch_off_normal);
                    tv_applock.setText(R.string.settings_state_off);
                    isApplock = false;
                    Intent intent = new Intent(this, ApplockService.class);
                    stopService(intent);
                } else {
                    iv_applock.setImageResource(R.drawable.switch_on_normal);
                    tv_applock.setText(R.string.settings_state_on);
                    isApplock = true;
                    Intent intent = new Intent(this, ApplockService.class);
                    startService(intent);
                }
                SafeSharedpreference.save(this, ConstConfig.ISAPPLOCK,
                        isApplock);
                break;
            default:
                break;
        }
    }
}
