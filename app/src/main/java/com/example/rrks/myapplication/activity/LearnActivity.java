package com.example.rrks.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.rrks.myapplication.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LearnActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.IPC_ji_zhi, R.id.btn_aidl, R.id.btn_socket})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.IPC_ji_zhi:
                break;
            case R.id.btn_aidl:
                break;
            case R.id.btn_socket:
                startActivity(new Intent(this, TCPActivity.class));
                break;
            default:
                break;
        }

    }
}
