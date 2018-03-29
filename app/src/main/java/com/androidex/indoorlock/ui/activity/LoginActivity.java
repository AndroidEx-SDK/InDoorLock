package com.androidex.indoorlock.ui.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidex.indoorlock.R;
import com.androidex.indoorlock.base.BaseActivity;
import com.androidex.indoorlock.bean.Event;
import com.androidex.indoorlock.bean.SignModel;
import com.androidex.indoorlock.service.AndroidexService;
import com.androidex.indoorlock.utils.SharedPreTool;
import com.androidex.indoorlock.utils.Utils;
import com.androidex.indoorlock.view.MessageAlert;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/2/27.
 */

public class LoginActivity extends BaseActivity {
    private LinearLayout backLayout;
    private TextView titleBar;
    private EditText phoneEdittext, passwordEdittext;
    private Button loginButton, registerButton;
    private boolean isInit = true;
    private boolean isTopAccount = false;
    private MessageAlert messageAlert;

    @Override
    public void initParms(Bundle parms) {
        if (!AndroidexService.isRun) {
            isInit = false;
            startService(AndroidexService.class);
        }
        if (parms != null) {
            int mode = parms.getInt("mode");
            isTopAccount = mode == 2 ? true : false;
            if (isTopAccount) {
                if (messageAlert == null) {
                    messageAlert = new MessageAlert(this, R.style.Dialog, "账号异常", "您的账号，已经在别的地方登录了！", null);
                }
                messageAlert.show();
            }
        }
    }

    @Override
    public int bindView() {
        return R.layout.activity_login;
    }

    @Override
    public void initView(View v) {
        backLayout = findViewById(R.id.titlebar_back_layout);
        backLayout.setOnClickListener(this);
        backLayout.setVisibility(View.GONE);
        titleBar = findViewById(R.id.title_bar_text);
        titleBar.setText("登    录");
        phoneEdittext = findViewById(R.id.sign_in_phone);
        passwordEdittext = findViewById(R.id.sign_in_password);
        loginButton = findViewById(R.id.sign_in_button_login);
        registerButton = findViewById(R.id.sign_in_button_register);
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    @Override
    public void onMessage(Message msg) {

    }

    @Override
    public void onEvent(Event event) {
        switch (event.what) {
            case EVENT_WHAT_SERVICE_LOGIN_CALLBACK:
                hideLoadingDialog();
                SignModel model = (SignModel) event.msg;
                if (model != null) {
                    if (model.code == 0) {
                        showToast(true, "登录成功");
                        startActivity(HomeActivity.class, null);
                        this.finish();
                    } else if (model.code == NETWORK_ERROR) {
                        showToast(false, "无网络连接");
                    } else if (model.code == SERVER_ERROR) {
                        showToast(false, "服务器拒绝访问");
                    } else if (model.code == 2) {
                        showToast(false, "密码错误");
                    }
                }
                break;
            case EVENT_WHAT_SERVICE_INIT:
                showL("AndroidexService init ");
                isInit = true;
                break;
        }
    }

    @Override
    public void mainThread() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.titlebar_back_layout:
                this.finish();
                break;
            case R.id.sign_in_button_login:
                login();
                break;
            case R.id.sign_in_button_register:
                startActivity(RegisterActivity.class, null);
                break;
        }
    }

    private void login() {
        if (!isNetWork()) {
            showToast(false, "无网络连接");
            return;
        }
        String mobile = phoneEdittext.getText().toString().trim();
        String psd = passwordEdittext.getText().toString().trim();
        if (mobile == null || mobile.length() <= 0) {
            showToast(false, "请输入手机号码");
            return;
        }
        if (psd == null || psd.length() <= 0) {
            showToast(false, "请输入密码");
            return;
        }
        if (!isInit) {
            showToast(false, "正在初始化");
            return;
        }
        Map<String, String> data = new HashMap<>();
        data.put("username", mobile);
        data.put("password", psd);
        data.put("deviceUuid", SharedPreTool.getUUID());
        postEvent(EVENT_WHAT_SERVICE_LOGIN, data);
        showLoading("正在登录....");
    }
}
