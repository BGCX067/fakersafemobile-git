package com.faker.mobilesafe.dao;

import java.io.File;

import com.faker.mobilesafe.deal.ConstConfig;
import com.faker.mobilesafe.deal.SafeSharedpreference;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

public class PhoneAddressQueryDao {

	private final String dirPath = Environment.getExternalStorageDirectory()
			.getPath() + "/MobileSafe/db";

	public PhoneAddressQueryDao() {

	}

	/**
	 * 判断归属地数据库是否存在
	 * 
	 * @return
	 */
	public boolean isExist(Context context) {
		try {
			File file = new File(dirPath, "address.db");
			boolean b = SafeSharedpreference.getBoolean(context,
					ConstConfig.ISINITDB, false);
			return file.exists() && b;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 通过传入的电话号码返回该号码归属地信息
	 * 
	 * @param number
	 * @return
	 */
	public String queryAddress(String number) {
		String result = null;
		try {
			// 获得数据库对象
			File dbFile = new File(getDirpath(), "address.db");
			SQLiteDatabase db = SQLiteDatabase.openDatabase(
					dbFile.getAbsolutePath(), null,
					SQLiteDatabase.OPEN_READONLY);
			if (db.isOpen()) {
				if (number.length() == 14) {
					number = number.substring(3, 14);
				}
				Log.i("lichfaker", number);
				boolean isPhone = number.matches("^1[358]\\d{9}$");
				if (isPhone) {
					// 查询的是手机号码,号码前七位表示归属地
					String pre_number = number.substring(0, 7);
					Cursor cursor = db.query("info",
							new String[] { "cardtype" }, "mobileprefix = ?",
							new String[] { pre_number }, null, null, null);
					if (cursor.moveToFirst()) {
						result = cursor.getString(0);
					} else {
						result = "未知号码";
					}
					cursor.close();
				} else {
					// 查询的是电话号码
					int count = number.length();
					String area = null;
					switch (count) {
					case 10: // 三位区号加上7位号码
						area = number.substring(0, 3);
						Cursor cursor = db.query("info",
								new String[] { "city" }, "area = ?",
								new String[] { area }, null, null, null);
						if (cursor.moveToFirst()) {
							result = cursor.getString(0);
						} else {
							result = "未知号码";
						}
						cursor.close();
						break;
					case 11: // 3+8 或者 4+7
						area = number.substring(0, 3);
						Cursor cursor2 = db.query("info",
								new String[] { "city" }, "area = ?",
								new String[] { area }, null, null, null);
						if (cursor2.moveToFirst()) {
							result = cursor2.getString(0);
						} else {
							area = number.substring(0, 4);
							Cursor cursor3 = db.query("info",
									new String[] { "city" }, "area = ?",
									new String[] { area }, null, null, null);
							if (cursor3.moveToFirst()) {
								result = cursor3.getString(0);
							} else {
								result = "未知号码";
							}
							cursor3.close();
						}
						cursor2.close();
						break;
					case 12: // 4+8
						area = number.substring(0, 4);
						Cursor cursor4 = db.query("info",
								new String[] { "city" }, "area = ?",
								new String[] { area }, null, null, null);
						if (cursor4.moveToFirst()) {
							result = cursor4.getString(0);
						} else {
							result = "未知号码";
						}
						cursor4.close();
						break;
					default:
						result = "未知号码";
						break;
					}
				}
				db.close();
			}
			return result;
		} catch (Exception e) {
			return "未知号码";
		}

	}

	public String getDirpath() {
		return dirPath;
	}
}
