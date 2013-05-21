package com.dayscript.bichitofutbolapp;

import java.io.IOException;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.adsdk.sdk.banner.AdView;
import com.dayscript.bichitofutbolapp.FullContent.NewsDetailFetcherTask;
import com.dayscript.bichitofutbolapp.async.StaticAsyncTask;
import com.dayscript.bichitofutbolapp.http.ClaroService;
import com.dayscript.bichitofutbolapp.persistence.entity.BaseEntity;
import com.dayscript.bichitofutbolapp.persistence.entity.NewsItem;
import com.dayscript.bichitofutbolapp.util.UIHelper;
import com.dayscript.bichitofutbolapp.utils.ImageUtils;
import com.dayscript.bichitofutbolapp.utils.UIUtils;
import com.nostra13.universalimageloader.core.ImageLoader;



public class TablaDeGoleadoresActivity extends SherlockFragmentActivity {
	public static final String TAG="Full content";
	WebView tableContent;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.positions_table);
		UIUtils.stylizeActionBar(this);
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		loadInfo();
		findViewById(R.id.retry_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				loadInfo();
			}
		});
	}

	private void loadInfo() {
		showLoading();
		fetchData();
	}


	public boolean onCreateOptionsMenu(Menu menu) {
		this.getSherlock().getMenuInflater().inflate(R.menu.news_detail_menu, menu);
		return true;
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
			Log.v("Tabla de posiciones", tablaPosiciones);
			tableContent = (WebView) this.findViewById(R.id.table_content);
			tableContent.loadData("<body style=\"background:#4a4a4a\">" + tablaPosiciones +"</body>","text/html; charset=utf-8", "UTF-8");
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
		TablaDeGoleadoresActivity act;
		public PositionTableFetcherTask(Activity activity) {
			super(activity);
			act =(TablaDeGoleadoresActivity)activity;
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
				ret = service.getTablaGoleadores();
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
	
}
