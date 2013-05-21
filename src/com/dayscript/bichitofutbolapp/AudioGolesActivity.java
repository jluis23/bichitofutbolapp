package com.dayscript.bichitofutbolapp;

import java.lang.reflect.InvocationTargetException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.adsdk.sdk.banner.AdView;
import com.dayscript.bichitofutbolapp.utils.UIUtils;



public class AudioGolesActivity extends SherlockFragmentActivity {
	WebView wv;
	private AdView mAdView;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.audiogoles);
		 wv=(WebView)this.findViewById(R.id.contentWebView);
		 final Activity activity = this;
	
		wv.setWebChromeClient(new WebChromeClient() {
			
			 public void onProgressChanged(WebView view, int progress) {
			 Log.v("MICLAROMOVIL_INFORMATIVO","Updating progress" + progress);
			activity.setProgress(progress * 100);
			   
			 }});
			       
		wv.setWebViewClient(new WebViewClient());	  
		WebSettings webSettings = wv.getSettings();
		wv.loadUrl("http://jorgeisaac.com/api_futbol/web/goles.html?var=" + Math.random());
		webSettings.setJavaScriptEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		UIUtils.stylizeActionBar(this);
		mAdView = new AdView(this, getResources().getString(R.string.ads_base_path),
				getResources().getString(R.string.ads_token), true, true);
		ViewGroup rootLayout=(ViewGroup)this.findViewById(R.id.adsdkContent);
		rootLayout.addView(mAdView);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==android.R.id.home)
		{
			killWebView();
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    wv.loadUrl("about:blank");
	}
	private void killWebView() {
		 try {
		        Class.forName("android.webkit.WebView")
		                .getMethod("onPause", (Class[]) null)
		                            .invoke(wv, (Object[]) null);

		    } catch(ClassNotFoundException cnfe) {
		        
		    } catch(NoSuchMethodException nsme) {
		        
		    } catch(InvocationTargetException ite) {
		      
		    } catch (IllegalAccessException iae) {
		       
		    }
	}
	public void onPause() {
		  super.onPause();
		  if (wv != null) {
			  try{
				 killWebView();
		      
			  }
			  catch(Exception e)
			  {
				  
			  }
		  }
		}
}
