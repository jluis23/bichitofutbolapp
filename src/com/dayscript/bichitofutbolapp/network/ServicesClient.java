package com.dayscript.bichitofutbolapp.network;

import java.util.HashMap;
import java.util.Properties;
import java.util.prefs.Preferences;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import com.dayscript.bichitofutbolapp.R;
import com.dayscript.bichitofutbolapp.util.PreferencesHelper;

public class ServicesClient extends AsyncHttpClient {
	public static final String TAG = "BICHITOFUTBOL_ASYNCHTTPCLIENT";

	public ServicesClient(boolean doFillHeaders) {
		super();
		if (doFillHeaders) {
			fillHeaders();
		}
	}

	public ServicesClient() {
		this(true);
	}

	public void fillHeaders() {
		addHeader("accept", "application/json");
	}

	public void getNewsList(final Callback caller) { // this.setTimeout(1);
														// super.setTimeout(2000);
		this.fillHeaders();
		// DefaultHttpClient clt=(DefaultHttpClient) this.getHttpClient();
		// clt.setHttpRequestRetryHandler(null);
		this.get(
				caller.getContext(),
				caller.getContext().getString(
						com.dayscript.bichitofutbolapp.R.string.base_path)
						+ caller.getContext()
								.getString(
										com.dayscript.bichitofutbolapp.R.string.get_news_list),
				new ServicesClient.JsonHttpResponseHandler(caller));
	}

	public void getNewsDetail(final Callback caller, int id) {
		this.fillHeaders();
		this.get(
				caller.getContext().getString(R.string.base_path)
						+ String.format(
								caller.getContext().getString(
										R.string.news_detail), id),
				new ServicesClient.JsonHttpResponseHandler(caller));
	}

	public void getMatchResults(final Callback caller, String id) {
		this.fillHeaders();
		this.get(String.format(caller.getContext().getString(R.string.base_path)
				+ caller.getContext().getString(R.string.get_match_results)
				,id), new ServicesClient.JsonHttpResponseHandler(caller));
	}

	public void getTeamsList(final Callback caller) {
		this.fillHeaders();
		this.get(
				caller.getContext(),
				caller.getContext().getString(
						com.dayscript.bichitofutbolapp.R.string.base_path)
						+ caller.getContext()
								.getString(
										com.dayscript.bichitofutbolapp.R.string.get_teams_list),
				new ServicesClient.JsonHttpResponseHandler(caller));
	}
	public void getTeamDetail(final Callback caller,String id) {
		this.fillHeaders();
		Properties user=PreferencesHelper.isAuthenticated(caller.getContext());
		String param="";
		if(user!=null)
		{
			param="?USER_ID=" + user.getProperty("id");
		}
		String url=
				caller.getContext().getString(
						com.dayscript.bichitofutbolapp.R.string.base_path)
						+ String.format(caller.getContext()
								.getString(
										com.dayscript.bichitofutbolapp.R.string.get_team_detail)+param,id);
		this.get(url,
				new ServicesClient.JsonHttpResponseHandler(caller));
	}
	public void getMatchDetail(final Callback caller,String id) {
		this.fillHeaders();
		
		String url=
				caller.getContext().getString(
						com.dayscript.bichitofutbolapp.R.string.base_path)
						+ String.format(caller.getContext()
								.getString(
										com.dayscript.bichitofutbolapp.R.string.get_match_detail),id);
		this.get(url,
				new ServicesClient.JsonHttpResponseHandler(caller));
	}
	public void getCalendarDates(final Callback caller)
	{
		this.get(
				caller.getContext(),
				caller.getContext().getString(
						com.dayscript.bichitofutbolapp.R.string.base_path)
						+ caller.getContext()
								.getString(
										com.dayscript.bichitofutbolapp.R.string.get_calendar_dates),
				new ServicesClient.JsonHttpResponseHandler(caller));
	}
	public void getPositionsTable(final Callback caller)
	{
		this.get(
				caller.getContext(),
				caller.getContext().getString(
						com.dayscript.bichitofutbolapp.R.string.base_path)
						+ caller.getContext()
								.getString(
										com.dayscript.bichitofutbolapp.R.string.get_table_positions),
				new ServicesClient.JsonHttpResponseHandler(caller));
	}
	public void saveUser(HashMap<String,String>params,final Callback caller)
	{
		RequestParams tmpParams=new RequestParams();
		for(String index:params.keySet())
		{
			tmpParams.put(index, params.get(index));
		}
		Log.v("Guardando usuario", "guardando usuario");
		this.post(caller.getContext().getString(
				com.dayscript.bichitofutbolapp.R.string.base_path)
				+ caller.getContext()
						.getString(
								com.dayscript.bichitofutbolapp.R.string.save_user),tmpParams, new ServicesClient.JsonHttpResponseHandler(caller));
	}
	public void saveUserNotifications(String idFacebook,String data,final Callback caller)
	{
		System.out.println("Id de facebook " + idFacebook);
		RequestParams tmpParams=new RequestParams();
		tmpParams.put("data", data);
		this.post(caller.getContext().getString(
				com.dayscript.bichitofutbolapp.R.string.base_path)
				+ String.format(caller.getContext()
						.getString(
								com.dayscript.bichitofutbolapp.R.string.save_user_notifications),idFacebook),tmpParams, new ServicesClient.JsonHttpResponseHandler(caller));
	}
	public void getNotifications(final Callback caller)
	{
		this.get(
				caller.getContext(),
				caller.getContext().getString(
						com.dayscript.bichitofutbolapp.R.string.base_path)
						+ caller.getContext()
								.getString(
										com.dayscript.bichitofutbolapp.R.string.get_notifications),
				new ServicesClient.JsonHttpResponseHandler(caller));
	}
	@Override
	public void get(String url, AsyncHttpResponseHandler responseHandler) {
		Log.v(TAG, "Conectando a:" + url);
		super.get(url, responseHandler);
	}
	/*public void post(String url, AsyncHttpResponseHandler responseHandler) {
		Log.v(TAG, "Conectando a:" + url);
		//super.get(url, responseHandler);
	}*/
	public void post(String url, RequestParams params,AsyncHttpResponseHandler responseHandler) {
		Log.v(TAG, "Conectando a:" + url);
		super.post(url, params,responseHandler);
	}
	static class JsonHttpResponseHandler extends
			com.loopj.android.http.JsonHttpResponseHandler {
		Callback caller;

		public JsonHttpResponseHandler(Callback c) {
			caller = c;
		}

		@Override
		public void onSuccess(JSONObject arg0) {
			Log.v(TAG, "Retornando con exito\n" + arg0.toString());
			super.onSuccess(arg0);
			caller.returnWithSuccess(arg0);
		}

		@Override
		public void onFailure(Throwable arg0, String arg1) {
			Log.v(TAG, "Ha ocurrido un error en request " + arg0.toString());
			arg0.printStackTrace();
			super.onFailure(arg0, arg1);
			caller.returnWithFailure(null);
		}
	}

	public interface Callback {
		public void returnWithSuccess(JSONObject ret);

		public void returnWithFailure(JSONObject ret);

		public Context getContext();
	}
}
