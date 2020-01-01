package com.yasoka.eazyscreenrecord.float_camera;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.WindowManager;
import java.io.IOException;

public class CameraPreview extends SurfaceView implements Callback {
    private static final boolean DEBUG = true;
    private static final String TAG = "CameraPreview";
    private Camera mCamera;
    private Context mContext;
    private SurfaceHolder mHolder = getHolder();

    public CameraPreview(Context context) {
        super(context);
        this.mContext = context;
        this.mHolder.addCallback(this);
    }

    public CameraPreview(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
        this.mHolder.addCallback(this);
    }

    public CameraPreview(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mContext = context;
        this.mHolder.addCallback(this);
    }

    public CameraPreview(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mContext = context;
        this.mHolder.addCallback(this);
    }

    public static void setCameraDisplayOrientation(Context context, int i, Camera camera) {
        int i2;
        CameraInfo cameraInfo = new CameraInfo();
        Camera.getCameraInfo(i, cameraInfo);
        int rotation = ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getRotation();
        int i3 = 0;
        if (rotation != 0) {
            if (rotation == 1) {
                i3 = 90;
            } else if (rotation == 2) {
                i3 = 180;
            } else if (rotation == 3) {
                i3 = 270;
            }
        }
        if (cameraInfo.facing == 1) {
            i2 = (360 - ((cameraInfo.orientation + i3) % 360)) % 360;
        } else {
            i2 = ((cameraInfo.orientation - i3) + 360) % 360;
        }
        camera.setDisplayOrientation(i2);
    }

    public void setCamera(Camera camera) {
        this.mCamera = camera;
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        String str = TAG;
        Log.i(str, "surfaceCreated(SurfaceHolder");
        try {
            this.mCamera.setPreviewDisplay(surfaceHolder);
            this.mCamera.startPreview();
        } catch (IOException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Error setting cameraBhavik preview: ");
            sb.append(e.getMessage());
            Log.e(str, sb.toString());
        }
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        String str = TAG;
        Log.i(str, "surfaceChanged(SurfaceHolder, int, int, int");
        if (this.mHolder.getSurface() != null) {
            try {
                this.mCamera.stopPreview();
            } catch (Exception unused) {
            }
            setCameraDisplayOrientation(this.mContext, 0, this.mCamera);
            try {
                this.mCamera.setPreviewDisplay(this.mHolder);
                this.mCamera.startPreview();
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder();
                sb.append("Error starting cameraBhavik preview: ");
                sb.append(e.getMessage());
                Log.d(str, sb.toString());
            }
        }
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.i(TAG, "surfaceDestroyed(SurfaceHolder");
    }
}
