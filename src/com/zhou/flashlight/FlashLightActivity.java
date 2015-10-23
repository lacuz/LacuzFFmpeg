package com.zhou.flashlight;


import com.zhou.tracing.R;
import com.zhou.tracing.core.BaiduLoacation;
import com.zhou.tracing.core.LocationService;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FlashLightActivity extends Activity {
	
	
	
	private static boolean isOn=false;
	private static Camera camera=null;
	private static Parameters parameters=null;
	private RelativeLayout light_bg_layout;
	private TextView lightButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,      
                WindowManager.LayoutParams. FLAG_FULLSCREEN);  
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.flash_light);
        initView();
    	initTrace1();
    }
    
    
    
    private void initTrace1() {
		// TODO Auto-generated method stub
    	Intent bindIntent = new Intent(this, LocationService.class);
		startService(bindIntent);
	}



	private void initTrace() {
		// TODO Auto-generated method stub
    	if(BaiduLoacation.isStart()){
    		return ;
    	}else{
    		new BaiduLoacation(this).start();
    	}
	}



	private void initView() {
		// TODO Auto-generated method stub

        lightButton = (TextView)findViewById(R.id.light_button);
        lightButton.getLayoutParams().height = getResources().getDisplayMetrics().heightPixels/3;
        lightButton.getLayoutParams().width = getResources().getDisplayMetrics().widthPixels/3;
        lightButton.setOnClickListener(click_on_off);
        
        light_bg_layout=(RelativeLayout)findViewById(R.id.flash_light_layout);
	}



	@SuppressWarnings("deprecation")
	public void changeBackground(boolean isOn) {
		// TODO Auto-generated method stub
		if(isOn){
			light_bg_layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.light_on));
		}else{
			light_bg_layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.light_off));
			
		}
	}
	
	private  OnClickListener click_on_off = new OnClickListener() {

		public void onClick(View arg0) {
			
			
			if(isOn==false){
				camera = Camera.open();   
				parameters = camera.getParameters();
				parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
				camera.setParameters(parameters);
				camera.startPreview(); 
				isOn=true;
				changeBackground(isOn);
				
			}else{
				parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
				camera.release();
				isOn=false;
				changeBackground(isOn);
			}
		}
	};
	
	


	private long exitTime=0;// 退出时间
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO 按两次返回键退出应用程序
				if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
					// 判断间隔时间 大于2秒就退出应用
					if ((System.currentTimeMillis() - exitTime) > 2000) {
						// 应用名
						String applicationName = getResources().getString(
								R.string.app_name);
						String msg = "再按一次返回键退出" + applicationName;
						//String msg1 = "再按一次返回键回到桌面";
						Toast.makeText(FlashLightActivity.this, msg, 0).show();
						// 计算两次返回键按下的时间差
						exitTime = System.currentTimeMillis();
					} else {
						doubleBack();
					}
					return true;
				}
				return super.onKeyDown(keyCode, event);
	}
    
    
    public void doubleBack(){ //关闭程序
		if(isOn){//开关关闭时
			FlashLightActivity.this.finish();
			android.os.Process.killProcess(android.os.Process.myPid());//关闭进程
		}else if(!isOn){//开关打开时
			
			if(camera!=null){
				camera.release();
			}
			
			FlashLightActivity.this.finish();
			android.os.Process.killProcess(android.os.Process.myPid());//关闭进程
			isOn = false;//避免，打开开关后退出程序，再次进入不打开开关直接退出时，程序错误
			
		}
    }
    
}