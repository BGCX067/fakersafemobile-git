package com.faker.mobilesafe.deal;

import java.util.ArrayList;
import java.util.List;

import com.faker.mobilesafe.bean.ContactBean;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

public class ContactInfoService {

	private Context context;

	public ContactInfoService(Context context) {
		this.context = context;
	}

	/**
	 * 获取所有的联系人并返回
	 * 
	 * @return
	 */
	public List<ContactBean> getContacts() {
		List<ContactBean> list_contacts = new ArrayList<ContactBean>();
		ContentResolver cr = context.getContentResolver();
		// 查询raw_contacts表得到联系人的_id
		Uri uri = Uri.parse("content://com.android.contacts/contacts");
		Cursor c = cr.query(uri, new String[] { "_id", "display_name" }, null,
				null, null);
		// String[] names = c.getColumnNames();
		// for(String name : names){
		// Log.i("i", name);
		// }
		while (c.moveToNext()) {
			ContactBean infoBean = new ContactBean();
			String name = c.getString(c.getColumnIndex("display_name"));
			String _id = c.getString(c.getColumnIndex("_id"));
			infoBean.setName(name);
			// 查询data表
			uri = Uri.parse("content://com.android.contacts/raw_contacts/"
					+ _id + "/data");
			Cursor c1 = cr.query(uri, new String[] { "data1", "mimetype" },
					null, null, null);
			while (c1.moveToNext()) {
				String data1 = c1.getString(c1.getColumnIndex("data1"));
				String mimetype = c1.getString(c1.getColumnIndex("mimetype"));
				if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
					data1 = data1.replaceAll("-", "");
					infoBean.setPhoneNumber(data1);
					list_contacts.add(infoBean);
				}
			}
			c1.close();
		}
		c.close();
		return list_contacts;
	}

	/**
	 * 根据电话号码查询联系人名称
	 * 
	 * @param number
	 * @return
	 */
	public String queryContact(String number) {
		String nameString = "";
		ContentResolver cr = context.getContentResolver();
		String[] projection = { ContactsContract.PhoneLookup.DISPLAY_NAME };
		Uri uri = Uri.withAppendedPath(
				ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(number));
		Cursor cursor = cr.query(uri, projection,
				null, null, null);
		if(cursor.moveToFirst()){
			nameString = cursor.getString(0);
		}
		return nameString;
	}
}
