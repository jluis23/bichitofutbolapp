package com.dayscript.bichitofutbolapp.ui.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.dayscript.bichitofutbolapp.R;
import com.dayscript.bichitofutbolapp.async.StaticAsyncTask;
import com.dayscript.bichitofutbolapp.http.ClaroService;
import com.dayscript.bichitofutbolapp.persistence.entity.AlbumEntity;
import com.dayscript.bichitofutbolapp.persistence.entity.BaseEntity;
import com.dayscript.bichitofutbolapp.persistence.entity.CategoriaEntity;
import com.dayscript.bichitofutbolapp.ui.adapter.GalleryTypeSpinnerItem;


public class GalleryFragment extends SherlockFragment  {
	public static String TAG="NEWS_FRAGMENT_TAG";
	String[] categorias={"Campeonato Nacional","Seleccion Nacional"};
	String[] tipos_album ={"campeonato","seleccion"};
	ArrayList<BaseEntity> ListaAlbums;
	String tipo_album = "campeonato";
	Spinner categoriaSpinner;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		 View v = inflater.inflate(R.layout.gallery_fragment_layout, container,false);
		 ListaAlbums = new ArrayList<BaseEntity>();
		 fetchData();
	     return v;		
	}
	
	private void showLoading() {
		Activity act = this.getActivity();
		if (act != null) {
			//act.findViewById(R.id.busqueda_noticias_date_button).setVisibility(View.GONE);
		}
	}

	private void showLayout() {
		Activity act = this.getActivity();
		if (act != null) {
			//act.findViewById(R.id.busqueda_noticias_layout).setVisibility(View.VISIBLE);
		}
	}

	private void showRetry() {
		Activity act = this.getActivity();
		if (act != null) {
			act.findViewById(R.id.busqueda_noticias_layout).setVisibility(View.GONE);
		}
	}
	public void onRefresh(){
		fetchData();
	}

	public void fetchData() {
		showLoading();
		GalleryFetcherTask task = new GalleryFetcherTask(this);
		task.execute("");
	} 
	

	protected void agregarCategoriasAlbumASpinner(){
		categoriaSpinner =(Spinner)this.getActivity().findViewById(R.id.tipo_albums);
		ArrayList<String>cats=new ArrayList<String>(Arrays.asList(categorias));
	    ArrayAdapter<String> adapter=new GalleryTypeSpinnerItem(this.getActivity(),cats);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    categoriaSpinner.setAdapter(adapter);
	}
	
	protected void agregarEventoCategoriasAlbum(){
		categoriaSpinner = (Spinner)this.getActivity().findViewById(R.id.tipo_albums);
		categoriaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				
				AlbumListFragment res=new AlbumListFragment();
				String tipo_album ;
				tipo_album = tipos_album[arg2];
				Bundle args=new Bundle();
				Log.v("Tipo Album", tipo_album);
				args.putString("tipo_album",tipo_album);
				res.setArguments(args);
				FragmentTransaction ft=getChildFragmentManager().beginTransaction();
				ft.replace(R.id.fragment_gallery_albums_container, res).commit();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		
		});
	} 
	
	public void returnFromTask(JSONObject result) {
		JSONArray albums;
		try {
			
			albums = result.getJSONArray("data");
			ListaAlbums = new ArrayList<BaseEntity>();
			
			for (int i = 0; i < albums.length(); i++)
			{
				AlbumEntity itmAlbum = new AlbumEntity();
				itmAlbum.fillEntityFromJson(albums.optJSONObject(i));
				ListaAlbums.add(itmAlbum);
				Log.v("ALBUM", itmAlbum.getTitulo());
			}
			agregarCategoriasAlbumASpinner();
			agregarEventoCategoriasAlbum();
			//showLayout();
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
		GalleryFragment fragment;
		
		public GalleryFetcherTask(Activity activity) {
			super(activity);
		}
		
		public GalleryFetcherTask(GalleryFragment fr) {
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