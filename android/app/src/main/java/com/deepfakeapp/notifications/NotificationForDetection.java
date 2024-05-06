package com.deepfakeapp.notifications;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.deepfakeapp.R;
import com.deepfakeapp.ScreenRecordActivity;

public class NotificationForDetection {
    private final Context context;

    public NotificationForDetection(Context context) {
        this.context = context;
    }

    public void showNotification() {
        Resources res = context.getResources();
        int NOTIFICATION_ID = res.getInteger(R.integer.NOTIFICATION_3);
        String CHANNEL_ID = res.getString(R.string.CHANNEL_3);
        CharSequence CHANNEL_NAME = res.getString(R.string.CHANNEL_NAME_3);
        CharSequence TITLE = res.getString(R.string.DETECTED_TITLE);
        CharSequence CONTENT_TEXT = res.getString(R.string.DETECTED_MESSAGE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Intent activityIntent = new Intent(context, ScreenRecordActivity.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, activityIntent, PendingIntent.FLAG_MUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(TITLE)
                .setContentText(CONTENT_TEXT)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
    }
}
