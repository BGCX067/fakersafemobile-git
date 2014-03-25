package com.faker.mobilesafe.deal;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.faker.mobilesafe.util.FileUtil;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CommonNumberService {

	private Context context;
	private final static String fileName = "commonnum.db";

	public CommonNumberService(Context context) {
		this.context = context;
	}

	/**
	 * 判断数据库文件是否存在,不存在则初始化
	 * 
	 * @return
	 * @throws Exception
	 */
	public void initDB() throws Exception {
		File file = new File(context.getFilesDir(), fileName);
		if (!file.exists()) {
			InputStream is = context.getAssets().open(fileName);
			FileUtil.moveFile(is, context.getFilesDir(), fileName);
		}
	}

	public List<Map<String, String>> getGroundData() {
		List<Map<String, String>> groundData = new ArrayList<Map<String, String>>();
		File dbFile = new File(context.getFilesDir(), fileName);
		SQLiteDatabase db = SQLiteDatabase.openDatabase(
				dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
		if (db.isOpen()) {
			Cursor c = db.query("classlist", new String[] { "name", "idx" },
					null, null, null, null, null);
			while (c.moveToNext()) {
				Map<String, String> map = new HashMap<String, String>();
				String name = c.getString(c.getColumnIndex("name"));
				map.put("name", name);
				String idx = c.getString(c.getColumnIndex("idx"));
				map.put("idx", idx);
				groundData.add(map);
			}
			c.close();
			db.close();
		}
		return groundData;
	}

	public List<List<Map<String, String>>> getChildData() {
		List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
		List<Map<String, String>> parent = getGroundData();
		File dbFile = new File(context.getFilesDir(), fileName);
		SQLiteDatabase db1 = SQLiteDatabase.openDatabase(
				dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
		if (db1.isOpen()) {
			for (int i = 0; i < parent.size(); i++) {
				List<Map<String, String>> list = new ArrayList<Map<String, String>>();
				String idx = parent.get(i).get("idx");
				Cursor cursor = db1.query("table" + idx, new String[] { "_id",
						"number", "name" }, null, null, null, null, null);
				Log.i("lichfaker", "i:"+i);
				while (cursor.moveToNext()) {
					Map<String, String> map = new HashMap<String, String>();
					String name = cursor.getString(cursor.getColumnIndex("name"));
					String number = cursor.getString(cursor.getColumnIndex("number"));
					map.put("name", name);
					map.put("number", number);
					list.add(map);
				}
				cursor.close();
				childData.add(list);
			}
			db1.close();
		}
		return childData;
	}
}
