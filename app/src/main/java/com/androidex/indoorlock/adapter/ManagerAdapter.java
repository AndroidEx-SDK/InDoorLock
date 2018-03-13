package com.androidex.indoorlock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidex.indoorlock.R;

/**
 * Created by Administrator on 2018/2/28.
 */

public class ManagerAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context context;
    private String content[] = {"小区门禁","乘坐电梯","访客通行","开门记录","物业缴费","我的房屋","我的车辆","投诉建议","维修申报","物业联系","社区论坛","家庭对讲"};
    private int icon[] = {R.mipmap.manager_icon_1,R.mipmap.manager_icon_2,R.mipmap.manager_icon_3,R.mipmap.manager_icon_4,R.mipmap.manager_icon_5,R.mipmap.manager_icon_6,R.mipmap.manager_icon_7,R.mipmap.manager_icon_8,R.mipmap.manager_icon_9,R.mipmap.manager_icon_10,R.mipmap.manager_icon_11,R.mipmap.manager_icon_12};
    private int background[] = {R.mipmap.manager_bg_1,R.mipmap.manager_bg_2,R.mipmap.manager_bg_3,R.mipmap.manager_bg_4,R.mipmap.manager_bg_3,R.mipmap.manager_bg_4,R.mipmap.manager_bg_1,R.mipmap.manager_bg_2,R.mipmap.manager_bg_4,R.mipmap.manager_bg_2,R.mipmap.manager_bg_3,R.mipmap.manager_bg_1};
    public ManagerAdapter(Context context){
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return content.length;
    }

    @Override
    public Object getItem(int i) {
        return content[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.manager_adapter, parent, false); //加载布局
            holder = new ViewHolder();
            holder.layout = (RelativeLayout) convertView;
            holder.icon = (ImageView)convertView.findViewById(R.id.manager_item_image);
            holder.content = (TextView)convertView.findViewById(R.id.manager_item_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.layout.setBackgroundResource(background[i]);
        holder.icon.setImageResource(icon[i]);
        holder.content.setText(content[i]);
        return convertView;
    }

    private class ViewHolder {
        RelativeLayout layout;
        ImageView icon;
        TextView content;
    }
}
