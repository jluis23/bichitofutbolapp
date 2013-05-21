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
import com.dayscript.bichitofutbolapp.persistence.entity.Player;




public class PlayersListItem extends ArrayAdapter<BaseEntity> {
	ArrayList<BaseEntity> elements;
	public PlayersListItem(Context ctx,ArrayList<BaseEntity> items)
	{
		super(ctx,R.layout.news_list_item,items);
		this.elements=items;
	}
	public View getView(int position, View convertView, ViewGroup parent) {
	
		View row = convertView;

	
		if (row==null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.player_list_item, parent, false);
		}
		Player jugador=(Player)elements.get(position);
		TextView number=(TextView)row.findViewById(R.id.numero);
		number.setText(jugador.getNumber());
		number.setVisibility(View.GONE);
		TextView nombre=(TextView)row.findViewById(R.id.nombre);
		nombre.setText(jugador.getName() + " " + jugador.getSurname());
		TextView pos=(TextView)row.findViewById(R.id.position);
		pos.setText(jugador.getPosition());
		return(row); 
	}
}
