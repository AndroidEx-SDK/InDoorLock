package com.androidex.indoorlock.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidex.indoorlock.R;
import com.androidex.indoorlock.adapter.SlectPhotoAdapter;
import com.androidex.indoorlock.base.BaseActivity;
import com.androidex.indoorlock.bean.Event;

import java.io.File;
import java.util.ArrayList;

import me.nereo.multi_image_selector.MultiImageSelector;

/**
 * Created by Administrator on 2018/3/16.
 */

public class TroubleCreateActivity extends BaseActivity {
    private LinearLayout backLayout;
    private TextView title;
    private RecyclerView recyclerView;
    private RelativeLayout addButton;
    private static final int REQUEST_IMAGE = 0x01;
    private ArrayList<String> mSelectPath;

    private EditText troubleTitle;
    private EditText content;
    private Button cancel;
    private Button confirm;

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public int bindView() {
        return R.layout.activity_troublecreate;
    }

    @Override
    public void initView(View v) {
        backLayout = findViewById(R.id.back);
        backLayout.setOnClickListener(this);
        title = findViewById(R.id.title);
        title.setText("提交维修申报");
        addButton = findViewById(R.id.add_photo);
        addButton.setOnClickListener(this);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        troubleTitle = findViewById(R.id.trouble_title);
        content = findViewById(R.id.content);
        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        confirm = findViewById(R.id.confirm);
        confirm.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancel:
            case R.id.back:
                this.finish();
                break;
            case R.id.add_photo:
                selectPhoto();
                break;
            case R.id.confirm:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE){
            if(resultCode == RESULT_OK){
                mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                if(mSelectPath!=null && mSelectPath.size()>0){
                    showLoading("正在提交图片...");
                    postEvent(EVENT_WHAT_UPLOAD_IMAGE,new File(mSelectPath.get(0)));
                    recyclerView.setAdapter(new SlectPhotoAdapter(this,mSelectPath));
                }
            }
        }
    }

    @Override
    public void onMessage(Message msg) {

    }

    @Override
    public void onEvent(Event event) {
        if(event.what == EVENT_WHAT_UPLOAD_IMAGE_RESULT){
            hideLoadingDialog();
        }
    }

    @Override
    public void mainThread() {

    }

    private void selectPhoto(){
        MultiImageSelector selector = MultiImageSelector.create(TroubleCreateActivity.this);
        selector.showCamera(false);
        selector.count(9);
        selector.multi();
        selector.origin(mSelectPath);
        selector.start(TroubleCreateActivity.this, REQUEST_IMAGE);
    }

    private void checkTrouble(){
        if(troubleTitle.getText().toString().trim().length()<=0){
            showToast(false,"请输入标题");
            return;
        }
        if(content.getText().toString().trim().length()<=0){
            showToast(false,"请对维修申报进行描述");
            return;
        }
    }
}
