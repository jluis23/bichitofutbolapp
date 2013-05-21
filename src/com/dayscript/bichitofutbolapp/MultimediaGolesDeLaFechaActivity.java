package com.dayscript.bichitofutbolapp;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.adsdk.sdk.Ad;
import com.adsdk.sdk.AdListener;
import com.adsdk.sdk.banner.AdView;
import com.dayscript.bichitofutbolapp.ui.fragment.AudioMultimediaFragment;
import com.dayscript.bichitofutbolapp.ui.fragment.NewsFragment;
import com.dayscript.bichitofutbolapp.ui.fragment.VideoMultimediaFragment;
import com.dayscript.bichitofutbolapp.utils.UIUtils;

public class MultimediaGolesDeLaFechaActivity extends SherlockFragmentActivity implements AdListener
{
	String[] categorias={"Audio","Video"};
	private AdView mAdView;
	public static final String TAG="MULTIMEDIACABINACLARO_ACTIVITY";
	public void showLoading()
	{
		getSherlock().setProgressBarIndeterminateVisibility(true);
	}
	public void hideLoading()
	{
		getSherlock().setProgressBarIndeterminateVisibility(false);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==android.R.id.home)
		{
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onCreate(Bundle arg0) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.multimedia_cabina_claro);
		UIUtils.stylizeActionBar(this);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		Spinner sp=(Spinner)findViewById(R.id.multimedia_category);
		ArrayList<String>cats=new ArrayList<String>(Arrays.asList(categorias));
	    ArrayAdapter<String> adapter=new MediaTypeSpinnerItem(this,cats);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    sp.setAdapter(adapter);
	    /*PREPARANDO ADS**************************************************************/
			mAdView = new AdView(this, getResources().getString(R.string.ads_base_path),
			getResources().getString(R.string.ads_token), true, true);
			mAdView.setAdListener(this);
			ViewGroup rootLayout=(ViewGroup)this.findViewById(R.id.adsdkContent12);
			rootLayout.addView(mAdView);
		/*PREPARANDO ADS**************************************************************/
	    sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if(arg2==0)
			{
				Log.v(TAG,"Seleccionado audio");
				hideLoading();
				getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AudioMultimediaFragment()).commit();
			}
			if(arg2==1)
			{
				Log.v(TAG,"Seleccionado video");
				hideLoading();
				getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new VideoMultimediaFragment()).commit();
			}
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}});
	
		super.onCreate(arg0);
	}
	static class MediaTypeSpinnerItem extends ArrayAdapter<String> {
		ArrayList<String> elements;
		public MediaTypeSpinnerItem(Context ctx,ArrayList<String> items)
		{
			super(ctx,R.layout.news_list_item,items);
			this.elements=items;
		}
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			if (row==null) {
				LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row=inflater.inflate(R.layout.media_types_spinner_item, parent, false);
			}
		
			String result=(String)elements.get(position);
			TextView tv=(TextView)row.findViewById(R.id.description);
			tv.setText(result);
			if(position==1)	
			{
				ImageView iv=(ImageView)row.findViewById(R.id.mediaTypeImage);
				iv.setImageDrawable(this.getContext().getResources().getDrawable(R.drawable.icono_video));
			}
			return(row);
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

}
