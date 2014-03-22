package com.faker.mobilesafe.deal;


import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class GPSService {

	private Context context;
	private static GPSService mInstance = null;
	private LocationManager locationManager;

	private GPSService(Context context) {
		this.context = context;
		locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
	}

	public synchronized static GPSService getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new GPSService(context);
		}
		return mInstance;
	}

	public void registLocationChangeListener() {
		Criteria criteria = new Criteria();
		// 设置定位的精度
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		// 是否关注海拔信息
		criteria.setAltitudeRequired(false);
		// 对外界事物是否关心
		criteria.setBearingRequired(false);
		// 是否支持收费的查询
		criteria.setCostAllowed(true);
		// 耗电
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		// 对速度是否关心
		criteria.setSpeedRequired(false);
		String provider = locationManager.getBestProvider(criteria, true);
		// 注册监听
		locationManager.requestLocationUpdates(provider, 50000, 0,
				getListener());
	}

	public void unregistLocationChangeListener() {
		locationManager.removeUpdates(getListener());
	}

	public String getLocation() {
		return SafeSharedpreference.getString(context, "location","The Location service is starting Please try latter");
	}

	private MyLocationListener listener = null;

	private MyLocationListener getListener() {
		if (listener == null) {
			listener = new MyLocationListener();
		}
		return listener;
	}

	private final class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			double latitude = location.getLatitude();
			double longlatitude = location.getLongitude();
			SafeSharedpreference.save(context, "location", "latitude:"
					+ latitude + ";longlatitude" + longlatitude);
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		@Override
		public void onProviderEnabled(String provider) {

		}

		@Override
		public void onProviderDisabled(String provider) {

		}

	}
}
