package com.androidex.indoorlock.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.androidex.indoorlock.R;
import com.androidex.indoorlock.adapter.ShopListAdapter;
import com.androidex.indoorlock.adapter.ShopViewPagerAdapter;
import com.androidex.indoorlock.base.BaseActivity;
import com.androidex.indoorlock.bean.AroundListModel;
import com.androidex.indoorlock.bean.Event;
import com.androidex.indoorlock.bean.ShopListModel;
import com.androidex.indoorlock.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/29.
 */

public class ShopDetailsActivity extends BaseActivity {
    private AroundListModel.Around around;

    private LinearLayout backLayout;
    private TextView title;

    private TextView details;
    private TextView goods;
    private TextView coupon;

    private ScrollView scrollView;
    private ViewPager viewPager;
    private ImageView shopImage;
    private TextView shopName;
    private TextView shopPhone;
    private TextView shopAddress;
    private TextView showAbout;
    private int pageIndex;
    private List<View> views;

    private LinearLayout goodsLayout;
    private ListView listView;
    private TextView goodsError;

    private LinearLayout couponLayout;


    @Override
    public void initParms(Bundle parms) {
        around = (AroundListModel.Around) getIntent().getSerializableExtra("around");
    }

    @Override
    public int bindView() {
        return R.layout.activity_shopdetails;
    }

    @Override
    public void initView(View v) {
        backLayout = findViewById(R.id.back);
        backLayout.setOnClickListener(this);
        title = findViewById(R.id.title);
        title.setText("店铺详情");

        details = findViewById(R.id.details);
        details.setOnClickListener(this);
        goods = findViewById(R.id.goods);
        goods.setOnClickListener(this);
        coupon = findViewById(R.id.coupon);
        coupon.setOnClickListener(this);

        scrollView = findViewById(R.id.details_scrollview);
        viewPager = findViewById(R.id.viewpager);
        setViewPagerDate();
        shopImage = findViewById(R.id.shop_image);
        loadUrlImage(around.shopImage,shopImage);
        shopName = findViewById(R.id.shop_name);
        shopName.setText(around.shopName);
        shopPhone = findViewById(R.id.shop_phone);
        shopPhone.setText(around.tel);
        shopAddress = findViewById(R.id.shop_address);
        shopAddress.setText(around.address);
        showAbout = findViewById(R.id.shop_about);
        showAbout.setText(around.remark);

        goodsLayout = findViewById(R.id.goods_layout);
        listView = findViewById(R.id.listview);
        goodsError = findViewById(R.id.goods_error);

        couponLayout = findViewById(R.id.coupon_layout);
        switchLayout(0);
        getShopList();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.details:
                switchLayout(0);
                break;
            case R.id.goods:
                switchLayout(1);
                break;
            case R.id.coupon:
                switchLayout(2);
                break;
        }
    }

    @Override
    public void onMessage(Message msg) {
        switch (msg.what){
            case 0x01:
                pageIndex++;
                if(pageIndex>=views.size()){
                    pageIndex = 0;
                }
                viewPager.setCurrentItem(pageIndex);
                mHandler.sendEmptyMessageDelayed(0x01,5000);
                break;
        }
    }

    @Override
    public void onEvent(Event event) {
        if(event.what == EVENT_WHAT_SHOPLIST_RESULT){
            hideLoadingDialog();
            ShopListModel shopListModel = (ShopListModel) event.msg;
            if(shopListModel!=null && shopListModel.data!=null && shopListModel.data.size()>0){
                listView.setVisibility(View.VISIBLE);
                goodsError.setVisibility(View.GONE);
                listView.setAdapter(new ShopListAdapter(this,shopListModel.data));
            }else{
                if(shopListModel.code == NETWORK_ERROR){
                    showToast(false,"请检查网络");
                }else if(shopListModel.code == SERVER_ERROR){
                    showToast(false,"服务器异常");
                }
                listView.setVisibility(View.GONE);
                goodsError.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public void mainThread() {

    }

    private void getShopList(){
        showLoading("正在加载店铺数据...");
        Map<String,String> data = new HashMap<>();
        data.put("appKey", Utils.getKey());
        data.put("token",signModel.token);
        data.put("shopId",around.rid+"");
        postEvent(EVENT_WHAT_SHOPLIST,data);
    }

    private void setViewPagerDate(){
        LayoutInflater inflater = LayoutInflater.from(this);
        ArrayList<String> images = JSON.parseObject(around.images, new TypeReference<ArrayList<String>>() {});
        views = new ArrayList<>();
        if(images != null && images.size() > 0){
            if(images.size() == 1){
                images.add(images.get(0));
                images.add(images.get(0));
            }
            for(int i=0;i<images.size();i++){
                View view = inflater.inflate(R.layout.fragment_around_adapter_item, null);
                ImageView imageView = view.findViewById(R.id.image);
                imageView.setVisibility(View.VISIBLE);
                loadUrlImage(images.get(i),imageView);
                views.add(view);
            }
        }else{
            if(images == null || images.size() <= 0){
                for(int i=0;i<3;i++){
                    View view = inflater.inflate(R.layout.fragment_around_adapter_item, null);
                    ImageView imageView = view.findViewById(R.id.image);
                    imageView.setVisibility(View.VISIBLE);
                    loadUrlImage(R.mipmap.ic_error,imageView);
                    views.add(view);
                }
            }
        }
        pageIndex = 0;
        viewPager.setAdapter(new ShopViewPagerAdapter(views));
        mHandler.sendEmptyMessageDelayed(0x01,5000);
    }

    public void switchLayout(int page){
        switch (page){
            case 0:
                scrollView.setVisibility(View.VISIBLE);
                goodsLayout.setVisibility(View.GONE);
                couponLayout.setVisibility(View.GONE);
                details.setBackgroundColor(Color.parseColor("#ffffff"));
                goods.setBackgroundColor(Color.parseColor("#f2f2f2"));
                coupon.setBackgroundColor(Color.parseColor("#f2f2f2"));
                break;
            case 1:
                scrollView.setVisibility(View.GONE);
                goodsLayout.setVisibility(View.VISIBLE);
                couponLayout.setVisibility(View.GONE);
                details.setBackgroundColor(Color.parseColor("#f2f2f2"));
                goods.setBackgroundColor(Color.parseColor("#ffffff"));
                coupon.setBackgroundColor(Color.parseColor("#f2f2f2"));
                break;
            case 2:
                scrollView.setVisibility(View.GONE);
                goodsLayout.setVisibility(View.GONE);
                couponLayout.setVisibility(View.VISIBLE);
                details.setBackgroundColor(Color.parseColor("#f2f2f2"));
                goods.setBackgroundColor(Color.parseColor("#f2f2f2"));
                coupon.setBackgroundColor(Color.parseColor("#ffffff"));
                break;
        }
    }
}
