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


public class MarcadorClaroFragment extends SherlockFragment  {
	public static String TAG="CALENDARIO";
	private AdView mAdView;
	String fecha_actual;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		 View v = inflater.inflate(R.layout.marcador_claro_fragment_layout, container,false);
		 fetchData();
	     return v;		
	}
	
	public void fetchData() {
		CalendarFetcherTask task = new CalendarFetcherTask(this);
		task.execute("");
	}

	public void returnFromTask(JSONObject result) {
		try {
			fecha_actual = result.getString("data");
			DateListFragment res=new DateListFragment();
			Bundle args=new Bundle();
			args.putString("Id", fecha_actual);
			res.setArguments(args);
			FragmentTransaction ft=getChildFragmentManager().beginTransaction();
			ft.replace(R.id.fragment_calendar_dates_container, res).commit();
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

		MarcadorClaroFragment fragment;

		public CalendarFetcherTask(MarcadorClaroFragment fr) {
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
				ret = service.getIdFechaActual();
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