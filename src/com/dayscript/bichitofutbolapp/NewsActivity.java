package com.dayscript.bichitofutbolapp;

import java.util.Calendar;
import java.util.HashMap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.adsdk.sdk.Ad;
import com.adsdk.sdk.AdListener;
import com.adsdk.sdk.banner.AdView;
import com.dayscript.bichitofutbolapp.R;
import com.dayscript.bichitofutbolapp.ui.fragment.CalendarFragment;
import com.dayscript.bichitofutbolapp.ui.fragment.DateDialogFragment;
import com.dayscript.bichitofutbolapp.ui.fragment.ListaNoticiasFragment;
import com.dayscript.bichitofutbolapp.ui.fragment.NewsFragment;
import com.dayscript.bichitofutbolapp.ui.fragment.TabNetworkFragment;
import com.dayscript.bichitofutbolapp.utils.UIUtils;

public class NewsActivity extends SherlockFragmentActivity implements  AdListener{
	public static final String TAG="NOTICIAS";
	public static final String NEWS_TAG="NOTICIAS";
	public static final String INFO_TAG="info";
	public static final String SCORES_TAG = "RESULTADOS_TAG";
	HashMap<String, Fragment> fragmentList = new HashMap<String, Fragment>();
	private AdView mAdView;
	DateDialogFragment frag;
	Button button;
    Calendar now;
    public void showLoading()
	{
		getSherlock().setProgressBarIndeterminateVisibility(true);
	}
	public void hideLoading()
	{
		getSherlock().setProgressBarIndeterminateVisibility(false);
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_main_layout);
		getSupportFragmentManager().beginTransaction().replace(R.id.container_news_fragment, new NewsFragment()).commit();
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
	
	public void showDialog() {
    	/*android.app.FragmentTransaction ft = getFragmentManager().beginTransaction(); //get the fragment
    	frag = DateDialogFragment.newInstance(this, new DateDialogFragmentListener(){
    		public void updateChangedDate(int year, int month, int day){
    			button.setText(String.valueOf(month+1)+"-"+String.valueOf(day)+"-"+String.valueOf(year));
    			now.set(year, month, day);
    		}
    	}, now);
    	
    	frag.show(ft, "DateDialogFragment");*/
    }
    
    public interface DateDialogFragmentListener{
    	//this interface is a listener between the Date Dialog fragment and the activity to update the buttons date
    	public void updateChangedDate(int year, int month, int day);
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
			getSupportFragmentManager().beginTransaction().replace(R.id.container_news_fragment, new NewsFragment()).commit();
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		if(mAdView!=null)
			mAdView.release();
	}
	
}
