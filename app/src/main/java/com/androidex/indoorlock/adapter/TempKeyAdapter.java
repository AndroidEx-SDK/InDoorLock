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
import com.androidex.indoorlock.bean.TempKeyModel;

import java.util.List;

/**
 * Created by Administrator on 2018/3/6.
 */

public class TempKeyAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<TempKeyModel.Data> data;

    public TempKeyAdapter(Context context, List<TempKeyModel.Data> data) {
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
            convertView = mInflater.inflate(R.layout.activity_tempkey_item, viewGroup, false); //加载布局
            holder = new ViewHolder();
            holder.tempkey = convertView.findViewById(R.id.tempkey);
            holder.limit = convertView.findViewById(R.id.limit);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String strLimit = "";
        int enterTime = data.get(i).enterTime;
        if (enterTime != -1) {
            strLimit = "可使用次数：" + enterTime + "次";
        } else {
            strLimit = data.get(i).startDate + " 至 " + data.get(i).endDate;
        }
        holder.tempkey.setText(data.get(i).tempkey);
        holder.limit.setText(strLimit);

        return convertView;
    }

    private class ViewHolder {
        TextView tempkey;
        TextView limit;
    }
}
