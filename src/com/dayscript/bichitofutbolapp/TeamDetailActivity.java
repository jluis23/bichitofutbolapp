package com.dayscript.bichitofutbolapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.dayscript.bichitofutbolapp.async.StaticAsyncTask;
import com.dayscript.bichitofutbolapp.http.ClaroService;
import com.dayscript.bichitofutbolapp.persistence.entity.BaseEntity;
import com.dayscript.bichitofutbolapp.persistence.entity.Player;
import com.dayscript.bichitofutbolapp.persistence.entity.Team;
import com.dayscript.bichitofutbolapp.ui.adapter.PlayersListItem;
import com.dayscript.bichitofutbolapp.ui.fragment.dialog.ConfirmationDialogFragment;
import com.dayscript.bichitofutbolapp.ui.fragment.dialog.LoadingFragment;
import com.dayscript.bichitofutbolapp.util.PreferencesHelper;
import com.dayscript.bichitofutbolapp.utils.ImageUtils;
import com.dayscript.bichitofutbolapp.utils.UIUtils;
import com.nostra13.universalimageloader.core.ImageLoader;


public class TeamDetailActivity extends SherlockFragmentActivity {
	public static final String TAG = "BICHITOFUTBOL_MATCH_ACTIVITY";
	public static final String DIALOG_TAG = "DIALOG";
	
