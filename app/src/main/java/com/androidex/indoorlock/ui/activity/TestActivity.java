package com.androidex.indoorlock.ui.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import com.androidex.indoorlock.R;
import com.pureman.dysmart.util.DeviceUtil;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //横屏、竖屏
        setRequestedOrientation(DeviceUtil.isPad(this) ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//横屏

        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        float density = dm.density;
        int densityDpi = dm.densityDpi;
        //1280/752/1.0/160/800 平板
        //1080/1920/3.0/480/360 手机
        Log.i("indoorlock", "TestActivity/initView-->" + screenWidth + "/" + screenHeight + "/" + density + "/" + densityDpi);

        setContentView(R.layout.activity_test);
    }
}
