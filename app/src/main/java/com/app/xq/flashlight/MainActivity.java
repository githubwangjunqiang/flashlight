package com.app.xq.flashlight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int CODE_PER = 1001;
    private static final int CODE_PER2 = 1002;
    private CameraManager manager;
    private Camera mCamera;
    private View mViewGuangzhu;
    private Switch mSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewGuangzhu = findViewById(R.id.guangzhu);
        mSwitch = findViewById(R.id.switchid);
        mViewGuangzhu.setVisibility(View.INVISIBLE);


        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    open(null);
                } else {
                    off(null);
                }
            }
        });
    }

    public void open(View view) {
        boolean youmeiyou = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED;

        if (youmeiyou) {
            String[] persissions = new String[]{Manifest.permission.CAMERA};
            ActivityCompat.requestPermissions(this, persissions, CODE_PER);
            return;
        }
        openFlashlight();


    }

    public void off(View view) {
        boolean youmeiyou = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED;

        if (youmeiyou) {
            String[] persissions = new String[]{Manifest.permission.CAMERA};
            ActivityCompat.requestPermissions(this, persissions, CODE_PER2);
            return;
        }
        closeFlash();
    }

    private void openFlashlight() {
        mViewGuangzhu.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                // "0"是主闪光灯
                manager.setTorchMode("0", true);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Camera camera = Camera.open();
                camera.startPreview();
                Camera.Parameters parameters = camera.getParameters();
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(parameters);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void closeFlash() {
        mViewGuangzhu.setVisibility(View.INVISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                if (manager == null) {
                    return;
                }
                manager.setTorchMode("0", false);
                manager = null;
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        } else {
            try {
                if (mCamera != null) {
                    Camera.Parameters params = mCamera.getParameters();
                    params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    mCamera.setParameters(params);
                    mCamera.release();
                    mCamera = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != CODE_PER) {
            return;
        }
        for (int i = 0; i < grantResults.length; i++) {
            boolean quanxian = grantResults[i] == PackageManager.PERMISSION_DENIED;
            if (quanxian) {
                Toast.makeText(this, "没有获取权限", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        open(null);
    }
}