package com.androidex.indoorlock.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.androidex.indoorlock.R;
import com.androidex.indoorlock.adapter.AdviceAdapter;
import com.androidex.indoorlock.base.BaseActivity;
import com.androidex.indoorlock.bean.AdviceListModel;
import com.androidex.indoorlock.bean.Event;
import com.androidex.indoorlock.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/14.
 */

public class AdviceActivity extends BaseActivity {
    private LinearLayout backLayout;
    private TextView title;
    private ListView listview;

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindView() {
        return R.layout.activity_advice;
    }

    @Override
    public void initView(View v) {
        backLayout = findViewById(R.id.back);
        backLayout.setOnClickListener(this);
        title = findViewById(R.id.title);
        title.setText("投诉建议");
        listview = findViewById(R.id.listview);
    }

    @Override
    public void onMessage(Message msg) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.menu:
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        showLoading("正在加载数据...");
        Map<String,String> data = new HashMap<>();
        data.put("arrayLength",0+"");
        data.put("appKey", Utils.getKey());
        data.put("token",signModel.token);
        postEvent(EVENT_WHAT_ADVICE,data);
    }

    @Override
    public void onEvent(Event event) {
        if(event.what == EVENT_WHAT_ADVICE_RESULT){
            hideLoadingDialog();
            final AdviceListModel model = (AdviceListModel) event.msg;
            if(model!=null && model.data!=null && model.data.size()>0){
                listview.setAdapter(new AdviceAdapter(this,model.data));
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(AdviceActivity.this,AdviceDetailsActivity.class);
                        intent.putExtra("advice",model.data.get(i));
                        startActivity(intent);
                    }
                });
            }
        }
    }

    @Override
    public void mainThread() {

    }
}
