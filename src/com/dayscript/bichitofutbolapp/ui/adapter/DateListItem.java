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
import com.dayscript.bichitofutbolapp.persistence.entity.MatchResult;
import com.dayscript.bichitofutbolapp.persistence.entity.NewsItem;
import com.dayscript.bichitofutbolapp.persistence.entity.Team;
import com.dayscript.bichitofutbolapp.utils.ImageUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DateListItem extends ArrayAdapter<BaseEntity> {
	ArrayList<BaseEntity> elements;
	Context ctx;
	public DateListItem(Context ctx,ArrayList<BaseEntity> items)
	{
		super(ctx,R.layout.news_list_item,items);
		this.elements=items;
		this.ctx = ctx;
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row==null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.match_result_list_item, parent, false);
		}
	
		MatchResult result=(MatchResult)elements.get(position);
		TextView tv=(TextView)row.findViewById(R.id.txtEquipo1);
		tv.setText(result.getTeam1());
		tv=(TextView)row.findViewById(R.id.txtEquipo2);
		tv.setText(result.getTeam2());
		tv=(TextView)row.findViewById(R.id.golesEquipo1);
		tv.setText(result.getGoalsTeam1());
		tv=(TextView)row.findViewById(R.id.golesEquipo2);
		tv.setText(result.getGoalsTeam2());
		
		ImageLoader imageLoader=ImageUtils.getImageLoader(getContext());
		String url1="";
		url1=result.getLogoTeam1();
		imageLoader.displayImage(url1, (ImageView) row.findViewById(R.id.logoEquipo1),ImageUtils.getDefaultImageOptions());
		
		ImageLoader imageLoader2=ImageUtils.getImageLoader(getContext());
		String url2="";
		url2= result.getLogoTeam2();
		imageLoader2.displayImage(url2, (ImageView) row.findViewById(R.id.logoEquipo2),ImageUtils.getDefaultImageOptions());
		
		ImageView estado =  (ImageView) row.findViewById(R.id.imagen_estado_partido);
		if(result.getEstado().equalsIgnoreCase("Terminado")){
			estado.setImageDrawable(this.ctx.getResources().getDrawable(R.drawable.finalizado));
		}else if(result.getEstado().equalsIgnoreCase("Jugando")){
			estado.setImageDrawable(this.ctx.getResources().getDrawable(R.drawable.boton_activo_verde));
		}else if(result.getEstado().equalsIgnoreCase("Medio_tiempo") || result.getEstado().equalsIgnoreCase("Medio Tiempo")){
			estado.setImageDrawable(this.ctx.getResources().getDrawable(R.drawable.medio_tiempo));
		}
		return(row);
	}
}
