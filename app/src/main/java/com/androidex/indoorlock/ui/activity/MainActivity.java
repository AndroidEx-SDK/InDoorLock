package com.androidex.indoorlock.ui.activity;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.androidex.indoorlock.R;
import com.androidex.indoorlock.base.BaseActivity;
import com.androidex.indoorlock.bean.Event;
import com.androidex.indoorlock.bean.SignModel;
import com.androidex.indoorlock.service.AndroidexService;

import org.greenrobot.eventbus.EventBus;

public class MainActivity extends BaseActivity {
    private int flag = -1;
    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindView() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(View v) {

    }


    @Override
    public void onMessage(Message msg) {
        switch (msg.what){
            case 0x01:
                if(flag == 1){
                    startActivity(HomeActivity.class,null); //HomeActivity
                }else{
                    startActivity(LoginActivity.class,null); //LoginActivity
                }
                this.finish();
                break;
        }
    }

    @Override
    public void onEvent(Event event) {
        switch (event.what){
            case EVENT_WHAT_SERVICE_INIT:
                //AndroidexService init
                loginInit();
                break;
            case EVENT_WHAT_SERVICE_LOGIN_CALLBACK:
                SignModel model = (SignModel) event.msg;
                if(model.code == 0){
                    flag = 1;
                }
                break;
        }
    }

    @Override
    public void mainThread() {
        startService(AndroidexService.class);
    }

    private void loginInit(){
        mHandler.sendEmptyMessageDelayed(0x01,5*1000);
        if(signModel != null && isNetWork()){
            postEvent(EVENT_WHAT_SERVICE_LOGIN);
        }
    }
}
