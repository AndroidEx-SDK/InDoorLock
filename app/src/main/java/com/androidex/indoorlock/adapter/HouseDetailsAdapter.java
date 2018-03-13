package com.androidex.indoorlock.adapter;

import android.content.Context;
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
        ViewHolder holder = null;
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
        if(data.get(i).headimgurl!=null){
            Glide.with(context).load(data.get(i).headimgurl).placeholder(R.mipmap.ic_default).error(R.mipmap.ic_default).into(holder.headimage);
        }
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
