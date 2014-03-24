package com.faker.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MsgRecordDBHelper extends SQLiteOpenHelper {

	private final static String dbName = "msgrecord";
	private static MsgRecordDBHelper mInstance = null;

	public static MsgRecordDBHelper getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new MsgRecordDBHelper(context, dbName, null, 1);
		}
		return mInstance;
	}

	private MsgRecordDBHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table "
				+ dbName
				+ "(_id integer primary key autoincrement,number text,address text,content text,time text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
