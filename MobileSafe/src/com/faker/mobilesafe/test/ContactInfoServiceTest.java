package com.faker.mobilesafe.test;

import java.util.List;

import com.faker.mobilesafe.bean.ContactBean;
import com.faker.mobilesafe.deal.ContactInfoService;

import android.test.AndroidTestCase;
import android.util.Log;

public class ContactInfoServiceTest extends AndroidTestCase {

	public void testGetContacts() throws Exception{
		ContactInfoService service = new ContactInfoService(getContext());
		List<ContactBean> contacts = service.getContacts();
		for(ContactBean info:contacts){
			Log.i("test", info.toString());
		}
	}
	
	public void testQueryContact() throws Exception{
		ContactInfoService service = new ContactInfoService(getContext());
		Log.i("test",service.queryContact("5545"));
	}
}
