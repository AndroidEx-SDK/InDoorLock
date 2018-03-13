package com.androidex.indoorlock.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidex.indoorlock.R;
import com.androidex.indoorlock.base.BaseActivity;
import com.androidex.indoorlock.bean.Event;
import com.androidex.indoorlock.bean.SignModel;
import com.androidex.indoorlock.service.AndroidexService;
import com.androidex.indoorlock.ui.fragment.AroundFragment;
import com.androidex.indoorlock.ui.fragment.ManagerFragment;
import com.androidex.indoorlock.ui.fragment.SelfFragment;
import com.androidex.indoorlock.utils.RTCTools;
import com.androidex.indoorlock.utils.SharedPreTool;
import com.androidex.indoorlock.utils.Utils;
import com.androidex.indoorlock.view.HouseAlert;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2018/2/28.
 */

public class HomeActivity extends BaseActivity {
    private RelativeLayout managerLayout;
    private RelativeLayout aroundrLayout;
    private RelativeLayout selfLayout;
    private LinearLayout houseLayout;
    private ImageView managerImage,aroundImage,selfImage;
    private TextView houseText;
    private FragmentManager fragmentManager;
    private Fragment mFragment,aFragment,sFragment;
    private ImageView rtcNotifyImage;
    private TextView rtcNotifyText;

    private HouseAlert houseAlert;

    private int signoutmode = -1;


    @Override
    public void initParms(Bundle parms) {
        fragmentManager = getSupportFragmentManager();
    }

    @Override
    public int bindView() {
        return R.layout.activity_home;
    }

    @Override
    public void initView(View v) {
        managerLayout = findViewById(R.id.manager_layout);
        aroundrLayout = findViewById(R.id.around_layout);
        selfLayout = findViewById(R.id.self_layout);
        managerLayout.setOnClickListener(this);
        aroundrLayout.setOnClickListener(this);
        selfLayout.setOnClickListener(this);
        managerImage = findViewById(R.id.manager_icon);
        aroundImage =findViewById(R.id.around_icon);
        selfImage = findViewById(R.id.self_icon);
        houseLayout = findViewById(R.id.home_house_layout);
        houseLayout.setOnClickListener(this);
        houseText = findViewById(R.id.home_house_text);
        rtcNotifyImage = findViewById(R.id.home_rtc_notify_image);
        rtcNotifyText = findViewById(R.id.home_rtc_notify_text);


        setHouseText(); //显示选定的房屋
        showPage(1); //默认显示第一页
    }

    @Override
    protected void onDestroy() {
        if(houseAlert!=null){
            houseAlert.dismiss();
            houseAlert = null;
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.manager_layout:
                showPage(1);
                break;
            case R.id.around_layout:
                showPage(2);
                break;
            case R.id.self_layout:
                showPage(3);
                break;
            case R.id.home_house_layout:
                //切换房屋
                if(houseAlert == null){
                    houseAlert = new HouseAlert(this,R.style.Dialog);
                }
                houseAlert.show();
                break;
        }
    }

    @Override
    public void onMessage(Message msg) {

    }

    @Override
    protected void onTopAccount() {
        postEvent(EVENT_WHAT_SIGN_OUT,2);
    }

    @Override
    public void onEvent(Event event) {
        switch (event.what){
            case EVENT_WHAT_HOUSE_CHANGE:
                showToast(true,"切换成功");
                setHouseText();
                break;
            case EVENT_WHAT_RTC_NOTIFY:
                setRtcNotify((int) event.msg);
                break;
            case EVENT_WHAT_SIGN_OUT:
                signoutmode = (int) event.msg;
                stopService(AndroidexService.class);
                break;
            case EVENT_WHAT_SIGN_OUT_CALLBACK:
                Bundle bundle = new Bundle();
                bundle.putInt("mode",signoutmode);
                startActivity(LoginActivity.class,bundle);
                this.finish();
                break;
            case EVENT_WHAT_EXIT:
                unregisterEventBus();
                stopService(AndroidexService.class);
                SharedPreTool.removeObject(SharedPreTool.sign_model);
                finish();
                break;
        }
    }

