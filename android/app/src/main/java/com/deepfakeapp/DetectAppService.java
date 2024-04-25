package com.deepfakeapp;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class DetectAppService extends AccessibilityService {
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            // Lấy thông tin về ứng dụng hiện tại được mở
            String currentApp = event.getPackageName().toString();
            Log.i("App nme","App name is: "+ currentApp);
        }
    }

    @Override
    public void onInterrupt() {
        Log.e("Error","interrupt");
    }
}
