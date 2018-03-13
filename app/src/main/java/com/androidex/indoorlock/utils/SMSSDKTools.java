package com.androidex.indoorlock.utils;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by Administrator on 2018/3/12.
 */

public class SMSSDKTools {
    private static SMSSDKTools instance;
    private OnSMSEvent event;
    private EventHandler eventHandler = new EventHandler(){
        @Override
        public void afterEvent(int eve, int result, Object data) {
            if(eve == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                if(result == SMSSDK.RESULT_COMPLETE){
                    event.onCode(OnSMSEvent.SMS_EVENT_GET_COMPLETE);
                }else{
                    event.onCode(OnSMSEvent.SMS_EVENT_GET_ERROR);
                }
            }else if(eve == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                if(result == SMSSDK.RESULT_COMPLETE){
                    event.onCode(OnSMSEvent.SMS_EVENT_CHECK_COMPLETE);
                }else{
                    event.onCode(OnSMSEvent.SMS_EVENT_CHECK_ERROR);
                }
            }
        }
    };

    public static SMSSDKTools newInstance(){
        if(instance == null){
            instance = new SMSSDKTools();
        }
        return instance;
    }

    public void initSMS(OnSMSEvent event){
        this.event = event;
        SMSSDK.registerEventHandler(eventHandler);
    }

    public void exitSMS(){
        SMSSDK.unregisterAllEventHandler();
    }

    public void getVerificationCode(String mobile){
        if(mobile == null || mobile.length()<=0){
            event.onCode(OnSMSEvent.SMS_EVENT_MOBILE_NULL);
            return;
        }
        if(!Utils.isNetworkAvailable()){
            event.onCode(OnSMSEvent.SMS_EVENT_NETWORK);
            return;
        }
        SMSSDK.getVerificationCode("86",mobile);
    }

    public void submitVerificationCode(String mobile,String code){
        if(mobile == null || mobile.length()<=0){
            event.onCode(OnSMSEvent.SMS_EVENT_MOBILE_NULL);
            return;
        }
        if(code == null || code.length()<=0){
            event.onCode(OnSMSEvent.SMS_EVENT_CODE_NULL);
            return;
        }
        SMSSDK.submitVerificationCode("86",mobile,code);
    }
    public interface OnSMSEvent{
        public static int  SMS_EVENT_MOBILE_NULL = 0x01;
        public static int SMS_EVENT_CODE_NULL = 0x05;
        public static int SMS_EVENT_NETWORK = 0x02;
        public static int SMS_EVENT_GET_COMPLETE = 0x03;
        public static int SMS_EVENT_GET_ERROR = 0x04;
        public static int SMS_EVENT_CHECK_COMPLETE = 0x06;
        public static int SMS_EVENT_CHECK_ERROR = 0x07;
        public void onCode(int code);
    }
}
