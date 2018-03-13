package com.androidex.indoorlock.base;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.androidex.indoorlock.bean.Event;
import com.androidex.indoorlock.bean.SignModel;
import com.androidex.indoorlock.utils.Constants;
import com.androidex.indoorlock.utils.SharedPreTool;
import com.androidex.indoorlock.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Administrator on 2018/2/26.
 */

public abstract class BaseActivity extends AppCompatActivity implements Constants,View.OnClickListener{
    private View v = null;
    private Dialog dialog;
    protected Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            onMessage(msg);
        }
    };
    protected SignModel signModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signModel = (SignModel) SharedPreTool.getObject(SharedPreTool.sign_model);
        initParms(getIntent().getExtras());
        getSupportActionBar().hide();
        v = LayoutInflater.from(this).inflate(bindView(), null);
        setContentView(v);
        initView(v);
        EventBus.getDefault().register(this);
        mainThread();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onDestroy() {
        if(dialog!=null){
            hideLoadingDialog();
            dialog = null;
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public abstract void initParms(Bundle parms);
    public abstract int bindView();
    public abstract void initView(View v);
    public abstract void onMessage(Message msg);
    public abstract void onEvent(Event event);
    public abstract void mainThread();

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusMessage(Event event){
        if(event.what == EVENT_WHAT_TOP_ACCOUNT){
            //被顶号了
            onTopAccount();
            return;
        }
        onEvent(event);
    }

    protected void onTopAccount(){
        this.finish();
    }

    private void onExit(){
        this.finish();
    }

    public void postEvent(int what,Object obj){
        EventBus.getDefault().post(new Event(what,obj));
    }

    public void postEvent(int what){
        postEvent(what,null);
    }

    public void unregisterEventBus(){
        EventBus.getDefault().unregister(this);
    }

    public void showLoading(String msg){
        if(dialog == null){
            dialog = Utils.createDialog(this,msg);
        }
        if(dialog!=null && !dialog.isShowing()){
            dialog.show();
        }
    }

    public void hideLoadingDialog(){
        if(dialog!=null && dialog.isShowing()){
            dialog.dismiss();
            dialog = null;
        }
    }

    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void startService(Class<?> clz){
        Intent intent = new Intent(this,clz);
        startService(intent);
    }

    public void stopService(Class<?> clz){
        Intent intent = new Intent(this,clz);
        stopService(intent);
    }

    public boolean isNetWork(){
        return Utils.isNetworkAvailable();
    }

    public void showL(String msg){
        Log.e("xiao_",msg);
    }

    public void showToast(final boolean type,final String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Utils.showCustomToast(BaseActivity.this,type,msg);
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}
