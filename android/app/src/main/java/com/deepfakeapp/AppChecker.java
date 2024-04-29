package com.deepfakeapp;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class AppChecker {
    private Context context;

    public AppChecker(Context context) {
        this.context = context;
    }

    public boolean isAppRunning(String packageName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = activityManager.getRunningAppProcesses();
            if (runningProcesses != null) {
                for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                    Log.e("X ",processInfo.processName);
                    if (processInfo.processName.equals(packageName)) {
                        return true; // Tiến trình của ứng dụng đang chạy
                    }
                }
            }
        }
        return false; // Tiến trình của ứng dụng không đang chạy
    }
}