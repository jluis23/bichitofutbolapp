package com.dayscript.bichitofutbolapp.ui.fragment.dialog;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockDialogFragment;

import com.dayscript.bichitofutbolapp.R;

public class LoadingFragment extends SherlockDialogFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v=inflater.inflate(R.layout.loading_fragment_layout, container,false);
		TextView tv=(TextView)v.findViewById(R.id.loading_message);
		tv.setText(getArguments().getString("message"));
		String title="";
		title=(getArguments().getString("title")!=null?getArguments().getString("title"):"");
		this.getDialog().setTitle(title);
		Log.v("Loading fragment","Loading Fragment");
		return v;
	}

}
