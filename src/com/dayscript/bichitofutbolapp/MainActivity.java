package com.dayscript.bichitofutbolapp;

import java.util.Properties;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.adsdk.sdk.Ad;
import com.adsdk.sdk.AdListener;
import com.adsdk.sdk.banner.AdView;
import com.dayscript.bichitofutbolapp.R;
import com.dayscript.bichitofutbolapp.util.PreferencesHelper;
import com.dayscript.bichitofutbolapp.util.TelephonyHelper;
import com.dayscript.bichitofutbolapp.utils.UIUtils;
import com.google.android.gcm.GCMRegistrar;
import com.slidingmenu.lib.SlidingMenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends BichitoBaseActivity implements ActionBar.TabListener, AdListener{
TextView txt;
Properties user;
private AdView mAdView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setBehindContentView(R.layout.main_sliding_menu_fragment);
		setContentView(R.layout.home_layout);
		SlidingMenu menu = this.getSlidingMenu();
		menu.setMode(SlidingMenu.RIGHT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		menu.setTouchModeBehind(SlidingMenu.TOUCHMODE_NONE);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		
		//setContentView(R.layout.home_layout);
		UIUtils.stylizeActionBarHome(this);
		
		/*REGISTRANDO LAS NOTIFICACIONES PUSH*/
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		final String regId = GCMRegistrar.getRegistrationId(this);
		if (regId.equals("")) {
		  GCMRegistrar.register(this, GCMIntentService.APP_ID );
		  Log.v("MAIN", "Registrando para recibir notifiaciones push y el id es " + GCMRegistrar.getRegistrationId(this));
		} else {
			if(PreferencesHelper.getPreference(this, PreferencesHelper.GCM_IDENTIFIER)==null)
			{
				Log.v("MAIN", "Ya registrado pero no est‡ grabado localmente, grabando" );
				PreferencesHelper.savePreference(this, PreferencesHelper.GCM_IDENTIFIER,GCMRegistrar.getRegistrationId(this));
			}
		  Log.v("MAIN", "Ya esta registrado, obteniendo desde base local " + PreferencesHelper.getPreference(this, PreferencesHelper.GCM_IDENTIFIER));
		 
		}
		/*REGISTRANDO LAS NOTIFICACIONES PUSH*/
		/*
		 * NOTIFICACIONES POLLING
		Intent intentService=new Intent(this,NotificationsFetcherService.class);
		this.startService(intentService);
		startService(intentService);
		*/
		try{
		if(TelephonyHelper.getImei(this.getApplicationContext()).equals("353091052852383"))
		{
			PreferencesHelper.saveLogcatToFile(this.getApplicationContext());
		}
		}catch(RuntimeException e)
		{
			PreferencesHelper.saveLogcatToFile(this.getApplicationContext());
		}
		user=PreferencesHelper.isAuthenticated(this);
		if(user==null)
		{
			System.out.println("Usuario no est‡ autenticado");
		}
		else
		{
			System.out.println("Usuario est‡ autenticado");
		}
		
		/*PREPARANDO ADS**************************************************************/
		mAdView = new AdView(this, getResources().getString(R.string.ads_base_path),
		getResources().getString(R.string.ads_token), true, true);
		mAdView.setAdListener(this);
		ViewGroup rootLayout=(ViewGroup)this.findViewById(R.id.adsdkContent12);
		rootLayout.addView(mAdView);
	/*PREPARANDO ADS**************************************************************/
	}
	
	public void navigate(View v)
    {
    	if(v.getId()==R.id.btn_noticias){
    		Log.v("Boton de noticias", "Boton de noticias");
    		Intent i=new Intent(this,NewsActivity.class);
    		i.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
    		startActivity(i);
    	}
    	else if(v.getId()==R.id.btn_tablaposiciones){
    		Log.v("Boton de tabla de posiciones", "Boton de posiciones");
    		Intent i=new Intent(this,PositionsTableActivity.class);
    		i.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
    		startActivity(i);
    	}
    	else if(v.getId()==R.id.btn_equipos){
    		Log.v("Boton de equipos", "Boton de equipos");
    		Intent i=new Intent(this,TeamsActivity.class);
    		i.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
    		startActivity(i);
    	}
    	else if(v.getId()==R.id.btn_cabina_claro){
    		Log.v("Boton de cabina", "Cabina Claro");
    		Intent i=new Intent(this,CabinaClaro2Activity.class);
    		i.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
    		startActivity(i);
    	}
    	else if(v.getId()==R.id.btn_goles){
    		Log.v("Boton de goles", "Boton de goles");
    		Intent i=new Intent(this,MultimediaGolesDeLaFechaActivity.class);
    		i.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
    		startActivity(i);
    	}
    	if(v.getId()==R.id.btn_calendario){
    		Log.v("Boton de calendario", "Boton de calendario");
    		Intent i=new Intent(this,CalendarActivity.class);
    		i.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
    		startActivity(i);
    	}
    	if(v.getId()==R.id.btn_fotos){
    		Log.v("Boton de galeria", "Boton de galeria");
    		Intent i=new Intent(this,GaleriaActivity.class);
    		i.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
    		startActivity(i);
    	}
    	if(v.getId()==R.id.btn_redes_sociales){
    		Log.v("Boton de redes sociales", "Boton de redes sociales");
    		Intent i=new Intent(this,TweetsActivity.class);
    		i.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
    		startActivity(i);
    	}
    	if(v.getId()==R.id.btn_marcador){
    		Log.v("Boton de marcador claro", "Boton de marcador claro");
    		Intent i=new Intent(this,MarcadorClaroActivity.class);
    		i.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
    		startActivity(i);
    	}
    	if(v.getId()==R.id.btn_claro_tv){
    		Log.v("Boton de claro tv", "Boton de claro tv");
    		Intent i=new Intent(this,ClaroTvActivity.class);
    		i.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
    		startActivity(i);
    	}
    	else
    	{  
    		
    	}
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		this.getSherlock().getMenuInflater().inflate(R.menu.main_activity_menu, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item.getItemId() == R.id.menu_toggle_drawer) {
			this.getSlidingMenu().toggle();
		}
		if (item.getItemId() == android.R.id.home) {

		}
		return true;
	}


	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
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
