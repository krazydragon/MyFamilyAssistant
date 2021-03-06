package com.rbarnes.myfamilyassistant.other;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class FamDeviceAdminReceiver extends DeviceAdminReceiver{
	
	static final String TAG = "FamDeviceAdminReceiver";
	
	
	@Override
	public void onEnabled(Context context, Intent intent) {
		super.onEnabled(context, intent);
	//	Toast.makeText(context, R.string.device_admin_enabled,
		//		Toast.LENGTH_LONG).show();
		Log.d(TAG, "onEnabled");
	}

	
	@Override
	public void onDisabled(Context context, Intent intent) {
		super.onDisabled(context, intent);
		//Toast.makeText(context, R.string.device_admin_disabled,
			//	Toast.LENGTH_LONG).show();
		Log.d(TAG, "onDisabled");
	}

	@Override
	public void onPasswordChanged(Context context, Intent intent) {
		super.onPasswordChanged(context, intent);
		Log.d(TAG, "onPasswordChanged");
	}

	@Override
	public void onPasswordFailed(Context context, Intent intent) {
		super.onPasswordFailed(context, intent);
		Log.d(TAG, "onPasswordFailed");
	}

	@Override
	public void onPasswordSucceeded(Context context, Intent intent) {
		super.onPasswordSucceeded(context, intent);
		Log.d(TAG, "onPasswordSucceeded");
	}

}
