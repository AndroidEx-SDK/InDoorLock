package com.androidex.indoorlock.ui.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.androidex.indoorlock.R;
import com.androidex.indoorlock.adapter.CarAdapter;
import com.androidex.indoorlock.base.BaseActivity;
import com.androidex.indoorlock.bean.CarListModel;
import com.androidex.indoorlock.bean.Event;
import com.androidex.indoorlock.utils.SharedPreTool;
import com.androidex.indoorlock.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/13.
 */

public class CarDetailsActivity extends BaseActivity {
    private LinearLayout backLayout;
    private TextView title;
    private TextView add;
    private ListView listview;
    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindView() {
        return R.layout.activity_cardetails;
    }

    @Override
    public void initView(View v) {
        backLayout = findViewById(R.id.back);
        backLayout.setOnClickListener(this);
        title = findViewById(R.id.title);
        title.setText("我的车辆");

        add = findViewById(R.id.add);
        add.setOnClickListener(this);
        listview = findViewById(R.id.listview);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.add:
                startActivity(CarApplyActivity.class,null);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        showLoading("正在加载车辆信息...");
        Map<String,String> data = new HashMap<>();
        data.put("unitId",SharedPreTool.getIntValue(SharedPreTool.house_rid)+"");
        data.put("appKey", Utils.getKey());
        data.put("token",signModel.token);
        postEvent(EVENT_WHAT_CAR,data);
    }

    @Override
    public void onMessage(Message msg) {

    }

    @Override
    public void onEvent(Event event) {
        if(event.what == EVENT_WHAT_CAR_RESULT){
            hideLoadingDialog();
            CarListModel model = (CarListModel) event.msg;
            if(model!=null && model.code == 0){
                if(model.cars != null && model.cars.size()>0){
                    listview.setAdapter(new CarAdapter(this, model.cars, new CarAdapter.OnEvent() {
                        @Override
                        public void onDatele(int rid) {
                            showL("按了删除"+rid);
                        }

                        @Override
                        public void onLock(int rid) {
                            showL("按了锁定"+rid);
                        }
                    }));
                }
            }else if(model.code == NETWORK_ERROR){
                showToast(false,"请检查网络");
            }else if(model.code == SERVER_ERROR){
                showToast(false,"服务器异常");
            }else{
                showToast(false,"数据获取失败，请稍后重试");
            }
        }
    }

    @Override
    public void mainThread() {

    }
}
