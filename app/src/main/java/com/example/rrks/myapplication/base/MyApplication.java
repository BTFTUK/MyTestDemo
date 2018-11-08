package com.example.rrks.myapplication.base;

import android.app.Application;

import com.bumptech.glide.Glide;

//import com.alibaba.android.arouter.launcher.ARouter;

public class MyApplication extends Application{

    private static MyApplication mApplication;

    public static MyApplication getInstance(){
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
//        ARouter.openLog();     // 打印日志
//        ARouter.openDebug();
//        ARouter.init(mApplication);

//        Glide.init(this,);
    }
}
