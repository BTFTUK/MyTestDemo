package com.example.rrks.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.rrks.myapplication.R;
import com.example.rrks.myapplication.adapter.ListAdapter;
import com.example.rrks.myapplication.base.BaseActivity;
import com.example.rrks.myapplication.base.ListBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    public int initContentView() {
        return R.layout.activity_list;
    }

    @Override
    public void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final List<ListBean> listBeans = new ArrayList<>();
        listBeans.add(new ListBean(TActivity.class, "泛型"));
        listBeans.add(new ListBean(RegularActivity.class, "正则"));

        ListAdapter adapter = new ListAdapter(listBeans);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.text_view) {
                    ListBean listBean = (ListBean) adapter.getData().get(position);
                    Intent intent = new Intent(ListActivity.this, listBean.getTarget());
                    startActivity(intent);
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void initData() {

    }
}
