package com.faker.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;
import com.faker.mobilesafe.bean.SmsBean;
import com.faker.mobilesafe.deal.SmsBackupService;

import java.util.List;

/**
 * User: LichFaker
 * Date: 14-4-4
 * Time: 下午6:48
 * Email: lichfaker@gmail.com
 */
public class BackupService extends Service {

    private SmsBackupService service;

    @Override
    public void onCreate() {
        super.onCreate();
        service = new SmsBackupService(getApplicationContext());
        new Thread() {
            @Override
            public void run() {
                try {
                    List<SmsBean> smsBeans = service.findAll();
                    service.createBackupXml(smsBeans);
                    //looper是一个消息泵,从消息队列（MessageQueue）里面抽取消息,把消息交给Handler处理
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "短信备份完成", 0).show();
                    Looper.loop();
                } catch (Exception e) {
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "短信备份失败", 0).show();
                    Looper.loop();
                }
                stopSelf();
            }
        }.start();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }
}
