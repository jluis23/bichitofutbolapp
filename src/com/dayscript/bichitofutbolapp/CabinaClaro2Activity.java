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
import com.dayscript.bichitofutbolapp.ui.fragment.AudioMultimediaFragment;
import com.dayscript.bichitofutbolapp.ui.fragment.CabinaClaroAudioMultimediaFragment;
import com.dayscript.bichitofutbolapp.ui.fragment.NewsFragment;
import com.dayscript.bichitofutbolapp.ui.fragment.VideoMultimediaFragment;
import com.dayscript.bichitofutbolapp.utils.UIUtils;

public class CabinaClaro2Activity extends SherlockFragmentActivity 
{
	String[] categorias={"Audio","Video"};
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
		setContentView(R.layout.audio_cabina_claro);
		UIUtils.stylizeActionBar(this);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		super.onCreate(arg0);
		getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CabinaClaroAudioMultimediaFragment()).commit();
	}

}
