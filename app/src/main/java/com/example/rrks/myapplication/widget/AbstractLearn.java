package com.example.rrks.myapplication.widget;

import android.util.Log;

public abstract class AbstractLearn {

    private static final String TAG = "AbstractLearn";

    public AbstractLearn() {
        Log.d(TAG, "AbstractLearn:" + getTitle());
    }

    public abstract String getTitle();

}
