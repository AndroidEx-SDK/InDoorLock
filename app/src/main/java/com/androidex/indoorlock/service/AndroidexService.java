package com.androidex.indoorlock.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.androidex.indoorlock.bean.AccessModel;
import com.androidex.indoorlock.bean.AdviceListModel;
import com.androidex.indoorlock.bean.ApplyHouseModel;
import com.androidex.indoorlock.bean.BlockListModel;
import com.androidex.indoorlock.bean.CarApplyModel;
import com.androidex.indoorlock.bean.CarListModel;
import com.androidex.indoorlock.bean.CityListModel;
import com.androidex.indoorlock.bean.CommuntityListModel;
import com.androidex.indoorlock.bean.CreateTempKeyModel;
import com.androidex.indoorlock.bean.Event;
import com.androidex.indoorlock.bean.HouseDetailModel;
import com.androidex.indoorlock.bean.OwnerListModel;
import com.androidex.indoorlock.bean.RegisterModel;
import com.androidex.indoorlock.bean.SignModel;
import com.androidex.indoorlock.bean.TempKeyModel;
import com.androidex.indoorlock.bean.UnitListModel;
import com.androidex.indoorlock.bean.UpdateModel;
import com.androidex.indoorlock.net.NetApi;
import com.androidex.indoorlock.net.base.ResultCallBack;
import com.androidex.indoorlock.ui.activity.LoginActivity;
import com.androidex.indoorlock.utils.Constants;
import com.androidex.indoorlock.utils.RTCTools;
import com.androidex.indoorlock.utils.SharedPreTool;
import com.androidex.indoorlock.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.Request;

/**
 * Created by Administrator on 2018/2/27.
 */

