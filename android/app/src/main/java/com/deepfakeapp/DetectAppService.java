package com.deepfakeapp;

import android.accessibilityservice.AccessibilityService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class DetectAppService extends AccessibilityService {
    private CountingService boundService;
    private boolean isBound;
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
            String currentApp = event.getPackageName().toString();
            Log.e("App", "App is " + currentApp);
            Intent intent = new Intent(this, CountingService.class);
            if (currentApp != null && ("com.facebook.orca".equals(currentApp) || "com.zing.zalo".equals(currentApp) )) {
                if (!isBound) {
                    bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
                    Log.e("DetectAppService", "Bind CountingService successfully");
                }
            }
        }
    }


    @Override
    public void onInterrupt() {
        Log.e("DetectAppService", "Accessibility service interrupted");
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
