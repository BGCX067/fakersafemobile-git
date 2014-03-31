package com.faker.mobilesafe.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.faker.mobilesafe.db.AppLockDBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 14-3-27.
 */
public class AppLockDao {
    private AppLockDBHelper mOpenHelper = null;
    private final static String table = "applock";

    public AppLockDao(Context context) {
        mOpenHelper = AppLockDBHelper.getInstance(context);
    }

    /**
     * 通过包名判断应用是否已经加锁
     *
     * @param packageName
     * @return
     */
    public boolean isLocked(String packageName) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        boolean b = false;
        if (db.isOpen()) {
            Cursor c = db.query(table, null, "packageName = ?", new String[]{packageName}, null, null, null);
            if (c.moveToFirst()) {
                b = true;
            }
            c.close();
            db.close();
        }
        return b;
    }

    /**
     * 反回数据库中所有的信息
     *
     * @return
     */
    public List<String> findAll() {
        List<String> applocks = new ArrayList<String>();
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        if (db.isOpen()) {
            Cursor c = db.query(table, new String[]{"packageName"}, null, null, null, null, null);
            while (c.moveToNext()) {
                String packageName = c.getString(0);
                applocks.add(packageName);
            }
            c.close();
            db.close();
        }
        return applocks;
    }

    /**
     * 根据包名删除对应记录
     *
     * @param packageName
     */
    public void delete(String packageName) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(table, "packageName = ?", new String[]{packageName});
            db.close();
        }
    }

    /**
     * 添加一条记录
     *
     * @param packageName
     */
    public void add(String packageName) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put("packageName", packageName);
            db.insert(table, "_id", values);
            db.close();
        }
    }

    /**
     * 事务添加一组数据
     *
     * @param packages
     * @return
     */
    public boolean add(List<String> packages) {
        boolean flag = false;
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.beginTransaction();
            try {
                for (String packageName : packages) {
                    ContentValues values = new ContentValues();
                    values.put("packageName",packageName);
                    db.insertOrThrow(table,"_id",values);
//                    db.execSQL("insert into " + table
//                            + " ('packageName') values (" + packageName + ")");
                }
                db.setTransactionSuccessful();
                flag = true;
            } finally {
                db.endTransaction();
                db.close();
            }
        }
        return flag;
    }

}
