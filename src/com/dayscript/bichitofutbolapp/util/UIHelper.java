package com.dayscript.bichitofutbolapp.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class UIHelper {
	public static AlertDialog createInformationalPopup(Context ctx,String title,String message)
	{
		  return createInformationalPopup(ctx,title,message,null);
	}
	public static AlertDialog createInformationalPopup(Context ctx,String title,String message,DialogInterface.OnClickListener listener)
	{
		Activity act=(Activity)ctx;
		if(!act.hasWindowFocus()){return null;}
		AlertDialog.Builder builder=new AlertDialog.Builder(ctx);
		builder.setMessage(message);
		builder.setTitle(title);
		builder.setPositiveButton(android.R.string.ok, listener);
		
		return builder.show();
	}
}
