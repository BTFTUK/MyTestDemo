package com.example.rrks.myapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.blankj.utilcode.util.ScreenUtils;
import com.example.rrks.myapplication.R;
import com.example.rrks.myapplication.widget.CameraTopRectView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//import com.alibaba.android.arouter.facade.annotation.Route;

/**
 * @time 2018/7/13 11:46
 * @anthor yuankang
 * @email yk94great@163.com
 * @describe 相机界面
 */
//@Route(path = "ui/cameraActivity")
public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    @BindView(R.id.sv_camera)
    SurfaceView mySurfaceView;
    @BindView(R.id.ctrv_view)
    CameraTopRectView topView;
    @BindView(R.id.btn_take)
    Button btnTake;
    @BindView(R.id.btn_ok)
    Button btnOk;

    private Camera myCamera = null;
    private Camera.Parameters myParameters;

    private SurfaceHolder mySurfaceHolder;
    private boolean isPreviewing = false;
    private Bitmap bm;
    private static final String IMG_PATH = "SHZQ";
    private File file;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        Window myWindow = this.getWindow();
        myWindow.setFlags(flag, flag);

        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);

        mySurfaceView.setZOrderOnTop(false);
        mySurfaceHolder = mySurfaceView.getHolder();
        mySurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
        mySurfaceHolder.addCallback(this);
        mySurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        initCamera();
    }

    private void initCamera() {
        if (myCamera == null) {
            myCamera = getCameraInstance();
        }
        if (myCamera != null) {
            myParameters = myCamera.getParameters();
            myParameters.setPictureFormat(PixelFormat.JPEG);
            myParameters.set("rotation", 90);
            if (getCameraFocusable() != null) {
                myParameters.setFocusMode(getCameraFocusable());
            }
//          myParameters.setPreviewSize(1280, 720);
//          myParameters.setPictureSize(2048, 1152); // 1280, 720
            Point point = getBestCameraResolution(myParameters,new Point(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight()));
            Log.d("","===x==="+point.x+"=========="+point.y);
            myParameters.setPreviewSize(point.x,point.y);

            myCamera.setDisplayOrientation(90);
            myCamera.setParameters(myParameters);
        } else {
            Toast.makeText(this, "相机错误", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            if (myCamera == null) {
                return;
            }
            myCamera.setPreviewDisplay(mySurfaceHolder);
            myCamera.startPreview();
            isPreviewing = true;
            btnTake.setClickable(true);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (myCamera != null) {
            myCamera.stopPreview();
            myCamera.release();
            myCamera = null;
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (myCamera == null) {
            initCamera();
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (myCamera != null) {
            isPreviewing = false;
            btnTake.setClickable(false);
            myCamera.release(); // release the camera for other applications
            myCamera = null;
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (myCamera != null) {
            myCamera.release();
            myCamera = null;
        }
    }


    Camera.ShutterCallback myShutterCallback = new Camera.ShutterCallback() {

        public void onShutter() {
            // TODO Auto-generated method stub

        }
    };
    Camera.PictureCallback myRawCallback = new Camera.PictureCallback() {

        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub

        }
    };

    Camera.PictureCallback myjpegCalback = new Camera.PictureCallback() {

        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub
            if (data != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,
                        data.length);
//              myCamera.stopPreview();
//              myCamera.release();
//              myCamera = null;
                isPreviewing = false;
                btnTake.setText("确定");

                Bitmap sizeBitmap = Bitmap.createScaledBitmap(bitmap,
                        topView.getViewWidth(), topView.getViewHeight(), true);
                bm = Bitmap.createBitmap(sizeBitmap, topView.getRectLeft(),
                        topView.getRectTop(),
                        topView.getRectRight() - topView.getRectLeft(),
                        topView.getRectBottom() - topView.getRectTop());// 截取
            }
        }
    };

    private String getCameraFocusable() {
        List<String> focusModes = myParameters.getSupportedFocusModes();
        if (focusModes
                .contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            return Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE;
        } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            return Camera.Parameters.FOCUS_MODE_AUTO;
        }
        return null;
    }

    public Camera getCameraInstance() {
        Camera c = null;
        try {
            if (getCameraId() >= 0) {
                c = Camera.open(getCameraId()); // attempt to get a Camera
                // instance
            } else {
                return null;
            }
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            Log.e("getCameraInstance", e.toString());
        }
        return c; // returns null if camera is unavailable
    }

    private int getCameraId() {
        if (!checkCameraHardware(getApplication())) {
            return -1;
        }
        int cNum = Camera.getNumberOfCameras();
        int defaultCameraId = -1;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < cNum; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                defaultCameraId = i;
            }
        }
        return defaultCameraId;
    }

    /**
     * Check if this device has a camera
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Create a File for saving an image or video
     */
    private File getOutputMediaFile() {
        File mediaStorageDir = null;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            mediaStorageDir = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    IMG_PATH);
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    return null;
                }
            }
        } else {
            Toast.makeText(this, "没有sd卡", Toast.LENGTH_SHORT).show();
            return null;
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @OnClick({R.id.btn_take, R.id.btn_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_take:
                if (isPreviewing) {
                    // 拍照
                    myCamera.takePicture(myShutterCallback, myRawCallback,
                            myjpegCalback);
                } else {
                    // 保存图片
                    File file = getOutputMediaFile();
                    this.file = file;
                    this.uri = Uri.fromFile(file);
                    if (file != null && bm != null) {
                        FileOutputStream fout;
                        try {
                            fout = new FileOutputStream(file);
                            BufferedOutputStream bos = new BufferedOutputStream(
                                    fout);
                            // Bitmap mBitmap = Bitmap.createScaledBitmap(bm,
                            // topView.getViewWidth(), topView.getViewHeight(),
                            // false);
                            bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                            // bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                            bos.flush();
                            bos.close();
                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        //返回数据
                        Intent intent = new Intent();
                        intent.setData(uri);
                        Bundle bundle = new Bundle();
                        intent.putExtras(bundle);
                        setResult(RESULT_OK, intent);
                    }
                    finish();
                }
                break;
            case R.id.btn_ok:
                if (isPreviewing) {
                    finish();
                    // 退出相机
                } else {
                    if(myCamera == null){
                        initCamera();
                    }
                    // 重新拍照
                    try {
                        myCamera.setPreviewDisplay(mySurfaceHolder);
                        myCamera.startPreview();
                        isPreviewing = true;
                        btnTake.setText("拍照");
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private Point getBestCameraResolution(Camera.Parameters parameters, Point screenResolution){
        float tmp = 0f;
        float mindiff = 100f;
        float x_d_y = (float)screenResolution.x / (float)screenResolution.y;
        Camera.Size best = null;
        List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        for(Camera.Size s : supportedPreviewSizes){
            tmp = Math.abs(((float)s.height/(float)s.width)-x_d_y);
            if(tmp<mindiff){
                mindiff = tmp;
                best = s;
            }
        }
        return new Point(best.width, best.height);
    }
}
