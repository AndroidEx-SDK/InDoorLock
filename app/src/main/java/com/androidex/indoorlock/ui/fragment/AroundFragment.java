package com.androidex.indoorlock.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidex.indoorlock.R;
import com.androidex.indoorlock.adapter.AroundAdapter;
import com.androidex.indoorlock.base.BaseFragment;
import com.androidex.indoorlock.bean.AroundListModel;
import com.androidex.indoorlock.bean.Event;
import com.androidex.indoorlock.bean.ShopTypeModel;
import com.androidex.indoorlock.bean.SignModel;
import com.androidex.indoorlock.ui.activity.ShopDetailsActivity;
import com.androidex.indoorlock.utils.SharedPreTool;
import com.androidex.indoorlock.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/2/28.
 */

public class AroundFragment extends BaseFragment {
    private ListView listView;
    private TextView error;
    private SignModel signModel;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_around;
    }

    @Override
    protected void initView() {
        signModel = (SignModel) SharedPreTool.getObject(SharedPreTool.sign_model);
        EventBus.getDefault().register(this);
        listView = findViewById(R.id.listview);
        error = findViewById(R.id.error);
        //getShopType();
        getAroundList();
    }

    private void getShopType(){
        showLoading("正在加载周边数据...");
        Map<String,String> data = new HashMap<>();
        data.put("appKey",Utils.getKey());
        data.put("token",signModel.token);
        postEvent(EVENT_WHAT_SHOPTYPE,data);
    }

    private void getAroundList(){
        showLoading("正在加载周边数据...");
        Map<String,String> data = new HashMap<>();
        data.put("appKey",Utils.getKey());
        data.put("token",signModel.token);
        postEvent(EVENT_WHAT_AROUND,data);
    }

    @Subscribe
    public void onEvent(Event event){
        if(event.what == EVENT_WHAT_AROUND_RESULT){
            hideLoadingDialog();
            final AroundListModel aroundListModel = (AroundListModel) event.msg;
            if(aroundListModel!=null && aroundListModel.data!=null && aroundListModel.data.size()>0){
                listView.setAdapter(new AroundAdapter(mContext,null,aroundListModel.data));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(mContext,ShopDetailsActivity.class);
                        intent.putExtra("around",aroundListModel.data.get(i));
                        startActivity(intent);
                    }
                });
                listView.setVisibility(View.VISIBLE);
                error.setVisibility(View.GONE);
            }else{
                listView.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
                if(aroundListModel.code == NETWORK_ERROR){
                    showToast(false,"请检查网络");
                }else if(aroundListModel.code == SERVER_ERROR){
                    showToast(false,"服务器异常");
                }
            }
        }else if(event.what == EVENT_WHAT_SHOPTYPE_RESULT){
            ShopTypeModel shopTypeModel = (ShopTypeModel) event.msg;
            if(shopTypeModel!=null && shopTypeModel.data!=null && shopTypeModel.data.size()>0){
                getAroundList();
            }else{
                hideLoadingDialog();
                listView.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
                if(shopTypeModel.code == NETWORK_ERROR){
                    showToast(false,"请检查网络");
                }else if(shopTypeModel.code == SERVER_ERROR){
                    showToast(false,"服务器异常");
                }
            }
        }
    }

    @Override
    protected void initData() {

    }
}
