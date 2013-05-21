package com.dayscript.bichitofutbolapp.ui.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.adsdk.sdk.Ad;
import com.adsdk.sdk.AdListener;
import com.adsdk.sdk.banner.AdView;
import com.dayscript.bichitofutbolapp.FullContent;
import com.dayscript.bichitofutbolapp.NewsActivity.DateDialogFragmentListener;
import com.dayscript.bichitofutbolapp.R;
import com.dayscript.bichitofutbolapp.async.StaticAsyncTask;
import com.dayscript.bichitofutbolapp.http.ClaroService;
import com.dayscript.bichitofutbolapp.persistence.entity.BaseEntity;
import com.dayscript.bichitofutbolapp.persistence.entity.CategoriaEntity;
import com.dayscript.bichitofutbolapp.persistence.entity.FechaEntity;
import com.dayscript.bichitofutbolapp.persistence.entity.NewsItem;
import com.dayscript.bichitofutbolapp.ui.adapter.NewsListItem;
import com.dayscript.bichitofutbolapp.ui.adapter.TeamsListItem;
import com.dayscript.bichitofutbolapp.util.UIHelper;
import com.dayscript.bichitofutbolapp.utils.ImageUtils;
import com.nostra13.universalimageloader.core.ImageLoader;


public class ListaNoticiasFragment extends SherlockFragment{
	public static String TAG="LISTA_NOTICIAS";
	ArrayList<BaseEntity> mListItems;
	ArrayList<BaseEntity> mFirstListItem;
	ListView listNews;
	ImageView imagenNoticia;
	TextView txtTitulo;
	TextView txtTexto;
	NewsItem firstItem;
	Button retry;
	private AdView mAdView;
	DateDialogFragment frag;
	ImageButton imgButton;
    Calendar now;
    TextView txtDate;
	ArrayList<BaseEntity> ListCategorias;
	Spinner categoria;
	public String fechaBusqueda;
	String categoria_a_buscar = "7";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		 View v = inflater.inflate(R.layout.news_list, container,false);
		 v.findViewById(R.id.retry_button).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.v("On create", fechaBusqueda+"-"+categoria_a_buscar);
					showLoading();
					fetchData();
				}
		 });
		
		//Calendario
	 	now = Calendar.getInstance();
        imgButton = (ImageButton)v.findViewById(R.id.date_calendar_button);
        txtDate = (TextView)v.findViewById(R.id.date_txt);
        
        String mes = String.valueOf(3);
        String dia =  String.valueOf(26);
        String anio = String.valueOf(2013);
        fechaBusqueda =anio+"-"+mes+"-"+dia; 
        txtDate.setText(fechaBusqueda);
        imgButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		showDialog();	
        	}
        });

		//getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			/*PREPARANDO ADS**************************************************************/
				/*mAdView = new AdView(this.getActivity(), this.getActivity().getString(R.string.ads_base_path),
				this.getActivity().getResources().getString(R.string.ads_token), true, true);
				mAdView.setAdListener(this);
				ViewGroup rootLayout=(ViewGroup)this.getActivity().findViewById(R.id.adsdkContent12);
				rootLayout.addView(mAdView);*/
			/*PREPARANDO ADS**************************************************************/
	     return v;		
	}
	
	 public void showDialog() {
	    	FragmentTransaction ft = getFragmentManager().beginTransaction(); //get the fragment
	    	frag = DateDialogFragment.newInstance(this.getActivity(), new DateDialogFragmentListener(){
	    		public void updateChangedDate(int year, int month, int day){
	    			txtDate.setText(String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(day));
	    			now.set(year, month, day);
	    			fechaBusqueda = String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(day); 
	    			showLoading();
	    			Log.v("Show Dialog", "Show Dialog");
	    			fetchData();
	    		}
	    	}, now);
	    	frag.show(ft, "DateDialogFragment");
	}
	
	public void onRefresh(){
		showLoading();
		fetchData();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Activity act = this.getActivity();
		try {
			if (act != null) {
				
				if(listNews!=null)
				{
					setActionListItem();
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

	public void fetchData() {
		NewsListFetcherTask task = new NewsListFetcherTask(this);
		task.execute("");
	}

	protected void agregarCategoriasASpinner(){
		categoria = (Spinner) this.getActivity().findViewById(R.id.categorias_noticias);
		ArrayAdapter<BaseEntity> dataAdapter = new ArrayAdapter<BaseEntity>(this.getActivity(),android.R.layout.simple_spinner_item,ListCategorias);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		categoria.setAdapter(dataAdapter);
	}
	
	protected void agregarEventoCategoria(){
	
	}
	public void returnFromTask(JSONObject result) {
		JSONArray objs;
		try {
			NewsItem itm;
			objs = result.getJSONArray("data");
			for (int i = 0; i < objs.length(); i++)
			{
				itm = new NewsItem();
				itm.fillEntityFromJson(objs.optJSONObject(i));
				mListItems.add(itm);
			}
			
			//Sacamos la primera noticia
			firstItem = new NewsItem();
			firstItem.fillEntityFromJson(objs.optJSONObject(0));
			//setFirstNew();
			NewsListItem adapter2 =new NewsListItem(getActivity(), mListItems);
			listNews = (ListView)this.getActivity().findViewById(R.id.list_view);
			listNews.setAdapter(adapter2);
			setActionListItem();
			setActionFirstNew();
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
					mListItems = new ArrayList<BaseEntity>();
					try {
						returnFromTask(result);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					this.showRetry();
					String mensaje = result.optString("mensaje");
					Toast t = Toast.makeText(this.getActivity(),mensaje,Toast.LENGTH_LONG);
					t.setGravity(Gravity.BOTTOM, 50, 50);
					t.show();
					Log.v(TAG,"success:false");
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			this.showRetry();
		}
	}

	private void showLoading() {
		Activity act = this.getActivity();
		if (act != null) {
			act.findViewById(R.id.empty).setVisibility(View.GONE);
			act.findViewById(R.id.list_view).setVisibility(View.GONE);
			act.findViewById(R.id.loading_layout).setVisibility(View.VISIBLE);
			act.findViewById(R.id.retry_layout).setVisibility(View.GONE);
		}
	}

	private void showLayout() {
		Activity act = this.getActivity();
		if (act != null) {
			act.findViewById(R.id.empty).setVisibility(View.VISIBLE);
			act.findViewById(R.id.list_view).setVisibility(View.VISIBLE);
			act.findViewById(R.id.loading_layout).setVisibility(View.GONE);
			act.findViewById(R.id.retry_layout).setVisibility(View.GONE);
		}
	}

	private void showRetry() {
		Activity act = this.getActivity();
		if (act != null) {
			act.findViewById(R.id.empty).setVisibility(View.GONE);
			act.findViewById(R.id.list_view).setVisibility(View.GONE);
			act.findViewById(R.id.loading_layout).setVisibility(View.GONE);
			act.findViewById(R.id.retry_layout).setVisibility(View.VISIBLE);
		}
	}
	
	private void setFirstNew(){
		txtTexto = (TextView)this.getActivity().findViewById(R.id.textoNoticia1);
		txtTexto.setText(firstItem.getSummary());
		
		txtTitulo =(TextView)this.getActivity().findViewById(R.id.tituloNoticia1);
		txtTitulo.setText(firstItem.getTitle());
		
		ImageLoader imageLoader=ImageUtils.getImageLoader(getActivity());
		String url="";
		url=firstItem.getImg();
		Log.v("Loader", "no null");
		imageLoader.displayImage(url, (ImageView)this.getActivity().findViewById(R.id.imagenNoticia1),ImageUtils.getDefaultImageOptions());
	}

	private void setActionListItem(){
		if(listNews!=null){
			listNews.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
					//-AlertDialog alert = UIHelper.createInformationalPopup(getActivity(), "Item click", "Si funciona");
					Intent i = new Intent(getActivity(), FullContent.class);
					NewsItem itm = (NewsItem)arg0.getItemAtPosition(arg2);
					i.putExtra("entity",itm);
					startActivity(i);
				}
			});
		}
	}
	
	private void setActionFirstNew(){
		RelativeLayout first = (RelativeLayout) this.getActivity().findViewById(R.id.layout_noticia1);
		first.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), FullContent.class);
				NewsItem itm = firstItem;
				if(itm!=null){
					i.putExtra("entity",itm);
					startActivity(i);
				}
			}
		});
	}
	static class NewsListFetcherTask extends StaticAsyncTask {
		String mensaje = "No hay registros de Noticias";
		public NewsListFetcherTask(Activity activity) {
			super(activity);
		}

		ListaNoticiasFragment fragment;

		public NewsListFetcherTask(ListaNoticiasFragment fr) {
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
					fragment.callback(result);
					/*Log.v("Retorno con succes", "false");
					//fragment.showRetry();
					String mensaje = result.optString("mensaje");
					Toast t = Toast.makeText(this.fragment.getActivity(),"No hay registros de noticias",Toast.LENGTH_LONG);
					t.setGravity(Gravity.BOTTOM, 50, 50);
					t.show();*/
					/*Log.v("Retorno con resul", "null");
					Toast t=Toast.makeText(fragment.getActivity(),R.string.network_error,Toast.LENGTH_LONG);
					t.setGravity(Gravity.BOTTOM, 50, 50);
					t.show();*/
				}
			} else {
				Log.v("Retorno con resul", "null");
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
				Log.v("Fecha-Categoria Antes",fragment.fechaBusqueda+"-"+fragment.categoria_a_buscar);
				fragment.fechaBusqueda= fragment.getArguments().getString("fecha");
				fragment.categoria_a_buscar= fragment.getArguments().getString("categoria");
				Log.v("Fecha-Categoria Despues",fragment.fechaBusqueda+"-"+fragment.categoria_a_buscar);
				ret = service.getNewsListXDateAndCategory(fragment.fechaBusqueda, fragment.categoria_a_buscar);
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
	public void onDestroy() {
		super.onDestroy();
		
		if(mAdView!=null)
			mAdView.release();
	}
}
