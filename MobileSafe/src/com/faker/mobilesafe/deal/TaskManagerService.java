package com.faker.mobilesafe.deal;

import java.util.ArrayList;
import java.util.List;

import com.faker.mobilesafe.R;
import com.faker.mobilesafe.bean.TaskInfoBean;
import com.faker.mobilesafe.util.ImageUtil;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class TaskManagerService {

	private Context context;
	private ActivityManager am;

	public TaskManagerService(Context context) {
		this.context = context;
		am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
	}

	/**
	 * 返回所有当前正在运行的进程信息
	 * 
	 * @return
	 */
	public List<TaskInfoBean> getTaskInfos() {
		List<TaskInfoBean> taskInfos = new ArrayList<TaskInfoBean>();
		List<RunningAppProcessInfo> runningAppProcessInfos = am
				.getRunningAppProcesses();
		PackageManager pm = context.getPackageManager();

		for (RunningAppProcessInfo info : runningAppProcessInfos) {
			String packageName = info.processName;
			if (!packageName.equals(context.getPackageName())) {
				TaskInfoBean bean = new TaskInfoBean();
				bean.setPackageName(packageName);
				try {
					ApplicationInfo applicationInfo = pm.getApplicationInfo(
							packageName, 0);
					// 应用图标
					Drawable drawable = applicationInfo.loadIcon(pm);
					if (drawable == null) {
						drawable = context.getResources().getDrawable(
								R.drawable.ic_launcher);
					}
					Bitmap paramBitmap = ((BitmapDrawable) drawable)
							.getBitmap();
					bean.setAppIcon(ImageUtil.zoomImg(paramBitmap, 50, 50));
					// 应用名称
					String appName = applicationInfo.loadLabel(pm).toString();
					bean.setAppName(appName);
					// 是否是系统进程
					bean.setUserApp(filterApp(applicationInfo));
					bean.setChecked(filterApp(applicationInfo));
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					Drawable drawable = context.getResources().getDrawable(
							R.drawable.ic_launcher);
					Bitmap paramBitmap = ((BitmapDrawable) drawable)
							.getBitmap();
					bean.setAppIcon(ImageUtil.zoomImg(paramBitmap, 50, 50));
					bean.setAppName(packageName);
				}
				// 进程pid
				int pid = info.pid;
				bean.setPid(pid);
				// 进程占用的内存
				android.os.Debug.MemoryInfo[] memoryInfos = am
						.getProcessMemoryInfo(new int[] { pid });
				android.os.Debug.MemoryInfo memoryInfo = memoryInfos[0];
				long memorySize = memoryInfo.getTotalPrivateDirty(); // KB
				bean.setMemorySize(memorySize);
				taskInfos.add(bean);
			}
		}
		return taskInfos;
	}

	private boolean filterApp(ApplicationInfo info) {
		// TODO Auto-generated method stub
		// 原来是系统应用，用户手动升级
		if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
			return true;
			// 用户自己安装的应用程序
		} else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
			return true;
		}
		return false;
	}
}
