package com.faker.mobilesafe.test;

import com.faker.mobilesafe.dao.PhoneAddressQueryDao;

import android.test.AndroidTestCase;
import android.util.Log;

public class PhoneAddressQueryServiceTest extends AndroidTestCase {

	public void testQueryAddress() throws Exception {
		PhoneAddressQueryDao queryService = new PhoneAddressQueryDao();
		String resultString = queryService.queryAddress("15068748660");
		Log.i("test", resultString);
	}
}
