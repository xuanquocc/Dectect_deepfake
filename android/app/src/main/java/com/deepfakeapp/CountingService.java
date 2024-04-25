package com.deepfakeapp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class CountingService extends Service {
    private static final String TAG = "CountingService";
    private final IBinder binder = new LocalBinder();
    private boolean isCounting = false;
    private int count = 0;

    public class LocalBinder extends Binder {
        CountingService getService() {
            return CountingService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void startCounting() {
        isCounting = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isCounting) {
                    Log.d(TAG, "Count: " + count);
                    count++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void stopCounting() {
        isCounting = false;
    }
}
