package com.dayscript.bichitofutbolapp.ui.fragment;

import java.io.IOException;
import java.util.ArrayList;

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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.adsdk.sdk.banner.AdView;
import com.dayscript.bichitofutbolapp.R;
import com.dayscript.bichitofutbolapp.async.StaticAsyncTask;
import com.dayscript.bichitofutbolapp.http.ClaroService;
import com.dayscript.bichitofutbolapp.persistence.entity.BaseEntity;
import com.dayscript.bichitofutbolapp.persistence.entity.CampeonatoEntity;
import com.dayscript.bichitofutbolapp.persistence.entity.FechaEntity;
import com.dayscript.bichitofutbolapp.persistence.entity.NewsItem;
import com.dayscript.bichitofutbolapp.ui.adapter.CampeonatoSpinnerItem;
import com.dayscript.bichitofutbolapp.ui.adapter.FechaSpinnerItem;
import com.dayscript.bichitofutbolapp.ui.adapter.NewsListItem;
import com.dayscript.bichitofutbolapp.ui.fragment.ListaNoticiasFragment.NewsListFetcherTask;


public class CalendarFragment extends SherlockFragment  {
	public static String TAG="CALENDARIO";
	private AdView mAdView;
	ArrayList<BaseEntity> ListaCampeonatos;
	ArrayList<BaseEntity> ListaFechas;
	Spinner campeonatos;
	Spinner fechas;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		 View v = inflater.inflate(R.layout.calendar_fragment_layout, container,false);
		 ListaCampeonatos = new ArrayList<BaseEntity>();
		 fetchData();
	     return v;		
	}
	
	public void fetchData() {
		CalendarFetcherTask task = new CalendarFetcherTask(this);
		task.execute("");
	}

	protected void agregarCampeonatosASpinner(){
		CampeonatoSpinnerItem adapter2 = new CampeonatoSpinnerItem(getActivity(), ListaCampeonatos);
		campeonatos = (Spinner) this.getActivity().findViewById(R.id.torneos);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		campeonatos.setAdapter(adapter2);
	}
	
	protected void agregarFechasASpinner(CampeonatoEntity campeonato){
		fechas = (Spinner) this.getActivity().findViewById(R.id.fechas);
		FechaSpinnerItem adapter = new FechaSpinnerItem(getActivity(), campeonato.getFechas());
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		fechas.setAdapter(adapter);
		Log.v("Fecha actual", String.valueOf(campeonato.getFecha_actual()));
		fechas.setSelection(campeonato.getFecha_actual());
	}
	
	protected void agregarEventoACampeonato(){
		/* Agregar comportamiento al hacer click */
		campeonatos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				CampeonatoEntity campeonato;
				campeonato=(CampeonatoEntity) arg0.getAdapter().getItem(arg2);
				agregarFechasASpinner(campeonato);
				/*MatchResultsListFragment res=new MatchResultsListFragment();
				Bundle args=new Bundle();
				Log.v(TAG,"Date: " + date.getDate() + " " + date.getId());
				args.putString("mode", MatchResultsListFragment.MODE_CALENDAR);
				args.putString("weekId", String.valueOf(date.getId()));
				res.setArguments(args);
				FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
				ft.replace(R.id.fragment_container, res
						).commit();*/
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		
		});
	}
	
	
	protected void agregarEventoAFecha(){
		fechas = (Spinner)this.getActivity().findViewById(R.id.fechas);
		/* Agregar comportamiento al seleccionar una fecha */
		fechas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				FechaEntity fecha;
				fecha= (FechaEntity) arg0.getAdapter().getItem(arg2);
				DateListFragment res=new DateListFragment();
				Bundle args=new Bundle();
				args.putString("Id", String.valueOf(fecha.getId()));
				res.setArguments(args);
				FragmentTransaction ft=getChildFragmentManager().beginTransaction();
				ft.replace(R.id.fragment_calendar_dates_container, res).commit();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		
		});
	} 
	
	public void returnFromTask(JSONObject result) {
		JSONArray objs;
		JSONObject obj;
		JSONArray fechas;
		try {
			objs = result.getJSONArray("data");
			CampeonatoEntity itm = new CampeonatoEntity();
			for( int indiceCampeonato=0 ; indiceCampeonato<objs.length() ; indiceCampeonato++){
				itm = new CampeonatoEntity();
				obj = objs.getJSONObject(indiceCampeonato);
				itm.setId(obj.getInt("id"));
				itm.setNombre(obj.getString("nombreCampeonato"));
				Log.v("Nombre campeonato",obj.getString("nombreCampeonato"));
				fechas = obj.getJSONArray("fechas");
				ListaFechas = new ArrayList<BaseEntity>();
				for (int i = 0; i < fechas.length(); i++)
				{
					FechaEntity itmFecha = new FechaEntity();
					itmFecha.fillEntityFromJson(fechas.optJSONObject(i));
					if(itmFecha.getActualSiguiente().equalsIgnoreCase("A")){
						itm.setFecha_actual(i);
					}
					ListaFechas.add(itmFecha);
				}
				itm.setFechas(ListaFechas);
				ListaCampeonatos.add(itm);
				}
			
			/*Adapter  **/
				agregarCampeonatosASpinner();
				agregarEventoACampeonato();
				agregarEventoAFecha();
			/* */
			Log.v(TAG, itm.toString());
			Log.v("ListaFechas", Integer.toString(ListaCampeonatos.size()));
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

	static class CalendarFetcherTask extends StaticAsyncTask {

		public CalendarFetcherTask(Activity activity) {
			super(activity);
		}

		CalendarFragment fragment;

		public CalendarFetcherTask(CalendarFragment fr) {
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
				ret = service.getCalendar();
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