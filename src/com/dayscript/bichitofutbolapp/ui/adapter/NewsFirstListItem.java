package com.dayscript.bichitofutbolapp.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dayscript.bichitofutbolapp.R;
import com.dayscript.bichitofutbolapp.persistence.entity.BaseEntity;
import com.dayscript.bichitofutbolapp.persistence.entity.NewsItem;
import com.dayscript.bichitofutbolapp.utils.ImageUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class NewsFirstListItem extends ArrayAdapter<BaseEntity> {
	ArrayList<BaseEntity> elements;
	public NewsFirstListItem(Context ctx,ArrayList<BaseEntity> items)
	{
		super(ctx,R.layout.news_list_item,items);
		this.elements=items;
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (((NewsItem) elements.get(position)).getId()==1 && row==null) {
			Log.v("Item nuevo: ", "id : "+Integer.toString(((NewsItem) elements.get(position)).getId()));
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.first_list_item, parent, false);
		}else if(((NewsItem) elements.get(position)).getId()>1 && row==null){
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.news_list_item, parent, false);
		}
		
		TextView label=(TextView)row.findViewById(R.id.textoNoticia);
		label.setText(((NewsItem) elements.get(position)).getSummary());
		label.setVisibility(View.VISIBLE);
		TextView titulo=(TextView)row.findViewById(R.id.tituloNoticia);
		titulo.setText(((NewsItem) elements.get(position)).getTitle());
		TextView fecha=(TextView)row.findViewById(R.id.fechaNoticia);
		fecha.setText(((NewsItem) elements.get(position)).getFechaCreacion());
		ImageLoader imageLoader=ImageUtils.getImageLoader(getContext());
		String url="";
		url=((NewsItem) elements.get(position)).getImg();
		imageLoader.displayImage(url, (ImageView) row.findViewById(R.id.imageView1),ImageUtils.getDefaultImageOptions());
		return(row); 
	}
}
