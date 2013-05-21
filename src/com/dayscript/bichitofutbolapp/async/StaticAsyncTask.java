package com.dayscript.bichitofutbolapp.async;
import org.json.*;

import com.dayscript.bichitofutbolapp.R;
import com.dayscript.bichitofutbolapp.util.UIHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.*;
import android.view.View;
import android.widget.ProgressBar;

public abstract class StaticAsyncTask extends AsyncTask<String,Integer,JSONObject>{
	public boolean isDirty=false;
	public boolean isFinished=false;
	protected Activity activity=null;
	protected ProgressDialog pd;
	protected AlertDialog alert;
	public StaticAsyncTask(Activity activity)
	{
		attach(activity);
	}
	public void attach(Activity activity2) {
		this.activity=activity2;
		
		}
		public void dettach()
		{
			this.activity=null;
		}
	

	public void setProgressDialog(ProgressDialog show) {
		this.pd=show;
		
	}
	public ProgressDialog getProgressDialog() {
		return this.pd;
	}
	public AlertDialog getAlertDialog()
	{
		return alert;
	}
	public void setAlertDialog(AlertDialog d)
	{
	  this.alert=d;
	}
	@Override
	protected void onProgressUpdate(Integer... values) {
		/*if(activity!=null){
		alert=com.dayscript.bichitofutbolapp.util.UIHelper.createInformationalPopup(activity, activity
				.getResources().getString(R.string.network_error), activity
				.getResources().getString(R.string.network_error));
		}*/
		super.onProgressUpdate(values);
	}
	@Override
	protected void onPreExecute() {
		/*ProgressBar pb=(ProgressBar)activity.findViewById(R.id.main_progressbar);
		if(pb!=null){
		pb.setVisibility(View.VISIBLE);
		}*/
		super.onPreExecute();
	}
}


