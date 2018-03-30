package com.androidex.indoorlock.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.androidex.indoorlock.bean.CallModel;
import com.androidex.indoorlock.bean.Event;
import com.androidex.indoorlock.bean.SignModel;
import com.androidex.indoorlock.ui.activity.TelephoneActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import jni.http.HttpManager;
import jni.http.HttpResult;
import jni.http.RtcHttpClient;
import rtc.sdk.clt.RtcClientImpl;
import rtc.sdk.common.RtcConst;
import rtc.sdk.common.SdkSettings;
import rtc.sdk.core.RtcRules;
import rtc.sdk.iface.ClientListener;
import rtc.sdk.iface.Connection;
import rtc.sdk.iface.ConnectionListener;
import rtc.sdk.iface.Device;
import rtc.sdk.iface.DeviceListener;
import rtc.sdk.iface.RtcClient;

/**
 * Created by Administrator on 2018/3/1.
 */

public class RTCTools implements Constants {
    private static RTCTools instance = null;
    private static final String APP_ID = "71012";
    private static final String APP_KEY = "71007b1c-6b75-4d6f-85aa-40c1f3b842ef";

    private RTCListener listener;
    private Context context;
    private RtcClient mClt;
    private Device device;
    private String RTCtoken = "";
    private SignModel model;
    private RTCDeviceListener deviceListener;
    private RTCConnectionListener connectionListener;

