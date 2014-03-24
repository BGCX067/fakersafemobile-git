package com.faker.mobilesafe.test;

import java.util.List;

import com.faker.mobilesafe.bean.MsgRecordBean;
import com.faker.mobilesafe.dao.MsgRecordDao;
import com.faker.mobilesafe.util.FormatUtil;

import android.test.AndroidTestCase;
import android.util.Log;

public class MsgRecordDaoTest extends AndroidTestCase {

	public void testAddRecord() throws Exception {
		MsgRecordDao dao = new MsgRecordDao(getContext());
		for (int i = 0; i < 10; i++) {
			dao.addRecord("1002602" + i, "hsghsdlkghj",
					FormatUtil.formatTime(System.currentTimeMillis()));
		}
	}

	public void testFindAll() throws Exception {
		MsgRecordDao dao = new MsgRecordDao(getContext());
		List<MsgRecordBean> daoBeans = dao.findAll();
		for (MsgRecordBean bean : daoBeans) {
			Log.i("lichfaker", bean.toString());
		}
	}

	public void testDeleteAll() throws Exception {
		MsgRecordDao dao = new MsgRecordDao(getContext());
		dao.deleteAll();
	}
}
