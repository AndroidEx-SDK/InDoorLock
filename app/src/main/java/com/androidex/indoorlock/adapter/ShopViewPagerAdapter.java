package com.androidex.indoorlock.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2018/3/30.
 */

public class ShopViewPagerAdapter extends PagerAdapter {
    private List<View> list;
    public ShopViewPagerAdapter(List<View> list){
        this.list = list;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v=list.get(position);
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View v=list.get(position);
        //前一张图片划过后删除该View
        container.removeView(v);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
}
