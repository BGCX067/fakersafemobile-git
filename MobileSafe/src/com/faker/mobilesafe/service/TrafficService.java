package com.faker.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.net.TrafficStats;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;
import com.faker.mobilesafe.bean.TrafficBean;
import com.faker.mobilesafe.dao.TrafficDao;
import com.faker.mobilesafe.deal.ConstConfig;
import com.faker.mobilesafe.deal.NetworkHelper;
import com.faker.mobilesafe.deal.SafeSharedpreference;
import com.faker.mobilesafe.util.FormatUtil;

import java.util.Calendar;

/**
 * User:LichFaker
 * Date:14-4-10
 * Time:下午5:46
 * Email:lichfaker@gmail.com
 */
public class TrafficService extends Service {

    private TrafficDao dao;
    private TrafficBean bean;

    private String time;
    private long total;
    private NetworkHelper networkHelper;
    @Override
    public void onCreate() {
        super.onCreate();
        dao = new TrafficDao(getApplicationContext());
        bean = dao.findAll();

        total = Long.valueOf(SafeSharedpreference.getString(getApplicationContext(),
                ConstConfig.TRAFFIC_MOBILE_TOTAL, "0")) * 1024 * 1024;

        time = calcFirstMouthDay();

        networkHelper = new NetworkHelper(getApplicationContext());
//        boolean b1 = networkHelper.isMobileConnected();
//        Log.i("lichfaker", "pre:" + b1);
//        try {
//            networkHelper.setMobileDataEnabled(false);
//        } catch (Exception e) {
//            networkHelper.setNetwork();
//        }
//        boolean b2 = networkHelper.isMobileConnected();
//        Log.i("lichfaker", "next:" + b2);
        new Thread(){
            @Override
            public void run() {
                while(true){
                    /** 获取手机通过 2G/3G 发出的字节流量总数 */
                    long currMobileTx = TrafficStats.getMobileTxBytes();
                    /** 获取手机通过 2G/3G 接收的字节流量总数 */
                    long currMobileRx = TrafficStats.getMobileRxBytes();

                    String current = FormatUtil.formatDate(System.currentTimeMillis());
                    if(time.equals(current)){
                        time = calcFirstMouthDay();
                        bean.setMobileTx(currMobileTx);
                        bean.setMobileRx(currMobileRx);
                        bean.setOffset(0);
                        dao.update(bean);
                    }

                    long dx = currMobileRx + currMobileTx - bean.getMobileTx() -
                            bean.getMobileRx() + bean.getOffset();
                    if(dx >= total){
                        // 使用量达到最大限度
                        if(networkHelper.isMobileConnected()){
                            try {
                                networkHelper.setMobileDataEnabled(false);
                            } catch (Exception e) {
                                Looper.prepare();
                                Toast.makeText(getApplicationContext(),"您当前使用流量超标",
                                        Toast.LENGTH_SHORT).show();
                                SystemClock.sleep(1000);
                                networkHelper.setNetwork();
                            }
                        }
                    }
                    SystemClock.sleep(3000);
                }
            }
        }.start();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    private String calcFirstMouthDay() {
        //获取下个月第一天：
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 1);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return FormatUtil.formatDate(c.getTimeInMillis());
    }
}
