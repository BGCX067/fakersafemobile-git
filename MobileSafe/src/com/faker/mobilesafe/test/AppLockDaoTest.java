package com.faker.mobilesafe.test;

import android.test.AndroidTestCase;
import android.util.Log;
import com.faker.mobilesafe.dao.AppLockDao;

/**
 * Created by Administrator on 14-3-27.
 */
public class AppLockDaoTest extends AndroidTestCase {
    public void testIsLocked() throws Exception {
        AppLockDao dao = new AppLockDao(getContext());
        boolean b = dao.isLocked("com.faker.test");
        Log.i("lichfaker",String.valueOf(b));
    }

    public void testAdd() throws Exception {
        AppLockDao dao = new AppLockDao(getContext());
        dao.add("com.faker.test");
    }

    public void testDelete() throws Exception {
        AppLockDao dao = new AppLockDao(getContext());
        dao.delete("com.faker.test");
    }

    public void testFindAll() throws Exception {
        AppLockDao dao = new AppLockDao(getContext());
        for (String s : dao.findAll()) {
            Log.i("lichfaker", s);
        }
    }
}
