package com.dayscript.bichitofutbolapp.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import com.dayscript.bichitofutbolapp.R;

public class NotificationsHelper {
	public static void showNotification(Context ctx,String title, String text)
	{
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(ctx)
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setContentTitle(title)
		        .setContentText(text).setSound(Uri.parse("android.resource://" + ctx.getPackageName() +"/" + R.raw.notificacion_bichito));
		        mBuilder.setAutoCancel(true);
		        mBuilder.setContentIntent(PendingIntent.getActivity(ctx, 0, new Intent(), 0));
		        NotificationManager mNotificationManager =
		    (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(2, mBuilder.build());
	}
}
