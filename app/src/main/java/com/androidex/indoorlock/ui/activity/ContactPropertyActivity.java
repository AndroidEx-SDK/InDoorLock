package com.androidex.indoorlock.ui.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.androidex.indoorlock.R;
import com.androidex.indoorlock.adapter.PropertyAdapter;
import com.androidex.indoorlock.base.BaseActivity;
import com.androidex.indoorlock.bean.Event;
import com.androidex.indoorlock.bean.PropertyDataModel;
import com.androidex.indoorlock.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/15.
 */

public class ContactPropertyActivity extends BaseActivity {
    private LinearLayout backLayout;
    private TextView title;
    private ListView listview;

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindView() {
        return R.layout.activity_property;
    }

    @Override
    public void initView(View v) {
        backLayout = findViewById(R.id.back);
        backLayout.setOnClickListener(this);
        title = findViewById(R.id.title);
        title.setText("联系物业");
        listview = findViewById(R.id.listview);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
        }
    }

    @Override
    public void onMessage(Message msg) {

    }

    @Override
    public void onEvent(Event event) {
        if(event.what == EVENT_WHAT_PROPERTY_RESULT){
             hideLoadingDialog();
            PropertyDataModel model = (PropertyDataModel) event.msg;
            if(model!=null && model.data!=null && model.data.size()>0){
                listview.setAdapter(new PropertyAdapter(this,model.data));
            }else{
                if(model.code == NETWORK_ERROR){
                    showToast(false,"请检查网络");
                }else if(model.code == SERVER_ERROR){
                    showToast(false,"服务器异常");
                }else{
                    showToast(false,"数据获取失败，请稍后重试");
                }
            }
        }
    }

    @Override
    public void mainThread() {
        getPropertyData();
    }

    private void getPropertyData(){
        showLoading("正在获取物业信息...");
        Map<String,String> data = new HashMap<>();
        data.put("arrayLength",0+"");
        data.put("appKey", Utils.getKey());
        data.put("token",signModel.token);
        postEvent(EVENT_WHAT_PROPERTY,data);
    }
}
