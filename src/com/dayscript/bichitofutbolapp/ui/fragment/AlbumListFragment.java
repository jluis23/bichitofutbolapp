package com.dayscript.bichitofutbolapp.ui.fragment;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import com.dayscript.bichitofutbolapp.Constants;
import com.dayscript.bichitofutbolapp.Constants.Extra;
import com.dayscript.bichitofutbolapp.ImageGridActivity;
import com.dayscript.bichitofutbolapp.R;
import com.dayscript.bichitofutbolapp.async.StaticAsyncTask;
import com.dayscript.bichitofutbolapp.http.ClaroService;
import com.dayscript.bichitofutbolapp.persistence.entity.AlbumEntity;
import com.dayscript.bichitofutbolapp.persistence.entity.BaseEntity;
import com.dayscript.bichitofutbolapp.persistence.entity.NewsItem;
import com.dayscript.bichitofutbolapp.ui.adapter.AlbumListItem;

public class AlbumListFragment extends SherlockListFragment {
	public static final String TAG = "BICHITOFUTBOL_ALBUMLISTFRAGMENT";
	ArrayList<BaseEntity> mListAlbums;
	String id="1";
	String tipo_album = "campeonato";
	ArrayList<String> ListaFotos;
	
	@Override
	public void onResume() {
		super.onResume();
		setEmptyText(getResources().getString(R.string.retry));
		getListView().getEmptyView().setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onRefresh();
			}
		});
		getListView().setDividerHeight(0);
	
		Activity act = this.getActivity();
		try {
			if (act != null) {
				if(mListAlbums!=null)
				{
					setListAdapter(new AlbumListItem(this.getSherlockActivity(), mListAlbums));
					showLayout();
				}
				else{
					showLoading();
					fetchData();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onRefresh(){
		showLoading();
		fetchData();
	}
	public void fetchData() {
		this.tipo_album = this.getArguments().getString("tipo_album");
		GalleryFetcherTask task = new GalleryFetcherTask(this);
		task.execute("");
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		
		super.onListItemClick(l, v, position, id);
		Log.v(TAG,"Click en item " + position);
		AlbumEntity album = (AlbumEntity) mListAlbums.get(position);
		this.id = String.valueOf(album.getId());
		Log.v(TAG,"El id del album es: " + this.id);
		Intent intent = new Intent(getSherlockActivity(), ImageGridActivity.class);
		intent.putExtra(Extra.IMAGES, Constants.IMAGES);
		intent.putExtra("idAlbum",this.id);
		intent.putExtra("entity",album);
		startActivity(intent);
	}
	
	private void showLoading() {
		Activity act = this.getActivity();
		if (act != null) {
			setListShown(false);
		}
	}
	private void showLayout() {
		Activity act = this.getActivity();
		if (act != null) {
		setListShown(true);
		}
	}
	private void showRetry() {
		Activity act = this.getActivity();
		if (act != null) {
			getListView().setAdapter(null);
			setListShown(true);
			
		}
	}

	public void returnFromTask(JSONObject result) {
		JSONArray albums;
		try {
			albums = result.getJSONArray("data");
			mListAlbums = new ArrayList<BaseEntity>();
			
			for (int i = 0; i < albums.length(); i++)
			{
				AlbumEntity itmAlbum = new AlbumEntity();
				itmAlbum.fillEntityFromJson(albums.optJSONObject(i));
				mListAlbums.add(itmAlbum);
				Log.v("ALBUM", itmAlbum.getTitulo());
			}
			setListAdapter(new AlbumListItem(this.getSherlockActivity(), mListAlbums));
			showLayout();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void callback(JSONObject result) {
		boolean success=false;
		Activity act = this.getActivity();
		if (!this.isVisible()) {
			return;
		}
		try {
			if (act != null) {
				success = result.optBoolean("success");
				JSONObject data = result.optJSONObject("data");
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
	
	static class GalleryFetcherTask extends StaticAsyncTask {
		AlbumListFragment fragment;
		
		public GalleryFetcherTask(Activity activity) {
			super(activity);
		}
		
		public GalleryFetcherTask(AlbumListFragment fr) {
			this(fr.getActivity());
			fragment = fr;
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
					Toast t=Toast.makeText(this.fragment.getActivity(),result.optString("mensaje","Error de conexion"),Toast.LENGTH_LONG);
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
			ClaroService service = ClaroService.getInstance(activity);
			this.isDirty = true;
			JSONObject ret = null;
			try {
				ret = service.getListAlbumsXType(fragment.tipo_album);
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
