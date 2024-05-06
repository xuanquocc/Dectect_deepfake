package com.deepfakeapp;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;

import androidx.annotation.RequiresApi;

import com.deepfakeapp.notifications.NotificationToOpenApp;

public class CallDetectionService extends AccessibilityService {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        if (eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            // Kiểm tra thông báo
            String eventText = event.getPackageName().toString();
            if ((("com.facebook.orca".equals(eventText)
                    && event.getParcelableData().toString().contains("voip_incoming"))
                    || ("com.zing.zalo".equals(eventText)
                    && event.getParcelableData().toString().contains("call_channel")))) {
                NotificationToOpenApp notification = new NotificationToOpenApp(this);
                notification.showNotification();
            }
        }
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        setServiceInfo(info);
    }
}
