package com.androidex.indoorlock.bean;

/**
 * Created by Administrator on 2018/2/27.
 */

public class Event {
    public int what;
    public Object msg;

    public Event(int what,Object msg){
        this.what = what;
        this.msg = msg;
    }
}
