package com.example.rrks.myapplication.activity;

import android.app.SharedElementCallback;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.rrks.myapplication.R;
import com.example.rrks.myapplication.bean.EventBean;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;
import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AnimationActivity extends AppCompatActivity {

    @BindView(R.id.btn_animation)
    Button btnAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        ButterKnife.bind(this);
        ViewCompat.setTransitionName(btnAnimation,"btn");
//        EventBus.getDefault().register(this);

//        TransitionSet transitionSet = new TransitionSet();
//        AutoTransition autoTransition = new AutoTransition();
//        transitionSet.addTransition(autoTransition);
//        transitionSet.addTarget(btnAnimation);

//        setEnterSharedElementCallback(new SharedElementCallback() {
//            @Override
//            public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
//                super.onSharedElementStart(sharedElementNames, sharedElements, sharedElementSnapshots);
//            }
//
//            @Override
//            public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
//                super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
//            }
//
//            @Override
//            public void onRejectSharedElements(List<View> rejectedSharedElements) {
//                super.onRejectSharedElements(rejectedSharedElements);
//            }
//
//            @Override
//            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
//                super.onMapSharedElements(names, sharedElements);
//            }
//
//            @Override
//            public Parcelable onCaptureSharedElementSnapshot(View sharedElement, Matrix viewToGlobalMatrix, RectF screenBounds) {
//                return super.onCaptureSharedElementSnapshot(sharedElement, viewToGlobalMatrix, screenBounds);
//            }
//
//            @Override
//            public View onCreateSnapshotView(Context context, Parcelable snapshot) {
//                return super.onCreateSnapshotView(context, snapshot);
//            }
//
//            @Override
//            public void onSharedElementsArrived(List<String> sharedElementNames, List<View> sharedElements, OnSharedElementsReadyListener listener) {
//                super.onSharedElementsArrived(sharedElementNames, sharedElements, listener);
//            }
//        });
    }

    @OnClick(R.id.btn_animation)
    public void onViewClicked() {
        EventBus.getDefault().post(new EventBean("tag","内容1"));
        EventBus.getDefault().post("2");
        Log.e("AnimationActivity", "=======onViewClicked:执行了点击事件 ");
        onBackPressed();
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
//        EventBus.getDefault().unregister(this);
    }

    private void reflect(){
        
    }
}
