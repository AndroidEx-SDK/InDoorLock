package com.androidex.indoorlock.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.androidex.indoorlock.R;
import com.androidex.indoorlock.bean.CarListModel;
import com.androidex.indoorlock.utils.Utils;

import java.util.List;

/**
 * Created by Administrator on 2018/3/13.
 */

public class CarAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater mInflater;
    private List<CarListModel.Car> data;
    private OnEvent event;
    private int index = 0;
    public CarAdapter(Context context, List<CarListModel.Car> data,OnEvent event){
        this.context = context;
        this.data = data;
        this.event = event;
        this.mInflater = LayoutInflater.from(context);
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
        index = i;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.activity_cardetails_item, viewGroup, false); //加载布局
            holder = new ViewHolder();
            holder.carNumber = convertView.findViewById(R.id.carNumber);
            holder.carState = convertView.findViewById(R.id.carState);
            holder.datele = convertView.findViewById(R.id.datele);
            holder.lock = convertView.findViewById(R.id.lock);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.carNumber.setText(data.get(i).carNo);
        holder.carState.setText(Utils.judgeCarState(data.get(i).state));
        holder.datele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                event.onDatele(data.get(index).rid);
            }
        });
        holder.lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                event.onLock(data.get(index).rid);
            }
        });
        return convertView;
    }

    private class ViewHolder{
        TextView carNumber;
        TextView carState;
        TextView datele;
        TextView lock;
    }

    public interface OnEvent{
        public void onDatele(int rid);
        public void onLock(int rid);
    }
}
