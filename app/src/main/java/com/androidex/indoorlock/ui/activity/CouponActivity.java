package com.androidex.indoorlock.ui.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidex.indoorlock.R;
import com.androidex.indoorlock.base.BaseActivity;
import com.androidex.indoorlock.bean.Event;

/**
 * Created by Administrator on 2018/3/5.
 */

public class CouponActivity extends BaseActivity {
    private LinearLayout backLayout;
    private TextView title;
    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindView() {
        return R.layout.activity_coupon;
    }

    @Override
    public void initView(View v) {
        backLayout = findViewById(R.id.back);
        backLayout.setOnClickListener(this);
        title  = findViewById(R.id.title);
        title.setText("我的优惠券");
    }

    @Override
    public void onMessage(Message msg) {

    }

    @Override
    public void onEvent(Event event) {

    }

    @Override
    public void mainThread() {

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.back){
            this.finish();
        }
    }
}
