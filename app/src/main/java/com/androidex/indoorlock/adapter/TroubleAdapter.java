package com.androidex.indoorlock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.androidex.indoorlock.R;
import com.androidex.indoorlock.bean.TroubleListModel;
import com.androidex.indoorlock.utils.Utils;

import java.util.List;

/**
 * Created by Administrator on 2018/3/16.
 */

public class TroubleAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<TroubleListModel.Trouble> data;
    public TroubleAdapter(Context context,List<TroubleListModel.Trouble> data){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null){
            view = inflater.inflate(R.layout.activity_trouble_item, viewGroup, false);
            holder = new ViewHolder();
            holder.time = view.findViewById(R.id.time);
            holder.title = view.findViewById(R.id.title);
            holder.state = view.findViewById(R.id.state);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        holder.title.setText(data.get(i).troubleTitle);
        holder.time.setText(Utils.UTCStringtODefaultString(data.get(i).creDate));
        holder.state.setText(Utils.judgeTroubleState(data.get(i).state));
        return view;
    }

    private class ViewHolder{
        TextView title;
        TextView time;
        TextView state;
    }
}
