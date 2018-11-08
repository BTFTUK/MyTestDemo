package com.example.rrks.myapplication;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.example.rrks.myapplication.widget.AbstractChild;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MvvmActivity extends AppCompatActivity {
    private static final String TAG = "MvvmActivity";
    @BindView(R.id.btn_start)
    Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvvm);
        ButterKnife.bind(this);
        myLog("onCreate");
        init();
    }

    private void myLog(String msg) {
        Log.e(TAG, "===" + msg + "===");
    }

    private void init(){
        AbstractChild abstractChild = new AbstractChild();
    }


    @OnClick(R.id.btn_start)
    public void onViewClicked() {
        Observable observable = Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter emitter) throws Exception {
                emitter.onNext("文字");
                emitter.onNext(1);
                emitter.onNext(new Paint());
            }
        });

        Observer observer = new Observer() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object o) {
                myLog(o.toString());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        observable.subscribe(observer);
    }
}
