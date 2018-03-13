package com.androidex.indoorlock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidex.indoorlock.R;
import com.androidex.indoorlock.bean.SignModel;
import com.androidex.indoorlock.utils.SharedPreTool;

import java.util.List;

/**
 * Created by Administrator on 2018/3/1.
 */

public class HouseAlertAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<SignModel.Data> data;
    public HouseAlertAdapter(Context context, List<SignModel.Data> data){
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
            convertView = mInflater.inflate(R.layout.dialog_house_item, viewGroup, false); //加载布局
            holder = new ViewHolder();
            holder.image = convertView.findViewById(R.id.image);
            holder.text = convertView.findViewById(R.id.text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(data.get(i).rid == SharedPreTool.getIntValue(SharedPreTool.house_rid)){
            holder.image.setImageResource(R.mipmap.ic_key);
        }else{
            holder.image.setImageDrawable(null);
        }
        holder.text.setText(data.get(i).unitName);
        return convertView;
    }

    private class ViewHolder {
        ImageView image;
        TextView text;
    }
}
