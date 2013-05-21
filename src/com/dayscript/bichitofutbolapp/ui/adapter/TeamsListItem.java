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
import com.dayscript.bichitofutbolapp.persistence.entity.Team;
import com.dayscript.bichitofutbolapp.utils.ImageUtils;
import com.nostra13.universalimageloader.core.ImageLoader;




public class TeamsListItem extends ArrayAdapter<BaseEntity> {
	ArrayList<BaseEntity> elements;
	public TeamsListItem(Context ctx,ArrayList<BaseEntity> items)
	{
		super(ctx,R.layout.news_list_item,items);
		this.elements=items;
	}
	public View getView(int position, View convertView, ViewGroup parent) {
	
		View row = convertView;

	
		if (row==null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.teams_list_item, parent, false);
		}
	
		TextView titulo=(TextView)row.findViewById(R.id.nombreEquipo);
		titulo.setText(((Team) elements.get(position)).getName());
		ImageLoader imageLoader=ImageUtils.getImageLoader(getContext());
		String url="";
		url=((Team) elements.get(position)).getImg();
		imageLoader.displayImage(url, (ImageView) row.findViewById(R.id.imageView1),ImageUtils.getDefaultImageOptions());
		return(row); 
	}
}
