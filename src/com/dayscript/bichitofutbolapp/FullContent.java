package com.dayscript.bichitofutbolapp;

import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.adsdk.sdk.Ad;
import com.adsdk.sdk.AdListener;
import com.adsdk.sdk.banner.AdView;
import com.dayscript.bichitofutbolapp.async.StaticAsyncTask;
import com.dayscript.bichitofutbolapp.http.ClaroService;
import com.dayscript.bichitofutbolapp.persistence.entity.BaseEntity;
import com.dayscript.bichitofutbolapp.persistence.entity.NewsItem;
import com.dayscript.bichitofutbolapp.util.UIHelper;
import com.dayscript.bichitofutbolapp.utils.ImageUtils;
import com.dayscript.bichitofutbolapp.utils.UIUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FullContent extends SherlockFragmentActivity implements AdListener{
	public static final String RECEIVE_FULL_ARTICLE_ACTION="ec.com.dayscript.bichitofutbol.RECEIVEFULLARTICLE";
	public static final String TAG="Full content";
	BaseEntity entity;
	NewsItem itemDetail;
	ImageView imagenNoticiaFull;
	TextView txtTituloFull;
	TextView txtFechaFull;
	WebView txtTextoFull;
	private AdView mAdView;
	@Override
	protected void onCreate(Bundle arg0) {
		
		super.onCreate(arg0);
		setContentView(R.layout.fullcontent);
		UIUtils.stylizeActionBar(this);
		Intent callerIntent=getIntent();
		if(!callerIntent.hasExtra("entity"))
		{
			finish();
		}
		entity=(BaseEntity) callerIntent.getSerializableExtra("entity");
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		loadInfo();
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
		findViewById(R.id.relative_fullcontent_layout).setVisibility(View.GONE);
		findViewById(R.id.fullcontent_layout).setVisibility(View.GONE);
		findViewById(R.id.retry_layout).setVisibility(View.GONE);
		findViewById(R.id.loading_layout).setVisibility(View.VISIBLE);
	}
	
	private void showLayout() {
		findViewById(R.id.relative_fullcontent_layout).setVisibility(View.VISIBLE);
		findViewById(R.id.fullcontent_layout).setVisibility(View.VISIBLE);
		findViewById(R.id.retry_layout).setVisibility(View.GONE);
		findViewById(R.id.loading_layout).setVisibility(View.GONE);
	}

	private void showRetry() {
		findViewById(R.id.relative_fullcontent_layout).setVisibility(View.GONE);
		findViewById(R.id.fullcontent_layout).setVisibility(View.GONE);
		findViewById(R.id.retry_layout).setVisibility(View.VISIBLE);
		findViewById(R.id.loading_layout).setVisibility(View.GONE);
	}
	
	public void fetchData() {
		NewsDetailFetcherTask task = new NewsDetailFetcherTask(this, entity);
		task.execute("");
	}

	public void returnFromTask(JSONObject result) {
		JSONObject obj;
		try {
			obj = result.getJSONObject("data");
			imagenNoticiaFull = (ImageView) this.findViewById(R.id.main_image);
			txtTextoFull = (WebView) this.findViewById(R.id.text_fullcontent);
			txtTituloFull = (TextView) this.findViewById(R.id.newsTitle);
			txtFechaFull = (TextView) this.findViewById(R.id.newsDate);
			
			itemDetail = new NewsItem();
			itemDetail.fillEntityFromJson(obj);
			ImageLoader imageLoader=ImageUtils.getImageLoader(this);
			String url="";
			url=itemDetail.getImg();
			Log.v("Loader", "no null");
			
			imageLoader.displayImage(url, (ImageView)this.findViewById(R.id.main_image),ImageUtils.getDefaultImageOptions());
			txtTextoFull.loadData("<body style=\"background:#4a4a4a\">" + itemDetail.getFullText() +"</body>","text/html; charset=utf-8", "UTF-8");
			txtTituloFull.setText(itemDetail.getTitle());
			txtFechaFull.setText(itemDetail.getFechaCreacion());
			
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
	
	
	static class NewsDetailFetcherTask extends StaticAsyncTask {
		FullContent act;
		BaseEntity base;
		
		public NewsDetailFetcherTask(Activity activity, BaseEntity entity) {
			super(activity);
			act =(FullContent)activity;
			base = entity;
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
			int id =0;
			JSONObject ret = null;
			try {
				id = ((NewsItem)base).getId();
				ret = service.getNewsDetail(id);
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
