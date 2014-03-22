package com.faker.mobilesafe.receiver;

import com.faker.mobilesafe.deal.ConstConfig;
import com.faker.mobilesafe.deal.SafeSharedpreference;
import com.faker.mobilesafe.view.activitys.LostProtectedActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PhoneOutReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String command = SafeSharedpreference.getString(context,
				ConstConfig.RELOAD_COMMAND, ConstConfig.RELOAD_COMMAND);
		if (command.equals(getResultData())) {
			setResultData(null);
			Intent intent2 = new Intent(context, LostProtectedActivity.class);
			intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent2);
		} else {
			boolean auto_ip = SafeSharedpreference.getBoolean(context,
					ConstConfig.ISAUTOIP, false);
			if (auto_ip) {
				String ipNum = SafeSharedpreference.getString(context,
						ConstConfig.IPNUMBER);
				setResultData(ipNum + getResultData());
			}
		}
	}

}
