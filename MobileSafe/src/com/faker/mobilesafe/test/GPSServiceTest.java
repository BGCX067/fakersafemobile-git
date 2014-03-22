package com.faker.mobilesafe.test;

import com.faker.mobilesafe.deal.GPSService;

import android.test.AndroidTestCase;
import android.util.Log;

public class GPSServiceTest extends AndroidTestCase {

	public void testRegistLocationChangeListener() throws Exception{
		GPSService gpsService = GPSService.getInstance(getContext());
		gpsService.registLocationChangeListener();
		Log.i("test", gpsService.getLocation());
	}
}