public class AndroidexService extends Service implements Constants{
    public static boolean isRun = false;
    private RTCTools rtcTools;
    private Context context;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

        }
    };
    private RTCTools.RTCListener rtcListener = new RTCTools.RTCListener() {
        @Override
        public void onResult(int code) {
            postEvent(EVENT_WHAT_RTC_NOTIFY,code);
        }
        @Override
        public void onTopAccount() {
            postEvent(EVENT_WHAT_TOP_ACCOUNT);
        }
        @Override
        public void onNoNetWork() {
            postEvent(EVENT_WHAT_RTC_NOTIFY, RTCTools.RTCListener.INIT_NONETWORK);
        }
        @Override
        public void onReLoginError() {
            postEvent(EVENT_WHAT_RTC_NOTIFY, RTCTools.RTCListener.INIT_RELOGINERROR);
        }
    };
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isRun = true;
        context = this;
        EventBus.getDefault().register(this);
        postEvent(EVENT_WHAT_SERVICE_INIT);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event event){
        switch (event.what){
            case EVENT_WHAT_SERVICE_LOGIN:{
                //登录
                if(event.msg!=null){
                    login((Map<String, String>) event.msg);
                }else{
                    login(null);
                }

            }break;
            case EVENT_WHAT_INITRTC:{
                if(rtcTools == null){
                    rtcTools = RTCTools.newInstance();
                }
                rtcTools.initRTC(this,rtcListener);
            }break;
            case EVENT_WHAT_UPDATE_PASSWORD:{
                updatePassword((Map<String, String>) event.msg);
            }break;
            case EVENT_WHAT_TEMPKEY:{
                listTempKey((Map<String, String>) event.msg);
            }break;
            case EVENT_WHAT_CREATE_TEMPKEY:{
                createTempKey((Map<String, String>) event.msg);
            }break;
            case EVENT_WHAT_RECEVICE_ACCESS:{
                getAccessList((Map<String, String>) event.msg);
            }break;
            case EVENT_WHAT_REGISTER:{
                registerAccount((Map<String, String>) event.msg);
            }break;
            case EVENT_WHAT_HOUSEINFO:{
                getHouseDetails((Map<String, String>) event.msg);
            }break;
            case EVENT_WHAT_RECEVICE_CITYLIST:{
                getCityList((Map<String, String>) event.msg);
            }break;
            case EVENT_WHAT_RECEVICE_COMMUNITY:{
                getCommunityList((Map<String, String>) event.msg);
            }break;
            case EVENT_WHAT_RECEVICE_BLOCK:{
                getBlockList((Map<String, String>) event.msg);
            }break;
            case EVENT_WHAT_RECEVICE_UNIT:{
                getUnitList((Map<String, String>) event.msg);
            }break;
            case EVENT_WHAT_OWNER:{
                getOwnerList((Map<String, String>) event.msg);
            }break;
            case EVENT_WHAT_APPLY_HOUSE:{
                applyHouse((Map<String, String>) event.msg);
            }break;
            case EVENT_WHAT_CAR:{
                getCarList((Map<String, String>) event.msg);
            }break;
            case EVENT_WHAT_APPLYCAT:{
                applyCar((Map<String, String>) event.msg);
            }break;
            case EVENT_WHAT_ADVICE:{
                getAdviceList((Map<String, String>) event.msg);
            }break;
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        rtcTools.exitRTC();
        isRun = false;
        showL("service 注销完成");
        postEvent(EVENT_WHAT_SIGN_OUT_CALLBACK);
        super.onDestroy();
    }

    private void getAdviceList(Map<String,String> data){
        NetApi.getAdviceList(data,new ResultCallBack<AdviceListModel>(){
            @Override
            public void onSuccess(int statusCode, Headers headers, AdviceListModel model) {
                super.onSuccess(statusCode, headers, model);
                postEvent(EVENT_WHAT_ADVICE_RESULT,model);
            }

            @Override
            public void onFailure(int statusCode, Request request, Exception e) {
                super.onFailure(statusCode, request, e);
                AdviceListModel model = new AdviceListModel();
                if(Utils.isNetworkAvailable()){
                    model.code = SERVER_ERROR;
                }else{
                    model.code = NETWORK_ERROR;
                }
                postEvent(EVENT_WHAT_ADVICE_RESULT,model);
            }
        });
    }

    private void applyCar(Map<String,String> data){
        NetApi.applyCar(data,new ResultCallBack<CarApplyModel>(){
            @Override
            public void onSuccess(int statusCode, Headers headers, CarApplyModel model) {
                super.onSuccess(statusCode, headers, model);
                postEvent(EVENT_WHAT_APPLYCAT_RESULT,model);
            }

            @Override
            public void onFailure(int statusCode, Request request, Exception e) {
                super.onFailure(statusCode, request, e);
                CarApplyModel model = new CarApplyModel();
                if(Utils.isNetworkAvailable()){
                    model.code = SERVER_ERROR;
                }else{
                    model.code = NETWORK_ERROR;
                }
                postEvent(EVENT_WHAT_APPLYCAT_RESULT,model);
            }
        });
    }

    private void getCarList(Map<String,String> data){
        NetApi.getCarList(data,new ResultCallBack<CarListModel>(){
            @Override
            public void onSuccess(int statusCode, Headers headers, CarListModel model) {
                super.onSuccess(statusCode, headers, model);
                postEvent(EVENT_WHAT_CAR_RESULT,model);
            }

            @Override
            public void onFailure(int statusCode, Request request, Exception e) {
                super.onFailure(statusCode, request, e);
                CarListModel model = new CarListModel();
                if(Utils.isNetworkAvailable()){
                    model.code = SERVER_ERROR;
                }else{
                    model.code = NETWORK_ERROR;
                }
                postEvent(EVENT_WHAT_CAR_RESULT,model);
            }
        });
    }

    private void applyHouse(Map<String,String> data){
        NetApi.applyHouse(data,new ResultCallBack<ApplyHouseModel>(){
            @Override
            public void onSuccess(int statusCode, Headers headers, ApplyHouseModel model) {
                super.onSuccess(statusCode, headers, model);
                postEvent(EVENT_WHAT_APPLY_HOUSE_RESULT,model);
            }

            @Override
            public void onFailure(int statusCode, Request request, Exception e) {
                super.onFailure(statusCode, request, e);
                ApplyHouseModel model = new ApplyHouseModel();
                if(Utils.isNetworkAvailable()){
                    model.code = SERVER_ERROR;
                }else{
                    model.code = NETWORK_ERROR;
                }
                postEvent(EVENT_WHAT_APPLY_HOUSE_RESULT,model);
            }
        });
    }

    private void getOwnerList(Map<String,String> data){
        NetApi.getOwnerList(data,new ResultCallBack<OwnerListModel>(){
            @Override
            public void onSuccess(int statusCode, Headers headers, OwnerListModel model) {
                super.onSuccess(statusCode, headers, model);
                postEvent(EVENT_WHAT_OWNER_RESULT,model);
            }

            @Override
            public void onFailure(int statusCode, Request request, Exception e) {
                super.onFailure(statusCode, request, e);
                OwnerListModel model = new OwnerListModel();
                if(Utils.isNetworkAvailable()){
                    model.code = SERVER_ERROR;
                }else{
                    model.code = NETWORK_ERROR;
                }
                postEvent(EVENT_WHAT_OWNER_RESULT,model);
            }
        });
    }

    private void getUnitList(Map<String,String> data){
        NetApi.getUnitList(data,new ResultCallBack<UnitListModel>(){
            @Override
            public void onSuccess(int statusCode, Headers headers, UnitListModel model) {
                super.onSuccess(statusCode, headers, model);
                postEvent(EVENT_WHAT_RECEVICE_UNIT_RESULT,model);
            }

            @Override
            public void onFailure(int statusCode, Request request, Exception e) {
                super.onFailure(statusCode, request, e);
                UnitListModel model = new UnitListModel();
                if(Utils.isNetworkAvailable()){
                    model.code = SERVER_ERROR;
                }else{
                    model.code = NETWORK_ERROR;
                }
                postEvent(EVENT_WHAT_RECEVICE_UNIT_RESULT,model);
            }
        });
    }

    private void getBlockList(Map<String,String> data){
        NetApi.getBlockList(data,new ResultCallBack<BlockListModel>(){
            @Override
            public void onSuccess(int statusCode, Headers headers, BlockListModel model) {
                super.onSuccess(statusCode, headers, model);
                postEvent(EVENT_WHAT_RECEVICE_BLOCK_RESULT,model);
            }

            @Override
            public void onFailure(int statusCode, Request request, Exception e) {
                super.onFailure(statusCode, request, e);
                BlockListModel model = new BlockListModel();
                if(Utils.isNetworkAvailable()){
                    model.code = SERVER_ERROR;
                }else{
                    model.code = NETWORK_ERROR;
                }
                postEvent(EVENT_WHAT_RECEVICE_BLOCK_RESULT,model);
            }
        });
    }

    private void getCommunityList(Map<String,String> data){
        NetApi.getCommunityList(data,new ResultCallBack<CommuntityListModel>(){
            @Override
            public void onSuccess(int statusCode, Headers headers, CommuntityListModel model) {
                super.onSuccess(statusCode, headers, model);
                postEvent(EVENT_WHAT_RECEVICE_COMMUNITY_RESULT,model);
            }

            @Override
            public void onFailure(int statusCode, Request request, Exception e) {
                super.onFailure(statusCode, request, e);
                CommuntityListModel model = new CommuntityListModel();
                if(Utils.isNetworkAvailable()){
                    model.code = SERVER_ERROR;
                }else{
                    model.code = NETWORK_ERROR;
                }
                postEvent(EVENT_WHAT_RECEVICE_COMMUNITY_RESULT,model);
            }
        });
    }

    private void getCityList(Map<String,String> data){
        NetApi.getCityList(data,new ResultCallBack<CityListModel>(){
            @Override
            public void onSuccess(int statusCode, Headers headers, CityListModel model) {
                super.onSuccess(statusCode, headers, model);
                postEvent(EVENT_WHAT_RECEVICE_CITYLIST_RESULT,model);
            }

            @Override
            public void onFailure(int statusCode, Request request, Exception e) {
                super.onFailure(statusCode, request, e);
                CityListModel model = new CityListModel();
                if(Utils.isNetworkAvailable()){
                    model.code = SERVER_ERROR;
                }else{
                    model.code = NETWORK_ERROR;
                }
                postEvent(EVENT_WHAT_RECEVICE_CITYLIST_RESULT,model);
            }


        });
    }

    private void getHouseDetails(Map<String,String> data){
        NetApi.getHouseDetails(data,new ResultCallBack<HouseDetailModel>(){
            @Override
            public void onSuccess(int statusCode, Headers headers, HouseDetailModel model) {
                super.onSuccess(statusCode, headers, model);
                postEvent(EVENT_WHAT_HOUSEINFO_RESULT,model);
            }

            @Override
            public void onFailure(int statusCode, Request request, Exception e) {
                super.onFailure(statusCode, request, e);
                HouseDetailModel model = new HouseDetailModel();
                if(Utils.isNetworkAvailable()){
                    model.code = SERVER_ERROR;
                }else{
                    model.code = NETWORK_ERROR;
                }
                postEvent(EVENT_WHAT_REGISTER_RESULT,model);
            }
        });
    }

    private void registerAccount(Map<String,String> data){
        NetApi.registerAccount(data,new ResultCallBack<RegisterModel>(){
            @Override
            public void onSuccess(int statusCode, Headers headers, RegisterModel model) {
                super.onSuccess(statusCode, headers, model);
                postEvent(EVENT_WHAT_REGISTER_RESULT,model);
            }

            @Override
            public void onFailure(int statusCode, Request request, Exception e) {
                super.onFailure(statusCode, request, e);
                RegisterModel model = new RegisterModel();
                if(Utils.isNetworkAvailable()){
                    model.code = SERVER_ERROR;
                }else{
                    model.code = NETWORK_ERROR;
                }
                postEvent(EVENT_WHAT_REGISTER_RESULT,model);
            }
        });
    }

    private void getAccessList(Map<String,String> data){
        NetApi.getAccessList(data,new ResultCallBack<AccessModel>(){
            @Override
            public void onSuccess(int statusCode, Headers headers, AccessModel model) {
                super.onSuccess(statusCode, headers, model);
                postEvent(EVENT_WHAT_RECEVICE_ACCESS_RESULT,model);
            }

            @Override
            public void onFailure(int statusCode, Request request, Exception e) {
                super.onFailure(statusCode, request, e);
                AccessModel model = new AccessModel();
                if(Utils.isNetworkAvailable()){
                    model.code = SERVER_ERROR;
                }else{
                    model.code = NETWORK_ERROR;
                }
                postEvent(EVENT_WHAT_RECEVICE_ACCESS_RESULT,model);
            }
        });
    }

    private void createTempKey(Map<String,String> data){
        NetApi.createTempKey(data,new ResultCallBack<CreateTempKeyModel>(){
            @Override
            public void onSuccess(int statusCode, Headers headers, CreateTempKeyModel model) {
                super.onSuccess(statusCode, headers, model);
                showL("创建临时密码成功");
                postEvent(EVENT_WHAT_CREATE_TEMPKEY_RESULT,model);
            }

            @Override
            public void onFailure(int statusCode, Request request, Exception e) {
                super.onFailure(statusCode, request, e);
                showL("创建临时密码失败");
                CreateTempKeyModel model = new CreateTempKeyModel();
                if(Utils.isNetworkAvailable()){
                    model.code = SERVER_ERROR;
                }else{
                    model.code = NETWORK_ERROR;
                }
                postEvent(EVENT_WHAT_CREATE_TEMPKEY_RESULT,model);
            }
        });
    }

    private void listTempKey(Map<String,String> data){
        NetApi.listTempKey(data,new ResultCallBack<TempKeyModel>(){
            @Override
            public void onSuccess(int statusCode, Headers headers, TempKeyModel model) {
                super.onSuccess(statusCode, headers, model);
                postEvent(EVENT_WHAT_TEMPKEY_RESULT,model);
            }

            @Override
            public void onFailure(int statusCode, Request request, Exception e) {
                super.onFailure(statusCode, request, e);
                TempKeyModel model = new TempKeyModel();
                if(Utils.isNetworkAvailable()){
                    model.code = SERVER_ERROR;
                }else{
                    model.code = NETWORK_ERROR;
                }
                postEvent(EVENT_WHAT_TEMPKEY_RESULT,model);
            }
        });
    }

    private void login(Map<String,String> data){
        if(data == null){
            SignModel model = (SignModel) SharedPreTool.getObject(SharedPreTool.sign_model);
            data = new HashMap<>();
            data.put("username",model.user.mobile);
            data.put("password",model.user.password);
            data.put("deviceUuid", SharedPreTool.getUUID());
        }
        NetApi.login(data,new ResultCallBack<SignModel>(){
            @Override
            public void onSuccess(int statusCode, Headers headers, SignModel model) {
                super.onSuccess(statusCode, headers, model);
                if(model.code == 0){
                    SharedPreTool.saveObject(SharedPreTool.sign_model,model);
                }
                postEvent(EVENT_WHAT_SERVICE_LOGIN_CALLBACK,model); //发布登录结果
            }
            @Override
            public void onFailure(int statusCode, Request request, Exception e) {
                super.onFailure(statusCode, request, e);
                SignModel model = new SignModel();
                if(Utils.isNetworkAvailable()){
                    model.code = SERVER_ERROR;
                }else{
                    model.code = NETWORK_ERROR;
                }
                postEvent(EVENT_WHAT_SERVICE_LOGIN_CALLBACK,model); //发布登录结果
            }
        });
    }

    private void updatePassword(Map<String,String> data){
        NetApi.updataPassword(data,new ResultCallBack<UpdateModel>(){
            @Override
            public void onSuccess(int statusCode, Headers headers, UpdateModel model) {
                super.onSuccess(statusCode, headers, model);
                postEvent(EVENT_WHAT_UPDATE_PASSWORD_CALLBACK,model); //发布修改结果
            }

            @Override
            public void onFailure(int statusCode, Request request, Exception e) {
                super.onFailure(statusCode, request, e);
                UpdateModel model = new UpdateModel();
                if(Utils.isNetworkAvailable()){
                    model.code = SERVER_ERROR;
                }else{
                    model.code = NETWORK_ERROR;
                }
                postEvent(EVENT_WHAT_UPDATE_PASSWORD_CALLBACK); //发布修改结果
            }
        });
    }

    private void postEvent(int what,Object obj){
        EventBus.getDefault().post(new Event(what,obj));
    }

    private void postEvent(int what){
        postEvent(what,null);
    }

    private void showL(String msg){
        Log.e("xiao_",msg);
    }

    private void showToast(final boolean type,final String msg){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Utils.showCustomToast(AndroidexService.this,type,msg);
            }
        });
    }
}
