package com.dayscript.bichitofutbolapp.network.handler;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.dayscript.bichitofutbolapp.network.ServicesClient;

public abstract class DefaultNetworkHandler implements Runnable, ServicesClient.Callback {
	String intentFilter;
	Context ctx;
	LocalBroadcastManager lbm;
	protected boolean fullResponse;
	public static final String TAG="BICHITOFUTBOL_NETWORKHANDLER";
	public DefaultNetworkHandler(Context ctx, String intentFilter,LocalBroadcastManager lbm)
	{
		this(ctx, intentFilter, lbm, false);
	}
	public DefaultNetworkHandler(Context ctx, String intentFilter,LocalBroadcastManager lbm,boolean fullResponse)
	{
		this.ctx=ctx;
		this.intentFilter=intentFilter;
		this.lbm=lbm;
		this.fullResponse=fullResponse;
	}
	@Override
	public void returnWithSuccess(JSONObject ret) {
			handleResponse(ret);
	}

	@Override
	public void returnWithFailure(JSONObject ret) {
			handleResponse(null);
	}
	public void handleResponse(JSONObject ret)
	{	Intent i = new Intent(intentFilter);
		if(ret==null)
		{
			Log.v(TAG,"Ha ocurrido un error de red, broadcast a " + intentFilter);
			
			i.putExtra("success", false);
			i.putExtra("mensaje", this.getContext().getString(com.dayscript.bichitofutbolapp.R.string.network_error));
		}
		else
		{	
			if(fullResponse==true)
			{
				i.putExtra("fullResponse", ret.toString());
			}
			if(ret.optBoolean("success")==true)
			{
				i.putExtra("success", true);
				if(ret.optJSONArray("data")!=null)
				{
					i.putExtra("data", ret.optJSONArray("data").toString());
				}
				else if(ret.optJSONObject("data")!=null)
				{
					i.putExtra("data", ret.optJSONObject("data").toString());
				}
			}
			else
			{
				i.putExtra("success", false);
				i.putExtra("mensaje", ret.optString("mensaje"));
			}
		}
		lbm.sendBroadcast(i);
	}

	@Override
	public Context getContext() {
	
		return ctx;
	}

	

}
