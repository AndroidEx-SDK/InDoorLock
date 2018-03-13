package com.androidex.indoorlock.ui.activity;

import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidex.indoorlock.R;
import com.androidex.indoorlock.base.BaseActivity;
import com.androidex.indoorlock.bean.Event;
import com.androidex.indoorlock.bean.SignModel;
import com.androidex.indoorlock.utils.SharedPreTool;
import com.androidex.indoorlock.utils.Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/5.
 */

public class UpdateActivity extends BaseActivity {
    private LinearLayout backLayout;
    private TextView title;
    private EditText oldPassword;
    private EditText newPassword;
    private EditText renewPassword;
    private Button confirm;
    private String oldText = "",newText = "",renewText = "";
    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindView() {
        return R.layout.activity_update;
    }

    @Override
    public void initView(View v) {
        backLayout = findViewById(R.id.back);
        backLayout.setOnClickListener(this);
        title = findViewById(R.id.title);
        oldPassword = findViewById(R.id.old_password);
        oldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                oldText = editable.toString();
                updateConfirm();
            }
        });
        newPassword = findViewById(R.id.new_password);
        newPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                newText = editable.toString();
                updateConfirm();
            }
        });
        renewPassword = findViewById(R.id.re_new_password);
        renewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                renewText = editable.toString();
                updateConfirm();
            }
        });
        confirm = findViewById(R.id.confirm);
        confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.back){
            this.finish();
        }else if(view.getId() == R.id.confirm){
            updatePassword();
        }
    }

    @Override
    public void onMessage(Message msg) {

    }

    @Override
    public void onEvent(Event event) {
        switch (event.what){
            case EVENT_WHAT_UPDATE_PASSWORD_CALLBACK:
                hideLoadingDialog();
                break;
        }
    }

    @Override
    public void mainThread() {

    }

    private void updatePassword(){
        if(oldText == null || oldText.length()<6 || oldText.length()>12){
            showToast(false,"请输入6至12位有效密码");
            return;
        }
        if(newText == null || newText.length()<6 || newText.length()>12){
            showToast(false,"请输入6至12位有效密码");
            return;
        }
        if(renewText == null || renewText.length()<6 || renewText.length()>12){
            showToast(false,"请输入6至12位有效密码");
            return;
        }
        if(!newText.equals(renewText)){
            showToast(false,"新密码两次输入不一致，请重新确认");
           return;
        }
        SignModel model = (SignModel) SharedPreTool.getObject(SharedPreTool.sign_model);
        if(model != null){
            Map<String,String> data = new HashMap<String,String>();
            data.put("rid",model.user.rid+"");
            data.put("password",oldText);
            data.put("password1",newText);
            data.put("appKey", Utils.getKey());
            data.put("token",model.token);
            postEvent(EVENT_WHAT_UPDATE_PASSWORD,data);
            showLoading("正在修改密码");
        }


    }

    private void updateConfirm(){
        if(oldText == null || oldText.length()<6 || oldText.length()>12){
            confirm.setBackgroundResource(R.mipmap.ic_update_e);
            return;
        }
        if(newText == null || newText.length()<6 || newText.length()>12){
            confirm.setBackgroundResource(R.mipmap.ic_update_e);
            return;
        }
        if(renewText == null || renewText.length()<6 || renewText.length()>12){
            confirm.setBackgroundResource(R.mipmap.ic_update_e);
            return;
        }
        confirm.setBackgroundResource(R.mipmap.ic_update_s);
    }
}
