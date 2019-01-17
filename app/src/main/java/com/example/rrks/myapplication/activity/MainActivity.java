package com.example.rrks.myapplication.activity;

import android.animation.AnimatorSet;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.example.rrks.myapplication.R;
import com.example.rrks.myapplication.bean.EventBean;
import com.example.rrks.myapplication.getui.DemoIntentService;
import com.example.rrks.myapplication.getui.DemoPushService;
import com.igexin.sdk.PushManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    @BindView(R.id.tv_eventbus)
    TextView tvEventbus;
    @BindView(R.id.tv_eventbus2)
    TextView tvEventbus2;
    //    @BindView(R.id.waveView)
//    WaveView mWaveView;
    private AnimatorSet mAnimatorSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        PushManager.getInstance().initialize(this.getApplicationContext(), DemoPushService.class);
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(),DemoIntentService.class);

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                startActivity(new Intent(MainActivity.this, FullscreenActivity.class));
            }
        });

        ViewCompat.setTransitionName(btnAnimation, "btn");

    }

//        ARouter.getInstance().build("ui/cameraActivity").navigation();


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void tvEventbusText(EventBean eventBean) {
        if (eventBean == null) {
            return;
        }
        tvEventbus.setText(eventBean.toString());
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void tvEventbusText(String s) {
//        if (TextUtils.isEmpty(s)) {
//            return;
//        }
//        tvEventbus.setText(s);
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void tvEventbusText2(String s) {
        if (TextUtils.isEmpty(s)) {
            return;
        }
        tvEventbus2.setText(s);
    }

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
                default:
                    break;
            }

        }
    }

    @OnClick({R.id.btn, R.id.btn2, R.id.btn3, R.id.btn_animation, R.id.btn_rxjava, R.id.btn_list})
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
            case R.id.btn_list:
                startActivity(new Intent(this, ListActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
