package com.deepfakeapp;

import android.accessibilityservice.AccessibilityService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class DetectAppOpenedService extends AccessibilityService {
    private BoundService boundService;
    private boolean isBound;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BoundService.LocalBinder binder = (BoundService.LocalBinder) service;
            boundService = binder.getService();
            isBound = true;

            Log.d("DetectAppOpenedService", "Service connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
            Log.d("DetectAppOpenedService", "Service disconnected");
        }
    };

//    @Override
//    public void onCreate() {
//        super.onCreate();
//        if (appChecker.isAppRunning("com.facebook.orca")) {
//            Log.v("Checker","messenger is running");
//        }
//    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            String currentApp = event.getPackageName().toString();
            Log.e("App", "App is " + currentApp);
            Intent intent = new Intent(this, BoundService.class);
            if (currentApp != null && ("com.facebook.orca".equals(currentApp) || "com.zing.zalo".equals(currentApp) )) {
                if (!isBound) {
                    bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
                    Log.e("DetectAppOpenedService", "Bind BoundService successfully");
                }
            }
        }
    }


    @Override
    public void onInterrupt() {
        Log.e("DetectAppOpenedService", "Accessibility service interrupted");
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
