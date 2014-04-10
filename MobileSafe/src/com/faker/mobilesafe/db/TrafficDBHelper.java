package com.faker.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class TrafficDBHelper extends SQLiteOpenHelper {

	private final static String dbName = "traffic";
	private static TrafficDBHelper mInstance = null;

	public static TrafficDBHelper getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new TrafficDBHelper(context, dbName, null, 1);
		}
		return mInstance;
	}

	private TrafficDBHelper(Context context, String name,
                            CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + dbName
				+ "(_id integer primary key autoincrement,mobiletx text,mobilerx text,wifitx text,wifirx text,offset text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
