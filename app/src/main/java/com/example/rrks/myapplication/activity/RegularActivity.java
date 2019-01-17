package com.example.rrks.myapplication.activity;

import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.util.Pair;

import com.example.rrks.myapplication.R;
import com.example.rrks.myapplication.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegularActivity extends BaseActivity {

    @BindView(R.id.edit)
    AppCompatEditText edit;

    @Override
    public int initContentView() {
        return R.layout.activity_regular;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn)
    public void onViewClicked() {
        String s = edit.getText().toString();
//        String regularS = "^(?![0-9]+$)(?![a-zA-Z]+$)(?![0-9a-z]+$)(?![0-9A-Z]+$)[0-9A-Za-z]{6,15}$";
        Pair<String, String> regular1 = Pair.create("^(?![0-9]+$)(?![a-zA-Z]+$)(?![0-9a-z]+$)(?![0-9A-Z]+$)[0-9A-Za-z]{6,15}$",
                "请输入包含大小写字母及数字的6-15位密码");

        Pair<String, String> regular2 = Pair.create("^{5,10}$", "5-10位");


        Pair<String, String> regular = regular2;

        if (!s.matches(regular.first)) {
            edit.setText("");
            showToast(regular.second);
        } else {
            showToast("校验成功");
        }
    }
}
