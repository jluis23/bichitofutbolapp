package com.dayscript.bichitofutbolapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;

import com.dayscript.bichitofutbolapp.util.NotificationsHelper;
import com.dayscript.bichitofutbolapp.util.PreferencesHelper;

public class GCMIntentService extends GCMBaseIntentService {
	public static final String APP_ID="66563463603";
	public GCMIntentService()
	{
		super(APP_ID);
	}
	@Override
	protected void onError(Context arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onMessage(Context arg0, Intent arg1) {
		String k="";
		for (String i:arg1.getExtras().keySet())
		{
			k+=i;
		}
		NotificationsHelper.showNotification(arg0, arg1.getExtras().getString("titulo") ,arg1.getExtras().getString("mensaje") );
	}

	@Override
	protected void onRegistered(Context arg0, String arg1) {
			Log.v("INTENT","Dispositivo ha sido registrado");
			PreferencesHelper.savePreference(this, PreferencesHelper.GCM_IDENTIFIER, arg1);
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		// TODO Auto-generated method stub

	}

}
