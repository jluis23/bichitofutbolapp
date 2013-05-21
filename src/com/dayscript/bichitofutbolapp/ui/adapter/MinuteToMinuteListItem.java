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
import com.dayscript.bichitofutbolapp.persistence.entity.MinuteToMinuteItem;
import com.dayscript.bichitofutbolapp.utils.ImageUtils;
import com.nostra13.universalimageloader.core.ImageLoader;


public class MinuteToMinuteListItem extends ArrayAdapter<BaseEntity> {
	ArrayList<BaseEntity> elements;
	public MinuteToMinuteListItem(Context ctx,ArrayList<BaseEntity> items)
	{
		super(ctx,R.layout.minute_to_minute_list_item,items);
		this.elements=items;
	}
	public View getView(int position, View convertView, ViewGroup parent) {
	
		View row = convertView;
		if (row==null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.minute_to_minute_list_item, parent, false);
		}
		MinuteToMinuteItem item=(MinuteToMinuteItem)elements.get(position);
		TextView tv=(TextView)row.findViewById(R.id.jugador);
		tv.setText(item.getNombreJugador());
		tv=(TextView)row.findViewById(R.id.minuto);
		tv.setText(item.getMinuto());
		ImageLoader imageLoader=ImageUtils.getImageLoader(getContext());
		String url="";
		url=item.getLogoEquipo();
		imageLoader.displayImage(url, (ImageView) row.findViewById(R.id.logoEquipo),ImageUtils.getDefaultImageOptions());
		ImageView evento=(ImageView) row.findViewById(R.id.evento);
		if(item.getTipo().equals("GOL"))
		{
			evento.setImageDrawable(getContext().getResources().getDrawable(R.drawable.gol));
		}
		if(item.getTipo().equals("AMARILLA"))
		{
			evento.setImageDrawable(getContext().getResources().getDrawable(R.drawable.amarilla));
		}
		if(item.getTipo().equals("ROJA"))
		{
			evento.setImageDrawable(getContext().getResources().getDrawable(R.drawable.roja));
		}
		return(row); 
	}
}
