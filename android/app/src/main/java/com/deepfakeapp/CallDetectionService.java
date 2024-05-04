package com.deepfakeapp;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraMetadata;
import android.media.AudioManager;
import android.view.accessibility.AccessibilityEvent;
import android.util.Log;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;

import java.util.Arrays;

public class CallDetectionService extends AccessibilityService {
    private static final String TAG = "CallDetectionService";


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        if (eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            // Kiểm tra thông báo
            String eventText = event.getPackageName().toString();
//            Log.e(TAG, "evetntext" + event.toString());
            if (eventText != null && (("com.facebook.orca".equals(eventText) && event.getParcelableData().toString().contains("voip_incoming")) ||  ("com.zing.zalo".equals(eventText) && event.getParcelableData().toString().contains("call_channel"))) ) {
                Log.i(TAG, "Incoming call is detected");
                // Kiểm tra xem người dùng đã bấm trả lời hay không
//                if (isCameraOpened()) {
//                    Log.i(TAG, "Camera is opened");
//                } else {
//                    Log.i(TAG, "Camera is not opened");
//                }
//                if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED) {
//                    String packageName = event.getPackageName().toString();
//                    String className = event.getClassName().toString();
//                    if ("android.widget.Button".equals(className)) {
//                        // Kiểm tra xem gói ứng dụng và lớp có phù hợp với Messenger
//                        CharSequence text = (CharSequence) event.getText();
//                        if (text != null && ("ANSWER".equals(text.toString()) || ("Answer".equals(text.toString())))) {
//                            // Người dùng đã bấm nút "ANSWER" trong Messenger
//                            // Tiến hành kích hoạt chức năng quay màn hình
//                            Log.e(TAG, "thành công " );
//                        }
//                    }
//                }

            }
        }
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        setServiceInfo(info);
    }

//    public void checkCameraStatus(Context context) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
//            try {
//                String[] cameraIdList = cameraManager.getCameraIdList();
//                Log.d(TAG, "checkCameraStatus: " + Arrays.toString(cameraIdList));
//
//                for (String cameraId : cameraIdList) {
//                    boolean isOpen = cameraManager.getCameraCharacteristics(cameraId).get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL) != CameraMetadata.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY;
//                    if (isOpen) {
//                        Log.i(TAG, "Camera is opened");
//                        return;
//                    }
//                }
//                Log.i(TAG, "Camera is not opened");
//            } catch (CameraAccessException e) {
//                Log.e(TAG, "Camera access exception: " + e.getMessage());
//            }
//        } else {
//            // Handle older versions of Android where Camera2 API is not available
//            Log.i(TAG, "Camera status cannot be checked on this device");
//        }
//    }

//    private boolean isCameraOpened() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
//            try {
//                String[] cameraIdList = cameraManager.getCameraIdList();
//                for (String cameraId : cameraIdList) {
//                    CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
//                    Boolean available = characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL) != CameraMetadata.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY;
//
//                    if (available != null && available) {
//                        // Camera đã được mở
//                        Log.i(TAG, "Camera is opened");
//                        return true;
//                    }
//                }
//            } catch (CameraAccessException e) {
//                Log.e(TAG, "Camera access exception: " + e.getMessage());
//            }
//        } else {
//            Log.i(TAG, "Camera status cannot be checked on this device");
//        }
//        return false;
//    }



}
