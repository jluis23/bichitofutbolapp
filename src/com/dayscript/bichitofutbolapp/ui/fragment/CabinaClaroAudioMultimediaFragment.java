package com.dayscript.bichitofutbolapp.ui.fragment;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.dayscript.bichitofutbolapp.CabinaClaro2Activity;
import com.dayscript.bichitofutbolapp.R;
import com.dayscript.bichitofutbolapp.async.StaticAsyncTask;
import com.dayscript.bichitofutbolapp.http.ClaroService;
import com.dayscript.bichitofutbolapp.persistence.entity.BaseEntity;

public class CabinaClaroAudioMultimediaFragment extends SherlockFragment {
	ListView list;
	ArrayList<BaseEntity> items;
	FrameLayout anchor;
	MediaPlayer mp;
	int currentlyPlaying=-1;
	MediaController mc;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v= inflater.inflate(R.layout.audio_cabina_multimedia_fragment, container,false);
		ViewGroup vg=(ViewGroup)v;
		vg.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.v("asfe","asdf");
				return false;
			}
		});
		list=(ListView) v.findViewById(R.id.audioList); 
		anchor=(FrameLayout)v.findViewById(R.id.controls_layout);
		v.findViewById(R.id.retry_button).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loadTracks();
			}
		});
		return v;
	}
	public void showContent()
	{
		getActivity().findViewById(R.id.loading_layout).setVisibility(View.GONE);
		getActivity().findViewById(R.id.retry_layout).setVisibility(View.GONE);
		getActivity().findViewById(R.id.content).setVisibility(View.VISIBLE);
	}
	public void showLoading()
	{
		getActivity().findViewById(R.id.loading_layout).setVisibility(View.VISIBLE);
		getActivity().findViewById(R.id.retry_layout).setVisibility(View.GONE);
		getActivity().findViewById(R.id.content).setVisibility(View.GONE);
	}
	public void showRetry()
	{
		getActivity().findViewById(R.id.loading_layout).setVisibility(View.GONE);
		getActivity().findViewById(R.id.retry_layout).setVisibility(View.VISIBLE);
		getActivity().findViewById(R.id.content).setVisibility(View.GONE);
	}
	public void loadTracks()
	{
		showLoading();
		MultimediaItemsFetcherTask task = new MultimediaItemsFetcherTask(this);
		task.execute("");
	}
	public void callback(JSONObject result)
	{
		items=new ArrayList<BaseEntity>();
		if(result.optBoolean("success")==true)
		{
			JSONArray data=result.optJSONArray("data");
			for(int i=0;i<data.length();i++)
			{
				JSONObject row=data.optJSONObject(i);
				AudioItemTitle titulo=new AudioItemTitle();
				titulo.setDescription(row.optString("description"));
				items.add(titulo);
				JSONArray itms=row.optJSONArray("items");
				for(int j=0;j<itms.length();j++)
				{
					AudioItem itm=new AudioItem();
					itm.fillEntityFromJson(itms.optJSONObject(j));
					items.add(itm);
				}
			}
			list.setAdapter(new AudioListItem(getSherlockActivity(), items));
			
			showContent();
		}
		else
		{
			showRetry();
		}
		/*AudioItemTitle title=new AudioItemTitle();
		title.setDescription("Partido Barcelona vs Emelec");
		items.add(title);
		AudioItem itm=new AudioItem();
		itm.setPath("http://jorgeisaac.com/api_futbol/web/audio/ECU_PAR1.wav");
		itm.setDescription("Gol Barcelona .- Barcelona 1 - Emelec 2");
		items.add(itm);
		itm=new AudioItem();
		itm.setPath("http://jorgeisaac.com/api_futbol/web/audio/ECU_PAR2.wav");
		itm.setDescription("Gol Barcelona.- Barcelona 2 - Emelec 2");
		items.add(itm);*/
		
		
	}
	static class MultimediaItemsFetcherTask extends StaticAsyncTask {

		public MultimediaItemsFetcherTask(Activity activity) {
			super(activity);
		}

		CabinaClaroAudioMultimediaFragment fragment;

		public MultimediaItemsFetcherTask(CabinaClaroAudioMultimediaFragment fr) {
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
				ret = service.getContenidoCabinaClaro();
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
	public void onStart() {
		super.onStart();
		loadTracks();
		
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log.v("TAG","Click en item "+ arg2);
				if(items.get(arg2) instanceof AudioItemTitle){
					return;
				}
				for(int i=0;i<list.getCount();i++)
				{
					if(items.get(i) instanceof AudioItem)
					{
					ViewGroup vg=(ViewGroup) list.getChildAt(i);
					//TextView tv=(TextView)vg.findViewById(R.id.estado);
					vg.findViewById(R.id.row_background).setBackgroundColor(0xffd1d3d4);
					ImageView img=(ImageView)vg.findViewById(R.id.image);
					img.setImageDrawable(getResources().getDrawable(R.drawable.icono_play));
					//tv.setText("Encerado");
					}
				}
				if(arg2==currentlyPlaying)
				{
					//TextView tv=(TextView)arg1.findViewById(R.id.estado);
					//tv.setText("Now playing");
					arg1.findViewById(R.id.row_background).setBackgroundColor(0xff939598);
					ImageView img=(ImageView)arg1.findViewById(R.id.image);
					img.setImageDrawable(getResources().getDrawable(R.drawable.icono_now_playing));
					if(mc.isShowing())
					{
						mc.hide();
					}
					else
					{
						mc.show();
					}
					return;
				}
				if(mp!=null)
				{
					mp.stop();
					mp.release();
					mc.hide();	
				}
				//TextView tv=(TextView)arg1.findViewById(R.id.estado);
				//tv.setText("Now playing");
				ImageView img=(ImageView)arg1.findViewById(R.id.image);
				img.setImageDrawable(getResources().getDrawable(R.drawable.icono_now_playing));
				arg1.findViewById(R.id.row_background).setBackgroundColor(0xff939598);
				currentlyPlaying=arg2;
				mp = new MediaPlayer();
			    mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
			    mc=new MediaController(getSherlockActivity()){
			        @Override
			        public void hide()
			        {	super.hide();
			          // show(0);
				        }
		
				         
				};
					MediaControllerHandler mch=new MediaControllerHandler(mp,mc);
					mc.setAnchorView(anchor);
				   try {
					  
						   mp.setDataSource(((AudioItem) (items.get(arg2))).getPath());
					  
					   Thread t=new Thread(new Runnable(){

						@Override
						public void run() {
							boolean didPrepare=true;
							 try {
								 final CabinaClaro2Activity act=(CabinaClaro2Activity)getSherlockActivity();
								act.runOnUiThread(new Runnable()
								{

									@Override
									public void run() {
										list.setClickable(false);
										act.showLoading();
										
									}
								 
								});
							
								try{
								mp.prepare();
								}
								catch(IllegalStateException ise)
								{	
									didPrepare=false;
									ise.printStackTrace();
								}
								
							}  catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							 final CabinaClaro2Activity act=(CabinaClaro2Activity)getSherlockActivity();
								act.runOnUiThread(new Runnable()
								{

									@Override
									public void run() {
										list.setClickable(true);
										act.hideLoading();
										
									}
								 
								});
								if(didPrepare)
								{
							   mp.start();
								}
						}
						   
					   });
					  t.start();
					 
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
	}
	public class AudioListItem extends ArrayAdapter<BaseEntity>  {
		ArrayList<BaseEntity> elements;
		
		
		public AudioListItem(Context ctx,ArrayList<BaseEntity> items)
		{
			super(ctx,R.layout.news_list_item,items);
			this.elements=items;
		}
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			
			if (row==null) {
				
				LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				if(getItemViewType(position)==1)
				{
					row=inflater.inflate(R.layout.media_types_list_item, parent, false);
					row.findViewById(R.id.row_background).setBackgroundColor(0xffd1d3d4);
				}
				else
				{
					row=inflater.inflate(R.layout.media_list_title_item, parent, false);
				}
			}
			if(getItemViewType(position)==1)
			{
			TextView tv=(TextView)row.findViewById(R.id.description);
			AudioItem itm=(AudioItem)items.get(position);
			tv.setText(itm.getDescription());
			}
			else
			{		AudioItemTitle itm=(AudioItemTitle)items.get(position);
				TextView tv=(TextView)row.findViewById(android.R.id.text1);
				tv.setText(itm.getDescription());
			}
			return row;
		}
		@Override
		public int getItemViewType(int position) {
			int ret;
			if(elements.get(position) instanceof AudioItemTitle)
			{
				ret=0;
			}
			else
			{
				ret=1;
			}
			return ret;
		}
		@Override
		public int getViewTypeCount() {
			
			return 2;
		}
		
		
	}
	static class MediaControllerHandler implements OnPreparedListener, MediaPlayerControl 
	{	MediaPlayer mediaPlayer;
		MediaController mediaController;
		Handler handler=new Handler();
		public MediaControllerHandler(MediaPlayer mp, MediaController mc)
		{	this.mediaPlayer=mp;
			this.mediaController=mc;
			mp.setOnPreparedListener(this);
			mc.setMediaPlayer(this);
		}
		@Override
		public boolean canPause() {
			// TODO Auto-generated method stub
			return true;
		}
		@Override
		public boolean canSeekBackward() {
			// TODO Auto-generated method stub
			return true;
		}
		@Override
		public boolean canSeekForward() {
			// TODO Auto-generated method stub
			return true;
		}
		@Override
		public int getBufferPercentage() {
			// TODO Auto-generated method stub
			return 0;
		}
		@Override
		public int getCurrentPosition() {
			// TODO Auto-generated method stub
			return mediaPlayer.getCurrentPosition();
		}
		@Override
		public int getDuration() {
			// TODO Auto-generated method stub
			return mediaPlayer.getDuration();
		}
		@Override
		public boolean isPlaying() {
			// TODO Auto-generated method stub
			return mediaPlayer.isPlaying();
		}
		@Override
		public void pause() {
			Log.v("aqui","tratando de pausar");
			
			mediaPlayer.pause();
			mediaController.hide();
		}
		@Override
		public void seekTo(int pos) {
			mediaPlayer.seekTo(pos);
			
		}
		@Override
		public void start() {
			// TODO Auto-generated method stub
			
			mediaPlayer.start();
			 
		}
		@Override
		public void onPrepared(MediaPlayer mp) {
			 mediaController.setMediaPlayer(this);

			    handler.post(new Runnable() {
			      public void run() {
			        mediaController.setEnabled(true);
			         mediaController.show();
			    	 
			      }
			    });
			
		}
		 
		
	}
	
	@Override
	public void onStop() {
		if(mp!=null)
		{
			mp.stop();
		}
		mp=null;
		currentlyPlaying=-1;
		super.onStop();
	}
	static class AudioItemTitle extends BaseEntity
	{
		String description;
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
	}
	static class AudioItem extends BaseEntity
	{
		String id;
		String description;
		String path;
		String dividerText=null;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getPath() {
			return path;
		}
		public void setPath(String path) {
			this.path = path;
		}
		public String getDividerText() {
			return dividerText;
		}
		public void setDividerText(String dividerText) {
			this.dividerText = dividerText;
		}
		public AudioItem(String id, String description, String path) {
			super();
			this.id = id;
			this.description = description;
			this.path = path;
		}
		public AudioItem() {
			super();
			
		}
	}
	
}
