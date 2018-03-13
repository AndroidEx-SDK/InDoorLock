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
import com.androidex.indoorlock.adapter.TempKeyHouseAdapter;
import com.androidex.indoorlock.bean.Event;
import com.androidex.indoorlock.bean.SignModel;
import com.androidex.indoorlock.utils.SharedPreTool;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2018/3/7.
 */

public class TempKeyHouseAlert extends Dialog {
    private Context mContext;
    private HouseSetLinsten linsten;
    private SignModel model;
    private TextView errorText;
    private ListView listView;

    public TempKeyHouseAlert(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public TempKeyHouseAlert(@NonNull Context context, int themeResId,HouseSetLinsten linsten) {
        super(context, themeResId);
        this.mContext = context;
        this.linsten = linsten;
    }

    protected TempKeyHouseAlert(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
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
            listView.setAdapter(new TempKeyHouseAdapter(mContext,model.data));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    linsten.onHouseSet(model.data.get(i));
                    TempKeyHouseAlert.this.dismiss();
                }
            });
        }else{
            listView.setVisibility(View.GONE);
            errorText.setVisibility(View.VISIBLE);
        }
    }

    public interface HouseSetLinsten{
        public void onHouseSet(SignModel.Data data);
    }
}
