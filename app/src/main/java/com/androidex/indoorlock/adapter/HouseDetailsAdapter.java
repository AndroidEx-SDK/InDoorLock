package com.androidex.indoorlock.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidex.indoorlock.R;
import com.androidex.indoorlock.bean.HouseDetailModel;
import com.androidex.indoorlock.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.List;

/**
 * Created by Administrator on 2018/3/9.
 */

public class HouseDetailsAdapter extends BaseAdapter {
    private Context context;
    private List<HouseDetailModel.Members> data;
    private LayoutInflater mInflater;
    public HouseDetailsAdapter(Context context, List<HouseDetailModel.Members> data){
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.data = data;
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.activity_housedetails_item, viewGroup, false); //加载布局
            holder = new ViewHolder();
            holder.headimage = convertView.findViewById(R.id.head_image);
            holder.name = convertView.findViewById(R.id.name);
            holder.type = convertView.findViewById(R.id.type);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(context).load(data.get(i).headimgurl).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                holder.headimage.setImageDrawable(resource);
            }
            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                holder.headimage.setImageResource(R.mipmap.ic_default);
            }
        });
        holder.name.setText(data.get(i).realname);
        holder.type.setText(Utils.judgeUserType(data.get(i).userType));
        return convertView;
    }

    private class ViewHolder{
        ImageView headimage;
        TextView name;
        TextView type;
    }
}
