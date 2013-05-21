package com.dayscript.bichitofutbolapp.ui.fragment;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dayscript.bichitofutbolapp.R;
import com.dayscript.bichitofutbolapp.async.StaticAsyncTask;
import com.dayscript.bichitofutbolapp.http.ClaroService;
import com.dayscript.bichitofutbolapp.util.UIHelper;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

/*import com.claroecuador.miclaro.Main;
import com.claroecuador.miclaro.R;
import com.claroecuador.miclaro.async.StaticAsyncTask;
import com.claroecuador.miclaro.http.ClaroService;
import com.claroecuador.miclaro.util.PreferencesHelper;
import com.claroecuador.miclaro.util.UIHelper;*/


public class NewListFragmentBAK extends Fragment {
	public static String TAG="MICLAROMOBILE_LISTADENOTICIAS";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.home_layout, container, false);
		Bundle args=this.getArguments();
		if(args!=null){
			//this.email = args.getString("email");
			//Log.v(TAG,email);
		}
		/*v.findViewById(R.id.retryButton).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						onStart();
					}
				});*/
		return v;
	}

	@Override
	public void onStart() {
		super.onStart();
		Activity act = this.getActivity();
		try {
			if (act != null) {
				showLoading();
				fetchData();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void fetchData() {
		NewListFetcherTask task = new NewListFetcherTask(this);
		task.execute("");
	}

	public void returnFromTask(JSONObject obj) {

	}

	private void showLoading() {
		Activity act = this.getActivity();
		if (act != null) {
			/*((Main) act).showLoading();
			act.findViewById(R.id.scrollView_informacion_factura).setVisibility(View.GONE);
			act.findViewById(R.id.layout_loading).setVisibility(View.VISIBLE);
			act.findViewById(R.id.layout_retryloading).setVisibility(View.GONE);*/
		}
	}

	private void showLayout() {
		Activity act = this.getActivity();
		if (act != null) {
			/*((Main) act).hideLoading();
			act.findViewById(R.id.scrollView_informacion_factura).setVisibility(View.VISIBLE);
			act.findViewById(R.id.layout_loading).setVisibility(View.GONE);
			act.findViewById(R.id.layout_retryloading).setVisibility(View.GONE);*/
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
				if (success == true)
				{
					Boolean activo = data.optBoolean("activo");
					if(activo)
					{
						String factura_activa = data.optString("mensaje","");
						/*TextView info = (TextView) act.findViewById(R.id.texto_informacion_factura);
						info.setText(factura_activa);
						ImageView imagen = (ImageView)act.findViewById(R.id.imagen_factura_activada);
						imagen.setImageDrawable(getResources().getDrawable(R.drawable.visto));
						Button btn = (Button) act.findViewById(R.id.btn_activar_factura_info);
						btn.setVisibility(View.GONE);*/
					}
					else
					{
						String informacion = data.optString("informacion","");
						/*TextView info = (TextView) act.findViewById(R.id.texto_informacion_factura);
						info.setText(informacion);*/
					}
				}else{
					String informacion = data.optString("mensaje","");
					/*TextView info = (TextView) act.findViewById(R.id.texto_informacion_factura);
					info.setText(informacion);*/
				}
				this.showLayout();
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.showRetry();
		}
	}

	private void showRetry() {
		Activity act = this.getActivity();
		if (act != null) {
			/*((Main) act).hideLoading();
			act.findViewById(R.id.scrollView_informacion_factura).setVisibility(View.GONE);
			act.findViewById(R.id.layout_loading).setVisibility(View.GONE);
			act.findViewById(R.id.layout_retryloading).setVisibility(View.VISIBLE);*/
		}

	}

	static class NewListFetcherTask extends StaticAsyncTask {

		public NewListFetcherTask(Activity activity) {
			super(activity);
		}
		
		NewListFragmentBAK fragment;
		public NewListFetcherTask(NewListFragmentBAK fr) {
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
					alert=UIHelper.createInformationalPopup(
							activity,
							activity.getResources().getString(
									android.R.string.dialog_alert_title),
							result.optString("mensaje",activity
									.getResources().getString(R.string.network_error)));
					fragment.showRetry();
				}
			} else {
				fragment.showRetry();
			}
			super.onPostExecute(result);
		}

		@Override
		protected JSONObject doInBackground(String... arg0) {
			ClaroService service = ClaroService.getInstance(activity);
			this.isDirty = true;
			JSONObject ret = null;
			try {
				ret = service.getNewsList();
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
