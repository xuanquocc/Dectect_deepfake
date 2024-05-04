package com.deepfakeapp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class BoundService extends Service {
    private final IBinder binder = new LocalBinder();
    public class LocalBinder extends Binder {
        BoundService getService() {
            return BoundService.this;
        }   
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("Unbind","Unbind "+intent.toString());
        return super.onUnbind(intent);
    }
}
