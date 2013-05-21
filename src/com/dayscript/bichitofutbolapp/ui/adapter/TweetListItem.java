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
import com.dayscript.bichitofutbolapp.persistence.entity.TweetEntity;
import com.dayscript.bichitofutbolapp.utils.ImageUtils;
import com.nostra13.universalimageloader.core.ImageLoader;


public class TweetListItem extends ArrayAdapter<BaseEntity> {
	ArrayList<BaseEntity> elements;
	int NORMAL=0,MODIFICADO=1;
	public TweetListItem(Context ctx,ArrayList<BaseEntity> items)
	{
		super(ctx,R.layout.tweet_list_item,items);
		this.elements=items;
	}
	
	@Override
	public int getItemViewType(int position) {
		
		if(position==0)
		{
			return MODIFICADO;
		}
		else return NORMAL;
		//return super.getItemViewTypºe(position);
	}

	@Override
	public int getViewTypeCount() {
	
		return 2;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row==null ) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.tweet_list_item, parent, false);
		}

		TweetEntity tweet = (TweetEntity) elements.get(position);
		TextView tw=(TextView)row.findViewById(R.id.tweetName);
		tw.setText(tweet.getUserName());
		
		tw=(TextView)row.findViewById(R.id.tweetUserName);
		tw.setText(("@"+tweet.getUser()));
		
		tw=(TextView)row.findViewById(R.id.tweetText);
		tw.setText((tweet.getText()));
		
		tw=(TextView)row.findViewById(R.id.tweetFecha);
		tw.setText((tweet.getDateFormat()));
		
		ImageLoader imageLoader=ImageUtils.getImageLoader(getContext());
		String url="";
		url= tweet.getProfileImage();
		imageLoader.displayImage(url, (ImageView) row.findViewById(R.id.tweetImageProfile),ImageUtils.getDefaultImageOptions());
		return row;
	}
}
