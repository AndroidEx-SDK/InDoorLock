package com.androidex.indoorlock.ui.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.androidex.indoorlock.R;
import com.androidex.indoorlock.adapter.HouseAdapter;
import com.androidex.indoorlock.base.BaseActivity;
import com.androidex.indoorlock.bean.Event;
import com.androidex.indoorlock.bean.SignModel;
import com.androidex.indoorlock.utils.SharedPreTool;

/**
 * Created by Administrator on 2018/3/6.
 */

public class HouseActivity extends BaseActivity {
    private LinearLayout backLayout;
    private TextView title;
    private ListView listView;
    private TextView menu;

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindView() {
        return R.layout.activity_house;
    }

    @Override
    public void initView(View v) {
        backLayout = findViewById(R.id.back);
        backLayout.setOnClickListener(this);
        title = findViewById(R.id.title);
        title.setText("我的房屋");
        menu = findViewById(R.id.menu);
        menu.setText("申请");
        menu.setOnClickListener(this);
        listView = findViewById(R.id.listview);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.menu:
                startActivity(ApplyHouseActivity.class,null);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        showLoading("正在获取房屋信息...");
        postEvent(EVENT_WHAT_SERVICE_LOGIN);
    }

    @Override
    public void onMessage(Message msg) {

    }

    @Override
    public void onEvent(Event event) {
        if(event.what == EVENT_WHAT_SERVICE_LOGIN_CALLBACK){
            hideLoadingDialog();
            final SignModel model  = (SignModel) SharedPreTool.getObject(SharedPreTool.sign_model);
            if(model!=null && model.data.size()>0){
                listView.setAdapter(new HouseAdapter(this,model.data));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Bundle b = new Bundle();
                        b.putInt("rid",model.data.get(i).rid);
                        b.putString("houseName",model.data.get(i).unitName);
                        b.putInt("unitArea",model.data.get(i).unitArea);
                        b.putString("unitType",model.data.get(i).unitType);
                        startActivity(HouseDetailsActivity.class,b);
                    }
                });
            }
        }
    }

    @Override
    public void mainThread() {

    }
}
