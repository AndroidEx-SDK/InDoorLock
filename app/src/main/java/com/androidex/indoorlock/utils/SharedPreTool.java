package com.androidex.indoorlock.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.androidex.indoorlock.AndroidexApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;

/**
 * Created by Administrator on 2018/2/27.
 */

public class SharedPreTool {
    private static final String SP_ANDROIDEX_OBJ = "com.androidex.indoorlock.obj";
    private static final String SP_ANDROIDEX_CONFIG = "com.androidex.indoorlock.config";

    public static final String sign_model = "sign_model";
    public static final String house_rid = "house_rid";

    public static String getUUID(){
        String uuid = AndroidexApplication.getContext().getSharedPreferences(SP_ANDROIDEX_CONFIG,Context.MODE_PRIVATE).getString("uuid","");
        if(uuid.length()<=0){
            uuid = UUID.randomUUID().toString();
            AndroidexApplication.getContext().getSharedPreferences(SP_ANDROIDEX_CONFIG,Context.MODE_PRIVATE).edit().putString("uuid",uuid).commit();
        }
        return uuid;
    }

    public static void saveStringValue(String key,String value){
        AndroidexApplication.getContext().getApplicationContext().getSharedPreferences(SP_ANDROIDEX_CONFIG, Activity.MODE_PRIVATE).edit().putString(key,value).commit();
    }
    public static String getStringValue(String key){
        return AndroidexApplication.getContext().getApplicationContext().getSharedPreferences(SP_ANDROIDEX_CONFIG, Activity.MODE_PRIVATE).getString(key,"");
    }
    public static void savaLongValue(String key,long value){
        AndroidexApplication.getContext().getApplicationContext().getSharedPreferences(SP_ANDROIDEX_CONFIG, Activity.MODE_PRIVATE).edit().putLong(key,value).commit();
    }

    public static long getLongValue(String key){
        return AndroidexApplication.getContext().getApplicationContext().getSharedPreferences(SP_ANDROIDEX_CONFIG, Activity.MODE_PRIVATE).getLong(key,0);
    }

    public static void saveIntValue(String key,int value){
        AndroidexApplication.getContext().getApplicationContext().getSharedPreferences(SP_ANDROIDEX_CONFIG, Activity.MODE_PRIVATE).edit().putInt(key,value).commit();
    }

    public static int getIntValue(String key){
        return AndroidexApplication.getContext().getApplicationContext().getSharedPreferences(SP_ANDROIDEX_CONFIG, Activity.MODE_PRIVATE).getInt(key,-1);
    }

    public static void removeValue(String key){
        AndroidexApplication.getContext().getApplicationContext().getSharedPreferences(SP_ANDROIDEX_CONFIG, Activity.MODE_PRIVATE).edit().remove(key).commit();
    }

    public static void saveObject(String key,Object obj){
        SharedPreferences sharedPreferences = AndroidexApplication.getContext().getSharedPreferences(SP_ANDROIDEX_OBJ, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String string = Object2String(obj);
        editor.putString(key, string);
        editor.commit();
    }

    public static Object getObject(String key){
        SharedPreferences sharedPreferences =  AndroidexApplication.getContext().getApplicationContext().getSharedPreferences(SP_ANDROIDEX_OBJ, Activity.MODE_PRIVATE);
        String string = sharedPreferences.getString(key, null);
        if (string != null) {
            Object object = String2Object(string);
            return object;
        } else {
            return null;
        }
    }

    public static void removeObject(String key){
        SharedPreferences sharedPreferences = AndroidexApplication.getContext().getSharedPreferences(SP_ANDROIDEX_OBJ, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }

    private static String Object2String(Object object) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            String string = new String(Base64.encode(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
            objectOutputStream.close();
            return string;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Object String2Object(String objectString) {
        byte[] mobileBytes = Base64.decode(objectString.getBytes(), Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(mobileBytes);
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object object = objectInputStream.readObject();
            objectInputStream.close();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
