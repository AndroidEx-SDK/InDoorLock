package com.androidex.indoorlock.ui.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.androidex.indoorlock.R;
import com.androidex.indoorlock.base.BaseActivity;
import com.androidex.indoorlock.bean.Event;
import com.androidex.indoorlock.bean.TroubleListModel;
import com.androidex.indoorlock.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/16.
 */

public class TroubleDetailsActivity extends BaseActivity {
    private TroubleListModel.Trouble trouble;
    private LinearLayout backLayout;
    private TextView title;

    private TextView troubleTitle;
    private TextView time;
    private TextView content;
    private LinearLayout imageLayout;

    @Override
    public void initParms(Bundle parms) {
        trouble = (TroubleListModel.Trouble) parms.getSerializable("trouble");
    }

    @Override
    public int bindView() {
        return R.layout.activity_troubledetails;
    }

    @Override
    public void initView(View v) {
        backLayout = findViewById(R.id.back);
        backLayout.setOnClickListener(this);
        title = findViewById(R.id.title);
        title.setText("详情");

        troubleTitle = findViewById(R.id.trouble_title);
        troubleTitle.setText(trouble.troubleTitle);
        time = findViewById(R.id.time);
        time.setText(Utils.UTCStringtODefaultString(trouble.creDate));

        content = findViewById(R.id.content);
        content.setText(trouble.remark);

        imageLayout = findViewById(R.id.imageLayout);
        ArrayList<String> images = JSON.parseObject(trouble.images, new TypeReference<ArrayList<String>>() {
        });
        appendImageLayout(images);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                this.finish();
                break;
        }
    }

    private void appendImageLayout(List<String> data) {
        for (int i = 0; i < data.size(); i++) {
            showL("地址：" + data.get(i));
            final ImageView imageView = new ImageView(this);
//            int left, top, right, bottom;
//            left = top = right = bottom = 64;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 16, 0, 0);
            imageView.setLayoutParams(params);
            imageLayout.addView(imageView);
            Glide.with(this).load(data.get(i)).placeholder(R.mipmap.ic_default).error(R.mipmap.ic_default).into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    imageView.setImageDrawable(resource);
                }
            });
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
