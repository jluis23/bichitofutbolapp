package com.dayscript.bichitofutbolapp;

import android.os.Bundle;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.adsdk.sdk.Ad;
import com.adsdk.sdk.AdListener;
import com.adsdk.sdk.banner.AdView;
import com.dayscript.bichitofutbolapp.ui.fragment.NewsFragment;
import com.dayscript.bichitofutbolapp.ui.fragment.TeamsListFragment;
import com.dayscript.bichitofutbolapp.utils.UIUtils;



public class TeamsActivity extends SherlockFragmentActivity implements AdListener{
	public static final String TEAMS_LIST_FRAGMENT_ID="TEAMS_FRAGMENT_ID";
	private AdView mAdView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teams_layout);
		getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TeamsListFragment()).commit();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		UIUtils.stylizeActionBar(this);
		//getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		/*PREPARANDO ADS**************************************************************/
			mAdView = new AdView(this, getResources().getString(R.string.ads_base_path),
			getResources().getString(R.string.ads_token), true, true);
			mAdView.setAdListener(this);
			ViewGroup rootLayout=(ViewGroup)this.findViewById(R.id.adsdkContent12);
			rootLayout.addView(mAdView);
		/*PREPARANDO ADS**************************************************************/
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.getSherlock().getMenuInflater().inflate(R.menu.main_activity_news, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==android.R.id.home)
		{
			finish();
		}
		if (item.getItemId() == R.id.refresh_content) {
			getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TeamsListFragment()).commit();
		}
		if (item.getItemId() == R.id.menu_home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void adClicked() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void adClosed(Ad arg0, boolean arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void adLoadSucceeded(Ad arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void adShown(Ad arg0, boolean arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void noAdFound() {
		// TODO Auto-generated method stub
		
	}
}
