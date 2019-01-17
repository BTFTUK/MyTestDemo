package com.example.rrks.myapplication.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.rrks.myapplication.R;
import com.example.rrks.myapplication.base.ListBean;

import java.util.List;

public class ListAdapter extends BaseQuickAdapter<ListBean, BaseViewHolder> {
    public ListAdapter(@Nullable List<ListBean> data) {
        super(R.layout.item_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ListBean item) {
        if (item == null) {
            return;
        }
        helper.addOnClickListener(R.id.text_view)
                .setText(R.id.text_view, item.getName());
    }
}
