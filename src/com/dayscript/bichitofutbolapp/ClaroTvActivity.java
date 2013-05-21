package com.dayscript.bichitofutbolapp;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.dayscript.bichitofutbolapp.utils.UIUtils;

public class ClaroTvActivity extends SherlockFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.claro_tv_banner);
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		UIUtils.stylizeActionBar(this);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==android.R.id.home)
		{
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
	public void onResume() {
		  super.onResume();
		}

		public void onPause() {
		  super.onPause();
		 
		}
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    
	   
	}
	
}
