package com.faker.mobilesafe.deal;

import android.R.integer;
import android.content.Context;
import android.content.SharedPreferences;

public class SafeSharedpreference {

	/**
	 * 保存sharedpreference数据
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void save(Context context, String key, Object value) {
		SharedPreferences sp = context.getSharedPreferences(ConstConfig.SPNAME,
				0);
		if (value instanceof String) {
			sp.edit().putString(key, (String) value).commit();
		} else if (value instanceof Boolean) {
			sp.edit().putBoolean(key, (Boolean) value).commit();
		} else if (value instanceof integer) {
			sp.edit().putInt(key, (Integer) value).commit();
		}
	}

	/**
	 * 通过key得到sharedpreference中字符串数据
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static String getString(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(ConstConfig.SPNAME,
				0);
		return sp.getString(key, "");
	}

	/**
	 * 通过key得到sharedpreference中字符串数据
	 * 
	 * @param context
	 * @param key
	 * @param value
	 * @return
	 */
	public static String getString(Context context, String key, String value) {
		SharedPreferences sp = context.getSharedPreferences(ConstConfig.SPNAME,
				0);
		return sp.getString(key, value);
	}

	/**
	 * 通过key得到sharedpreference中布尔数据
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static boolean getBoolean(Context context, String key, boolean flag) {
		SharedPreferences sp = context.getSharedPreferences(ConstConfig.SPNAME,
				0);
		return sp.getBoolean(key, flag);
	}

	/**
	 * 通过key得到sharedpreference中整型数据
	 * 
	 * @param context
	 * @param key
	 * @param value
	 * @return
	 */
	public static int getInt(Context context, String key, int value) {
		SharedPreferences sp = context.getSharedPreferences(ConstConfig.SPNAME,
				0);
		return sp.getInt(key, value);
	}
}
