package com.deepfakeapp;

import android.accessibilityservice.AccessibilityService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class DetectAppService extends AccessibilityService {
    private CountingService boundService;
    private boolean isBound = false;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CountingService.LocalBinder binder = (CountingService.LocalBinder) service;
            boundService = binder.getService();
            isBound = true;
            Log.d("DetectAppService", "Service connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
            Log.d("DetectAppService", "Service disconnected");
        }
    };
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            // Lấy thông tin về ứng dụng hiện tại được mở
            String currentApp = event.getPackageName().toString();
            Log.d("Name ap"," name app is " + currentApp );
            if (currentApp != null && "com.facebook.orca".equals(currentApp)) {
                if (!isBound) {
                    Intent intent = new Intent(this, CountingService.class);
                    bindService(intent, serviceConnection, BIND_AUTO_CREATE);
                }
            }
        }
    }

    @Override
    public void onInterrupt() {
        Log.e("Error","interrupt");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
    }
}