    @Override
    public void mainThread() {
        postEvent(EVENT_WHAT_INITRTC);
    }

    private void showPage(int page){
        FragmentTransaction ft = fragmentManager.beginTransaction();
        hideFragments(ft);
        switch (page){
            case 1:
                if (mFragment != null)
                    ft.show(mFragment);
                else {
                    mFragment = new ManagerFragment();
                    ft.add(R.id.home_content, mFragment);
                }
                break;
            case 2:
                if (aFragment != null)
                    ft.show(aFragment);
                else {
                    aFragment = new AroundFragment();
                    ft.add(R.id.home_content, aFragment);
                }
                break;
            case 3:
                if (sFragment != null)
                    ft.show(sFragment);
                else {
                    sFragment = new SelfFragment();
                    ft.add(R.id.home_content, sFragment);
                }
                break;
        }
        switchPageHead(page);
        ft.commit();
    }

    private void hideFragments(FragmentTransaction ft) {
        if (mFragment != null)
            ft.hide(mFragment);
        if (aFragment != null)
            ft.hide(aFragment);
        if (sFragment != null)
            ft.hide(sFragment);
    }

    private void switchPageHead(int page){
        switch (page){
            case 1:
                managerImage.setImageResource(R.mipmap.ic_manager_focus);
                aroundImage.setImageResource(R.mipmap.ic_around);
                selfImage.setImageResource(R.mipmap.ic_self);
                break;
            case 2:
                managerImage.setImageResource(R.mipmap.ic_manager);
                aroundImage.setImageResource(R.mipmap.ic_around_focus);
                selfImage.setImageResource(R.mipmap.ic_self);
                break;
            case 3:
                managerImage.setImageResource(R.mipmap.ic_manager_focus);
                aroundImage.setImageResource(R.mipmap.ic_around);
                selfImage.setImageResource(R.mipmap.ic_self_focus);
                break;
        }
    }

    private void setHouseText(){
        SignModel model = (SignModel) SharedPreTool.getObject(SharedPreTool.sign_model);
        if(model!=null){
            for(int i=0;i<model.data.size();i++){
                if(model.data.get(i).rid == SharedPreTool.getIntValue(SharedPreTool.house_rid)){
                    houseText.setText(model.data.get(i).unitName);
                    return;
                }
            }
        }
        houseText.setText("请选择房屋");
    }

    private void setRtcNotify(int code){
        if(code == RTCTools.RTCListener.INIT_SUCCESS){
            rtcNotifyImage.setImageResource(R.mipmap.ic_available);
            rtcNotifyText.setText("可视对讲上线成功");
            showToast(true,"可视对讲上线成功");
        }else if(code == RTCTools.RTCListener.INIT_NOSIGN){
            rtcNotifyImage.setImageResource(R.mipmap.ic_unavailable);
            rtcNotifyText.setText("可视对讲上线失败，请重新登录");
            showToast(false,"可视对讲上线失败，请重新登录");
        }else if(code == RTCTools.RTCListener.INIT_TIMEERROR){
            rtcNotifyImage.setImageResource(R.mipmap.ic_unavailable);
            rtcNotifyText.setText("系统时间错误，可视对讲无法使用");
            showToast(false,"系统时间错误，可视对讲无法使用");
        }else if(code == RTCTools.RTCListener.INIT_NONETWORK){
            rtcNotifyImage.setImageResource(R.mipmap.ic_unavailable);
            rtcNotifyText.setText("无网络连接，可视对讲无法使用");
            showToast(false,"无网络连接，可视对讲无法使用");
        }else if(code == RTCTools.RTCListener.INIT_RELOGINERROR){
            rtcNotifyImage.setImageResource(R.mipmap.ic_unavailable);
            rtcNotifyText.setText("可视对讲注册失败，请手动重新登录");
            showToast(false,"可视对讲注册失败，请尝试手动登录");
        }
    }
}
