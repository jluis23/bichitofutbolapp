package com.dayscript.bichitofutbolapp.utils;

import android.widget.ImageView;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.dayscript.bichitofutbolapp.R;

public class UIUtils {
	public static void stylizeActionBarHome(SherlockFragmentActivity act)
	{
		LinearLayout layout=new LinearLayout(act);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setPadding(0, 0, 0, 10);
		ImageView v = new ImageView(act);
		v.setImageResource(R.drawable.logo_bichito);
		v.setPadding(10, 10, 10, 0);
		v.setScaleType(ImageView.ScaleType.CENTER);
		//v.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_top_repet));
		layout.addView(v);
		act.getSupportActionBar().setTitle("");
		act.getSupportActionBar().setDisplayShowCustomEnabled(true);
		act.getSupportActionBar().setBackgroundDrawable(act.getResources().getDrawable(R.drawable.back_top));
		act.getSupportActionBar().setCustomView(layout);
	}
	
	public static void stylizeActionBar(SherlockFragmentActivity act)
	{
		LinearLayout layout=new LinearLayout(act);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setPadding(0, 0, 0, 10);
		ImageView v = new ImageView(act);
		v.setImageResource(R.drawable.logo_bichito);
		v.setPadding(10, 10, 10, 0);
		v.setScaleType(ImageView.ScaleType.CENTER);
		//v.setBackgroundDrawable(getResources().getDrawable(R.drawable.back_top_repet));
		layout.addView(v);
		act.getSupportActionBar().setTitle("");
		
		act.getSupportActionBar().setDisplayShowCustomEnabled(true);
		act.getSupportActionBar().setBackgroundDrawable(act.getResources().getDrawable(R.drawable.back_top_repet));
		act.getSupportActionBar().setCustomView(layout);
	}
}
