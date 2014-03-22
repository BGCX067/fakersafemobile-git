package com.faker.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CallRecordDBHelper extends SQLiteOpenHelper {

	private final static String dbName = "callrecord";
	private static CallRecordDBHelper mInstance = null;

	public static CallRecordDBHelper getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new CallRecordDBHelper(context, dbName, null, 1);
		}
		return mInstance;
	}

	private CallRecordDBHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table "
				+ dbName
				+ "(_id integer primary key autoincrement,number text,address text,type text,time text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
