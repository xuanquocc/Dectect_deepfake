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

public class BoundServiceModule extends ReactContextBaseJavaModule {
    private static final String TAG = "BoundServiceModule";
    private BoundService boundService;
    private Boolean isServiceConnected;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            BoundService.LocalBinder myBinder = (BoundService.LocalBinder) iBinder;
            boundService = myBinder.getService();
            boundService.startMusic();
            isServiceConnected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isServiceConnected = false;
        }
    };
    public BoundServiceModule(ReactApplicationContext reactContext) {
        super(reactContext);
        boundService = new BoundService();
    }

    @Override
    public String getName() {
        return TAG;
    }

    @ReactMethod
    public void startMusic() {
        Log.d("Start from React Native","Start music");
        Intent intent = new Intent(getCurrentActivity(), BoundService.class);
        getCurrentActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @ReactMethod
    public void stopMusic() {
        boundService.stopMusic();
    }

    @ReactMethod
    public void allowPermission()
    {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        getCurrentActivity().startActivity(intent);
    }

}
