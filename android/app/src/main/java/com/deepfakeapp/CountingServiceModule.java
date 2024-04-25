package com.deepfakeapp;

import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class CountingServiceModule extends ReactContextBaseJavaModule {
    private static final String TAG = "CountingServiceModule";
    private CountingService countingService;

    public CountingServiceModule(ReactApplicationContext reactContext) {
        super(reactContext);
        countingService = new CountingService();
    }

    @Override
    public String getName() {
        return "CountingService";
    }

    @ReactMethod
    public void startCounting() {
        countingService.startCounting();
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
