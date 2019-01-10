package com.example.rrks.myapplication.bean;

public class EventBean {
    private String tag;
    private String content;

    public EventBean(String tag, String content) {
        this.tag = tag;
        this.content = content;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "{tag" + ":" + getTag() + ",content" + ":" + getContent() + "}";
    }
}
