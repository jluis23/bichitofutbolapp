package com.dayscript.bichitofutbolapp.ui.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

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
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.adsdk.sdk.banner.AdView;
import com.dayscript.bichitofutbolapp.NewsActivity.DateDialogFragmentListener;
import com.dayscript.bichitofutbolapp.R;
import com.dayscript.bichitofutbolapp.async.StaticAsyncTask;
import com.dayscript.bichitofutbolapp.http.ClaroService;
import com.dayscript.bichitofutbolapp.persistence.entity.BaseEntity;
import com.dayscript.bichitofutbolapp.persistence.entity.CategoriaEntity;
import com.dayscript.bichitofutbolapp.ui.adapter.CampeonatoSpinnerItem;
import com.dayscript.bichitofutbolapp.ui.adapter.CategoriaNoticiaSpinnerItem;


public class NewsFragment extends SherlockFragment  {
	public static String TAG="NEWS_FRAGMENT_TAG";
	ArrayList<BaseEntity> ListaCategorias;
	Spinner categoriaSpinner;
	String  fecha_buscar_noticias= "2013-04-18";
	ImageButton imgButton;
	ImageButton imgButtonVerEdiciones;
	int  categoria_buscar_Id= 7;
	DateDialogFragment frag;
	Calendar now;
	TextView txtDate;
	int mMonth;
	int mDay;
	int mYear;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		 View v = inflater.inflate(R.layout.news_fragment_layout, container,false);
		 ListaCategorias = new ArrayList<BaseEntity>();
		 now = Calendar.getInstance();
	     imgButton = (ImageButton)v.findViewById(R.id.busqueda_noticias_date_button);
	     txtDate = (TextView)v.findViewById(R.id.busqueda_noticias_date_text);
		 imgButton.setOnClickListener(new View.OnClickListener() {
	        	public void onClick(View v) {
	        		showDialog();	
	        	}
	     });
		 
		 imgButtonVerEdiciones = (ImageButton)v.findViewById(R.id.busqueda_noticias_date_img_txt_ver);
		 imgButtonVerEdiciones.setOnClickListener(new View.OnClickListener() {
	        	public void onClick(View v) {
	        		showDialog();	
	        	}
	     });
		 this.mYear = now.get(Calendar.YEAR);
		 this.mMonth = now.get(Calendar.MONTH)+1;
		 this.mDay = now.get(Calendar.DAY_OF_MONTH);
		 fecha_buscar_noticias = String.valueOf(mYear)+"-"+String.valueOf(mMonth)+"-"+String.valueOf(mDay);
		 Log.v(TAG, fecha_buscar_noticias);
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
	 public void showDialog() {
	    	FragmentTransaction ft = getFragmentManager().beginTransaction(); //get the fragment
	    	frag = DateDialogFragment.newInstance(this.getActivity(), new DateDialogFragmentListener(){
	    		public void updateChangedDate(int year, int month, int day){
	    			txtDate.setText(String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(day));
	    			fecha_buscar_noticias = String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(day);
	    			Log.v("fecha_busqueda", fecha_buscar_noticias);
	    			ListaNoticiasFragment res=new ListaNoticiasFragment();
					Bundle args=new Bundle();
					args.putString("fecha", String.valueOf(fecha_buscar_noticias));
					args.putString("categoria", String.valueOf(categoria_buscar_Id));
					res.setArguments(args);
					FragmentTransaction ft=getChildFragmentManager().beginTransaction();
					ft.replace(R.id.container_list_news_fragment, res).commit();
	    		}
	    	}, now);
	    	frag.show(ft, "DateDialogFragment");
	 }

	public void fetchData() {
		showLoading();
		NewsFetcherTask task = new NewsFetcherTask(this);
		task.execute("");
	} 
	

	protected void agregarCategoriasASpinner(){
		
		categoriaSpinner = (Spinner) this.getActivity().findViewById(R.id.busqueda_noticias_categorias_spinner);
		CategoriaNoticiaSpinnerItem adapter = new CategoriaNoticiaSpinnerItem(getActivity(),ListaCategorias );
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		categoriaSpinner.setAdapter(adapter);
	}
	
	protected void agregarEventoCategoria(){
		categoriaSpinner = (Spinner)this.getActivity().findViewById(R.id.busqueda_noticias_categorias_spinner);
		categoriaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				CategoriaEntity categoria;
				categoria = (CategoriaEntity) arg0.getAdapter().getItem(arg2);
				categoria_buscar_Id = categoria.getId();
				ListaNoticiasFragment res=new ListaNoticiasFragment();
				Bundle args=new Bundle();
				args.putString("fecha", String.valueOf(fecha_buscar_noticias));
				args.putString("categoria", String.valueOf(categoria_buscar_Id));
				res.setArguments(args);
				FragmentTransaction ft=getChildFragmentManager().beginTransaction();
				ft.replace(R.id.container_list_news_fragment, res).commit();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		
		});
	} 
	
	public void returnFromTask(JSONObject result) {
		JSONArray categorias;
		try {
			
			categorias = result.getJSONArray("data");
			ListaCategorias = new ArrayList<BaseEntity>();
			
			for (int i = 0; i < categorias.length(); i++)
			{
				CategoriaEntity itmCategoria = new CategoriaEntity();
				itmCategoria.fillEntityFromJson(categorias.optJSONObject(i));
				ListaCategorias.add(itmCategoria);
			}
			agregarCategoriasASpinner();
			agregarEventoCategoria();
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
			this.showRetry();
		}
	}
	
	static class NewsFetcherTask extends StaticAsyncTask {
		NewsFragment fragment;
		
		public NewsFetcherTask(Activity activity) {
			super(activity);
		}
		
		public NewsFetcherTask(NewsFragment fr) {
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
					fragment.showRetry();
					Toast t=Toast.makeText(this.fragment.getActivity(),result.optString("mensaje","Error de conexion"),Toast.LENGTH_LONG);
					t.setGravity(Gravity.BOTTOM, 50, 50);
					t.show();
				}
			} else {
				fragment.showRetry();
				Toast t=Toast.makeText(this.fragment.getActivity(),R.string.network_error,Toast.LENGTH_LONG);
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
				ret = service.getCategoriesList();
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