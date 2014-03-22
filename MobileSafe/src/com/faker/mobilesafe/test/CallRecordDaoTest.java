package com.faker.mobilesafe.test;

import java.util.List;

import com.faker.mobilesafe.bean.CallRecordBean;
import com.faker.mobilesafe.dao.CallRecordDao;

import android.test.AndroidTestCase;
import android.util.Log;

public class CallRecordDaoTest extends AndroidTestCase {

	public void testAddRecord() throws Exception {
		CallRecordDao dao = new CallRecordDao(getContext());
		for (int i = 0; i < 10; i++) {
			dao.addRecord("555" + i, "faker" + i, "0",System.currentTimeMillis());
		}
	}

	public void testDelete() throws Exception {
		CallRecordDao dao = new CallRecordDao(getContext());
		dao.delete("1");
	}

	public void testDeleteAll() throws Exception {
		CallRecordDao dao = new CallRecordDao(getContext());
		dao.deleteAll();
	}

	public void testFindAll() throws Exception {
		CallRecordDao dao = new CallRecordDao(getContext());
		List<CallRecordBean> records = dao.findAll();
		CallRecordBean bean = null;
		for (int i = 0; i < records.size(); i++) {
			bean = records.get(i);
			Log.i("lichfaker", bean.toString());
		}
	}

	public void testGetCount() throws Exception {
		CallRecordDao dao = new CallRecordDao(getContext());
		Log.i("lichfaker", dao.getCount());
	}
}
