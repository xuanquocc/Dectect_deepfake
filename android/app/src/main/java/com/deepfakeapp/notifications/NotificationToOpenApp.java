package com.deepfakeapp.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.deepfakeapp.R;
import com.deepfakeapp.ScreenRecordActivity;

public class NotificationToOpenApp {
    private final Context context;

    public NotificationToOpenApp(Context context) {
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showNotification() {
        Resources res = context.getResources();
        int NOTIFICATION_ID = res.getInteger(R.integer.NOTIFICATION_1);
        String CHANNEL_ID = res.getString(R.string.CHANNEL_1);
        CharSequence CHANNEL_NAME = res.getString(R.string.CHANNEL_NAME_1);
        CharSequence TITLE = res.getString(R.string.OPEN_APP_RECOMMENDATION_TITLE);
        CharSequence CONTENT_TEXT = res.getString(R.string.ASK_TO_OPEN_APP_MESSAGE);

        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
        );
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        Intent activityIntent = new Intent(context, ScreenRecordActivity.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, activityIntent, PendingIntent.FLAG_MUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(TITLE)
                .setContentText(CONTENT_TEXT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
