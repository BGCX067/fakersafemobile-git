package com.faker.mobilesafe.test;

import java.util.ArrayList;
import java.util.List;

import android.test.AndroidTestCase;
import android.util.Log;

import com.faker.mobilesafe.dao.BlackNumberDao;

public class BlackNumberDaoTest extends AndroidTestCase {

	public void testIsBlackNumber() throws Exception {
		BlackNumberDao blackNumberDao = new BlackNumberDao(getContext());
		boolean result = blackNumberDao.isBlackNumber("5556");
		Log.i("test", "" + result);
	}

	public void testAddBalckNumber() throws Exception {
		BlackNumberDao blackNumberDao = new BlackNumberDao(getContext());
		for (int i = 0; i < 10; i++) {
			blackNumberDao.addBalckNumber("555" + i);
		}
	}

	public void testAddBlackNumbers() throws Exception {
		BlackNumberDao blackNumberDao = new BlackNumberDao(getContext());
		List<String> numbers = new ArrayList<String>();
		numbers.add("1-376-748-8880");
		numbers.add("1-376-848-8881");
		numbers.add("1-376-874-8882");
		blackNumberDao.addBlackNumbers(numbers);
	}

	public void testDelete() throws Exception {
		BlackNumberDao dao = new BlackNumberDao(getContext());
		dao.delete("5558");
	}

	public void testDeleteAll() throws Exception {
		BlackNumberDao dao = new BlackNumberDao(getContext());
		dao.deleteAll();
	}

	public void testUpdate() throws Exception {
		BlackNumberDao dao = new BlackNumberDao(getContext());
		dao.update(1, "8888");
	}

	public void testFindAll() throws Exception {
		BlackNumberDao dao = new BlackNumberDao(getContext());
		List<String> list = dao.findAll();
		for (int i = 0; i < list.size(); i++) {
			Log.i("test", list.get(i));
		}
	}

	public void testGetCount() throws Exception {
		BlackNumberDao dao = new BlackNumberDao(getContext());
		Log.i("test", "Count:" + dao.getCount());
	}
}
