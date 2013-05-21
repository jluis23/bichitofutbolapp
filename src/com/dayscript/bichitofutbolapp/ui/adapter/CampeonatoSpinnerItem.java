package com.dayscript.bichitofutbolapp.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dayscript.bichitofutbolapp.R;
import com.dayscript.bichitofutbolapp.persistence.entity.BaseEntity;
import com.dayscript.bichitofutbolapp.persistence.entity.CampeonatoEntity;
import com.dayscript.bichitofutbolapp.persistence.entity.MatchResult;
import com.dayscript.bichitofutbolapp.persistence.entity.NewsItem;
import com.dayscript.bichitofutbolapp.persistence.entity.Team;
import com.dayscript.bichitofutbolapp.utils.ImageUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CampeonatoSpinnerItem extends ArrayAdapter<BaseEntity> {
	ArrayList<BaseEntity> elements;
	public CampeonatoSpinnerItem(Context ctx,ArrayList<BaseEntity> items)
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
	
		CampeonatoEntity result=(CampeonatoEntity)elements.get(position);
		TextView tv=(TextView)row.findViewById(R.id.campeonato);
		tv.setText(result.getNombre());
		return(row);
	}
}
