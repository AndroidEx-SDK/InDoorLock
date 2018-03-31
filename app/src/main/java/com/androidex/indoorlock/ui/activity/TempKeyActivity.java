package com.androidex.indoorlock.ui.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.androidex.indoorlock.R;
import com.androidex.indoorlock.adapter.TempKeyAdapter;
import com.androidex.indoorlock.base.BaseActivity;
import com.androidex.indoorlock.bean.Event;
import com.androidex.indoorlock.bean.TempKeyModel;
import com.androidex.indoorlock.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/6.
 */

public class TempKeyActivity extends BaseActivity {
    private LinearLayout backLayout;
    private TextView title;
    private TextView menu;
    private ListView listView;

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindView() {
        return R.layout.activity_doorpassword;
    }

    @Override
    public void initView(View v) {
        backLayout = findViewById(R.id.back);
        backLayout.setOnClickListener(this);
        title = findViewById(R.id.title);
        title.setText("访客通行");
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
                startActivity(CreateTempKeyActivity.class, null);
                break;
        }
    }

    @Override
    public void onMessage(Message msg) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        showLoading("正在加载....");
        Map<String, String> data = new HashMap<>();
        data.put("arrayLength", 0 + "");
        data.put("appKey", Utils.getKey());
        data.put("token", signModel.token);
        postEvent(EVENT_WHAT_TEMPKEY, data);
    }

    @Override
    public void onEvent(Event event) {
        switch (event.what) {
            case EVENT_WHAT_TEMPKEY_RESULT:
                hideLoadingDialog();
                TempKeyModel model = (TempKeyModel) event.msg;
                if (model.code == 0) {
                    if (model.data.size() > 0) {
                        listView.setAdapter(new TempKeyAdapter(this, model.data));
                    }
                } else if (model.code == NETWORK_ERROR) {
                    showToast(false, "网络异常");
                } else if (model.code == SERVER_ERROR) {
                    showToast(false, "服务器异常");
                }
                break;
        }
    }

    @Override
    public void mainThread() {

    }
}
