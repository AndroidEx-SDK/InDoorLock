package com.androidex.indoorlock.ui.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.androidex.indoorlock.R;
import com.androidex.indoorlock.adapter.HouseDetailsAdapter;
import com.androidex.indoorlock.base.BaseActivity;
import com.androidex.indoorlock.bean.Event;
import com.androidex.indoorlock.bean.HouseDetailModel;
import com.androidex.indoorlock.bean.SignModel;
import com.androidex.indoorlock.utils.SharedPreTool;
import com.androidex.indoorlock.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/9.
 */

public class HouseDetailsActivity extends BaseActivity {
    private LinearLayout backLayout;
    private TextView title;
    private TextView name;
    private TextView area;
    private TextView type;
    private ListView listView;
    private int rid = -1;
    private String houseName = "";
    private int unitArea = -1;
    private String unitType = "";
    @Override
    public void initParms(Bundle parms) {
        rid = parms.getInt("rid");
        houseName = parms.getString("houseName");
        unitArea = parms.getInt("unitArea");
        unitType = Utils.judgeHouseType(parms.getString("unitType"));
    }

    @Override
    public int bindView() {
        return R.layout.activity_housedetails;
    }

    @Override
    public void initView(View v) {
        backLayout = findViewById(R.id.back);
        backLayout.setOnClickListener(this);
        title = findViewById(R.id.title);
        title.setText("我的房屋");

        name = findViewById(R.id.name);
        name.setText(houseName);
        area = findViewById(R.id.area);
        area.setText(unitArea+"平米");
        type = findViewById(R.id.type);
        type.setText(unitType);
        listView = findViewById(R.id.listview);
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
        switch (event.what){
            case EVENT_WHAT_HOUSEINFO_RESULT:
                hideLoadingDialog();
                HouseDetailModel model = (HouseDetailModel) event.msg;
                if(model!=null){
                    if(model.code == 0){
                        if(model.members!=null && model.members.size()>0){
                            listView.setAdapter(new HouseDetailsAdapter(this,model.members));
                        }
                    }else if(model.code == NETWORK_ERROR){
                        showToast(false,"请检查网络");
                    }else if(model.code == SERVER_ERROR){
                        showToast(false,"服务器异常");
                    }
                }
                break;
        }
    }

    @Override
    public void mainThread() {
        SignModel model = (SignModel) SharedPreTool.getObject(SharedPreTool.sign_model);
        if(model!=null){
            showLoading("正在加载...");
            Map<String,String> data = new HashMap<>();
            data.put("unitId",rid+"");
            data.put("appKey", Utils.getKey());
            data.put("token",model.token);
            postEvent(EVENT_WHAT_HOUSEINFO,data);
        }
    }
}
