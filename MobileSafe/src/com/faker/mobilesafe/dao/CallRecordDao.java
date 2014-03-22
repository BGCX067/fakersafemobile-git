package com.faker.mobilesafe.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.faker.mobilesafe.bean.CallRecordBean;
import com.faker.mobilesafe.db.CallRecordDBHelper;

public class CallRecordDao {

	private CallRecordDBHelper mOpenHelper = null;
	private final String table = "callrecord";

	public CallRecordDao(Context context) {
		mOpenHelper = CallRecordDBHelper.getInstance(context);
	}

	/**
	 * 添加一条拦截记录
	 * 
	 * @param number
	 *            电话号码
	 * @param name
	 *            联系人名称
	 * @param type
	 *            电话类型，未接来电或来电一声响
	 * @param time
	 *            来电时间
	 * @return
	 */
	public boolean addRecord(String number, String address, String type,
			long time) {
		long flag = 0;
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		if (db.isOpen()) {
			ContentValues values = new ContentValues();
			values.put("number", number);
			values.put("address", address);
			values.put("type", type);
			values.put("time", time);
			flag = db.insert(table, "_id", values);
		}
		db.close();
		return flag == -1 ? false : true;
	}

	/**
	 * 删除黑名单号码
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
	public List<CallRecordBean> findAll() {
		List<CallRecordBean> records = new ArrayList<CallRecordBean>();
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor c = db.query(table, null, null, null, null, null, null);
			while (c.moveToNext()) {
				CallRecordBean bean = new CallRecordBean();
				bean.setId(c.getString(0));
				bean.setNumber(c.getString(1));
				bean.setAddress(c.getString(2));
				bean.setType(c.getString(3));
				bean.setTime(c.getString(4));
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
