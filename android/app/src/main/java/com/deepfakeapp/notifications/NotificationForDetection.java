package com.deepfakeapp.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.deepfakeapp.R;

public class NotificationForDetection {
    private final Context context;
    private final boolean isDetected;

    public NotificationForDetection(Context context, boolean isDetected) {
        this.context = context;
        this.isDetected = isDetected;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showNotification() {
        Resources res = context.getResources();
        int NOTIFICATION_ID = res.getInteger(R.integer.NOTIFICATION_3);
        String CHANNEL_ID = res.getString(R.string.CHANNEL_3);
        CharSequence CHANNEL_NAME = res.getString(R.string.CHANNEL_NAME_3);
        CharSequence TITLE;
        CharSequence CONTENT_TEXT;
        int IMPORTANCE;

        if (isDetected) {
            TITLE = res.getString(R.string.DETECTED_TITLE);
            CONTENT_TEXT = res.getString(R.string.DETECTED_MESSAGE);
            IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;
        } else {
            TITLE = res.getString(R.string.NOT_DETECTED_TITLE);
            CONTENT_TEXT = res.getString(R.string.NOT_DETECTED_MESSAGE);
            IMPORTANCE = NotificationManager.IMPORTANCE_LOW;
        }

        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                IMPORTANCE
        );
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(TITLE)
                .setContentText(CONTENT_TEXT);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
