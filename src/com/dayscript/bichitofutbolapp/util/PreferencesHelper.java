package com.dayscript.bichitofutbolapp.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class PreferencesHelper {
	public static final String TAG="BICHITOFUTBOL_PREFERENCESHELPER";
	public static final String FACEBOOK_USER_FILE="facebook_user.properties";
	public static final String DATABASE_NAME="BichitoFutbol";
	public static final String CONFIG_FILE="USER_CONFIG";
	public static final String GCM_IDENTIFIER="GCM_SERIAL_ID";
	public static void saveLogcatToFile(Context context) {    
	    String fileName = "logcat_bichitofutbol_"+".txt";
	    System.out.println(context.getExternalCacheDir());
	    File outputFile = new File(context.getExternalCacheDir(),fileName);
	    try {
			@SuppressWarnings("unused")
			Process process = Runtime.getRuntime().exec("logcat -f "+outputFile.getAbsolutePath());
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	public static boolean saveFacebookObject(Context ctx,JSONObject obj)
	{
			
		try {
			Properties p=new Properties();
			HashMap<String,String> fbProperties=JSONObjectToHashMap(obj);
			if(fbProperties.size()>0)
			{
				p.putAll(fbProperties);
			}
			else
			{
				return false;
			}
			p.store(ctx.openFileOutput(FACEBOOK_USER_FILE, Context.MODE_PRIVATE),"Datos obtenidos desde fb");
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public static Properties isAuthenticated(Context ctx)
	{
		Properties p=new Properties();
		try {
			p.load(ctx.openFileInput(FACEBOOK_USER_FILE));
			Log.v(TAG,"Usuario si ha iniciado sesi—n");
		} catch (Exception e) {
			p=null;
			Log.v(TAG,"Usuario no ha iniciado sesi—n");
			//e.printStackTrace();
		} 
		return p;
	}
	public static void savePreference(Context ctx,String idPreference, String preference)
	{
		Editor editor=ctx.getSharedPreferences(CONFIG_FILE,Context.MODE_PRIVATE ).edit();
		editor.putString(idPreference, preference);
		editor.commit();
	}
	public static String getPreference(Context ctx,String idPreference)
	{
		return ctx.getSharedPreferences(CONFIG_FILE,Context.MODE_PRIVATE ).getString(idPreference,null);
		
	}
	public static void deleteSession(Context ctx)
	{
		ctx.deleteFile(FACEBOOK_USER_FILE);
		ctx.deleteDatabase(DATABASE_NAME);
		
	}
	public static HashMap<String,String> JSONObjectToHashMap (JSONObject obj)
	{
		HashMap<String,String> ret=new HashMap<String,String>();
		Iterator<String> it=obj.keys();
		while(it.hasNext())
		{
			String key=it.next();
			ret.put(key, obj.optString(key));
		}
		return ret;
		
	}
}
