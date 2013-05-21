package com.dayscript.bichitofutbolapp.http;



import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.util.Log;

public abstract class WebservicesProxy {
		private AndroidHttpClient client;
		protected String serializationType;
		public static final String TAG="MICLAROMOBILE_WEBSERVICESPROXY";
		protected HashMap<String,String> headerParams;
		protected Context context;
		public WebservicesProxy()
		{
			client=HttpConnection.getClient();
			
		}
		public Object execute(String url,String method) throws IOException,JSONException
		{
			return execute(url,method,null);
		}
		public abstract void fillHeaderParams(); 
		public Object execute(String url,String method,HashMap<String,String> params) throws IOException, JSONException
		{	
			fillHeaderParams();
			client=HttpConnection.getClient();
			HttpRequestBase request=null;
			if(method.toLowerCase()=="get"){
				 request = new HttpGet(url);
			}
			else if(method.toLowerCase()=="post")
			{
				request=new HttpPost(url);
				HttpPost post=(HttpPost)request;
		    	if(params!=null){
		    		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		    		for(String i : params.keySet())
		    		{
		    			nvps.add(new BasicNameValuePair(i,params.get(i)));
		    		}
		    		post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		    	}
			}
			else
			{
				throw new IOException("Unidentified method, use get or post");	
			}
			request.addHeader("accept",serializationType );
			if(headerParams!=null){
			for(String i : headerParams.keySet())
    		{
    			request.addHeader(i,headerParams.get(i));
    		}
			}
			HttpResponse response = client.execute(request);
			Scanner scanner=new Scanner(response.getEntity().getContent());
			String strResponse="";
			while(scanner.hasNextLine())
			{
				strResponse+=scanner.nextLine();
			}
			Log.v(TAG, strResponse);
			JSONObject jObject = new JSONObject(strResponse); 
			client.close();
			//PreferencesHelper.methodsPostProcess(context, jObject);
			return jObject;
			
		}
		
}
