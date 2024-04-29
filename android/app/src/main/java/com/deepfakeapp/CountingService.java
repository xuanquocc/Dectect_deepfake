package com.deepfakeapp;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class CountingService extends Service {
    private static final String TAG = "CountingService";
    private final IBinder binder = new LocalBinder();
    private MediaPlayer mediaPlayer;

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
        if(mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.music);
        }
        mediaPlayer.start();
    }

    public void stopCounting() {
        mediaPlayer.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("onDestroy","Task was destroyed");
        if(mediaPlayer != null)
        {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("Unbind","Unbind "+intent.toString());
        return super.onUnbind(intent);
    }
}
