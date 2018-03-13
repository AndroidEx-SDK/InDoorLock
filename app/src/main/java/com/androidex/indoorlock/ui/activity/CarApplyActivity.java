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
import com.androidex.indoorlock.bean.CarApplyModel;
import com.androidex.indoorlock.bean.Event;
import com.androidex.indoorlock.utils.SharedPreTool;
import com.androidex.indoorlock.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/13.
 */

public class CarApplyActivity extends BaseActivity {
    private LinearLayout backLayout;
    private TextView title;
    private EditText carNumber;
    private Button add;

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindView() {
        return R.layout.activity_carapply;
    }

    @Override
    public void initView(View v) {
        backLayout = findViewById(R.id.back);
        backLayout.setOnClickListener(this);
        title = findViewById(R.id.title);
        title.setText("车辆信息");

        carNumber = findViewById(R.id.carNumber);
        add = findViewById(R.id.add);
        add.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.add:
                String strNumber = carNumber.getText().toString().trim();
                if(strNumber == null || strNumber.length()<=0){
                    showToast(false,"请输入车牌号码");
                    return;
                }
                applyCarData();
                break;
        }
    }

    @Override
    public void onMessage(Message msg) {

    }

    @Override
    public void onEvent(Event event) {
        if(event.what ==EVENT_WHAT_APPLYCAT_RESULT){
            hideLoadingDialog();
            CarApplyModel model = (CarApplyModel) event.msg;
            if(model.code == 0){
                showToast(true,"您的申请已经提交到管理处审核，请耐心等待");
                this.finish();
            }else if(model.code == NETWORK_ERROR){
                showToast(false,"请检查网络");
            }else if(model.code == SERVER_ERROR){
                showToast(false,"服务器异常");
            }else{
                showToast(false,"提交失败，请稍后再尝试");
            }
        }
    }

    @Override
    public void mainThread() {

    }

    private void applyCarData(){
        showLoading("正在提交车辆信息...");
        Map<String,String> data = new HashMap<>();
        data.put("carNo",carNumber.getText().toString().trim());
        data.put("state","P");
        for(int i=0;i<signModel.data.size();i++){
            if(signModel.data.get(i).rid == SharedPreTool.getIntValue(SharedPreTool.house_rid)){
                data.put("unitId",signModel.data.get(i).rid+"");
                data.put("communityId",signModel.data.get(i).communityId+"");
            }
        }
        data.put("appKey", Utils.getKey());
        data.put("token",signModel.token);
        postEvent(EVENT_WHAT_APPLYCAT,data);
    }
}
