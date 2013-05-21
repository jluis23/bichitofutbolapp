package com.dayscript.bichitofutbolapp;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

import android.app.Activity;
import android.os.Bundle;

public class BichitoBaseActivity extends SlidingFragmentActivity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		//ORMDroidApplication.initialize(this);
	}
}
