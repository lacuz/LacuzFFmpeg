package com.zhou.tracing.core;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 开机启动Service
 */
public class BootBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.e("location", "唤醒应用");
//		new BaiduLoacation(context).start();
//		//此处不需要做任何操作，当开机启动时，应用会接受广播，并自动启动Application实施绑定Service  暂留以后做其他可能的处理
		Intent bindIntent = new Intent(context, LocationService.class);
		context.startService(bindIntent);
	}

}
