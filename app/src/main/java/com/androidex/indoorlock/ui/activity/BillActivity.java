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
 * Created by Administrator on 2018/3/17.
 */

public class BillActivity extends BaseActivity {
    private LinearLayout backLayout;
    private TextView title;
    private TextView menu;


    private TextView count;
    private TextView details;
    private TextView money;
    private TextView payment;
    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindView() {
        return R.layout.activity_bill;
    }

    @Override
    public void initView(View v) {
        backLayout = findViewById(R.id.back);
        backLayout.setOnClickListener(this);
        title = findViewById(R.id.title);
        title.setText("物业缴费");
        menu = findViewById(R.id.menu);
        menu.setText("已支付账单");
        menu.setOnClickListener(this);

        count = findViewById(R.id.count);
        details = findViewById(R.id.details);
        details.setOnClickListener(this);
        money = findViewById(R.id.money);
        payment = findViewById(R.id.payment);
        payment.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.details:
                break;
            case R.id.payment:
                break;
        }
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

}
