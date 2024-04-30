package com.deepfakeapp;

import android.content.Context;
import android.content.Intent;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class CallDetectionModule extends ReactContextBaseJavaModule {
    private static final String MODULE_NAME = "CallDetectionModule";

    public CallDetectionModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @ReactMethod
    public void startCallDetectionService() {
        Context context = getReactApplicationContext();
        Intent intent = new Intent(context, CallDetectionService.class);
        context.sendBroadcast(intent);
    }

    @ReactMethod
    public void stopCallDetectionService() {
        // Không cần implement phương thức này vì không dừng được dịch vụ từ broadcast
    }
}
