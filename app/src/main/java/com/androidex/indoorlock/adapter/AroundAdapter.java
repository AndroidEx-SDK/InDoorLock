package com.androidex.indoorlock.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidex.indoorlock.R;
import com.androidex.indoorlock.bean.AroundListModel;
import com.androidex.indoorlock.bean.ShopTypeModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.List;

/**
 * Created by Administrator on 2018/3/29.
 */

public class AroundAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<AroundListModel.Around> around;
    private List<ShopTypeModel.Type> type;
    public AroundAdapter(Context context,List<ShopTypeModel.Type> type,List<AroundListModel.Around> around){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.type = type;
        this.around = around;
    }

    @Override
    public int getCount() {
        return around.size();
    }

    @Override
    public Object getItem(int i) {
        return around.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if(view == null){
            view = inflater.inflate(R.layout.fragment_around_item, viewGroup, false);
            holder = new ViewHolder();
            holder.image = view.findViewById(R.id.image);
            holder.name = view.findViewById(R.id.name);
            holder.phone = view.findViewById(R.id.phone);
            holder.address = view.findViewById(R.id.address);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        Glide.with(context).load(around.get(i).shopImage).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                holder.image.setImageDrawable(resource);
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                holder.image.setBackgroundColor(Color.parseColor("#999999"));
            }
        });
        holder.name.setText(around.get(i).shopName);
        holder.phone.setText(around.get(i).tel);
        holder.address.setText(around.get(i).address);
        return view;
    }

    public class ViewHolder{
        ImageView image;
        TextView name;
        TextView phone;
        TextView address;
    }
}
