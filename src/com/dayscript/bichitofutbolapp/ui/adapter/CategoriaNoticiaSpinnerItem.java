package com.dayscript.bichitofutbolapp.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dayscript.bichitofutbolapp.R;
import com.dayscript.bichitofutbolapp.persistence.entity.BaseEntity;
import com.dayscript.bichitofutbolapp.persistence.entity.CategoriaEntity;

public class CategoriaNoticiaSpinnerItem extends ArrayAdapter<BaseEntity> {
	ArrayList<BaseEntity> elements;
	public CategoriaNoticiaSpinnerItem(Context ctx,ArrayList<BaseEntity> items)
	{
		super(ctx,R.layout.calendar_spinner_item,items);
		this.elements=items;
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row==null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.calendar_spinner_item, parent, false);
		}
	
		CategoriaEntity result=(CategoriaEntity)elements.get(position);
		TextView tv=(TextView)row.findViewById(R.id.campeonato);
		tv.setText(result.getNombre());
		return(row);
	}
}