    public static CallModel callModel;
    public static Connection callConnection;


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x01:
                    if(msg.arg1 == 0){
                        showL("Client获取成功");
                        mHandler.removeMessages(0x02);
                        initToken();
                    }else if(msg.arg1 == 9003){
                        showL("Client获取失败，时间不对");
                        mHandler.removeMessages(0x02);
                        listener.onResult(RTCListener.INIT_TIMEERROR); //时间不对
                    }else{
                        showL("Client获取失败，需要重复获取");
                        mHandler.sendEmptyMessageDelayed(0x02,1*1000);
                    }
                    break;
                case 0x02:
                    initClent();
                    break;
                case 0x03:
                    if(RTCtoken.length()>0){
                        showL("token 获取成功");
                        mHandler.removeMessages(0x04);
                        initRegister();
                    }else{
                        showL("token 获取失败，需要重复获取");
                        mHandler.sendEmptyMessageDelayed(0x04,1*1000);
                    }
                    break;
                case 0x04:
                    initToken();
                    break;
                case 0x05:
                    showL("initRegister 超时，重复注册");
                    initRegister();
                    break;
            }
        }
    };

    public static RTCTools newInstance(){
        if(instance == null){
            instance = new RTCTools();
        }
        return instance;
    }

    public void initRTC(Context context, RTCListener listener){
        this.context = context;
        this.listener = listener;
        this.model = (SignModel) SharedPreTool.getObject(SharedPreTool.sign_model);
        EventBus.getDefault().register(this);
        initClent();
    }

    @Subscribe
    public void onEvent(Event event){
        switch (event.what){
            case EVENT_WHAT_OPEN_DOOR:
                openDoor();
                break;
            case EVENT_WHAT_ANSWER:
                String type = (String) event.msg;
                call(type);
                break;
            case EVENT_WHAT_OFFHOOK_REJECT:
                hangUp();
                break;
            case EVENT_WHAT_OFFHOOK_OPEN_DOOR:
                openDoor();
                hangUp();
                break;
            case EVENT_WHAT_OPEN_LOCK:
                JSONObject j = (JSONObject) event.msg;
                try {
                    openLock(j.getString("key"), j.getString("unitNo"));
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }
    }

    private void openDoor(){
        if(callModel!=null){
            String userUrl = RtcRules.UserToRemoteUri_new(callModel.from, RtcConst.UEType_Any);
            String imageAppendValue = "";
            if (callModel.imageUrl != null && callModel.imageUrl.length() > 0) {
                imageAppendValue = "-" + callModel.imageUrl;
            }
            device.sendIm(userUrl, "text/plain", "open the door" + imageAppendValue);
        }
    }
    protected void openLock(String lockKey, String unitNo) {
        String userUrl = RtcRules.UserToRemoteUri_new(lockKey, RtcConst.UEType_Any);
        String append = "";
        if (unitNo != null) {
            append = "-" + unitNo;
        }
        device.sendIm(userUrl, "text/plain", "open the door" + append);
    }

    private void call(String type){
        try{
            if(callModel!=null){
                if(connectionListener == null){
                    connectionListener = new RTCConnectionListener();
                }
                JSONObject parameter = new JSONObject();
                String userUrl = RtcRules.UserToRemoteUri_new(callModel.from, RtcConst.UEType_Any);
                parameter.put(RtcConst.kCallRemoteUri, userUrl);
                if (type != null && type.equals("voice")) {
                    parameter.put(RtcConst.kCallType, RtcConst.CallType_Audio);
                } else {
                    parameter.put(RtcConst.kCallType, RtcConst.CallType_A_V);
                }
                callConnection = device.connect(parameter.toString(), connectionListener);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void initClent(){
        showL("initClent");
        if(!Utils.isNetworkAvailable()){
            listener.onResult(RTCListener.INIT_NONETWORK);
            return;
        }
        if(model == null){
            listener.onResult(RTCListener.INIT_NOSIGN);
            return;
        }
        if(mClt == null){
            mClt = new RtcClientImpl();
            mClt.initialize(context, new ClientListener() {
                @Override
                public void onInit(int i) {
                    if(i == 0){
                        mClt.setAudioCodec(RtcConst.ACodec_OPUS);
                        mClt.setVideoCodec(RtcConst.VCodec_VP8);
                        mClt.setVideoAttr(RtcConst.Video_SD);
                    }else{
                        mClt = null;
                    }
                    Message message = Message.obtain();
                    message.what = 0x01;
                    message.arg1 = i;
                    mHandler.sendMessage(message);
                }
            });
        }
    }

    private void initToken(){
        showL("initToken");
        if(!Utils.isNetworkAvailable()){
            listener.onResult(RTCListener.INIT_NONETWORK);
            return;
        }
        if(model == null){
            listener.onResult(RTCListener.INIT_NOSIGN);
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    RTCtoken = "";
                    RtcConst.UEAPPID_Current = RtcConst.UEAPPID_Self;
                    JSONObject jsonobj = HttpManager.getInstance().CreateTokenJson(0, model.user.mobile, RtcHttpClient.grantedCapabiltyID, "");
                    //jsonobj.put("userTerminalType",RtcConst.UEType_Pad); //设置设备类型为pad
                    HttpResult ret = HttpManager.getInstance().getCapabilityToken(jsonobj, APP_ID, APP_KEY);
                    JSONObject jsonrsp = (JSONObject) ret.getObject();
                    if (jsonrsp != null && jsonrsp.isNull("code") == false) {
                        String code = jsonrsp.getString(RtcConst.kcode);
                        if(code.equals("0")) {
                            RTCtoken =jsonrsp.getString(RtcConst.kcapabilityToken);
                        }else{

                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                mHandler.sendEmptyMessage(0x03);

            }
        }).start();
    }

    private void initRegister(){
        showL("initRegister");
        if(!Utils.isNetworkAvailable()){
            listener.onResult(RTCListener.INIT_NONETWORK);
            return;
        }
        if(model == null){
            listener.onResult(RTCListener.INIT_NOSIGN);
            return;
        }
        if(deviceListener == null){
            deviceListener = new RTCDeviceListener();
        }
        try{
            JSONObject jargs = SdkSettings.defaultDeviceSetting();
            jargs.put(RtcConst.kAccPwd,RTCtoken);
            jargs.put(RtcConst.kAccAppID,APP_ID);
            jargs.put(RtcConst.kAccUser,model.user.mobile); //号码
            //jargs.put(RtcConst.kAccType,RtcConst.UEType_Pad);//指定终端类型为 pad
            jargs.put(RtcConst.kAccType,RtcConst.UEType_Current); //默认类型
            device = mClt.createDevice(jargs.toString(), deviceListener); //注册
            mHandler.sendEmptyMessageDelayed(0x05,3*1000);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void exitRTC(){
        EventBus.getDefault().unregister(this);
        if(callConnection!=null){
            callConnection.disconnect();
            callConnection = null;
        }
        if(device != null){
            deviceListener = null;
            device.disConnectAll();
            device.release();
            device = null;
        }
        if(mClt != null){
            mClt.release();
            mClt = null;
        }
    }

    private void toTelPhoneActivity(){
        Intent i = new Intent(context,TelephoneActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    private void hangUp(){
        if(callConnection!=null){
            callConnection.disconnect();
            callConnection = null;
        }
    }



    private void postEvent(int what,Object obj){
        EventBus.getDefault().post(new Event(what,obj));
    }

    private void postEvent(int what){
        postEvent(what,null);
    }

    private void showL(String msg){
        Log.i("rtctools",msg);
    }


    class RTCConnectionListener implements ConnectionListener{

        @Override
        public void onConnecting() {
            showL("通话正在连接");
        }

        @Override
        public void onConnected() {
            showL("通话连接完成");
        }

        @Override
        public void onDisconnected(int i) {
            showL("通话连接失败");
            postEvent(EVENT_WHAT_ON_DISCONNECTED,i);
            hangUp();
        }

        @Override
        public void onVideo() {
            postEvent(EVENT_WHAT_ON_VIDEO);
        }

        @Override
        public void onNetStatus(int i, String s) {

        }
    }

    class RTCDeviceListener implements DeviceListener {

        @Override
        public void onDeviceStateChanged(int i) {
            showL("RTC状态变化："+i);
            switch (i){
                case RtcConst.CallCode_Success:
                    mHandler.removeMessages(0x05);
                    listener.onResult(RTCListener.INIT_SUCCESS);
                    showL("RTC注册完成");
                    break;
                case RtcConst.NoNetwork:
                    showL("网络断开，可选择是否挂断正在进行的通话");
                    listener.onNoNetWork();
                    break;
                case RtcConst.ChangeNetwork:
                    showL("连接上了网络，可以继续呼叫");
                    break;
                case RtcConst.ReLoginNetwork:
                    showL("重连失败应用可以选择重新登录，应限制呼叫");
                    listener.onReLoginError();
                    break;
                case RtcConst.DeviceEvt_KickedOff:
                    showL("同一账号在另一同类型终端登录，被踢，应限制呼叫");
                    listener.onTopAccount();
                    break;
            }
        }

        @Override
        public void onSendIm(int i) {
            if (i == RtcConst.CallCode_Success) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Utils.showCustomToast(context,true,"指令发送成功");
                    }
                });
            }else{
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Utils.showCustomToast(context,false,"指令发送失败");
                    }
                });
            }

        }

        @Override
        public void onReceiveIm(String from, String mime, String content) {
            String command = "";
            String unitName = "";
            String imageUrl = "";
            String imageUuid = "";
            String communityName = "";
            String lockName = "";
            JSONObject j = null;
            try{
                j = new JSONObject(content);
                if(j.has("command")){
                    command = j.getString("command");
                }
                if(j.has("imageUrl")){
                    imageUrl = j.getString("imageUrl");
                }
                if(j.has("imageUuid")){
                    imageUuid = j.getString("imageUuid");
                }
                if(j.has("communityName")){
                    communityName = j.getString("communityName");
                }
                if(j.has("lockName")){
                    lockName = j.getString("lockName");
                }
                if(command.equals("call")){
                    if(!TelephoneActivity.isCall){
                        callModel = new CallModel();
                        callModel.from = from;
                        callModel.unitName = unitName;
                        callModel.imageUrl = imageUrl;
                        callModel.imageUuid = imageUuid;
                        callModel.communityName = communityName;
                        callModel.lockName = lockName;
                        toTelPhoneActivity();
                    }
                }else if(command.equals("appendImage")){
                    if(callModel.from.equals(from) && callModel.imageUuid.equals(imageUuid)){
                        callModel.imageUrl = imageUrl;
                        postEvent(EVENT_WHAT_APPEND_IMAGE,imageUrl);
                    }
                }else if(command.equals("cancelCall")){
                    if (callModel.from.equals(from)) {
                        hangUp();
                        postEvent(EVENT_WHAT_CANCEL_CALL);
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void onNewCall(Connection connection) {

        }

        @Override
        public void onQueryStatus(int i, String s) {

        }
    }



    public interface RTCListener{
        public static final int INIT_SUCCESS = 0;
        public static final int INIT_TIMEERROR = 1;
        public static final int INIT_NOSIGN = 2;
        public static final int INIT_NONETWORK = 3;
        public static final int INIT_RELOGINERROR = 4;
        void onResult(int code);
        void onTopAccount();
        void onNoNetWork();
        void onReLoginError();
    }
}
