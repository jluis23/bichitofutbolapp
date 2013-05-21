package com.dayscript.bichitofutbolapp;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.UserSettingsFragment;
import com.nostra13.universalimageloader.core.ImageLoader;

import com.dayscript.bichitofutbolapp.FullContent.NewsDetailFetcherTask;
import com.dayscript.bichitofutbolapp.async.StaticAsyncTask;
import com.dayscript.bichitofutbolapp.http.ClaroService;
import com.dayscript.bichitofutbolapp.network.ServicesClient;
import com.dayscript.bichitofutbolapp.persistence.entity.BaseEntity;
import com.dayscript.bichitofutbolapp.persistence.entity.NewsItem;
import com.dayscript.bichitofutbolapp.ui.fragment.dialog.LoadingFragment;
import com.dayscript.bichitofutbolapp.util.PreferencesHelper;
import com.dayscript.bichitofutbolapp.utils.ImageUtils;

public class FacebookLoginActivity extends SherlockFragmentActivity{
	  private UserSettingsFragment userSettingsFragment;
	  public HashMap<String, String> parametros;
	  public static final String LOADING_FRAGMENT_TAG="LOADING_FMT";

	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        setContentView(R.layout.facebook_login_activity);
	        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	        FragmentManager fragmentManager = getSupportFragmentManager();
	        Session currentSession=Session.getActiveSession();
	        if(currentSession!=null)
	        {
	        	if(currentSession.isOpened()){
	        	/*
	        	 * Usuario ya asign— permisos
	        	 * 
	        	 * */
	        		Log.v("Sesion de facebook", "Iniciar Login");
	        		doLogin(currentSession);
	        	}
	        }
	        userSettingsFragment = (UserSettingsFragment) fragmentManager.findFragmentById(R.id.login_fragment);   
	        userSettingsFragment.setReadPermissions(Arrays.asList("user_location", "user_birthday", "user_likes","email"));
	        userSettingsFragment.setSessionStatusCallback(new Session.StatusCallback() {
	            @Override
	            public void call(Session session, SessionState state, Exception exception) {
	              
	            	Log.d("LoginUsingLoginFragmentActivity", String.format("New session state: %s", state.toString()));
	            	if (state.isOpened()) {
	            	   

	            	    // Request user data and show the results
	            		doLogin(session);
	            	}
	            }
	        });
	    }
	    void doLogin(Session session)
	    {
	    	LoadingFragment fmt=new LoadingFragment();
	    	Bundle params=new Bundle();
	    	params.putString("message", getString(R.string.loading));
	    	params.putString("title",getString(R.string.loading_title));
	    	fmt.setArguments(params);
	    	fmt.setCancelable(false);
	    	fmt.show(this.getSupportFragmentManager(), LOADING_FRAGMENT_TAG);
	    	
	    	Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

      	        @Override
      	        public void onCompleted(GraphUser user, Response response) {
      	            if (user != null) {
      	            	/*
      	            	 * 01-30 12:14:00.168: D/LoginUsingLoginFragmentActivity(11298): {"inspirational_people":[{"id":"112693112079585","name":"James Gosling"},{"id":"109823555710753","name":"Guido van Rossum"},{"id":"30858851362","name":"Kevin Mitnick"}],
      	            	 * "birthday":"02\/25\/1988","hometown":{"id":"102190893155987","name":"Guayaquil, Ecuador"},
      	            	 * "location":{"id":"102190893155987","name":"Guayaquil, Ecuador"},
      	            	 * "locale":"en_US","link":"http:\/\/www.facebook.com\/jorgejams88",
      	            	 * "updated_time":"2012-12-10T03:54:54+0000","id":"613466339",
      	            	 * "languages":[{"id":"108177092548456","name":"Espa–ol"},{"id":"106059522759137","name":"English"},{"id":"112134812132840","name":"Spanglish"}],
      	            	 * "first_name":"Jorge","timezone":-5,"username":"jorgejams88",
      	            	 * "quotes":"No dejes para tiempo de ejecuci—n lo que puedes controlar en tiempo de compliaci—n",
      	            	 * "email":"jorge.moran.jams@gmail.com","verified":true,"name":"Jorge Moran","last_name":"Moran",
      	            	 * "gender":"male","favorite_athletes":[{"id":"306709132744299","name":"Claro Antonio Valencia"}]}

      	            	 * */
      	               
      	               Log.d("LoginUsingLoginFragmentActivity",user.getInnerJSONObject().toString());
      	               PreferencesHelper.saveFacebookObject(getApplicationContext(), user.getInnerJSONObject());
      	        
      	               JSONObject userFb=user.getInnerJSONObject();
      	               parametros =new HashMap<String,String>(); 
      	               parametros.put("idFacebook", userFb.optString("id"));
      	               parametros.put("fechaNacimiento", userFb.optString("birthday"));
      	               parametros.put("email", userFb.optString("email"));
      	               parametros.put("nombre", userFb.optString("first_name"));
      	               parametros.put("apellido", userFb.optString("last_name"));
      	               parametros.put("telefono","000000000");
      	               parametros.put("genero", userFb.optString("gender"));
      	               parametros.put("push", PreferencesHelper.getPreference(FacebookLoginActivity.this, PreferencesHelper.GCM_IDENTIFIER));
      	               Log.v("save user", "Guardar usuario");
      	               fetchData();
      	               
      	            }
      	        }
      	    });
	    }
	    @Override
	    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	        userSettingsFragment.onActivityResult(requestCode, resultCode, data);
	        super.onActivityResult(requestCode, resultCode, data);
	    }
	    @Override
		public boolean onOptionsItemSelected(MenuItem item) {
			if(item.getItemId()==android.R.id.home)
			{
				finish();
			}
			return super.onOptionsItemSelected(item);
		}
		
		public void handleNetworkReturn(JSONObject ret)
		{	
			   LoadingFragment fmt=(LoadingFragment)getSupportFragmentManager().findFragmentByTag(LOADING_FRAGMENT_TAG);
			   fmt.dismiss();
               Intent i=new Intent(getApplicationContext(),MainActivity.class);
               i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               startActivity(i);
               finish();
		}
		
		public void fetchData() {
			SaveUserTask task = new SaveUserTask(this);
			task.execute("");
		}

		public void returnFromTask(JSONObject result) {
			JSONObject obj;
				String mensaje = result.optString("mensaje");
				handleNetworkReturn(result);
				Log.v("save user", mensaje);
		}

		public void callback(JSONObject result) {
			boolean success=false;
			Activity act = this;
			try {
				if (act != null) {
					success = result.optBoolean("success");
					if (success == true) {
						Log.v("Login","success:true");
						try {
							returnFromTask(result);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}else{
						Log.v("Login","success:false");
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
				//this.showRetry();
			}
		}
		
		static class SaveUserTask extends StaticAsyncTask {
			FacebookLoginActivity act;
			
			public SaveUserTask(Activity activity) {
				super(activity);
				act =(FacebookLoginActivity)activity;
			}

			@Override
			protected void onPostExecute(JSONObject result) {
				boolean success;
				this.isFinished = true;
				if (result != null) {
					success = result.optBoolean("success");
					if (success == true) {
						Log.v("Login",result.toString());
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
					ret = service.saveUser(act.parametros);
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
