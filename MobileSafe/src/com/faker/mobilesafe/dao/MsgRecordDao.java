package com.faker.mobilesafe.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.faker.mobilesafe.bean.MsgRecordBean;
import com.faker.mobilesafe.db.MsgRecordDBHelper;

public class MsgRecordDao {

	private MsgRecordDBHelper mOpenHelper = null;
	private final String table = "msgrecord";

	public MsgRecordDao(Context context) {
		mOpenHelper = MsgRecordDBHelper.getInstance(context);
	}

	/**
	 * 添加一条短信拦截记录
	 * 
	 * @param number
	 *            电话号码
	 * @param content
	 *            短信内容
	 * @param time
	 *            接收时间
	 * @return
	 */
	public boolean addRecord(String number, String content, String time) {
		long flag = 0;
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		if (db.isOpen()) {
			ContentValues values = new ContentValues();
			values.put("number", number);
			values.put("content", content);
			values.put("time", time);
			flag = db.insert(table, "_id", values);
		}
		db.close();
		return flag == -1 ? false : true;
	}

	/**
	 * 删除一条指定的记录
	 * 
	 * @param number
	 */
	public void delete(String id) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.delete(table, "_id = ?", new String[] { id });
			db.close();
		}
	}

	/**
	 * 全部清除
	 */
	public void deleteAll() {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.execSQL("delete from " + table);
			db.close();
		}
	}

	/**
	 * 查询所有的电话拦截记录并返回
	 * 
	 * @return
	 */
	public List<MsgRecordBean> findAll() {
		List<MsgRecordBean> records = new ArrayList<MsgRecordBean>();
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor c = db.query(table, null, null, null, null, null, null);
			while (c.moveToNext()) {
				MsgRecordBean bean = new MsgRecordBean();
				bean.setNumber(c.getString(1));
				bean.setContent(c.getString(2));
				bean.setTime(c.getString(3));
				records.add(bean);
			}
			c.close();
			db.close();
		}
		return records;
	}

	/**
	 * 返回记录数量
	 * 
	 * @return
	 */
	public String getCount() {
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		String count = "0";
		if (db.isOpen()) {
			Cursor c = db.rawQuery("select count(*) from " + table, null);
			if (c.moveToFirst()) {
				count = c.getString(0);
			}
			c.close();
			db.close();
		}
		return count;
	}
}
