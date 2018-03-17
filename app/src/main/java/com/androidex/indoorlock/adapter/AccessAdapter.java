package com.androidex.indoorlock.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidex.indoorlock.R;
import com.androidex.indoorlock.bean.AccessModel;
import com.androidex.indoorlock.net.UrlTool;
import com.androidex.indoorlock.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.List;

/**
 * Created by Administrator on 2018/3/8.
 */

public class AccessAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater mInflater;
    private List<AccessModel.Data> data;
    public AccessAdapter(Context context,List<AccessModel.Data> data){
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
            convertView = mInflater.inflate(R.layout.activity_access_item, viewGroup, false); //加载布局
            holder = new ViewHolder();
            holder.head = convertView.findViewById(R.id.head_image);
            holder.tempKey = convertView.findViewById(R.id.tempkey);
            holder.time = convertView.findViewById(R.id.tempkey_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tempKey.setText(data.get(i).lockName);
        holder.time.setText(Utils.UTCStringtODefaultString(data.get(i).creDate));
        String url = UrlTool.BASE_HEAD+data.get(i).imageUrl;
        Glide.with(context).load(url).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                holder.head.setImageDrawable(resource);
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                holder.head.setImageResource(R.mipmap.ic_default);
            }
        });
        return convertView;
    }
    private class ViewHolder {
        ImageView head;
        TextView tempKey;
        TextView time;
    }
}
