package com.dayscript.bichitofutbolapp;
import com.dayscript.bichitofutbolapp.R;
//import ec.com.dayscript.bichitofutbol.persistence.entity.User;
//import ec.com.dayscript.bichitofutbol.util.PreferencesHelper;

import android.app.Activity;
import android.content.Intent;


import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class SplashActivity extends Activity{
public static final int tiempo = 3;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
            	gotoMainScreen();
            }
        }, tiempo*1000);
	}
	
	

	@Override
	protected void onStart() {
		super.onStart();
	}
	
	public void splashButtonHandler(View v)
	{
	
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}

   /*
    * Ir a la pantalla principal de la aplicacion (HOME)
    */
   public void gotoMainScreen()
   {
   		Intent i=new Intent(this,MainActivity.class);
   		startActivity(i);
   		this.finish();
   }
   
}
	