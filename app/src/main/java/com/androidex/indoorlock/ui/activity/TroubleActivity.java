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
import com.androidex.indoorlock.adapter.TroubleAdapter;
import com.androidex.indoorlock.base.BaseActivity;
import com.androidex.indoorlock.bean.Event;
import com.androidex.indoorlock.bean.TroubleListModel;
import com.androidex.indoorlock.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/16.
 */

public class TroubleActivity extends BaseActivity {
    private LinearLayout backLayout;
    private TextView title;
    private TextView menu;
    private ListView listView;

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindView() {
        return R.layout.activity_trouble;
    }

    @Override
    public void initView(View v) {
        backLayout = findViewById(R.id.back);
        backLayout.setOnClickListener(this);
        title = findViewById(R.id.title);
        title.setText("维修申报");
        menu = findViewById(R.id.menu);
        menu.setText("新建");
        menu.setOnClickListener(this);
        listView = findViewById(R.id.listview);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                this.finish();
                break;
            case R.id.menu:
                //startActivity(TroubleCreateActivity.class,null); //图片提交接口不可用，先屏蔽
                break;
        }
    }

    @Override
    public void onMessage(Message msg) {

    }

    @Override
    public void onEvent(Event event) {
        if (event.what == EVENT_WHAT_TROUBLE_RESULT) {
            hideLoadingDialog();
            final TroubleListModel model = (TroubleListModel) event.msg;
            if (model.code == 0) {
                if (model.data != null && model.data.size() > 0) {
                    listView.setAdapter(new TroubleAdapter(this, model.data));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent(TroubleActivity.this, TroubleDetailsActivity.class);
                            intent.putExtra("trouble", model.data.get(i));
                            startActivity(intent);
                        }
                    });
                }
            } else {

            }
        }
    }

    @Override
    public void mainThread() {
        getTroubleList();
    }

    private void getTroubleList() {
        showLoading("正在加载数据...");
        Map<String, String> data = new HashMap<>();
        data.put("arrayLength", 0 + "");
        data.put("appKey", Utils.getKey());
        data.put("token", signModel.token);
        postEvent(EVENT_WHAT_TROUBLE, data);
    }
}
