package com.faker.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class AppLockDBHelper extends SQLiteOpenHelper {

	private final static String dbName = "applock";
	private static AppLockDBHelper mInstance = null;

	public static AppLockDBHelper getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new AppLockDBHelper(context, dbName, null, 1);
		}
		return mInstance;
	}

	private AppLockDBHelper(Context context, String name,
                            CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + dbName
				+ "(_id integer primary key autoincrement,packageName text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
