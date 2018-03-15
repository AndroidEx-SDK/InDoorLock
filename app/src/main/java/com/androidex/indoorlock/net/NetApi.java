package com.androidex.indoorlock.net;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.androidex.indoorlock.bean.AccessModel;
import com.androidex.indoorlock.bean.AdviceListModel;
import com.androidex.indoorlock.bean.ApplyHouseModel;
import com.androidex.indoorlock.bean.BlockListModel;
import com.androidex.indoorlock.bean.CarApplyModel;
import com.androidex.indoorlock.bean.CarListModel;
import com.androidex.indoorlock.bean.CityListModel;
import com.androidex.indoorlock.bean.CommuntityListModel;
import com.androidex.indoorlock.bean.CreateTempKeyModel;
import com.androidex.indoorlock.bean.HouseDetailModel;
import com.androidex.indoorlock.bean.OwnerListModel;
import com.androidex.indoorlock.bean.RegisterModel;
import com.androidex.indoorlock.bean.SignModel;
import com.androidex.indoorlock.bean.TempKeyModel;
import com.androidex.indoorlock.bean.UnitListModel;
import com.androidex.indoorlock.bean.UpdateModel;
import com.androidex.indoorlock.net.base.OkRequest;
import com.androidex.indoorlock.net.base.RequestParams;
import com.androidex.indoorlock.net.base.ResultCallBack;
import com.androidex.indoorlock.utils.Utils;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.Headers;

/**
 * @author liyp
 * @editTime 2017/9/22
 */

public class NetApi extends UrlTool{
    public static void login(Map<String,String> data,ResultCallBack<SignModel> callBack){
        RequestParams params = RequestParams.newInstance()
                .put("username", data.get("username"))
                .put("password",data.get("password"))
                .put("deviceUuid",data.get("deviceUuid"));
        String url = BASE_HEAD+LOGIN_URL;
        new OkRequest.Builder().url(url).headers(getHeaders(getToken(data))).params(params).post(callBack);
    }

    public static void registerAccount(Map<String,String> data,ResultCallBack<RegisterModel> callBack){
        RequestParams params = RequestParams.newInstance();
        try{
            JSONObject j = new JSONObject();
            j.put("mobile",data.get("mobile"));
            j.put("realname",data.get("realname"));
            j.put("password",data.get("password"));
            j.put("repassword",data.get("repassword"));
            j.put("code",data.get("code"));
            params.put("user",j);
        }catch (Exception e){
            e.printStackTrace();
        }
        params.put("appKey",data.get("appKey"));
        String url = BASE_HEAD+REGISTER_URL;
        new OkRequest.Builder().url(url).headers(getHeaders(getToken(data))).params(params).post(callBack);
    }

    public static void updataPassword(Map<String,String> data, ResultCallBack<UpdateModel> callBack){
        RequestParams params = RequestParams.newInstance();
        JSONObject j = null;
        try{
            j = new JSONObject();
            j.put("rid",data.get("rid"));
            j.put("password",data.get("password"));
            j.put("password1",data.get("password1"));
        }catch (Exception e){
            e.printStackTrace();
        }
        params.put("user",j.toString());
        params.put("appKey",data.get("appKey"));
        String url = BASE_HEAD+UPDATE_PASSWORD_URL;
        new OkRequest.Builder().url(url).headers(getHeaders(getToken(data))).params(params).post(callBack);
    }

    public static void listTempKey(Map<String,String> data, ResultCallBack<TempKeyModel> callBack){
        RequestParams params = RequestParams.newInstance();
        params.put("arrayLength",data.get("arrayLength"));
        params.put("appKey", data.get("appKey"));
        String url = BASE_HEAD+TEMPKEY_LIST;
        new OkRequest.Builder().url(url).headers(getHeaders(getToken(data))).params(params).get(callBack);
    }

    public static void createTempKey(Map<String,String> data, ResultCallBack<CreateTempKeyModel> callBack){
        RequestParams params = RequestParams.newInstance();
        try{
            JSONObject j = new JSONObject();
            j.put("communityId",Integer.valueOf(data.get("communityId")));
            j.put("userId",Integer.valueOf(data.get("userId")));
            j.put("unitId",Integer.valueOf(data.get("unitId")));
            j.put("unitNo",data.get("unitNo"));
            j.put("state",data.get("state"));
            j.put("realname",data.get("realname"));
            j.put("mobile",data.get("mobile"));
            j.put("enterTime",Integer.valueOf(data.get("enterTime")));
            j.put("endDate",data.get("endDate"));
            params.put("key",j);
        }catch (Exception e){
            e.printStackTrace();
        }
        params.put("appKey",data.get("appKey"));
        String url = BASE_HEAD+CREATE_TEMPKEY;
        new OkRequest.Builder().url(url).headers(getHeaders(getToken(data))).params(params).post(callBack);
    }

    public static void getAccessList(Map<String,String> data,ResultCallBack<AccessModel> callBack){
        RequestParams params = RequestParams.newInstance();
        params.put("arrayLength",Integer.valueOf(data.get("arrayLength")));
        params.put("appKey", data.get("appKey"));
        String url = BASE_HEAD+RECEVICE_ACCESS;
        new OkRequest.Builder().url(url).headers(getHeaders(getToken(data))).params(params).get(callBack);
    }

    public static void getHouseDetails(Map<String,String> data,ResultCallBack<HouseDetailModel> callBack){
        RequestParams params = RequestParams.newInstance();
        params.put("unitId",Integer.valueOf(data.get("unitId")));
        params.put("appKey", data.get("appKey"));
        String url = BASE_HEAD+RECEVICE_HOUSEINFO;
        new OkRequest.Builder().url(url).headers(getHeaders(getToken(data))).params(params).get(callBack);
    }

