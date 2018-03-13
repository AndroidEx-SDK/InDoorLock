package com.androidex.indoorlock.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidex.indoorlock.R;
import com.androidex.indoorlock.adapter.HouseAlertAdapter;
import com.androidex.indoorlock.bean.Event;
import com.androidex.indoorlock.bean.SignModel;
import com.androidex.indoorlock.utils.Constants;
import com.androidex.indoorlock.utils.SharedPreTool;

import org.greenrobot.eventbus.EventBus;
import org.w3c.dom.Text;

/**
 * Created by Administrator on 2018/3/1.
 */

public class HouseAlert extends Dialog implements Constants{
    private Context mContext;
    private SignModel model;

    private TextView errorText;
    private ListView listView;

    public HouseAlert(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public HouseAlert(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    protected HouseAlert(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_house);
        errorText = findViewById(R.id.error);
        listView = findViewById(R.id.listview);
        model = (SignModel) SharedPreTool.getObject(SharedPreTool.sign_model);
        if(model!=null && model.data!=null && model.data.size()>0){
            listView.setVisibility(View.VISIBLE);
            errorText.setVisibility(View.GONE);
            listView.setAdapter(new HouseAlertAdapter(mContext,model.data));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    SharedPreTool.saveIntValue(SharedPreTool.house_rid,model.data.get(i).rid);
                    EventBus.getDefault().post(new Event(EVENT_WHAT_HOUSE_CHANGE,null));
                    HouseAlert.this.dismiss();
                }
            });
        }else{
            listView.setVisibility(View.GONE);
            errorText.setVisibility(View.VISIBLE);
        }
    }
}
