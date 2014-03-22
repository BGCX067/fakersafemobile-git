package com.faker.mobilesafe.receiver;

import com.faker.mobilesafe.deal.ConstConfig;
import com.faker.mobilesafe.deal.SafeSharedpreference;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

public class BootCompleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		boolean b = SafeSharedpreference.getBoolean(context,
				ConstConfig.ISOPENLOST, false);
		if (b) {
			// 已经开启防盗
			String safenum = SafeSharedpreference.getString(context,
					ConstConfig.SAFENUMBER);
			String sim = SafeSharedpreference.getString(context,
					ConstConfig.SIMSERIAL);
			TelephonyManager tmManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			if (!sim.equalsIgnoreCase(tmManager.getSimSerialNumber())) {
				// 更换了sim卡发出短信通知
				SmsManager smsManager = SmsManager.getDefault();
				smsManager.sendTextMessage(safenum, null,
						"SIM Card has changed", null, null);
			}
		}
	}

}
