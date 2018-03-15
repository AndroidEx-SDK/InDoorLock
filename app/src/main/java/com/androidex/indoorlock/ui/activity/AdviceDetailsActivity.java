package com.androidex.indoorlock.ui.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.androidex.indoorlock.R;
import com.androidex.indoorlock.base.BaseActivity;
import com.androidex.indoorlock.bean.AdviceListModel;
import com.androidex.indoorlock.bean.Event;
import com.androidex.indoorlock.utils.Utils;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/14.
 */

public class AdviceDetailsActivity extends BaseActivity {
    private AdviceListModel.Advice advice;
    private LinearLayout backLayout;
    private TextView title;

    private TextView adviceTitle;
    private TextView time;
    private TextView content;
    private LinearLayout imageLayout;


    @Override
    public void initParms(Bundle parms) {
        advice = (AdviceListModel.Advice) getIntent().getSerializableExtra("advice");
    }

    @Override
    public int bindView() {
        return R.layout.activity_advicedetails;
    }

    @Override
    public void initView(View v) {
        backLayout = findViewById(R.id.back);
        backLayout.setOnClickListener(this);
        title = findViewById(R.id.title);
        title.setText("详情");

        adviceTitle = findViewById(R.id.advice_title);
        adviceTitle.setText(advice.adviceTitle);
        time = findViewById(R.id.time);
        time.setText(Utils.UTCStringtODefaultString(advice.creDate));
        content = findViewById(R.id.content);
        content.setText(advice.remark);
        imageLayout = findViewById(R.id.imageLayout);
        ArrayList<String> images = JSON.parseObject(advice.images, new TypeReference<ArrayList<String>>() {});
        addImage(images);
//        try {
//            JSONArray jsonArray = new JSONArray(advice.images);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                this.finish();
                break;
        }
    }

    private void addImage(List<String> data) {
        for (int i = 0; i < data.size(); i++) {
            ImageView imageView = new ImageView(this);
            int left, top, right, bottom;
            left = top = right = bottom = 64;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(left, top, right, bottom);
            imageView.setLayoutParams(params);
            imageLayout.addView(imageView);
            Glide.with(this).load(data.get(i)).placeholder(R.mipmap.ic_default).error(R.mipmap.ic_default).into(imageView);
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
