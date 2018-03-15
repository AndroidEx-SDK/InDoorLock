package com.androidex.indoorlock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidex.indoorlock.R;
import com.androidex.indoorlock.bean.AdviceListModel;
import com.androidex.indoorlock.utils.Utils;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Administrator on 2018/3/14.
 */

public class AdviceAdapter extends BaseAdapter {
    private Context context;
    private List<AdviceListModel.Advice> data;
    private LayoutInflater inflater;
    public AdviceAdapter(Context context,List<AdviceListModel.Advice> data){
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
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
        ViewHolder holder = null;
        if(view == null){
            view = inflater.inflate(R.layout.activity_advice_item, viewGroup, false);
            holder = new ViewHolder();
            holder.headImage = view.findViewById(R.id.head);
            holder.name = view.findViewById(R.id.name);
            holder.time = view.findViewById(R.id.time);
            holder.title = view.findViewById(R.id.title);
            holder.state = view.findViewById(R.id.state);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        String headimgurl = data.get(i).headimgurl;
        Glide.with(context).load(headimgurl).placeholder(R.mipmap.ic_default).error(R.mipmap.ic_default).into(holder.headImage);

        String realname = data.get(i).realname;
        if(realname == null || realname.length()<=0){
            realname = "未知用户";
        }
        holder.name.setText(realname);

        String title = data.get(i).adviceTitle;
        if(title == null || title.length()<=0){
            title = "未知用户";
        }
        holder.title.setText(title);
        holder.state.setText(Utils.judgeAdviceState(data.get(i).state));
        holder.state.setBackgroundResource(setStateBackground(data.get(i).state));
        holder.time.setText(Utils.UTCStringtODefaultString(data.get(i).creDate));
        return view;
    }
    private int setStateBackground(String state){
        if(state.equals("N")){
            return R.mipmap.ic_newadvice;
        }else{
            return R.mipmap.ic_adoptadvice;
        }
    }

    private class ViewHolder{
        ImageView headImage;
        TextView name;
        TextView title;
        TextView state;
        TextView time;
    }
}
