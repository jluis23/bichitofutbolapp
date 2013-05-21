package com.dayscript.bichitofutbolapp;

import java.lang.reflect.InvocationTargetException;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.dayscript.bichitofutbolapp.utils.UIUtils;

public class CabinaClaroActivity extends SherlockFragmentActivity {
	
	WebView wv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_detail);
		findViewById(R.id.fullcontent_layout).setVisibility(View.VISIBLE);
		findViewById(R.id.retry_layout).setVisibility(View.GONE);
		findViewById(R.id.loading_layout).setVisibility(View.GONE);
		findViewById(R.id.main_image).setVisibility(View.GONE);
		wv=(WebView)findViewById(R.id.text_fullcontent);
		WebSettings webSettings = wv.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setPluginsEnabled(true);
		webSettings.setPluginState(PluginState.ON);
		wv.setWebChromeClient(new WebChromeClient());
		wv.loadDataWithBaseURL(null,
		"<html><body bgcolor=\"#DEDEDE\" style=\"background:#DEDEDE\"><center><iframe width=\"420\" height=\"315\" src=\"http://www.youtube.com/embed/cDvbBZqNVfA?version=3&amp;hl=en_US\" frameborder=\"0\" allowfullscreen></iframe></center></body></html>", "text/html","UTF-8",null);
		
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		UIUtils.stylizeActionBar(this);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==android.R.id.home)
		{
			try{
				 killWebView();
		    wv.destroy();
			}
			catch(Exception e)
			{
				
			}
			finish();
		}
		return super.onOptionsItemSelected(item);
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
	public void onResume() {
		  super.onResume();
		  if (wv != null) {
			  try{
		      wv.resumeTimers();
			  }
			  catch(Exception e)
			  {
				  
			  }
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
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    
	   
	}
	
}