    public static void getCityList(Map<String,String> data,ResultCallBack<CityListModel> callBack){
        RequestParams params = RequestParams.newInstance();
        params.put("appKey", data.get("appKey"));
        String url = BASE_HEAD+RECEVICE_CITYLIST;
        new OkRequest.Builder().url(url).headers(getHeaders(getToken(data))).params(params).get(callBack);
    }

    public static void getCommunityList(Map<String,String> data, ResultCallBack<CommuntityListModel> callBack){
        RequestParams params = RequestParams.newInstance();
        params.put("city",data.get("city"));
        params.put("appKey", data.get("appKey"));
        params.put("arrayLength",Integer.valueOf(data.get("arrayLength")));
        String url = BASE_HEAD+RECEVICE_COMMUNITY;
        new OkRequest.Builder().url(url).headers(getHeaders(getToken(data))).params(params).get(callBack);
    }

    public static void getBlockList(Map<String,String> data, ResultCallBack<BlockListModel> callBack){
        RequestParams params = RequestParams.newInstance();
        params.put("communityId",Integer.valueOf(data.get("communityId")));
        params.put("appKey", data.get("appKey"));
        params.put("arrayLength",Integer.valueOf(data.get("arrayLength")));
        String url = BASE_HEAD+RECEVICE_BLOCK;
        new OkRequest.Builder().url(url).headers(getHeaders(getToken(data))).params(params).get(callBack);
    }

    public static void getUnitList(Map<String,String> data, ResultCallBack<UnitListModel> callBack){
        RequestParams params = RequestParams.newInstance();
        params.put("blockId",Integer.valueOf(data.get("blockId")));
        params.put("appKey", data.get("appKey"));
        params.put("arrayLength",Integer.valueOf(data.get("arrayLength")));
        String url = BASE_HEAD+RECEVICE_UNIT;
        new OkRequest.Builder().url(url).headers(getHeaders(getToken(data))).params(params).get(callBack);
    }

    public static void getOwnerList(Map<String,String> data, ResultCallBack<OwnerListModel> callBack){
        RequestParams params = RequestParams.newInstance();
        params.put("blockUnitId",Integer.valueOf(data.get("blockUnitId")));
        params.put("appKey", data.get("appKey"));
        String url = BASE_HEAD+OWNER_LIST;
        new OkRequest.Builder().url(url).headers(getHeaders(getToken(data))).params(params).get(callBack);
    }

    public static void applyHouse(Map<String,String> data, ResultCallBack<ApplyHouseModel> callBack){
        RequestParams params = RequestParams.newInstance();
        try{
            JSONObject j = new JSONObject();
            j.put("realname",data.get("realname"));
            j.put("mobile",data.get("mobile"));
            j.put("cardNo",data.get("cardNo"));
            j.put("unitId",Integer.valueOf(data.get("unitId")));
            j.put("communityId",Integer.valueOf(data.get("communityId")));
            j.put("userType",data.get("userType"));
            j.put("code",data.get("code"));
            j.put("ownerMobile",data.get("ownerMobile"));
            params.put("unitApplication",j);
        }catch (Exception e){
            e.printStackTrace();
        }
        params.put("appKey", data.get("appKey"));
        String url = BASE_HEAD+APPLY_HOUSE;
        new OkRequest.Builder().url(url).headers(getHeaders(getToken(data))).params(params).post(callBack);
    }

    public static void getCarList(Map<String,String> data, ResultCallBack<CarListModel> callBack){
        RequestParams params = RequestParams.newInstance();
        params.put("unitId",Integer.valueOf(data.get("unitId")));
        params.put("appKey", data.get("appKey"));
        String url = BASE_HEAD+CAR_LIST;
        new OkRequest.Builder().url(url).headers(getHeaders(getToken(data))).params(params).get(callBack);
    }

    public static void applyCar(Map<String,String> data,ResultCallBack<CarApplyModel> callBack){
        RequestParams params = RequestParams.newInstance();
        params.put("appKey", data.get("appKey"));
        try{
            JSONObject j = new JSONObject();
            j.put("carNo",data.get("carNo"));
            j.put("unitId",Integer.valueOf(data.get("unitId")));
            j.put("communityId",Integer.valueOf(data.get("communityId")));
            j.put("state",data.get("state"));
            params.put("car",j);
        }catch (Exception e){

        }
        String url = BASE_HEAD+APPLY_CAR;
        new OkRequest.Builder().url(url).headers(getHeaders(getToken(data))).params(params).get(callBack);
    }

    public static void getAdviceList(Map<String,String> data,ResultCallBack<AdviceListModel> callBack){
        RequestParams params = RequestParams.newInstance();
        params.put("appKey", data.get("appKey"));
        params.put("arrayLength",Integer.valueOf(data.get("arrayLength")));
        String url = BASE_HEAD+RECEVICE_ADVICE;
        new OkRequest.Builder().url(url).headers(getHeaders(getToken(data))).params(params).get(callBack);
    }



    private static Headers getHeaders(String token){
        Headers headers = new Headers.Builder()
                .add("Accept", "application/json")
                .add("Content-Type", "application/json")
                .add("Authorization","Bearer "+token)
                .build();
        return headers;
    }

    private static String getToken(Map<String,String> data){
        String token = "";
        if(data.get("token")!=null && data.get("token").length()>0){
            token = data.get("token");
        }
        return token;
    }



}
