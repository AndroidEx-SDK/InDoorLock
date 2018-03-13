package com.androidex.indoorlock.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidex.indoorlock.bean.Event;
import com.androidex.indoorlock.utils.Constants;
import com.androidex.indoorlock.utils.Utils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2018/2/1.
 */

public abstract class BaseFragment extends Fragment implements Constants,View.OnClickListener{
    protected Context mContext;
    protected  View view;
    private Dialog dialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = LayoutInflater.from(mContext)
                .inflate(getLayoutId(), container, false);
        initView();
        mainThread();
        return view;
    }

    public <T extends View> T findViewById( int id) {
        if(view !=null){
            return view.findViewById(id);
        }
        return null;
    }

    @Override
    public void onDestroyView() {
        if(dialog!=null){
            hideLoadingDialog();
            dialog = null;
        }
        super.onDestroyView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    protected void mainThread(){

    }

    public void startActivity(Class<?> clz,Bundle bundle){
        Intent intent = new Intent();
        intent.setClass(mContext, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        mContext.startActivity(intent);
    }

    public void showLoading(String msg){
        if(dialog == null){
            dialog = Utils.createDialog(mContext,msg);
        }
        if(dialog!=null && !dialog.isShowing()){
            dialog.show();
        }
    }

    public void hideLoadingDialog(){
        if(dialog!=null && dialog.isShowing()){
            dialog.dismiss();
        }
    }


    protected void postEvent(int what){
        postEvent(what,null);
    }

    protected void postEvent(int what,Object msg){
        EventBus.getDefault().post(new Event(what,msg));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initData();
}
