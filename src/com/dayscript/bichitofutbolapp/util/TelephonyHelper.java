package com.dayscript.bichitofutbolapp.util;

import android.content.Context;
import android.telephony.TelephonyManager;

public class TelephonyHelper {
	public static String getImei(Context ctx)
	{
		  return ((TelephonyManager)ctx.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
	}
}
