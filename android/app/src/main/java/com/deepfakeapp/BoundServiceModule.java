package com.deepfakeapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class CountingServiceModule extends ReactContextBaseJavaModule {
    private static final String TAG = "CountingServiceModule";
    private CountingService countingService;
    private Boolean isServiceConnected;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            CountingService.LocalBinder myBinder = (CountingService.LocalBinder) iBinder;
            countingService = myBinder.getService();
            countingService.startCounting();
            isServiceConnected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isServiceConnected = false;
        }
    };
    public CountingServiceModule(ReactApplicationContext reactContext) {
        super(reactContext);
        countingService = new CountingService();
    }

    @Override
    public String getName() {
        return TAG;
    }

    @ReactMethod
    public void startCounting() {
        Log.d("Start from React Native","Start music");
        Intent intent = new Intent(getCurrentActivity(), CountingService.class);
        getCurrentActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @ReactMethod
    public void stopCounting() {
        countingService.stopCounting();
    }

    @ReactMethod
    public void allowPermission()
    {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        getCurrentActivity().startActivity(intent);
    }

}
