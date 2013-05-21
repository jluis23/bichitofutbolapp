package com.dayscript.bichitofutbolapp.http;



import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.net.http.AndroidHttpClient;
public class HttpConnection {
		private static AndroidHttpClient client;
		
		private HttpConnection()
		{
			
		}
		public static AndroidHttpClient getClient()
		{
			//if(HttpConnection.client==null)
			//{
				if(client!=null){
				client.close();
				}
				HttpParams httpParameters = new BasicHttpParams();
				
				HttpConnectionParams.setConnectionTimeout(httpParameters, 30000);
				HttpConnectionParams.setSoTimeout(httpParameters, 50000);
			    
				client=AndroidHttpClient.newInstance("BichitoFutbolApp");
				client.enableCurlLogging("Connection", 2);
				
			//}
			return client;
		}
		
}
