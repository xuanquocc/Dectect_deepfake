package com.deepfakeapp;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;

import com.deepfakeapp.services.NotificationToOpenAppService;

public class CallDetectionService extends AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        if (eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            // Kiểm tra thông báo
            String eventText = event.getPackageName().toString();
            if ((("com.facebook.orca".equals(eventText) && event.getParcelableData().toString().contains("voip_incoming")) || ("com.zing.zalo".equals(eventText) && event.getParcelableData().toString().contains("call_channel")))) {
                Intent foregroundServiceIntent = new Intent(this, NotificationToOpenAppService.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(foregroundServiceIntent);
                }
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
