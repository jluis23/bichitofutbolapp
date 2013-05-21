package com.dayscript.bichitofutbolapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.adsdk.sdk.Ad;
import com.adsdk.sdk.AdListener;
import com.adsdk.sdk.banner.AdView;
import com.dayscript.bichitofutbolapp.FullContent.NewsDetailFetcherTask;
import com.dayscript.bichitofutbolapp.async.StaticAsyncTask;
import com.dayscript.bichitofutbolapp.http.ClaroService;
import com.dayscript.bichitofutbolapp.persistence.entity.BaseEntity;
import com.dayscript.bichitofutbolapp.persistence.entity.NewsItem;
import com.dayscript.bichitofutbolapp.ui.adapter.GalleryTypeSpinnerItem;
import com.dayscript.bichitofutbolapp.ui.fragment.NewsFragment;
import com.dayscript.bichitofutbolapp.util.UIHelper;
import com.dayscript.bichitofutbolapp.utils.ImageUtils;
import com.dayscript.bichitofutbolapp.utils.UIUtils;
import com.nostra13.universalimageloader.core.ImageLoader;



public class PositionsTableActivity extends SherlockFragmentActivity implements AdListener {
	public static final String TAG="Full content";
	WebView tableContent;
	WebView tableContentGoleadores;
	WebView tableContentAsistentes;
	String[] categorias={"Tabla de Posciones","Tabla de Goleadores","Tabla de Asistentes"};
	String[] tipos_album ={"posiciones","goleadores","asistentes"};
	Spinner tablaSpinner;
	private AdView mAdView;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.positions_table);
		UIUtils.stylizeActionBar(this);
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		loadInfo();
		agregarCategoriasAlbumASpinner();
		agregarEventoCategoriasAlbum();
		findViewById(R.id.retry_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				loadInfo();
			}
		});
		//getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		/*PREPARANDO ADS**************************************************************/
			mAdView = new AdView(this, getResources().getString(R.string.ads_base_path),
			getResources().getString(R.string.ads_token), true, true);
			mAdView.setAdListener(this);
			ViewGroup rootLayout=(ViewGroup)this.findViewById(R.id.adsdkContent12);
			rootLayout.addView(mAdView);
		/*PREPARANDO ADS**************************************************************/
	}
	
	protected void agregarCategoriasAlbumASpinner(){
		tablaSpinner =(Spinner)this.findViewById(R.id.tabla_posiciones_goleadores_spinner);
		ArrayList<String>cats=new ArrayList<String>(Arrays.asList(categorias));
	    ArrayAdapter<String> adapter=new GalleryTypeSpinnerItem(this,cats);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    tablaSpinner.setAdapter(adapter);
	}
	
	protected void agregarEventoCategoriasAlbum(){
		 tablaSpinner =(Spinner)this.findViewById(R.id.tabla_posiciones_goleadores_spinner);
		 tablaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				if(arg2==0){
					tableContent.setVisibility(View.VISIBLE);
					tableContentGoleadores.setVisibility(View.GONE);
					tableContentAsistentes.setVisibility(View.GONE);
				}else if(arg2==1){
					tableContent.setVisibility(View.GONE);
					tableContentGoleadores.setVisibility(View.VISIBLE);
					tableContentAsistentes.setVisibility(View.GONE);
				}else if(arg2==2){
					tableContent.setVisibility(View.GONE);
					tableContentGoleadores.setVisibility(View.GONE);
					tableContentAsistentes.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		
		});
	}

	private void loadInfo() {
		showLoading();
		fetchData();
	}


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
			loadInfo();
		}
		if (item.getItemId() == R.id.menu_home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}



	@Override
	protected void onPause() {
		
		super.onPause();
		//lbm.unregisterReceiver(receiver);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//lbm.registerReceiver(receiver, new IntentFilter(RECEIVE_FULL_ARTICLE_ACTION));
		
	}
	
	private void showLoading() {
		findViewById(R.id.layout_results).setVisibility(View.GONE);
		findViewById(R.id.positions_layout).setVisibility(View.GONE);
		findViewById(R.id.retry_layout).setVisibility(View.GONE);
		findViewById(R.id.loading_layout).setVisibility(View.VISIBLE);
	}
	
	private void showLayout() {
		findViewById(R.id.positions_layout).setVisibility(View.VISIBLE);
		findViewById(R.id.layout_results).setVisibility(View.VISIBLE);
		findViewById(R.id.retry_layout).setVisibility(View.GONE);
		findViewById(R.id.loading_layout).setVisibility(View.GONE);
	}

	private void showRetry() {
		findViewById(R.id.positions_layout).setVisibility(View.GONE);
		findViewById(R.id.layout_results).setVisibility(View.GONE);
		findViewById(R.id.retry_layout).setVisibility(View.VISIBLE);
		findViewById(R.id.loading_layout).setVisibility(View.GONE);
	}
	
	public void fetchData() {
		PositionTableFetcherTask task = new PositionTableFetcherTask(this);
		task.execute("");
	}

	public void returnFromTask(JSONObject result) {
		JSONObject obj;
		try {
			String tablaPosiciones = result.getString("data");
			String tablaGoleadores = result.getString("datag");
			String tablaAsistentes = result.getString("dataa");
			Log.v("Tabla de posiciones", tablaPosiciones);
			tableContent = (WebView) this.findViewById(R.id.table_content);			
			tableContent.loadData("<body style=\"background:#4a4a4a\">" + tablaPosiciones +"</body>","text/html; charset=utf-8", "UTF-8");
			tableContentGoleadores = (WebView) this.findViewById(R.id.table_content_goleadores);			
			tableContentGoleadores.loadData("<body style=\"background:#4a4a4a\">" + tablaGoleadores +"</body>","text/html; charset=utf-8", "UTF-8");
			tableContentAsistentes = (WebView) this.findViewById(R.id.table_content_asistentes);			
			tableContentAsistentes.loadData("<body style=\"background:#4a4a4a\">" + tablaAsistentes +"</body>","text/html; charset=utf-8", "UTF-8");
			showLayout();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void callback(JSONObject result) {
		boolean success=false;
		Activity act = this;
		try {
			if (act != null) {
				success = result.optBoolean("success");
				if (success == true) {
					Log.v(TAG,"success:true");
					try {
						returnFromTask(result);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					Log.v(TAG,"success:false");
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			this.showRetry();
		}
	}
	
	
	
	
	static class PositionTableFetcherTask extends StaticAsyncTask {
		PositionsTableActivity act;
		public PositionTableFetcherTask(Activity activity) {
			super(activity);
			act =(PositionsTableActivity)activity;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			boolean success;
			this.isFinished = true;
			if (result != null) {
				success = result.optBoolean("success");
				if (success == true) {
					Log.v(TAG,result.toString());
					act.callback(result);
				} else {
					act.showRetry();
					Toast t=Toast.makeText(act,result.optString("mensaje","Error de conexion"),Toast.LENGTH_LONG);
					t.setGravity(Gravity.BOTTOM, 50, 50);
					t.show();
				}
			} else {
				act.showRetry();
				Toast t=Toast.makeText(act,R.string.network_error,Toast.LENGTH_LONG);
				t.setGravity(Gravity.BOTTOM, 50, 50);
				t.show();
			}
			super.onPostExecute(result);
		}

		@Override
		protected JSONObject doInBackground(String... arg0) {
			ClaroService service = ClaroService.getInstance(activity);
			this.isDirty = true;
			JSONObject ret = null;
			try {
				ret = service.getPositionsGoleadoresTable();
			} catch (IOException e) {
				this.publishProgress(0);
				e.printStackTrace();
			} catch (JSONException e) {
				this.publishProgress(0);
				e.printStackTrace();
			}
			return ret;
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
