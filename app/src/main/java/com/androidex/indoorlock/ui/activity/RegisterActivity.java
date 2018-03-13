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
    @Override
    public void initParms(Bundle parms) {

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

                break;
            case R.id.register_button:
                registerAccount();
                break;
        }
    }

    @Override
    public void onMessage(Message msg) {

    }

    @Override
    public void onEvent(Event event) {
        if(event.what == EVENT_WHAT_REGISTER_RESULT){
            hideLoadingDialog();
            RegisterModel model = (RegisterModel) event.msg;
            if(model.code == 0){
                showToast(true,"注册成功");
                this.finish();
            }else if(model.code == 2){
                showToast(false,"同一手机号只能注册一个账号");
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
        String strName = name.getText().toString().trim();
        String strPhone = phone.getText().toString().trim();
        String strCode = code.getText().toString().trim();
        String strPassword = password.getText().toString().trim();
        String strrePassword = repassword.getText().toString().trim();
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
        registerAccount(strName,strPhone,strPassword,strrePassword,strCode);
    }

    private void registerAccount(String strName,String strPhone,String strPassword,String strrePassword,String strCode){
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
