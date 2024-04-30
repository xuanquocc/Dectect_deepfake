package com.deepfakeapp;

import static com.facebook.imagepipeline.nativecode.NativeJpegTranscoder.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ScreenRecordModule extends ReactContextBaseJavaModule implements ActivityEventListener {
    private static final String MODULE_NAME = "ScreenRecordModule";
    private static final int REQUEST_CODE_SCREEN_RECORD = 1;
    private static final String API_URL = "https://api-deepfake-detection-1.onrender.com/upload-video/";
    private final MediaProjectionManager mediaProjectionManager;
    private MediaProjection mediaProjection;
    private VirtualDisplay virtualDisplay;
    private MediaRecorder mediaRecorder;
    private final int DISPLAY_WIDTH = 720;
    private final int DISPLAY_HEIGHT = 1280;

    public ScreenRecordModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mediaProjectionManager = (MediaProjectionManager) reactContext.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        reactContext.addActivityEventListener(this);
    }

    @NonNull
    @Override
    public String getName() {
        return MODULE_NAME;
    }

    @ReactMethod
    public void stopScreenRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
        }

        if (virtualDisplay != null) {
            virtualDisplay.release();
            virtualDisplay = null;
        }

        if (mediaProjection != null) {
            mediaProjection.stop();
            mediaProjection = null;
        }

        sendVideoToApi();
    }

    private void sendVideoToApi(){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS) // Increase the connection timeout
                .readTimeout(30, TimeUnit.SECONDS) // Increase the read timeout
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        File videoFile = new File(setupFilePath());

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("video_file", videoFile.getName(),
                        RequestBody.create(videoFile, MediaType.parse("video/mp4")))
                .build();

        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                // Read the response
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    Log.d(TAG, "Result: " + jsonObject.getBoolean("result"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "onFailure: " + e);
            }
        });
    }

    // Quay màn hình
    // Create a notification channel for Android Oreo and above
    @RequiresApi(api = Build.VERSION_CODES.O)
    @ReactMethod
    public void startScreenRecording() {
        Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            return;
        }

        if (mediaProjectionManager != null) {
            currentActivity.startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), REQUEST_CODE_SCREEN_RECORD);
        }

        // Chạy foreground service (bắt buộc để hiển thị thông báo đang quay trên thanh thông báo)
        Intent foregroundServiceIntent = new Intent(currentActivity, ScreenRecordService.class);
        currentActivity.startForegroundService(foregroundServiceIntent);
    }

    // Xử lý kết quả trả về sau khi chọn các lựa chọn hiển thị trên màn hình
    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_SCREEN_RECORD && resultCode == Activity.RESULT_OK) {
            assert data != null;
            mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
            if (mediaProjection != null) {
                initRecorder();
                createVirtualDisplay();
                Toast.makeText(getReactApplicationContext(), "Recording...", Toast.LENGTH_SHORT).show();
                startScreenCapture();
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            // Quyền bị từ chối, dừng dịch vụ nền
            Intent foregroundServiceIntent = new Intent(activity, ScreenRecordService.class);
            activity.stopService(foregroundServiceIntent);
            Toast.makeText(getReactApplicationContext(), "Screen capture permission not available.", Toast.LENGTH_SHORT).show();
        }
    }

    // Tạo virtualDisplay chứa toàn bộ màn hình
    private void createVirtualDisplay() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getReactApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = windowManager.getDefaultDisplay();
        defaultDisplay.getMetrics(displayMetrics);
        int screenDensity = displayMetrics.densityDpi;
        virtualDisplay = mediaProjection.createVirtualDisplay("ScreenRecord",
                DISPLAY_WIDTH, DISPLAY_HEIGHT, screenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mediaRecorder.getSurface(), null, null);
    }

    // Thực hiện quay màn hình bằng cách execute thread mới để tránh xung đột
    private void startScreenCapture() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> mediaRecorder.start());

        new Handler().postDelayed(this::stopScreenRecording, 3000);
    }

    // Lớp con để xử lý sự kiện khi MediaProjection dừng
    private class MediaProjectionCallback extends MediaProjection.Callback {
        @Override
        public void onStop() {
            stopScreenRecording();
        }
    }

    // Khởi tạo MediaRecorder để ghi màn hình
    private void initRecorder() {
        MediaProjection.Callback mediaProjectionCallBack = new MediaProjectionCallback();
        mediaProjection.registerCallback(mediaProjectionCallBack, null);

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setVideoEncodingBitRate(512 * 1000);
        mediaRecorder.setVideoFrameRate(30);
        mediaRecorder.setVideoSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        mediaRecorder.setOutputFile(setupFilePath());

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String setupFilePath() {
        // Lấy thư mục Videos trong bộ nhớ ngoài
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "DeepfakeApp");

        // Tạo đường dẫn đầy đủ của file
        String filePath = directory.getAbsolutePath() + "/deepfake_check.mp4";

        // Tạo thư mục nếu nó không tồn tại
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                Log.e(TAG, "Failed to create directory");
                return "";
            }
        }

        return filePath;
    }

    @Override
    public void onNewIntent(Intent intent) {
    }
}
