package com.androidex.indoorlock.ui.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidex.indoorlock.R;
import com.androidex.indoorlock.adapter.AccessAdapter;
import com.androidex.indoorlock.base.BaseActivity;
import com.androidex.indoorlock.bean.AccessModel;
import com.androidex.indoorlock.bean.Event;
import com.androidex.indoorlock.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/8.
 */

public class AccessActivity extends BaseActivity {
    private LinearLayout backLayout;
    private TextView title;
    private ListView listView;

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindView() {
        return R.layout.activity_access;
    }

    @Override
    public void initView(View v) {
        backLayout = findViewById(R.id.back);
        backLayout.setOnClickListener(this);
        title = findViewById(R.id.title);
        title.setText("开门记录");
        listView = findViewById(R.id.listview);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.back){
            this.finish();
        }
    }

    @Override
    public void onMessage(Message msg) {

    }

    @Override
    public void onEvent(Event event) {
        switch (event.what){
            case EVENT_WHAT_RECEVICE_ACCESS_RESULT:
                hideLoadingDialog();
                AccessModel accessModel = (AccessModel) event.msg;
                if(accessModel!=null){
                    if(accessModel.code == 0){
                        if(accessModel.data!=null && accessModel.data.size()>0){
                            listView.setAdapter(new AccessAdapter(this,accessModel.data));
                        }
                    }else if(accessModel.code == NETWORK_ERROR){
                        showToast(false,"请检查网络");
                    }else if(accessModel.code ==SERVER_ERROR){
                        showToast(false,"服务器异常");
                    }
                }
                break;
        }
    }

    @Override
    public void mainThread() {
        Map<String,String> data = new HashMap<>();
        data.put("arrayLength",0+"");
        data.put("appKey", Utils.getKey());
        data.put("token",signModel.token);
        postEvent(EVENT_WHAT_RECEVICE_ACCESS,data);
        showLoading("正在获取开门记录...");
    }
}
