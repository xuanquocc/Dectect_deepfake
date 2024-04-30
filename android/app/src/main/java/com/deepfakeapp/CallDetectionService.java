package com.deepfakeapp;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.view.accessibility.AccessibilityEvent;
import android.util.Log;

public class CallDetectionService extends AccessibilityService {
    private static final String TAG = "CallDetectionService";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        Log.d(TAG, "hello");
        if (eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            // Kiểm tra thông báo
            Log.d(TAG, "message");

            String eventText = event.getPackageName().toString();
            Log.e(TAG, "evetntext" + event.toString());
            if (eventText != null && ("com.facebook.orca".equals(eventText)) && event.getParcelableData().toString().contains("voip_incoming")) {
                Log.i(TAG, "Incoming call is detected");
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
