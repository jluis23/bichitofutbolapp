/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.dayscript.bichitofutbolapp;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.dayscript.bichitofutbolapp.async.StaticAsyncTask;
import com.dayscript.bichitofutbolapp.http.ClaroService;
import com.dayscript.bichitofutbolapp.persistence.entity.AlbumEntity;
import com.dayscript.bichitofutbolapp.persistence.entity.BaseEntity;
import com.dayscript.bichitofutbolapp.persistence.entity.MediaEntity;
import com.dayscript.bichitofutbolapp.utils.ImageUtils;
import com.dayscript.bichitofutbolapp.utils.UIUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class ImageGridActivity extends SherlockFragmentActivity{

	ArrayList<String> imageUrls;
	String id="1";
	DisplayImageOptions options;
	BaseEntity entity;
	protected AbsListView listView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallery_image_grid);
		Bundle bundle = getIntent().getExtras();
		this.id = bundle.getString("idAlbum");
		Intent callerIntent=getIntent();
		if(!callerIntent.hasExtra("entity"))
		{
			finish();
		}
		entity=(BaseEntity) callerIntent.getSerializableExtra("entity");
		UIUtils.stylizeActionBar(this);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		loadInfo();
		//imageUrls = bundle.getStringArray(Extra.IMAGES);
	}
	
	private void loadInfo() {
		fetchData();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==android.R.id.home)
		{
			finish();
		}
		return super.onOptionsItemSelected(item);
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
		
		AlbumEntity album = (AlbumEntity) entity;
		TextView titulo = (TextView) findViewById(R.id.gallery_grid_titulo_album);
		titulo.setText(album.getTitulo());
		
		TextView descripcion = (TextView) findViewById(R.id.gallery_grid_descripcion_album);
		descripcion.setText(album.getDescripcion());
		
		TextView numeroFotos = (TextView) findViewById(R.id.gallery_grid_album_numeroFotos);
		numeroFotos.setText(album.getNumeroFotos()+" "+"Fotos");
		
		listView = (GridView) findViewById(R.id.gridview);
		((GridView) listView).setAdapter(new ImageAdapter());
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				startImagePagerActivity(position);
			}
		});
	}
	public void fetchData() {
		GalleryContentFetcherTask task = new GalleryContentFetcherTask(this, entity);
		task.execute("");
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
	
	public void returnFromTask(JSONObject result) {
		JSONArray albumUrls;
		imageUrls = new ArrayList<String>();
		try {
			albumUrls = result.getJSONArray("data");
			Log.v("ALBUM- size", String.valueOf(albumUrls.length()));
			for (int i = 0; i < albumUrls.length(); i++)
			{
				MediaEntity itmAlbum = new MediaEntity();
				itmAlbum.fillEntityFromJson(albumUrls.optJSONObject(i));
				Log.v("Media Items", itmAlbum.toString() );
				imageUrls.add(itmAlbum.getUrl());
				Log.v("ALBUM-URL", itmAlbum.getUrl());
			}
			cargarImagenes();
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
					Log.v("Galeria item","success:true");
					try {
						returnFromTask(result);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					Log.v("Galeria item","success:false");
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			//this.showRetry();
		}
	}
	
	static class GalleryContentFetcherTask extends StaticAsyncTask {
		ImageGridActivity fragment;
		BaseEntity base;
		
		public GalleryContentFetcherTask(Activity activity, BaseEntity entity) {
			super(activity);
			fragment =(ImageGridActivity)activity;
			base = entity;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			boolean success;
			this.isFinished = true;
			if (result != null) {
				success = result.optBoolean("success");
				if (success == true) {
					fragment.callback(result);
				} else {
					//fragment.showRetry();
					Toast t=Toast.makeText(fragment,result.optString("mensaje","Error de conexion"),Toast.LENGTH_LONG);
					t.setGravity(Gravity.BOTTOM, 50, 50);
					t.show();
				}
			} else {
				//fragment.showRetry();
				//Toast t=Toast.makeText(this.fragment.getActivity(),R.string.network_error,Toast.LENGTH_LONG);
				//t.setGravity(Gravity.BOTTOM, 50, 50);
				//t.show();
			}
			super.onPostExecute(result);
		}

		@Override
		protected JSONObject doInBackground(String... arg0) {
			ClaroService service = ClaroService.getInstance(fragment);
			this.isDirty = true;
			JSONObject ret = null;
			try {
				Log.v("Id del album", String.valueOf(fragment.id));
				ret = service.getContenidoAlbum(fragment.id);
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