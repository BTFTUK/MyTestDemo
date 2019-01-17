package com.example.rrks.myapplication.base;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initContentView());
        ButterKnife.bind(this);
        initView();
        addToolBar();
        initData();
    }
    
    public void showToast(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public abstract int initContentView();

    public abstract void initView();

    public abstract void initData();

    private void addToolBar(){
        Toolbar toolbar = new Toolbar(this);
        setSupportActionBar(toolbar);
    }
}
