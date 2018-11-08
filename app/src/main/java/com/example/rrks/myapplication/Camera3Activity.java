package com.example.rrks.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.cameraview.AspectRatio;
import com.google.android.cameraview.CameraView;
import com.google.android.cameraview.MyFrameView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

public class Camera3Activity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    @BindView(R.id.camera)
    CameraView mCameraView;

    private static final String TAG = "MainActivity";

    private static final int REQUEST_CAMERA_PERMISSION = 1;

    private static final String FRAGMENT_DIALOG = "dialog";

    private static final int[] FLASH_OPTIONS = {
            CameraView.FLASH_AUTO,
            CameraView.FLASH_OFF,
            CameraView.FLASH_ON,
    };

    private static final int[] FLASH_ICONS = {
            R.drawable.ic_flash_auto,
            R.drawable.ic_flash_off,
            R.drawable.ic_flash_on,
    };

    private static final int[] FLASH_TITLES = {
            R.string.flash_auto,
            R.string.flash_off,
            R.string.flash_on,
    };

    private int mCurrentFlash;

    private Handler mBackgroundHandler;

    private Bitmap mBitmap;
    private MyFrameView mMyFrameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera3);
        ButterKnife.bind(this);

        mMyFrameView = new MyFrameView(getApplication());
        mCameraView.addView(mMyFrameView);
        if (mCameraView != null) {
            mCameraView.addCallback(mCallback);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            mCameraView.start();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
//            ConfirmationDialogFragment
//                    .newInstance(R.string.camera_permission_confirmation,
//                            new String[]{Manifest.permission.CAMERA},
//                            REQUEST_CAMERA_PERMISSION,
//                            R.string.camera_permission_not_granted)
//                    .show(getSupportFragmentManager(), FRAGMENT_DIALOG);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        }
    }

    @Override
    protected void onPause() {
        mCameraView.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBackgroundHandler != null) {
            mBackgroundHandler.getLooper().quitSafely();
            mBackgroundHandler = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                if (permissions.length != 1 || grantResults.length != 1) {
                    throw new RuntimeException("Error on requesting camera permission.");
                }
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, R.string.camera_permission_not_granted,
                            Toast.LENGTH_SHORT).show();
                }
                // No need to start camera here; it is handled by onResume
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.aspect_ratio:
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                if (mCameraView != null
//                        && fragmentManager.findFragmentByTag(FRAGMENT_DIALOG) == null) {
//                    final Set<AspectRatio> ratios = mCameraView.getSupportedAspectRatios();
//                    final AspectRatio currentRatio = mCameraView.getAspectRatio();
//                    AspectRatioFragment.newInstance(ratios, currentRatio)
//                            .show(fragmentManager, FRAGMENT_DIALOG);
//                }
//                return true;
//            case R.id.switch_flash:
//                if (mCameraView != null) {
//                    mCurrentFlash = (mCurrentFlash + 1) % FLASH_OPTIONS.length;
//                    item.setTitle(FLASH_TITLES[mCurrentFlash]);
//                    item.setIcon(FLASH_ICONS[mCurrentFlash]);
//                    mCameraView.setFlash(FLASH_OPTIONS[mCurrentFlash]);
//                }
//                return true;
//            case R.id.switch_camera:
//                if (mCameraView != null) {
//                    int facing = mCameraView.getFacing();
//                    mCameraView.setFacing(facing == CameraView.FACING_FRONT ?
//                            CameraView.FACING_BACK : CameraView.FACING_FRONT);
//                }
//                return true;
//        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onAspectRatioSelected(@NonNull AspectRatio ratio) {
//        if (mCameraView != null) {
//            Toast.makeText(this, ratio.toString(), Toast.LENGTH_SHORT).show();
//            mCameraView.setAspectRatio(ratio);
//        }
//    }

    private Handler getBackgroundHandler() {
        if (mBackgroundHandler == null) {
            HandlerThread thread = new HandlerThread("background");
            thread.start();
            mBackgroundHandler = new Handler(thread.getLooper());
        }
        return mBackgroundHandler;
    }

    private CameraView.Callback mCallback
            = new CameraView.Callback() {

        @Override
        public void onCameraOpened(CameraView cameraView) {
            Log.d(TAG, "onCameraOpened");
        }

        @Override
        public void onCameraClosed(CameraView cameraView) {
            Log.d(TAG, "onCameraClosed");
        }

        @Override
        public void onPictureTaken(CameraView cameraView, final byte[] data) {
            Log.d(TAG, "onPictureTaken " + data.length);
            Toast.makeText(cameraView.getContext(), R.string.picture_taken, Toast.LENGTH_SHORT)
                    .show();
            getBackgroundHandler().post(new Runnable() {
                @Override
                public void run() {
                    final File file = getOutputMediaFile();
                    FileOutputStream fout;
                    try {
                        fout = new FileOutputStream(file);
                        BufferedOutputStream bos = new BufferedOutputStream(fout);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        Bitmap sizeBitmap = Bitmap.createScaledBitmap(bitmap,
                                mCameraView.getMeasuredWidth(), mCameraView.getMeasuredHeight(), true);
                        RectF rectF = mMyFrameView.getRectF();
                        mBitmap = Bitmap.createBitmap(sizeBitmap,(int)rectF.left,(int)rectF.top,(int)rectF.width(),(int)rectF.height());
                        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//                        os.write(data);
//                        os.close();
                        bos.flush();
                        bos.close();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Uri uri = Uri.fromFile(file);
                                getIntent().setData(uri);
                                setResult(RESULT_OK, getIntent());
                                finish();
                            }
                        });
                    } catch (IOException e) {
                        Log.w(TAG, "Cannot write to " + file, e);
                    } finally {
//                        if (bos != null) {
//                            try {
//                                fout.close();
//                            } catch (IOException e) {
//                                // Ignore
//                            }
//                        }
                    }
                }
            });
        }

    };


    private File getOutputMediaFile() {
        File mediaStorageDir = null;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            mediaStorageDir = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "YK");
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

    @OnClick(R.id.btn_take)
    public void onViewClicked() {
        if (mCameraView != null) {
            mCameraView.takePicture();
        }
    }
}
