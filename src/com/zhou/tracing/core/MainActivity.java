package com.zhou.tracing.core;


import com.zhou.tracing.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		

		Intent bindIntent = new Intent(this, LocationService.class);
		startService(bindIntent);
		finish();
	}


}
