package com.faker.mobilesafe.test;

import com.faker.mobilesafe.deal.AdminService;

import android.test.AndroidTestCase;

public class AdminServiceTest extends AndroidTestCase {

	public void testActiveAdmin() throws Exception {
		AdminService admin = AdminService.getInstance(getContext());
		admin.activeAdmin(getContext());
	}
	
	public void testWipeData() throws Exception{
		AdminService admin = AdminService.getInstance(getContext());
		admin.wipeData();
	}
}
