package com.deepfakeapp;

import static com.facebook.imagepipeline.nativecode.NativeJpegTranscoder.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.deepfakeapp.notifications.NotificationForDetection;
import com.deepfakeapp.services.NotificationToRecordService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ScreenRecordActivity extends AppCompatActivity {
    private String FILE_NAME;
    private MediaProjectionManager mediaProjectionManager;
    private MediaProjection mediaProjection;
    private VirtualDisplay virtualDisplay;
    private MediaRecorder mediaRecorder;
    private int DISPLAY_WIDTH;
    private int DISPLAY_HEIGHT;
    private Intent foregroundServiceIntent;

    ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                int resultCode = result.getResultCode();
                Intent data = result.getData();
                if (result.getResultCode() == Activity.RESULT_OK) {
                    assert data != null;
                    mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
                    if (mediaProjection != null) {
                        initRecorder();
                        createVirtualDisplay();
                        startScreenCapture();
                        Toast.makeText(this, "Recording...", Toast.LENGTH_SHORT).show();
                    }
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(this, "Screen capture permission not available.", Toast.LENGTH_SHORT).show();
                }
            });

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_record);

        Resources res = getApplicationContext().getResources();
        FILE_NAME = res.getString(R.string.FILE_NAME);
        DISPLAY_WIDTH = res.getInteger(R.integer.DISPLAY_WIDTH);
        DISPLAY_HEIGHT = res.getInteger(R.integer.DISPLAY_HEIGHT);

        mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);

        mGetContent.launch(mediaProjectionManager.createScreenCaptureIntent());

        foregroundServiceIntent = new Intent(getApplicationContext(), NotificationToRecordService.class);
        startForegroundService(foregroundServiceIntent);
    }


    // Thực hiện quay màn hình bằng cách execute thread mới để tránh xung đột
    private void startScreenCapture() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(() -> mediaRecorder.start(), 3, TimeUnit.SECONDS);

        finish();

        executor.schedule(() -> {
            stopScreenRecording();
            sendVideoToApi();
            executor.shutdown();
        }, 7, TimeUnit.SECONDS);
    }

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
        stopService(foregroundServiceIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Cancel all notifications associated with your activity
        notificationManager.cancelAll();
    }

    private void sendVideoToApi() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("video_file", FILE_NAME,
                        RequestBody.create(new File(setupFilePath()), MediaType.parse("video/mp4")))
                .build();

        Request request = new Request.Builder()
                .url("https://api-deepfake-detection-1.onrender.com/upload-video/")
                .post(requestBody)
                .build();

        Log.d(TAG, "sendVideoToApi: " + requestBody);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful() || response.body() == null) {
                    throw new IOException("Unexpected code " + response);
                }

                // Read the response
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    boolean result = jsonObject.getBoolean("result");
                    Log.d(TAG, "Result: " + result);
                    NotificationForDetection notification = new NotificationForDetection(ScreenRecordActivity.this, result);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        notification.showNotification();
                    }
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

    // Tạo virtualDisplay chứa toàn bộ màn hình
    private void createVirtualDisplay() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = windowManager.getDefaultDisplay();
        defaultDisplay.getMetrics(displayMetrics);
        int screenDensity = displayMetrics.densityDpi;
        virtualDisplay = mediaProjection.createVirtualDisplay("ScreenRecord",
                DISPLAY_WIDTH, DISPLAY_HEIGHT, screenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mediaRecorder.getSurface(), null, null);
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
        mediaRecorder.setVideoFrameRate(24);
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
        String filePath = directory.getAbsolutePath() + "/" + FILE_NAME;

        // Tạo thư mục nếu nó không tồn tại
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                Log.e(TAG, "Failed to create directory");
                return "";
            }
        }

        return filePath;
    }
}