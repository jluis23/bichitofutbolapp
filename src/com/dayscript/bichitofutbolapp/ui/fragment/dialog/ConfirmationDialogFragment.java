package com.dayscript.bichitofutbolapp.ui.fragment.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockDialogFragment;

import com.dayscript.bichitofutbolapp.R;


public  class ConfirmationDialogFragment extends SherlockDialogFragment implements View.OnClickListener
{
	public interface ConfirmationCallbacks
	{
		void aceptar();
		void cancelar();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.getDialog().setTitle("Confirmaci—n");
		View v=inflater.inflate(R.layout.confirmation_dialog, container,false);
		v.findViewById(R.id.aceptar).setOnClickListener(this);
		v.findViewById(R.id.cancelar).setOnClickListener(this);
		TextView text=(TextView)v.findViewById(R.id.text);
		text.setText(getArguments().getString("title"));
		return v;
	}

	@Override
	public void onClick(View v) {
		ConfirmationCallbacks callback;
		if(getSherlockActivity() instanceof ConfirmationCallbacks)
		{
			callback=(ConfirmationCallbacks) getSherlockActivity();
		}else{return;}
		if(v.getId()==R.id.aceptar)
		{
			callback.aceptar();
		}
		if(v.getId()==R.id.cancelar)
		{
			callback.cancelar();
		}
	}
	
}