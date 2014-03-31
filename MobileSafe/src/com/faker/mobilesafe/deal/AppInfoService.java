package com.faker.mobilesafe.deal;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.faker.mobilesafe.bean.AppInfoBean;
import com.faker.mobilesafe.dao.AppLockDao;
import com.faker.mobilesafe.util.ImageUtil;

public class AppInfoService {

	private PackageManager pm;
    private Context context;

	public AppInfoService(Context context) {
        this.context = context;
		pm = context.getPackageManager();
	}

	public List<AppInfoBean> getAppInfo() {
		List<AppInfoBean> appinfos = new ArrayList<AppInfoBean>();
		// 获得所有安装的应用信息
		List<ApplicationInfo> appinfo = pm
				.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
		for(ApplicationInfo info : appinfo){

            // 应用包名
            String packageName = info.packageName;
            if(packageName.equals(context.getPackageName())){
                continue;
            }
            AppInfoBean bean = new AppInfoBean();

            bean.setPackageName(info.packageName);
			// 应用图标
			Drawable icon = info.loadIcon(pm);
			Bitmap bitmap = ((BitmapDrawable)icon).getBitmap();
			bean.setAppIcon(ImageUtil.zoomImg(bitmap, 50, 50));
			// 应用名称
			String appName = info.loadLabel(pm).toString();
			bean.setAppName(appName);
			try {
				PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
				String appVersion = packageInfo.versionName;
                int length = appVersion.length();
				bean.setAppVersion(appVersion.substring(0,length >=5 ? 5 : length));
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			
			boolean isUserApp = filterApp(info);
			bean.setUserApp(isUserApp);
			appinfos.add(bean);
		}
		return appinfos;
	}

    public List<AppInfoBean> getUnlockAppInfo() {
        List<AppInfoBean> appinfos = new ArrayList<AppInfoBean>();
        AppLockDao dao = new AppLockDao(context);
        // 获得所有安装的应用信息
        List<ApplicationInfo> appinfo = pm
                .getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        for(ApplicationInfo info : appinfo){
            // 应用包名
            String packageName = info.packageName;
            if(packageName.equals(context.getPackageName()) || dao.isLocked(packageName)){
                continue;
            }
            AppInfoBean bean = new AppInfoBean();

            bean.setPackageName(info.packageName);
            // 应用图标
            Drawable icon = info.loadIcon(pm);
            Bitmap bitmap = ((BitmapDrawable)icon).getBitmap();
            bean.setAppIcon(ImageUtil.zoomImg(bitmap, 50, 50));
            // 应用名称
            String appName = info.loadLabel(pm).toString();
            bean.setAppName(appName);
            try {
                PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
                String appVersion = packageInfo.versionName;
                int length = appVersion.length();
                bean.setAppVersion(appVersion.substring(0,length >=5 ? 5 : length));
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
            bean.setChecked(false);
            appinfos.add(bean);
        }
        return appinfos;
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
