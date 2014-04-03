package com.faker.mobilesafe.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.*;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import com.faker.mobilesafe.MobilesafeApplication;
import com.faker.mobilesafe.dao.AppLockDao;
import com.faker.mobilesafe.view.activitys.EnterPasswordActivity;

import java.util.List;

/**
 * Created by Administrator on 14-4-1.
 */
public class ApplockService extends Service {

    private ActivityManager activityManager;
    private AppLockDao dao;
    private List<String> lockedAppList;
    private String tempLockapp;
    private MyContentObserver observer;
    private MobilesafeApplication application;
    //手机锁屏和解锁知道的服务
//    private KeyguardManager keyguardManager;
    private PowerManager powerManager;
    private LockScreenReceiver lockScreenReceiver;
    private ScreenonReceiver screenOnReceiver;

    @Override
    public void onCreate() {
        super.onCreate();

        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//        keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        application = MobilesafeApplication.getInstance(getApplicationContext());
        dao = application.getApplockDao();
        lockedAppList = dao.findAll();
        //注册一个内容观察者
        observer = new MyContentObserver();
        dao.addObserver(observer);
        //注册锁屏广播接收者
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        lockScreenReceiver = new LockScreenReceiver();
        registerReceiver(lockScreenReceiver, filter);
        // 注册屏幕解锁广播接收者
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction(Intent.ACTION_SCREEN_ON);
        screenOnReceiver = new ScreenonReceiver();
        registerReceiver(screenOnReceiver,filter1);
        new Thread() {
            @Override
            public void run() {
                while (true) {
//                    boolean isLockScreen = keyguardManager.inKeyguardRestrictedInputMode();
                    boolean isLockScreen = powerManager.isScreenOn();
                    if(!isLockScreen){
                        application.Lock();
                    }
                    List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
                    ActivityManager.RunningTaskInfo runningTaskInfo = runningTaskInfos.get(0);
                    ComponentName componentName = runningTaskInfo.topActivity;
                    String packageName = componentName.getPackageName();
                    if (lockedAppList.contains(packageName)) {
//                        Log.i("lickfaker","temp:"+tempLockapp);
                        if (!packageName.equals(tempLockapp)) {
                            application.setPackageName(packageName);
                            Intent intent = new Intent(getApplicationContext(), EnterPasswordActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                    SystemClock.sleep(500);
                }
            }
        }.start();
    }

    private MyBinder ibinder = new MyBinder();

    public IBinder onBind(Intent intent) {
        return ibinder;
    }

    private final class MyContentObserver implements com.faker.mobilesafe.inter.IService {

        @Override
        public void update() {
            lockedAppList = dao.findAll();
            tempLockapp = "";
        }
    }

    public final class MyBinder extends Binder {

        public void setTemp(String packageName) {
            tempLockapp = packageName;
        }
    }

    private final class LockScreenReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            tempLockapp = "";
        }
    }

    private final class ScreenonReceiver extends  BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            application.unLock();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dao.delObserver(observer);
        //取消锁屏的广播监听
        unregisterReceiver(lockScreenReceiver);
        // 取消屏幕解锁的广播监听
        unregisterReceiver(screenOnReceiver);
    }
}
