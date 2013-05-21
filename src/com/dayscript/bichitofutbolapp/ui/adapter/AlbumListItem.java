package com.dayscript.bichitofutbolapp.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dayscript.bichitofutbolapp.R;
import com.dayscript.bichitofutbolapp.persistence.entity.AlbumEntity;
import com.dayscript.bichitofutbolapp.persistence.entity.BaseEntity;
import com.dayscript.bichitofutbolapp.persistence.entity.MatchResult;
import com.dayscript.bichitofutbolapp.persistence.entity.NewsItem;
import com.dayscript.bichitofutbolapp.persistence.entity.Team;
import com.dayscript.bichitofutbolapp.utils.ImageUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class AlbumListItem extends ArrayAdapter<BaseEntity> {
	ArrayList<BaseEntity> elements;
	public AlbumListItem(Context ctx,ArrayList<BaseEntity> items)
	{
		super(ctx,R.layout.news_list_item,items);
		this.elements=items;
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout layout;
		View row = convertView;
		if (row==null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.gallery_album_list_item, parent, false);
		}
		AlbumEntity result=(AlbumEntity)elements.get(position);
		TextView tv=(TextView)row.findViewById(R.id.tituloAlbum);
		tv.setText(result.getTitulo());
		if(position%2!=0){
			layout = (LinearLayout)row.findViewById(R.id.album_layout);
			layout.setBackgroundColor(Color.parseColor("#E0E0E0"));
		}
		
		tv= (TextView)row.findViewById(R.id.fechaPublicacionAlbum);
		tv.setText(result.getFechaPublicacion());
		
		tv= (TextView)row.findViewById(R.id.numeroFotos);
		String numeroFotos = result.getNumeroFotos(); 
		tv.setText(numeroFotos+" "+"Fotos");
		
		ImageLoader imageLoader=ImageUtils.getImageLoader(getContext());
		String url="";
		url=((AlbumEntity) elements.get(position)).getImagen_portada();
		imageLoader.displayImage(url, (ImageView) row.findViewById(R.id.galeria_album_portada),ImageUtils.getDefaultImageOptions());
		return(row);
	}
}
