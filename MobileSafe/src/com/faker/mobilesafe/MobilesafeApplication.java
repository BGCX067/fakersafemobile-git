package com.faker.mobilesafe;


import android.content.Context;
import com.faker.mobilesafe.dao.AppLockDao;
import com.faker.mobilesafe.util.LockPatternUtils;

public class MobilesafeApplication {

    private static MobilesafeApplication mInstance = null;
    private LockPatternUtils mLockPatternUtils;
    private static AppLockDao mdao;
    private String packageName;

    private MobilesafeApplication(Context context){
        mLockPatternUtils = new LockPatternUtils(context);
        mdao = new AppLockDao(context);
    }

    public static MobilesafeApplication getInstance(Context context) {
        if(mInstance == null){
            mInstance = new MobilesafeApplication(context);
        }
        return mInstance;
    }

    public LockPatternUtils getLockPatternUtils() {
        return mLockPatternUtils;
    }

    public AppLockDao getApplockDao() {
        return mdao;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
