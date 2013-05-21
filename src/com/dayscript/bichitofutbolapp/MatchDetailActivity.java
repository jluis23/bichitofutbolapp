package com.dayscript.bichitofutbolapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.LauncherActivity.ListItem;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.adsdk.sdk.Ad;
import com.adsdk.sdk.AdListener;
import com.adsdk.sdk.banner.AdView;
import com.dayscript.bichitofutbolapp.FullContent.NewsDetailFetcherTask;
import com.dayscript.bichitofutbolapp.ImageGridActivity.ImageAdapter;
import com.dayscript.bichitofutbolapp.async.StaticAsyncTask;
import com.dayscript.bichitofutbolapp.http.ClaroService;
import com.dayscript.bichitofutbolapp.persistence.entity.AlbumEntity;
import com.dayscript.bichitofutbolapp.persistence.entity.BaseEntity;
import com.dayscript.bichitofutbolapp.persistence.entity.FechaEntity;
import com.dayscript.bichitofutbolapp.persistence.entity.MatchResult;
import com.dayscript.bichitofutbolapp.persistence.entity.MediaEntity;
import com.dayscript.bichitofutbolapp.persistence.entity.MinuteToMinuteItem;
import com.dayscript.bichitofutbolapp.persistence.entity.NewsItem;
import com.dayscript.bichitofutbolapp.ui.adapter.GalleryTypeSpinnerItem;
import com.dayscript.bichitofutbolapp.ui.adapter.MinuteToMinuteListItem;
import com.dayscript.bichitofutbolapp.ui.fragment.AlbumListFragment;
import com.dayscript.bichitofutbolapp.utils.ImageUtils;
import com.dayscript.bichitofutbolapp.utils.UIUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


public class MatchDetailActivity extends SherlockFragmentActivity implements AdListener {
	public static final String TAG = "BICHITOFUTBOL_MATCH_ACTIVITY";
	public static final String DIALOG_TAG = "DIALOG";
	
	
	String[] categorias={"Minuto a minuto","Fotos"};
	String[] tipos_album ={"minutoaminuto","fotos"};
	BaseEntity entity;
	ArrayList<BaseEntity> mListItems;
	ArrayList<BaseEntity> mListFotos;
	private AdView mAdView;
	
