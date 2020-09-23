package com.app.xq.flashlight;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Android-小强
 * @email: 15075818555@163.com
 * @data: on 2020/9/23 13:32
 */
class FlashlightUtils {


    static {
        try {
            Class.forName("android.hardware.Camera");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private Camera mCamera;
    private CameraManager manager;
    private boolean isSos = false;
    private TimerTask mTimerTask;
    private Timer mTimer;
    private int mInt = 0;
    private Context context;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    lightsOn(context, true);
                    break;
                case 0:
                    lightsOff(true);
                    break;
            }
        }
    };

    public boolean isOff() {
        if (isVersionM()) {
            return manager == null;
        } else {
            return mCamera == null;
        }

    }

    private boolean isVersionM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    //打开手电筒
    public void lightsOn(@NonNull Context context) {
        lightsOn(context, false);
    }

    private void lightsOn(@NonNull Context context, boolean isSos) {
        if (!isSos)
            offSos();
        if (hasFlashlight(context)) {
            if (isVersionM()) {
                linghtOn23(context);
            } else {
                lightOn22();
            }
        } else {
            Toast.makeText(context, "您的手机不支持开启闪光灯", Toast.LENGTH_SHORT).show();
        }
    }

    //关闭sos
    public void offSos() {
        isSos = false;
        if (mTimer == null) {
            return;
        }
        mTimer.cancel();
        mTimer = null;
    }

    /**
     * 判断设备是否有闪光灯
     *
     * @param context
     * @return true 有 false 没有
     */
    public boolean hasFlashlight(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    /**
     * 安卓6.0以上打开手电筒
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void linghtOn23(@NonNull Context context) {
        try {
            manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            manager.setTorchMode("0", true);// "0"是主闪光灯
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * android6.0以下打开手电筒
     */
    private void lightOn22() {
        if (mCamera == null) {
            mCamera = Camera.open();
            Camera.Parameters params = mCamera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            mCamera.setParameters(params);
        }
    }

    //关闭手电筒
    public void lightOff() {
        lightsOff(false);
    }

    private void lightsOff(boolean isSos) {
        if (!isSos) {
            offSos();
        } else if (isVersionM()) {
            lightsOff23();
        } else {
            lightsOff22();
        }
    }

    //安卓6.0以上打关闭电筒
    @TargetApi(Build.VERSION_CODES.M)
    private void lightsOff23() {
        try {
            if (manager == null) {
                return;
            }
            manager.setTorchMode("0", false);
            manager = null;
        } catch (Exception e) {
        }
    }

    //安卓6.0以下关闭手电筒
    private void lightsOff22() {
        if (mCamera != null) {
            Camera.Parameters params = mCamera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCamera.setParameters(params);
            mCamera.release();
            mCamera = null;
        }
    }

    public boolean isSos() {
        return isSos;
    }

    /**
     * 打开sos
     *
     * @param context
     * @param speed   闪烁速度，建议取值1~6
     */
    public void sos(@NonNull Context context, int speed) {
        offSos();
        if (speed <= 0) {
            throw new RuntimeException("speed不能小于等于0");
        }

        this.context = context;

        isSos = true;
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                mInt = mInt == 0 ? 1 : 0;
                message.what = mInt;
                handler.sendMessage(message);
            }
        };
        mTimer = new Timer();
        mTimer.schedule(mTimerTask, 0, 1500 / speed);
    }

}
