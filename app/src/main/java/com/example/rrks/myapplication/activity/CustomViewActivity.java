package com.example.rrks.myapplication.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.rrks.myapplication.R;
import com.example.rrks.myapplication.widget.YkView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomViewActivity extends AppCompatActivity {

    @BindView(R.id.yk_view)
    YkView ykView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);
        ButterKnife.bind(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
