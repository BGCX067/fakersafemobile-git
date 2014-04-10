package com.faker.mobilesafe.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.faker.mobilesafe.bean.TrafficBean;
import com.faker.mobilesafe.db.TrafficDBHelper;

/**
 * User:LichFaker
 * Date:14-4-9
 * Time:下午6:49
 * Email:lichfaker@gmail.com
 */
public class TrafficDao {
    private TrafficDBHelper mOpenHelper = null;
    private final String table = "traffic";

    public TrafficDao(Context context) {
        mOpenHelper = TrafficDBHelper.getInstance(context);
    }

    public void insert(long mobiletx, long mobilerx, long wifitx, long wifirx) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put("mobiletx", mobiletx);
            values.put("mobilerx", mobilerx);
            values.put("wifitx", wifitx);
            values.put("wifirx", wifirx);
            values.put("offset", 0);
            db.insert(table, "_id", values);
            db.close();
        }
    }

    public TrafficBean findAll() {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        TrafficBean bean = null;
        if (db.isOpen()) {
            Cursor c = db.query(table, null, null, null, null, null, null);
            if (c.moveToFirst()) {
                String mobileTx = c.getString(c.getColumnIndex("mobiletx"));
                String mobileRx = c.getString(c.getColumnIndex("mobilerx"));
                String wifiTx = c.getString(c.getColumnIndex("wifitx"));
                String wifiRx = c.getString(c.getColumnIndex("wifirx"));
                String offset = c.getString(c.getColumnIndex("offset"));
                bean = new TrafficBean(Long.valueOf(mobileTx), Long.valueOf(mobileRx),
                        Long.valueOf(wifiTx), Long.valueOf(wifiRx), Long.valueOf(offset));
            }
            c.close();
            db.close();
        }
        return bean;
    }

    public void update(TrafficBean bean) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put("mobiletx", bean.getMobileTx());
            values.put("mobilerx", bean.getMobileRx());
            values.put("wifitx", bean.getWifiTx());
            values.put("wifirx", bean.getWifiRx());
            values.put("offset", bean.getOffset());
            db.update(table, values, null, null);
            db.close();
        }
    }
}
