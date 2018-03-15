package com.androidex.indoorlock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.androidex.indoorlock.R;
import com.androidex.indoorlock.bean.PropertyDataModel;

import java.util.List;

/**
 * Created by Administrator on 2018/3/15.
 */

public class PropertyAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<PropertyDataModel.Property> data;
    public PropertyAdapter(Context context,List<PropertyDataModel.Property> data){
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
        ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_property_item, viewGroup, false); //加载布局
            holder.name = view.findViewById(R.id.name);
            holder.phone = view.findViewById(R.id.phone);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        holder.name.setText(data.get(i).department);
        holder.phone.setText(data.get(i).tel);
        return view;
    }
    private class ViewHolder{
        TextView name;
        TextView phone;
    }
}
