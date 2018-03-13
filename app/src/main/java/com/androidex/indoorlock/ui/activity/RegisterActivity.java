package com.androidex.indoorlock.ui.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidex.indoorlock.R;
import com.androidex.indoorlock.base.BaseActivity;
import com.androidex.indoorlock.bean.Event;
import com.androidex.indoorlock.bean.RegisterModel;
import com.androidex.indoorlock.utils.SMSSDKTools;
import com.androidex.indoorlock.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/9.
 */

public class RegisterActivity extends BaseActivity {
    private LinearLayout backLayout;
    private TextView title;
    private TextView name;
    private TextView phone;
    private TextView code;
    private TextView getcode;
    private TextView password;
    private TextView repassword;
    private Button register;
    private SMSSDKTools smssdkTools;
    private String strName, strPhone, strPassword, strrePassword, strCode;
    @Override
    public void initParms(Bundle parms) {
        smssdkTools = SMSSDKTools.newInstance();
        smssdkTools.initSMS(new SMSSDKTools.OnSMSEvent() {
            @Override
            public void onCode(int code) {
                hideLoadingDialog();
                switch (code){
                    case SMSSDKTools.OnSMSEvent.SMS_EVENT_NETWORK:
                        showToast(false,"请检查网络");
                        break;
                    case SMSSDKTools.OnSMSEvent.SMS_EVENT_MOBILE_NULL:
                        showToast(false,"手机号码不能为空");
                        break;
                    case SMSSDKTools.OnSMSEvent.SMS_EVENT_CODE_NULL:
                        showToast(false,"验证码不能为空");
                        break;
                    case SMSSDKTools.OnSMSEvent.SMS_EVENT_GET_COMPLETE:
                        showToast(true,"验证码获取成功");
                        mHandler.sendEmptyMessage(0x01);
                        break;
                    case SMSSDKTools.OnSMSEvent.SMS_EVENT_GET_ERROR:
                        showToast(false,"验证码获取失败");
                        break;
                    case SMSSDKTools.OnSMSEvent.SMS_EVENT_CHECK_COMPLETE:
                        showToast(true,"验证码校验成功");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                registerAccountData();
                            }
                        });
                        break;
                    case SMSSDKTools.OnSMSEvent.SMS_EVENT_CHECK_ERROR:
                        showToast(false,"验证码校验失败");
                        break;
                }
            }
        });
    }

    @Override
    public int bindView() {
        return R.layout.activity_register;
    }

    @Override
    public void initView(View v) {
        backLayout = findViewById(R.id.back);
        backLayout.setOnClickListener(this);
        title = findViewById(R.id.title);
        title.setText("注册");

        name = findViewById(R.id.register_name);
        phone = findViewById(R.id.register_phone);
        code = findViewById(R.id.register_code);
        getcode = findViewById(R.id.register_getcode);
        getcode.setOnClickListener(this);
        password = findViewById(R.id.register_password);
        repassword  =findViewById(R.id.register_repassword);
        register = findViewById(R.id.register_button);
        register.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.register_getcode:
                if(phone.getText().toString().trim().length()!=11){
                    showToast(false,"请输入正确的电话号码");
                    return;
                }
                showLoading("正在获取验证码...");
                smssdkTools.getVerificationCode(phone.getText().toString().trim());
                break;
            case R.id.register_button:
                registerAccount();
                break;
        }
    }

    private  int second = 60;

    @Override
    public void onMessage(Message msg) {
        if(msg.what == 0x01){
            second = 60;
            getcode.setEnabled(false);
            getcode.setText("等待"+second+"秒");
            mHandler.sendEmptyMessageDelayed(0x02,1000);
        }else if(msg.what == 0x02){
            second--;
            if(second >=0){
                getcode.setText("等待"+second+"秒");
                mHandler.sendEmptyMessageDelayed(0x02,1000);
            }else{
                getcode.setText("获取验证码");
                getcode.setEnabled(true);
            }
        }
    }

    @Override
    public void onEvent(Event event) {
        if(event.what == EVENT_WHAT_REGISTER_RESULT){
            hideLoadingDialog();
            RegisterModel model = (RegisterModel) event.msg;
            if(model.code == 0) {
                showToast(true, "注册成功");
                this.finish();
            }else if(model.code == 1){
                showToast(false,"您输入的手机号已经被注册");
            }else if(model.code == 2){
                showToast(false,"服务器异常");
            }else if(model.code == NETWORK_ERROR){
                showToast(false,"请检查网络");
            }else if(model.code == SERVER_ERROR){
                showToast(false,"服务器异常");
            }
        }
    }

    @Override
    public void mainThread() {

    }

    private void registerAccount(){
        strName = name.getText().toString().trim();
        strPhone = phone.getText().toString().trim();
        strCode = code.getText().toString().trim();
        strPassword = password.getText().toString().trim();
        strrePassword = repassword.getText().toString().trim();
        if(strName == null || strName.length()<=0){
            showToast(false,"用户名不能为空");
            return;
        }
        if(strPhone == null || strPhone.length()!=11){
            showToast(false,"请输入正确的手机号码");
            return;
        }
        if(strCode == null && strCode.length()<=0){
            showToast(false,"请输入验证码");
            return;
        }
        if(strPassword == null || strPassword.length()<6 || strPassword.length()>12){
            showToast(false,"请输入6-12位的密码");
            return;
        }
        if(strrePassword == null || !strPassword.equals(strrePassword)){
            showToast(false,"两次密码输入不一致，请从新输入");
            return;
        }
        showLoading("正在验证校验码...");
        smssdkTools.submitVerificationCode(strPhone,strCode);
    }

    private void registerAccountData(){
        showLoading("正在注册...");
        Map<String,String> data = new HashMap<>();
        data.put("realname",strName);
        data.put("mobile",strPhone);
        data.put("password",strPassword);
        data.put("repassword",strrePassword);
        data.put("code",strCode);
        data.put("appKey", Utils.getDefKey());
        postEvent(EVENT_WHAT_REGISTER,data);
    }
}
