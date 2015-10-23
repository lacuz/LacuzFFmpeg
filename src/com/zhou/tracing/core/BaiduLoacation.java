package com.zhou.tracing.core;


import android.content.Context;
import android.util.Log;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

public class BaiduLoacation {
	private static LocationClient mLocationClient;
	private String tempcoor="gcj02";
	private LocationMode tempMode = LocationMode.Battery_Saving;
	private  final static int TIME_INTERVAL = 30 ; //三十分钟获取一次地理位置
	
	public BaiduLoacation(Context context){

		mLocationClient = ((LocationApplication)context.getApplicationContext()).mLocationClient;
		InitLocation();
	}
	
	public void start(){
		Log.e("BaiduLoacation","start()");
		mLocationClient.start();
	}
	
	public static boolean isStart(){
		if(mLocationClient != null){
			return mLocationClient.isStarted();
		}
		return false;
	}

	private void InitLocation() {
		// TODO Auto-generated method stub
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);//设置定位模式
		option.setCoorType(tempcoor);//返回的定位结果是百度经纬度，默认值gcj02
        option.setScanSpan(TIME_INTERVAL*1000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(false);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
		mLocationClient.setLocOption(option);
		
	}
	

}
