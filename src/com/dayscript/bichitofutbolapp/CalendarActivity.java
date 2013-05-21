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
import com.dayscript.bichitofutbolapp.ui.fragment.TeamsListFragment;
import com.dayscript.bichitofutbolapp.utils.UIUtils;

public class CalendarActivity extends SherlockFragmentActivity implements AdListener{
	
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
		setContentView(R.layout.calendar_main_layout);
		getSupportFragmentManager().beginTransaction().replace(R.id.fragment_calendar_container, new CalendarFragment()).commit();
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
	
	public boolean onCreateOptionsMenu(Menu menu) {
		this.getSherlock().getMenuInflater().inflate(R.menu.main_activity_news, menu);
		return true;
	}
	
	
	public void showLoading()

	{

		getSherlock().setProgressBarIndeterminateVisibility(true);

	}

	public void hideLoading()

	{

	getSherlock().setProgressBarIndeterminateVisibility(false);

	}
	public class myAdapter extends ArrayAdapter<String>{

			public myAdapter(CalendarActivity mainActivity, int row, String[] strings) {
				super(mainActivity,row,strings);
			}

	        @Override
	        public View getDropDownView(int position, View convertView,ViewGroup parent) {
	            return getCustomView(position, convertView, parent);
	        }
	 
	        @Override
	        public View getView(int position, View convertView, ViewGroup parent) {
	            return getCustomView(position, convertView, parent);
	        }
	 
	        public View getCustomView(int position, View convertView, ViewGroup parent) {
	 
	            LayoutInflater inflater=getLayoutInflater();
	            View row=inflater.inflate(R.layout.row, parent, false);
	            //lineas que hacen que el contenido cambie
	            TextView label=(TextView)row.findViewById(R.id.company);
	            label.setText(strings[position]);
	            TextView sub=(TextView)row.findViewById(R.id.sub);
	            sub.setText(subs[position]);

	            return row;
	        }
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
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==android.R.id.home)
		{
			finish();
		}
		if (item.getItemId() == R.id.refresh_content) {
			getSupportFragmentManager().beginTransaction().replace(R.id.fragment_calendar_container, new CalendarFragment()).commit();
		}
		if (item.getItemId() == R.id.menu_home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
		
	}
	
