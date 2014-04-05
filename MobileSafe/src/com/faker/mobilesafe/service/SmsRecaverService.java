package com.faker.mobilesafe.service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;
import com.faker.mobilesafe.bean.SmsBean;
import com.faker.mobilesafe.deal.SmsBackupService;

import java.util.List;

/**
 * User: LichFaker
 * Date: 14-4-4
 * Time: 下午7:09
 * Email: lichfaker@gmail.com
 */
public class SmsRecaverService extends Service {

    private SmsBackupService service;

    @Override
    public void onCreate() {
        super.onCreate();
        service = new SmsBackupService(getApplicationContext());

        new Thread() {
            @Override
            public void run() {
                try {
                    List<SmsBean> smsBeans = service.getSmsFromBackupXml();
                    Uri uri = Uri.parse("content://sms");
                    // 删除当前短信
                    getContentResolver().delete(uri, null, null);
                    for (SmsBean bean : smsBeans) {
                        ContentValues values = new ContentValues();
                        values.put("address", bean.getAddress());
                        values.put("date", bean.getDate());
                        values.put("type", bean.getType());
                        values.put("body", bean.getBody());
                        getContentResolver().insert(uri, values);
                    }
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "短信恢复完成", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } catch (Exception e) {
                    e.printStackTrace();
                    Looper.prepare();
                    Toast.makeText(getApplicationContext(), "短信恢复失败", Toast.LENGTH_SHORT).show();
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
