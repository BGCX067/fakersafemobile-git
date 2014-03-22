package com.faker.mobilesafe.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.faker.mobilesafe.db.BlackNumberDBHelper;

public class BlackNumberDao {

	private BlackNumberDBHelper mOpenHelper = null;
	private final String table = "blacknumber";

	public BlackNumberDao(Context context) {
		mOpenHelper = BlackNumberDBHelper.getInstance(context);
	}

	/**
	 * 判断number是不是在黑名单中
	 * 
	 * @param number
	 * @return
	 */
	public boolean isBlackNumber(String number) {
		boolean isExit = false;
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		if (number.length() == 14) {
			number = number.substring(3, 14);
		}
		if (db.isOpen()) {
			Cursor c = db.query(table, null, "number = ?",
					new String[] { number }, null, null, null);
			if (c.moveToFirst()) {
				isExit = true;
			}
			c.close();
			db.close();
		}
		return isExit;
	}

	/**
	 * 将number添加进黑名单
	 * 
	 * @param number
	 */
	public boolean addBalckNumber(String number) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		long flag = 0;
		if (db.isOpen()) {
			Cursor c = db.query(table, null, "number = ?",
					new String[] { number }, null, null, null);
			if (!c.moveToFirst()) {
				ContentValues values = new ContentValues();
				values.put("number", number);
				flag = db.insert(table, "_id", values);
			}
			c.close();
			db.close();
		}
		return flag == -1 ? false : true;
	}

	/**
	 * 事务添加一组黑名单号码
	 * 
	 * @param numbers
	 * @return
	 */
	public boolean addBlackNumbers(List<String> numbers) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		boolean flag = false;
		if (db.isOpen()) {
			db.beginTransaction();
			try {
				for (int i = 0; i < numbers.size(); i++) {
					String number = numbers.get(i);
					if (number.length() == 14) {
						number = number.substring(3, 14);
					}
					Cursor c = db.query(table, null, "number = ?",
							new String[] { number }, null, null, null);
					if (!c.moveToFirst()) {
						db.execSQL("insert into " + table
								+ " ('number') values (" + number + ")");
					}
					c.close();
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

	/**
	 * 根据id更新黑名单号码
	 * 
	 * @param id
	 * @param number
	 * @return
	 */
	public boolean update(int id, String number) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		boolean flag = false;
		if (db.isOpen()) {
			ContentValues values = new ContentValues();
			values.put("number", number);
			db.update(table, values, "_id = ?", new String[] { id + "" });
			flag = true;
		}
		return flag;
	}

	/**
	 * 删除黑名单号码
	 * 
	 * @param number
	 */
	public void delete(String number) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.delete(table, "number = ?", new String[] { number });
			db.close();
		}
	}

	public void deleteAll() {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.execSQL("delete from " + table);
			db.close();
		}
	}

	/**
	 * 查询所有的黑名单号码并返回
	 * 
	 * @return
	 */
	public List<String> findAll() {
		List<String> blackNumbers = new ArrayList<String>();
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor c = db.query(table, new String[] { "number" }, null, null,
					null, null, null);
			while (c.moveToNext()) {
				blackNumbers.add(c.getString(0));
			}
			c.close();
			db.close();
		}
		return blackNumbers;
	}

	/**
	 * 返回黑名单数量
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
