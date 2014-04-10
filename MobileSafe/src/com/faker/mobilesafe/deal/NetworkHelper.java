package com.faker.mobilesafe.deal;

import android.content.ComponentName;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
import com.faker.mobilesafe.receiver.ConnectivityChangeReceiver;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.net.ConnectivityManagerCompat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class NetworkHelper {

    private Context context;
    // 当前连接网络的类型
    private int nNettype;

    public NetworkHelper(Context context) {
        nNettype = -1;
        this.context = context;
    }

    /**
     * 获得当前连接网络的类型
     *
     * @return
     */
    public int getNettype() {
        return nNettype;
    }

    /**
     * 判断是否有网络连接
     *
     * @return
     */
    public boolean isNetworkConnected() {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            nNettype = mNetworkInfo.getType();
            return mNetworkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 调用系统网络设置界面，进行网络设置
     */
    public void setNetwork() {
        Intent intent = null;
        //判断手机系统的版本  即API大于10 就是3.0或以上版本
        if (android.os.Build.VERSION.SDK_INT > 10) {
            intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
        } else {
            intent = new Intent();
            ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
            intent.setComponent(component);
            intent.setAction("android.intent.action.VIEW");
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public boolean isMobileConnected() {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public void setMobileDataEnabled(boolean enabled) throws Exception{
        final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Class conmanClass;
        conmanClass = Class.forName(conman.getClass().getName());
        final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
        iConnectivityManagerField.setAccessible(true);
        final Object iConnectivityManager = iConnectivityManagerField.get(conman);
        final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
        final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
        setMobileDataEnabledMethod.setAccessible(true);

        setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
    }

    /**
     * 对大数据传输时，需要调用该方法做出判断，如果流量敏感，应该提示用户
     *
     * @return true表示流量敏感，false表示不敏感
     */
    public boolean isActiveNetworkMetered() {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return ConnectivityManagerCompat.isActiveNetworkMetered(cm);
    }

    /**
     * 注册监听网络状态的广播接收者
     *
     * @param receiver
     * @return
     */
    public Intent registerReceiver(ConnectivityChangeReceiver receiver) {
        return context.registerReceiver(receiver,
                ConnectivityChangeReceiver.FILTER);
    }

    /**
     * 取消注册监听网络状态的广播接收者
     *
     * @param receiver
     */
    public void unregisterReceiver(ConnectivityChangeReceiver receiver) {
        context.unregisterReceiver(receiver);
    }
}
