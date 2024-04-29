package com.deepfakeapp;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class BoundService extends Service {
    private static final String TAG = "BoundService";
    private final IBinder binder = new LocalBinder();
    private MediaPlayer mediaPlayer;
    private AppChecker appChecker;
    public class LocalBinder extends Binder {
        BoundService getService() {
            return BoundService.this;
        }   
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void startMusic() {
        if(mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.music);
        }
        mediaPlayer.start();
    }

    public void stopMusic() {
        mediaPlayer.pause();
    }

//    @Override
//    public void onCreate() {
//        super.onCreate();
//        appChecker = new AppChecker(getApplicationContext());
//        if (appChecker.isAppRunning("com.facebook.orca")) {
//            Log.v("Checker","messenger is running");
//        }
//    }

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
