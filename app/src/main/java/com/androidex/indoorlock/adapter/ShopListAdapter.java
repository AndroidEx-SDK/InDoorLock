package com.androidex.indoorlock.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidex.indoorlock.R;
import com.androidex.indoorlock.bean.ShopListModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.List;

/**
 * Created by Administrator on 2018/3/30.
 */

public class ShopListAdapter extends BaseAdapter {
    private Context context;
    private List<ShopListModel.Shop> data;
    private LayoutInflater inflater;
    public ShopListAdapter(Context context, List<ShopListModel.Shop> data){
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder hoder;
        if(view == null){
            view = inflater.inflate(R.layout.activity_shopdeatils_item, viewGroup, false);
            hoder = new ViewHolder();
            hoder.image = view.findViewById(R.id.image);
            hoder.name = view.findViewById(R.id.name);
            hoder.title = view.findViewById(R.id.title);
            hoder.price = view.findViewById(R.id.g);
            hoder.originPrice = view.findViewById(R.id.og);
            view.setTag(hoder);
        }else{
            hoder = (ViewHolder) view.getTag();
        }
        loadUrl(data.get(i).cover,hoder.image);
        hoder.name.setText(data.get(i).name);
        hoder.title.setText(data.get(i).title);
        hoder.price.setText("￥"+data.get(i).price);
        hoder.originPrice.setText("￥"+data.get(i).originPrice);
        hoder.originPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG ); //中间横线
        return view;
    }

    public class ViewHolder{
        ImageView image;
        TextView name;
        TextView title;
        TextView price;
        TextView originPrice;
    }

    private void loadUrl(String url,final ImageView imageView){
        Glide.with(context).load(url).placeholder(R.mipmap.ic_error).error(R.mipmap.ic_error).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                imageView.setImageDrawable(resource);
            }
        });
    }
}
