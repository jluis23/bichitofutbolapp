package com.dayscript.bichitofutbolapp.ui.fragment;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.dayscript.bichitofutbolapp.MatchDetailActivity;
import com.dayscript.bichitofutbolapp.R;
import com.dayscript.bichitofutbolapp.TeamDetailActivity;
import com.dayscript.bichitofutbolapp.async.StaticAsyncTask;
import com.dayscript.bichitofutbolapp.http.ClaroService;
import com.dayscript.bichitofutbolapp.persistence.entity.BaseEntity;
import com.dayscript.bichitofutbolapp.persistence.entity.MatchResult;
import com.dayscript.bichitofutbolapp.persistence.entity.NewsItem;
import com.dayscript.bichitofutbolapp.persistence.entity.Team;
import com.dayscript.bichitofutbolapp.ui.adapter.TeamsListItem;
import com.dayscript.bichitofutbolapp.ui.fragment.ListaNoticiasFragment.NewsListFetcherTask;
import com.dayscript.bichitofutbolapp.util.UIHelper;

public class TeamsListFragment extends SherlockListFragment {
	public static final String RECEIVE_TEAMS_LIST_ACTION = "ec.com.dayscript.bichitofutbol.RECEIVETEAMSLIST";
	public static final String TAG = "BICHITOFUTBOL_TEAMSLISTLISTFRAGMENT";
	ArrayList<BaseEntity> mListItems;
	
	
	@Override
	public void onResume() {
		super.onResume();
		setEmptyText(getResources().getString(R.string.retry));
		getListView().getEmptyView().setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onRefresh();
			}
		});
		getListView().setDividerHeight(0);
	
		Activity act = this.getActivity();
		try {
			if (act != null) {
				
				if(mListItems!=null)
				{
					setListAdapter(new TeamsListItem(this.getSherlockActivity(), mListItems));
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
	
	public void onRefresh(){
		showLoading();
		fetchData();
	}
	public void fetchData() {
		TeamsListFetcherTask task = new TeamsListFetcherTask(this);
		task.execute("");
	}
	private void showLoading() {
		Activity act = this.getActivity();
		if (act != null) {
			setListShown(false);
			
		}
	}

	private void showLayout() {
		Activity act = this.getActivity();
		if (act != null) {
		setListShown(true);
		}
	}

	private void showRetry() {
		Activity act = this.getActivity();
		if (act != null) {
			getListView().setAdapter(null);
			setListShown(true);
			
		}
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		
		super.onListItemClick(l, v, position, id);
		Log.v(TAG,"Click en item " + position);
		Team team=(Team) mListItems.get(position);
		Log.v(TAG,"El id del mequipo es: " + team.getId());
		Intent i=new Intent(getSherlockActivity(),TeamDetailActivity.class);
		i.putExtra("entity",team);
		startActivity(i);
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
					Log.v(TAG,"success:false");
					this.showRetry();
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			this.showRetry();
		}
	}
	public void returnFromTask(JSONObject result) {
		JSONArray objs;
		try {
			Team itm;
			objs = result.getJSONArray("data");
			for (int i = 0; i < objs.length(); i++)
			{
				itm = new Team();
				itm.fillEntityFromJson(objs.optJSONObject(i));
				mListItems.add(itm);
				//Log.v(TAG,itm.getTitle());
			}
			
			setListAdapter(new TeamsListItem(this.getSherlockActivity(), mListItems));
			showLayout();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	
	static class TeamsListFetcherTask extends StaticAsyncTask {

		public TeamsListFetcherTask(Activity activity) {
			super(activity);
		}

		TeamsListFragment fragment;

		public TeamsListFetcherTask(TeamsListFragment fr) {
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
									result.optString("mensaje","Error de conexion"));
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
				ret = service.getTeamsList();
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
