package com.androidex.indoorlock.ui.activity;
import android.os.Message;
import android.os.Bundle;
import android.view.View;

import com.androidex.indoorlock.R;
import com.androidex.indoorlock.base.BaseActivity;
import com.androidex.indoorlock.bean.Event;
import com.androidex.indoorlock.bean.SignModel;
import com.androidex.indoorlock.service.AndroidexService;

import java.util.Date;

public class MainActivity extends BaseActivity {
    private int flag = -1;
    @Override
    public void initParms(Bundle parms) {
        showL("启动室内机"+new Date().getTime());
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
                showL("5S倒计时完成...");
                if(flag == 1){
                    showL("登录成功，跳转到Home");
                    startActivity(HomeActivity.class,null); //HomeActivity
                }else{
                    showL("登录失败，跳转到Login");
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
                showL("后台服务器初始化完成，进行登录");
                loginInit();
                break;
            case EVENT_WHAT_SERVICE_LOGIN_CALLBACK:
                showL("收到登录结果");
                SignModel model = (SignModel) event.msg;
                if(model.code == 0){
                    flag = 1;
                }
                break;
        }
    }

    @Override
    public void mainThread() {
        if(AndroidexService.isRun){
            showL("后台服务正在运行，不需要启动");
            postEvent(EVENT_WHAT_SERVICE_INIT);
        }else{
            showL("后台服务不在运行，需要启动");
            startService(AndroidexService.class);
        }


    }

    private void loginInit(){
        mHandler.sendEmptyMessageDelayed(0x01,5*1000);
        showL("倒计时5S跳转到登录界面");
        if(signModel != null && isNetWork()){
            showL("登录信息不为空，而且网络状态良好,进行登录操作");
            postEvent(EVENT_WHAT_SERVICE_LOGIN);
        }
    }
}
