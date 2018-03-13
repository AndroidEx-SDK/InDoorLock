package com.androidex.indoorlock.ui.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.androidex.indoorlock.R;
import com.androidex.indoorlock.adapter.HouseAdapter;
import com.androidex.indoorlock.base.BaseActivity;
import com.androidex.indoorlock.bean.Event;
import com.androidex.indoorlock.bean.SignModel;
import com.androidex.indoorlock.utils.SharedPreTool;

import org.json.JSONObject;

/**
 * Created by Administrator on 2018/3/6.
 */

public class DoorLockActivity extends BaseActivity {
    private LinearLayout lockLayout;
    private ListView listView;
    private TextView error;
    private LinearLayout backLayout;
    private TextView title;
    private SignModel model;

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindView() {
        return R.layout.activity_doorlock;
    }

    @Override
    public void initView(View v) {
        lockLayout = findViewById(R.id.lock_layout);
        listView = findViewById(R.id.listview);
        error = findViewById(R.id.error);
        backLayout = findViewById(R.id.back);
        backLayout.setOnClickListener(this);
        title = findViewById(R.id.title);
        title.setText("小区门禁");
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

    }

    @Override
    public void mainThread() {
        model = (SignModel) SharedPreTool.getObject(SharedPreTool.sign_model);
        if(model!=null && model.data.size()>0){
            lockLayout.setVisibility(View.VISIBLE);
            error.setVisibility(View.GONE);
            listView.setAdapter(new HouseAdapter(this,model.data));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    try{
                        String key = model.lockList.get(i).lockKey;
                        int blockid = model.lockList.get(i).blockId;
                        String unitNo = "";
                        for(int j=0;j<model.data.size();j++){
                            if(blockid == model.data.get(j).blockId){
                                unitNo = model.data.get(j).unitNo;
                                break;
                            }
                        }
                        JSONObject j = new JSONObject();
                        j.put("key",key);
                        j.put("unitNo",unitNo);
                        postEvent(EVENT_WHAT_OPEN_LOCK,j);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }else{
            lockLayout.setVisibility(View.GONE);
            error.setVisibility(View.VISIBLE);
        }
    }
}
