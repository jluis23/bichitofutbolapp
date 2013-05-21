package com.dayscript.bichitofutbolapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.adsdk.sdk.Ad;
import com.adsdk.sdk.AdListener;
import com.adsdk.sdk.banner.AdView;
import com.dayscript.bichitofutbolapp.ui.fragment.CalendarFragment;
import com.dayscript.bichitofutbolapp.ui.fragment.ListaNoticiasFragment;
import com.dayscript.bichitofutbolapp.ui.fragment.MarcadorClaroFragment;
import com.dayscript.bichitofutbolapp.ui.fragment.NewsFragment;
import com.dayscript.bichitofutbolapp.ui.fragment.TeamsListFragment;
import com.dayscript.bichitofutbolapp.utils.UIUtils;

public class MarcadorClaroActivity extends SherlockFragmentActivity implements AdListener{
	
	Spinner fechas,torneos;
	String [] strings = {"Fecha 1","Fecha 2","Fecha 3","Fecha 4","Fecha 5","Fecha 6","Fecha 7","Fecha 8","Fecha 9","Fecha 10"};
	String [] subs = {"Sub 1","Sub 2","Sub 3","Sub 4","Sub 5","Sub 6","Sub 7","Sub 8","Sub 9","Sub 10"};
	private AdView mAdView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		showLoading();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.marcador_claro_main_layout);
		getSupportFragmentManager().beginTransaction().replace(R.id.fragment_calendar_container, new MarcadorClaroFragment()).commit();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		UIUtils.stylizeActionBar(this);
		/*PREPARANDO ADS**************************************************************/
			mAdView = new AdView(this, getResources().getString(R.string.ads_base_path),
			getResources().getString(R.string.ads_token), true, true);
			mAdView.setAdListener(this);
			ViewGroup rootLayout=(ViewGroup)this.findViewById(R.id.adsdkContent12);
			rootLayout.addView(mAdView);
		/*PREPARANDO ADS**************************************************************/
	}
	
	public void showLoading()

	{

		getSherlock().setProgressBarIndeterminateVisibility(true);

	}

	public void hideLoading()
	{
		getSherlock().setProgressBarIndeterminateVisibility(false);

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
			getSupportFragmentManager().beginTransaction().replace(R.id.fragment_calendar_container, new MarcadorClaroFragment()).commit();
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		if (item.getItemId() == R.id.menu_home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
		
	}
	