	public BaseEntity entity;
	JSONArray jsonNotificaciones;
	ArrayList<BaseEntity> arrayJugadores;
	boolean dirty=false;
	LinearLayout necesitalogin;
	ImageButton loginFacebook;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.team_detail);
		Intent callerIntent = getIntent();
		if (!callerIntent.hasExtra("entity")) {
			finish();
		}
		UIUtils.stylizeActionBar(this);
		entity = (BaseEntity) callerIntent.getSerializableExtra("entity");
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		loadInfo();
		findViewById(R.id.retry_button).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						loadInfo();
					}
				});
		necesitalogin = (LinearLayout)this.findViewById(R.id.layout_necesita_login);
		loginFacebook = (ImageButton)this.findViewById(R.id.needLoginButton);
		loginFacebook.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				iniciarLogin();
			}
		});
	}

	public void iniciarLogin(){
		Log.v("Iniciar sesion", "Iniciar Sesion en Facebook");
		Intent i = new Intent(this,FacebookLoginActivity.class);
		startActivity(i);
	}
	
	private void loadInfo() {
		findViewById(R.id.fullcontent_layout).setVisibility(View.GONE);
		findViewById(R.id.retry_layout).setVisibility(View.GONE);
		findViewById(R.id.loading_layout).setVisibility(View.VISIBLE);
		fetchData();
	}

	public void fetchData() {
		TeamDetailFetcherTask task = new TeamDetailFetcherTask(this,entity);
		task.execute("");
	}

	public void returnFromTask(JSONObject result) {
		boolean error=false;
		 try {
             JSONObject obj = result.getJSONObject("data");
             if(entity instanceof Team)
             {
                 Log.v(TAG,"Iniciar armando la interfaz");
                 TextView tv=(TextView)findViewById(R.id.teamName);
                 tv.setText(obj.optString("name"));
                 tv=(TextView)findViewById(R.id.textPresident);
                 tv.setText(obj.optString("president"));
                 tv=(TextView)findViewById(R.id.textCoach);
                 tv.setText(obj.optString("coach"));
                 tv=(TextView)findViewById(R.id.textStadium);
                 tv.setText(obj.optString("stadium"));
                 tv=(TextView)findViewById(R.id.textCapacity);
                 tv.setText(obj.optString("capacity"));
                 tv=(TextView)findViewById(R.id.championships);
                 tv.setText(obj.optString("campeonatos"));
                 tv=(TextView)findViewById(R.id.second_places);
                 tv.setText(obj.optString("vicecampeonatos"));
                 tv=(TextView)findViewById(R.id.website);
                 tv.setText(obj.optString("sitio_web"));
                 ImageView img=(ImageView)findViewById(R.id.main_image);
                 ImageLoader imageLoader=ImageUtils.getImageLoader(TeamDetailActivity.this);
                 imageLoader.displayImage(obj.optString("img"), img,ImageUtils.getDefaultImageOptions());
                 JSONArray jugadores=obj.optJSONArray("jugadores");
                 if(jugadores!=null)
                 {   arrayJugadores=new ArrayList<BaseEntity>();
                     Player p;
                     for(int i=0;i<jugadores.length();i++)
                     {
                         p=new Player();
                         p.fillEntityFromJson(jugadores.optJSONObject(i));
                         arrayJugadores.add(p);
                     }
                 }
                 if(arrayJugadores!=null)
                 {
                     findViewById(R.id.btn_ver_jugadores).setVisibility(View.VISIBLE);
                     findViewById(R.id.btn_ver_jugadores).setOnClickListener(new View.OnClickListener() {
                         
                         @Override
                         public void onClick(View v) {
                             Bundle b=new Bundle();
                             b.putSerializable("jugadores",arrayJugadores);
                             ListaJugadores popup=new ListaJugadores();
                             popup.setArguments(b);
                             popup.show(getSupportFragmentManager(), "LISTA_JUGADORES");
                         }
                     });
                 }
                 /*ListView listView=(ListView)findViewById(R.id.players_list);
                 listView.setAdapter(new PlayersListItem(TeamDetailActivity.this, arrayJugadores));
                 */
             }
             jsonNotificaciones=obj.optJSONArray("notificaciones");
             prepareLayout();
             	findViewById(R.id.fullcontent_layout).setVisibility(View.VISIBLE);
     			findViewById(R.id.retry_layout).setVisibility(View.GONE);
     			findViewById(R.id.loading_layout).setVisibility(View.GONE);
         } catch (JSONException e) {
             e.printStackTrace();
             error=true;
         }
	}
	
	public void prepareLayout()
	{
		ViewGroup vg=(ViewGroup) findViewById(R.id.notifications_layout);
		String[] notificaciones=getResources().getStringArray(R.array.notification_types);
		String msgNotificaciones=getResources().getString(R.string.notifications);
		for(int i=0;i<vg.getChildCount();i++)
		{
			View v=vg.getChildAt(i);
			if(v==null){break;}
			if(v instanceof LinearLayout)
			{	
				for(int j=0;j<((ViewGroup)v).getChildCount();j++)
				{
					View v2=((ViewGroup)v).getChildAt(j);
					if(v2==null){break;}
					if(v2 instanceof TextView && !(v2 instanceof ToggleButton) )
					{
						TextView tv=(TextView) v2;
						tv.setText(String.format(msgNotificaciones, notificaciones[i-1]));
					}
					if(v2 instanceof ToggleButton)
					{
						if(jsonNotificaciones!=null)
						{
						JSONObject tmpNot=jsonNotificaciones.optJSONObject(i-1);
						if(tmpNot!=null)
						{
							if(tmpNot.optString("estado").equals("A"))
							{
								
								((ToggleButton) v2).setChecked(true);
							}
						}
						v2.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								dirty=true;
								
							}
						});
						}
					}
				}
			}
			
		}
		findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				saveNotifications();
			}
		});
		
		findViewById(R.id.activarTodas).setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						selectAll();
					}
		});
		
		if(PreferencesHelper.isAuthenticated(this)==null)
		{
			vg.setVisibility(View.GONE);
			necesitalogin.setVisibility(View.VISIBLE);
		}else{
			vg.setVisibility(View.VISIBLE);
			necesitalogin.setVisibility(View.GONE);
		}
	}
	
	public void saveNotifications()
	{
		LoadingFragment fmt=new LoadingFragment();
    	Bundle params=new Bundle();
    	params.putString("message", getString(R.string.loading));
    	params.putString("title",getString(R.string.loading_title));
    	fmt.setArguments(params);
    	fmt.setCancelable(false);
    	fmt.show(this.getSupportFragmentManager(), DIALOG_TAG);
    	
		NotificationsSaverFetcherTask task = new NotificationsSaverFetcherTask(this,entity);
		task.execute("");
	}
	
	public void selectAll()
	{
		ToggleButton cambios = (ToggleButton) this.findViewById(R.id.notificacion_cambios);
		ToggleButton faltas = (ToggleButton) this.findViewById(R.id.notificacion_faltas);
		ToggleButton goles = (ToggleButton) this.findViewById(R.id.notificacion_goles);
		ToggleButton noticias = (ToggleButton) this.findViewById(R.id.notificacion_noticias);
		ToggleButton partidos = (ToggleButton) this.findViewById(R.id.notificacion_partidos);
		ToggleButton penales = (ToggleButton) this.findViewById(R.id.notificacion_penales);
		if(cambios!=null){
			cambios.setChecked(true);
		}
		if(faltas!=null){
			faltas.setChecked(true);
		}
		if(goles!=null){
			goles.setChecked(true);
		}
		if(noticias!=null){
			noticias.setChecked(true);
		}
		if(partidos!=null){
			partidos.setChecked(true);
		}
		if(penales!=null){
			penales.setChecked(true);
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
	
	public void returnFromTaskNotificaciones(JSONObject result) {
		boolean error=false;
		boolean success = result.optBoolean("success");
		if (success == true) {
			LoadingFragment fmt=(LoadingFragment)getSupportFragmentManager().findFragmentByTag(DIALOG_TAG);
		 	fmt.dismiss();
			Log.v(TAG,"success:true");
		}else{
			Log.v(TAG,"success:false");
		}
	}
	
	public void callbackNotificaciones(JSONObject result) {
		boolean success=false;
		Activity act = this;
		try {
			if (act != null) {
				success = result.optBoolean("success");
				if (success == true) {
					Log.v(TAG,"success:true");
					try {
						returnFromTaskNotificaciones(result);
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

		//this.getSherlock().getMenuInflater().inflate(R.menu.match_detail_menu, menu);
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
	
	static class TeamDetailFetcherTask extends StaticAsyncTask {
		TeamDetailActivity act;
		BaseEntity base;
		
		public TeamDetailFetcherTask(Activity activity, BaseEntity entity) {
			super(activity);
			act = (TeamDetailActivity)activity;
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
				id = ((Team)base).getId();
				ret = service.getTeamDetail(Integer.toString(id),act);
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
	
	
	public static class ListaJugadores extends SherlockDialogFragment
	{

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			this.getDialog().setTitle("Plantilla");
			View ret=inflater.inflate(R.layout.players_list_dialog, container,false);
			ListView listView=(ListView)ret.findViewById(R.id.listJugadores);
			ArrayList<BaseEntity>jugadores=null;
			try{
				jugadores=(ArrayList<BaseEntity>) getArguments().get("jugadores");
			}
			catch(Exception e)
			{
				
			}
			listView.setAdapter(new PlayersListItem(getActivity(), jugadores));
			return ret;
		}
		
	}

	static class NotificationsSaverFetcherTask extends StaticAsyncTask{
		TeamDetailActivity act;
		BaseEntity base;
		
		public NotificationsSaverFetcherTask(Activity activity, BaseEntity entity) {
			super(activity);
			act = (TeamDetailActivity)activity;
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
					act.callbackNotificaciones(result);
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
			this.isDirty = true;
			JSONObject ret = null;
			Properties user=PreferencesHelper.isAuthenticated(act);
			Log.v(TAG, "Iniciando Obtencion de detalle de notificaciones");
			String data="";
			ClaroService service = ClaroService.getInstance(activity);
			try{
				ViewGroup vg=(ViewGroup) act.findViewById(R.id.notifications_layout);
				JSONArray tmpData=new JSONArray();
				for(int i=0;i<vg.getChildCount();i++)
				{
					View v=vg.getChildAt(i);
					if(v==null){break;}
					if(v instanceof LinearLayout)
					{	
						for(int j=0;j<((ViewGroup)v).getChildCount();j++)
						{
							View v2=((ViewGroup)v).getChildAt(j);
							if(v2==null){break;}
							if(v2 instanceof ToggleButton)
							{	
								ToggleButton btn=(ToggleButton)v2;
								int id=Integer.parseInt((String) btn.getTag());
								String estado=(btn.isChecked()?"A":"I");
								Team t=(Team)base;
								JSONObject obj=new JSONObject();
								obj.put("idEvento", id);
								obj.put("estado", estado);
								obj.put("idEquipo", t.getId());
								tmpData.put(obj);
							}
						}
					}
				}
				data=tmpData.toString();
				}
				catch(Exception e)
				{
					
				}
				try {
					ret = service.saveUserNotifications(user.getProperty("id"), data);
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
