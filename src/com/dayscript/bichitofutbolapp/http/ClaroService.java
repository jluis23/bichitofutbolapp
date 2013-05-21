package com.dayscript.bichitofutbolapp.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.json.JSONException;
import org.json.JSONObject;
import com.dayscript.bichitofutbolapp.R;
import com.dayscript.bichitofutbolapp.util.PreferencesHelper;

import android.content.Context;
import android.util.Log;



public class ClaroService extends WebservicesProxy {
		private static ClaroService instance;
		
		private ClaroService(Context context)
		{
			super();
			this.context=context;
			super.serializationType=context.getResources().getString(com.dayscript.bichitofutbolapp.R.string.http_accept);
		}
		
		public JSONObject getNewsList() throws IOException, JSONException {
			Log.v(TAG,"Lista de noticias");
			return (JSONObject) this.execute(context.getString(R.string.base_path) + context.getString(R.string.get_news_list), "get",null);
		}
		
		public JSONObject getNewsListXDateAndCategory(String date, String category) throws IOException, JSONException {
			Log.v(TAG,"Lista de categorias");
			return (JSONObject) this.execute(context.getString(R.string.base_path) + context.getString(R.string.get_news_list_x_date_category)+date+"/category/"+category, "get",null);
		}

		public JSONObject getNewsDetail(int id) throws IOException, JSONException {
			Log.v(TAG,"Detalle de noticia id: "+id);
			HashMap<String,String>params=new HashMap<String,String>();
			params.put("id",Integer.toString(id));
			return (JSONObject) this.execute(context.getString(R.string.base_path) + context.getString(R.string.news_detail)+id, "get",params);
		}

		public JSONObject getMatchResults(String id) throws IOException, JSONException {
			Log.v(TAG,"Resultados de partidos"+id);
			HashMap<String,String>params=new HashMap<String,String>();
			params.put("id",id);
			return (JSONObject) this.execute(context.getString(R.string.base_path) + context.getString(R.string.get_match_results)+id, "get",params);
		}

		public JSONObject getTeamsList() throws IOException, JSONException {
			Log.v(TAG,"Lista de equipos");
			return (JSONObject) this.execute(context.getString(R.string.base_path) + context.getString(R.string.get_teams_list), "get",null);
		}
		
		public JSONObject getTeamDetail(String id, Context ctx) throws IOException, JSONException {
			Properties user=PreferencesHelper.isAuthenticated(ctx);
			String param="?USER_ID=715554355";
			if(user!=null)
			{
				param="?USER_ID=" + user.getProperty("id");
			}
			Log.v(TAG,"Detalle de equipos: "+id);
			HashMap<String,String>params=new HashMap<String,String>();
			params.put("id",id);
			return (JSONObject) this.execute(context.getString(R.string.base_path) + context.getString(R.string.get_team_detail)+id+param, "get",params);
		}
		
		public JSONObject getMatchDetail(String id) throws IOException, JSONException {
			Log.v(TAG,"Detalle de partido: "+id);
			HashMap<String,String>params=new HashMap<String,String>();
			params.put("id",id);
			return (JSONObject) this.execute(context.getString(R.string.base_path) + context.getString(R.string.get_match_detail)+id, "get",null);
		}

		public JSONObject getCalendarDates() throws IOException, JSONException
		{
			return (JSONObject) this.execute(context.getString(R.string.base_path) + context.getString(R.string.get_calendar_dates), "get",null);
		}
		
		public JSONObject getCalendar() throws IOException, JSONException
		{
			Log.v(TAG,context.getString(R.string.base_path) + context.getString(R.string.get_calendar));
			return (JSONObject) this.execute(context.getString(R.string.base_path) + context.getString(R.string.get_calendar), "get",null);
		}
		public JSONObject getPositionsTable() throws IOException, JSONException
		{
			return (JSONObject) this.execute(context.getString(R.string.base_path) + context.getString(R.string.get_table_positions), "get",null);
		}
		
