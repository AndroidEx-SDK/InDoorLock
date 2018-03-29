package com.androidex.indoorlock;

import android.app.Application;
import android.content.Context;

import com.mob.MobSDK;
import com.pureman.dysmart.MainApplication;

/**
 * Created by Administrator on 2018/2/26.
 */

public class AndroidexApplication extends Application {
    private static Context c;


    @Override
    public void onCreate() {
        super.onCreate();
        init();

        MainApplication.getInstance().init(this);
    }

    private void init() {
        c = getApplicationContext();
        MobSDK.init(c);
    }

    public static Context getContext() {
        return c;
    }
}
