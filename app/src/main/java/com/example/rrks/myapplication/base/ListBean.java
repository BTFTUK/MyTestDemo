package com.example.rrks.myapplication.base;

import android.app.Activity;

public class ListBean {
    private Class target;
    private String name;

    public ListBean(Class target, String name) {
        this.target = target;
        this.name = name;
    }

    public Class getTarget() {
        return target;
    }

    public void setTarget(Class target) {
        this.target = target;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