	ArrayList<String> imageUrls = new ArrayList<String>();
	String id="1";
	DisplayImageOptions options;
	protected AbsListView listView;
	Spinner categoriaSpinner;
	ListView minutoaminuito;
	protected AbsListView galeria;
	TextView mensaje ;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.match_detail);
		minutoaminuito = (ListView) this.findViewById(R.id.listMinutoMinuto);
		galeria = (GridView) this.findViewById(R.id.gridviewmatch);
		mensaje = (TextView) this.findViewById(R.id.txt_match_detaiL_no_datos);
		mListItems = new ArrayList<BaseEntity>();
		mListFotos = new ArrayList<BaseEntity>();
		
		Intent callerIntent = getIntent();
		if (!callerIntent.hasExtra("entity")) {
			finish();
		}
		UIUtils.stylizeActionBar(this);
		entity = (BaseEntity) callerIntent.getSerializableExtra("entity");
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		agregarCategoriasAlbumASpinner();
		agregarEventoCategoriasAlbum();
		loadInfo();
		findViewById(R.id.retry_button).setOnClickListener(
				new View.OnClickListener() {

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
		categoriaSpinner =(Spinner)this.findViewById(R.id.fichapartido_fotos_spinner);
		ArrayList<String>cats=new ArrayList<String>(Arrays.asList(categorias));
	    ArrayAdapter<String> adapter=new GalleryTypeSpinnerItem(this,cats);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    categoriaSpinner.setAdapter(adapter);
	}
	
	protected void agregarEventoCategoriasAlbum(){
		categoriaSpinner =(Spinner)this.findViewById(R.id.fichapartido_fotos_spinner);
		categoriaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				mensaje.setVisibility(View.GONE);
				if(arg2==0){
					minutoaminuito.setVisibility(View.VISIBLE);
					galeria.setVisibility(View.GONE);
					if(mListItems.size()>0){
						Log.v("Tamaño items", String.valueOf(mListItems.size()));
						mensaje.setVisibility(View.GONE);
					}else{
						Log.v("Tamaño items", String.valueOf(mListItems.size()));
						mensaje.setVisibility(View.VISIBLE);
					}
				}else if(arg2==1){
					minutoaminuito.setVisibility(View.GONE);
					galeria.setVisibility(View.VISIBLE);
					if(imageUrls.size()>0){
						Log.v("Tamaño imagenes", String.valueOf(imageUrls.size()));
						mensaje.setVisibility(View.GONE);
					}else{
						Log.v("Tamaño imagenes", String.valueOf(imageUrls.size()));
						mensaje.setVisibility(View.VISIBLE);
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		
		});
	} 

	private void loadInfo() {
		/*findViewById(R.id.fullcontent_layout).setVisibility(View.GONE);
		findViewById(R.id.retry_layout).setVisibility(View.GONE);
		findViewById(R.id.loading_layout).setVisibility(View.VISIBLE);*/
		fetchData();
	}

	public void fetchData() {
		MatchDetailFetcherTask task = new MatchDetailFetcherTask(this,entity);
		task.execute("");
	}

	public void returnFromTask(JSONObject result) {
		JSONObject obj;
		try {
			obj = result.getJSONObject("data");
			if (entity instanceof MatchResult) {
				Log.v(TAG, "Iniciar armando la interfaz");
				TextView tv = (TextView) findViewById(R.id.equipo1);
				tv.setText(obj.optString("team1"));
				tv = (TextView) findViewById(R.id.equipo2);
				tv.setText(obj.optString("team2"));
				tv = (TextView) findViewById(R.id.golesEquipo1 );
				if(tv!=null && obj.optString("goalsTeam1")!=null && obj.optString("goalsTeam2")!="null"){
					tv.setText(obj.optString("goalsTeam1"));
				}
				tv = (TextView) findViewById(R.id.golesEquipo2);
				if(tv!=null && obj.optString("goalsTeam2")!=null && obj.optString("goalsTeam2")!="null"){
					tv.setText(obj.optString("goalsTeam2"));
				}
				String fecha=obj.optString("date") + "-" + obj.optString("month") + " " + obj.optString("time");
				tv = (TextView) findViewById(R.id.fecha);
				tv.setText(fecha);
				tv = (TextView) findViewById(R.id.estado);
				String estado = obj.optString("estado");
				if(estado.equalsIgnoreCase("medio_tiempo")==true){
					tv.setText("Medio Tiempo");
				}else {
					tv.setText(obj.optString("estado"));
				}
				tv = (TextView) findViewById(R.id.stadium);
				tv.setText(obj.optString("stadium"));
				ImageView img = (ImageView) findViewById(R.id.logoEquipo1);
				ImageLoader imageLoader = ImageUtils
						.getImageLoader(MatchDetailActivity.this);
				imageLoader.displayImage(obj.optString("logoTeam1"), img,
						ImageUtils.getDefaultImageOptions());
				ImageView img2 = (ImageView) findViewById(R.id.logoEquipo2);
				imageLoader = ImageUtils
						.getImageLoader(MatchDetailActivity.this);
				imageLoader.displayImage(obj.optString("logoTeam2"), img2,
						ImageUtils.getDefaultImageOptions());
				JSONArray detalles=obj.optJSONArray("detalles");
				if(detalles!=null)
				{
					try {
						mListItems=new ArrayList<BaseEntity>();
						for(int i=0;i<detalles.length();i++)
						{
							BaseEntity tmpObj=(BaseEntity) MinuteToMinuteItem.class.newInstance();
							tmpObj.fillEntityFromJson(detalles.optJSONObject(i));
							mListItems.add(tmpObj);
						}
					} catch (java.lang.InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						
						e.printStackTrace();
					}
				}
				if(mListItems!=null)
				{
					if(mListItems.size()>0){
						mensaje.setVisibility(View.GONE);
					}
					ListView lv=(ListView)findViewById(R.id.listMinutoMinuto);
					lv.setAdapter(new MinuteToMinuteListItem(MatchDetailActivity.this, mListItems));
				}
				
				JSONArray fotos = obj.optJSONArray("fotos");
				if(fotos!=null){
					try {
						mListFotos = new ArrayList<BaseEntity>();
						for(int i=0;i<fotos.length();i++)
						{
							BaseEntity tmpObj=(BaseEntity) MediaEntity.class.newInstance();
							tmpObj.fillEntityFromJson(fotos.optJSONObject(i));
							mListFotos.add(tmpObj);
							
							MediaEntity itmAlbum = new MediaEntity();
							itmAlbum.fillEntityFromJson(fotos.optJSONObject(i));
							imageUrls.add(itmAlbum.getUrl());
						}
						cargarImagenes();
					} catch (java.lang.InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void cargarImagenes(){
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.ic_stub)
		.showImageForEmptyUri(R.drawable.ic_empty)
		.showImageOnFail(R.drawable.ic_error)
		.cacheInMemory()
		.cacheOnDisc()
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		
		/*AlbumEntity album = (AlbumEntity) entity;
		TextView titulo = (TextView) findViewById(R.id.gallery_grid_titulo_album);
		titulo.setText(album.getTitulo());
		
		TextView descripcion = (TextView) findViewById(R.id.gallery_grid_descripcion_album);
		descripcion.setText(album.getDescripcion());
		
		TextView numeroFotos = (TextView) findViewById(R.id.gallery_grid_album_numeroFotos);
		numeroFotos.setText(album.getNumeroFotos()+" "+"Fotos");*/
		
		listView = (GridView) findViewById(R.id.gridviewmatch);
		((GridView) listView).setAdapter(new ImageAdapter());
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				startImagePagerActivity(position);
			}
		});
	}
	
	private void startImagePagerActivity(int position) {
		Intent intent = new Intent(this, ImagePagerActivity.class);
		intent.putExtra("imagenes", imageUrls);
		intent.putExtra("posicion", position);
		startActivity(intent);
	}
	
	public class ImageAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return imageUrls.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ImageView imageView;
			if (convertView == null) {
				imageView = (ImageView) getLayoutInflater().inflate(R.layout.gallery_item_grid_image, parent, false);
			} else {
				imageView = (ImageView) convertView;
			}
			ImageLoader imageLoader=ImageUtils.getImageLoader(getBaseContext());
			imageLoader.init(ImageLoaderConfiguration.createDefault(getBaseContext()));
			imageLoader.displayImage(imageUrls.get(position), imageView, options);
			return imageView;
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
			//this.showRetry();
		}
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		if (item.getItemId() == R.id.refresh_content) {
			loadInfo();
			Log.v(TAG,"recupera informacion");
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPause() {

		super.onPause();
		
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.getSherlock().getMenuInflater().inflate(R.menu.main_activity_news, menu);
		return true;
	}
	
	/*class FullContentNetworkHandler extends DefaultNetworkHandler {

		public FullContentNetworkHandler(Context ctx, String intentFilter,
				LocalBroadcastManager lbm) {
			super(ctx, intentFilter, lbm);

		}

		@Override
		public void run() {

			Log.v(TAG, "Obteniendo detalle de partido " + entity);
			ServicesClient client = new ServicesClient();
			if (entity instanceof MatchResult) {
				client.getMatchDetail(this,
						String.valueOf(((MatchResult) entity).getId()));
			}
		}

	}*/
	
	static class MatchDetailFetcherTask extends StaticAsyncTask {
		MatchDetailActivity act;
		BaseEntity base;
		
		public MatchDetailFetcherTask(Activity activity, BaseEntity entity) {
			super(activity);
			act = (MatchDetailActivity)activity;
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
					//act.showRetry();
					Toast t=Toast.makeText(act,result.optString("mensaje","Error de conexion"),Toast.LENGTH_LONG);
					t.setGravity(Gravity.BOTTOM, 50, 50);
					t.show();
				}
			} else {
				//act.showRetry();
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
				id = ((MatchResult)base).getId();
				ret = service.getMatchDetail(Integer.toString(id));
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