		public JSONObject getTablaGoleadores() throws IOException, JSONException
		{
			return (JSONObject) this.execute(context.getString(R.string.base_path) + context.getString(R.string.get_tabla_goleadores), "get",null);
		}
		public JSONObject getPositionsGoleadoresTable() throws IOException, JSONException
		{
			return (JSONObject) this.execute(context.getString(R.string.base_path) + context.getString(R.string.get_table_positions_goleadores), "get",null);
		}
		public JSONObject getCategoriesList() throws IOException, JSONException {
			Log.v(TAG,"Lista de categorias");
			return (JSONObject) this.execute(context.getString(R.string.base_path) + context.getString(R.string.get_categories), "get",null);
		}
		public JSONObject getMultimedia(String type) throws IOException, JSONException
		{
			Log.v(TAG,context.getString(R.string.base_path) + context.getString(R.string.get_multimedia));
			return (JSONObject) this.execute(context.getString(R.string.base_path) + String.format(context.getString(R.string.get_multimedia),type), "get",null);
		}
		public JSONObject getListAlbums() throws IOException, JSONException
		{
			Log.v(TAG,context.getString(R.string.base_path) + context.getString(R.string.get_albums_list));
			return (JSONObject) this.execute(context.getString(R.string.base_path) + context.getString(R.string.get_albums_list), "get",null);
		}
		public JSONObject getListAlbumsXType(String typeAlbum) throws IOException, JSONException
		{
			Log.v(TAG,context.getString(R.string.base_path) + context.getString(R.string.get_albums_list));
			return (JSONObject) this.execute(context.getString(R.string.base_path) + context.getString(R.string.get_albums_list_x_tipo)+typeAlbum, "get",null);
		}
		public JSONObject getContenidoAlbum(String id) throws IOException, JSONException
		{
			Log.v(TAG,context.getString(R.string.base_path) + context.getString(R.string.get_album_content)+id);
			return (JSONObject) this.execute(context.getString(R.string.base_path) + context.getString(R.string.get_album_content)+id, "get",null);
		}
		public JSONObject getTweetsList() throws IOException, JSONException
		{
			Log.v(TAG,context.getString(R.string.base_path) + context.getString(R.string.get_tweets_list));
			return (JSONObject) this.execute(context.getString(R.string.base_path) + context.getString(R.string.get_tweets_list), "get",null);
		}
		public JSONObject getIdFechaActual() throws IOException, JSONException
		{
			Log.v(TAG,context.getString(R.string.base_path) + context.getString(R.string.get_id_fecha_actual));
			return (JSONObject) this.execute(context.getString(R.string.base_path) + context.getString(R.string.get_id_fecha_actual), "get",null);
		}
		public JSONObject getContenidoCabinaClaro() throws IOException, JSONException
		{
			Log.v(TAG,context.getString(R.string.base_path) + context.getString(R.string.get_contenido_cabina_claro));
			return (JSONObject) this.execute(context.getString(R.string.base_path) + context.getString(R.string.get_contenido_cabina_claro), "get",null);
		}
		public JSONObject saveUser(HashMap<String,String>params) throws IOException, JSONException
		{
			Log.v("Save",context.getString(R.string.base_path) + context.getString(R.string.save_user));
			return (JSONObject) this.execute(context.getString(R.string.base_path) + context.getString(R.string.save_user),"post",params);
		}
		public JSONObject saveUserNotifications(String id, String data) throws IOException, JSONException
		{
			HashMap<String,String>params=new HashMap<String,String>();
			params.put("data",data);
			return (JSONObject) this.execute(context.getString(R.string.base_path) + String.format(context.getString(R.string.save_user_notifications),id),"post",params);
		}
		
		public static ClaroService getInstance(Context context)
		{
		
			if(instance==null)
			{ 	
				instance=new ClaroService(context);
			}
			
			return instance;
		}
		public static ClaroService getInstance()
		{
			if(instance==null)
			{
				throw new RuntimeException("Instancia de ClaroService no inicializada");
			}
			return instance;
		}
		
		@Override
		public void fillHeaderParams() {
			
		}
}
