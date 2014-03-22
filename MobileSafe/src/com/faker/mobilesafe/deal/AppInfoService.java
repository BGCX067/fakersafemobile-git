package com.faker.mobilesafe.deal;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.faker.mobilesafe.bean.AppInfoBean;

public class AppInfoService {

	private PackageManager pm;

	public AppInfoService(Context context) {
		pm = context.getPackageManager();
	}

	public List<AppInfoBean> getAppInfo() {
		List<AppInfoBean> appinfos = new ArrayList<AppInfoBean>();
		// 获得所有安装的应用信息
		List<ApplicationInfo> appinfo = pm
				.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
		for(ApplicationInfo info : appinfo){
			AppInfoBean bean = new AppInfoBean();
			// 应用图标
			Drawable icon = info.loadIcon(pm);
			Bitmap bitmap = ((BitmapDrawable)icon).getBitmap();
			bean.setAppIcon(zoomImg(bitmap, 50, 50));
			// 应用名称
			String appName = info.loadLabel(pm).toString();
			bean.setAppName(appName);
			// 应用包名
			String packageName = info.packageName;
			bean.setPackageName(info.packageName);
			try {
				PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
				String appVersion = packageInfo.versionName;
				bean.setAppVersion(appVersion);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			
			boolean isUserApp = filterApp(info);
			bean.setUserApp(isUserApp);
			appinfos.add(bean);
		}
		return appinfos;
	}

	private  Bitmap zoomImg(Bitmap paramBitmap, int paramInt1, int paramInt2)
	  {
	    int i = paramBitmap.getWidth();
	    int j = paramBitmap.getHeight();
	    Matrix localMatrix = new Matrix();
	    float f1 = paramInt1;
	    float f2 = i;
	    float f3 = f1 / f2;
	    float f4 = paramInt2;
	    float f5 = j;
	    float f6 = f4 / f5;
	    boolean bool = localMatrix.postScale(f3, f6);
	    Bitmap localBitmap = paramBitmap;
	    int k = 0;
	    return Bitmap.createBitmap(localBitmap, 0, k, i, j, localMatrix, true);
	}

	private boolean filterApp(ApplicationInfo info) {
		// TODO Auto-generated method stub
		//原来是系统应用，用户手动升级
        if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
            return true;
            //用户自己安装的应用程序
        } else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            return true;
        }
        return false;
	}
}
