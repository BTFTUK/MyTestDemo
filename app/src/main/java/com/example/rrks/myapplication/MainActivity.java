package com.example.rrks.myapplication;

import android.animation.AnimatorSet;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//import com.alibaba.android.arouter.launcher.ARouter;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_hello_world)
    TextView tvHelloWorld;
    @BindView(R.id.btn)
    Button btn;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.toggleButton)
    ToggleButton toggleButton;
    @BindView(R.id.btn_animation)
    Button btnAnimation;
    //    @BindView(R.id.waveView)
//    WaveView mWaveView;
    private AnimatorSet mAnimatorSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                startActivity(new Intent(MainActivity.this, FullscreenActivity.class));
            }
        });

        ViewCompat.setTransitionName(btnAnimation, "btn");

    }

//        ARouter.getInstance().build("ui/cameraActivity").navigation();


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case 123:
                    assert data != null;
                    Uri uri = data.getData();
                    Glide.with(getApplication())
                            .load(uri)
                            .into(imageView);
                    break;
                case 124:
                    break;
            }

        }
    }

    @OnClick({R.id.btn, R.id.btn2, R.id.btn3, R.id.btn_animation, R.id.btn_rxjava})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn:
                startActivityForResult(new Intent(this, CameraActivity.class), 123);
                break;
            case R.id.btn2:
                startActivityForResult(new Intent(this, Camera2Activity.class), 124);
                break;
            case R.id.btn3:
//                startActivityForResult(new Intent(this, Camera3Activity.class), 125);
//                mAnimatorSet.start();
                startActivity(new Intent(this, CustomViewActivity.class));
                break;
            case R.id.btn_animation:
                ActivityOptionsCompat option = ActivityOptionsCompat.makeSceneTransitionAnimation(this, btnAnimation, "btn");
                Intent intent = new Intent(this, AnimationActivity.class);
                ActivityCompat.startActivity(this, intent, option.toBundle());
                break;
            case R.id.btn_rxjava:
                startActivity(new Intent(this, MvvmActivity.class));
                break;
            default:
                break;
        }
    }

}
