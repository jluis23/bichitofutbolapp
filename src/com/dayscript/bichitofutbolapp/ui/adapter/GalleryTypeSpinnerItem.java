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


public class GalleryTypeSpinnerItem extends ArrayAdapter<String> {
		ArrayList<String> elements;
		public GalleryTypeSpinnerItem(Context ctx,ArrayList<String> items)
		{
			super(ctx,R.layout.news_list_item,items);
			this.elements=items;
		}
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			if (row==null) {
				LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row=inflater.inflate(R.layout.media_gallery_spinner_item, parent, false);
			}
		
			String result=(String)elements.get(position);
			TextView tv=(TextView)row.findViewById(R.id.description);
			tv.setText(result);
			if(position==1)	
			{
				//ImageView iv=(ImageView)row.findViewById(R.id.mediaTypeImage);
				//iv.setImageDrawable(this.getContext().getResources().getDrawable(R.drawable.icono_video));
			}
			return(row);
		}
	}