package com.dayscript.bichitofutbolapp.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.dayscript.bichitofutbolapp.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

public class ImageUtils {
	public static ImageLoader getImageLoader(Context ctx)
	{
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(ctx));
		return imageLoader;
	}
	public static DisplayImageOptions getDefaultImageOptions()
	{
		DisplayImageOptions options = new DisplayImageOptions.Builder()
        .showStubImage(R.drawable.ic_launcher)
        .resetViewBeforeLoading()
        .cacheInMemory()
        .showImageForEmptyUri(R.drawable.ic_launcher)
        .cacheOnDisc()
        .imageScaleType(ImageScaleType.EXACTLY_STRETCHED) // default
        .bitmapConfig(Bitmap.Config.ARGB_8888) // default
        .delayBeforeLoading(1000)
        .displayer(new SimpleBitmapDisplayer()) // default
        .build();
		return options;
	}
}
