package com.androidex.indoorlock.ui.fragment;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidex.indoorlock.R;
import com.androidex.indoorlock.base.BaseFragment;
import com.androidex.indoorlock.bean.SignModel;
import com.androidex.indoorlock.ui.activity.CouponActivity;
import com.androidex.indoorlock.ui.activity.HelpActivity;
import com.androidex.indoorlock.ui.activity.HouseActivity;
import com.androidex.indoorlock.ui.activity.UpdateActivity;
import com.androidex.indoorlock.utils.SharedPreTool;
import com.androidex.indoorlock.view.MessageAlert;

/**
 * Created by Administrator on 2018/2/28.
 */

public class SelfFragment extends BaseFragment{
    private TextView nameText;
    private TextView mobileText;
    private LinearLayout couponLayout;
    private LinearLayout houseLayout;
    private LinearLayout photoLayout;
    private LinearLayout updatePasswordLayout;
    private LinearLayout signOutLayout;
    private LinearLayout signExitLayout;
    private LinearLayout helpLayout;
    private MessageAlert messageAlert;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_self;
    }

    @Override
    protected void initView() {
        nameText = findViewById(R.id.self_nickname);
        mobileText = findViewById(R.id.self_mobile);
        SignModel model = (SignModel) SharedPreTool.getObject(SharedPreTool.sign_model);
        if(model !=null){
            nameText.setText(model.user.realname);
            mobileText.setText(model.user.mobile);
        }

        couponLayout = findViewById(R.id.self_coupon);
        couponLayout.setOnClickListener(this);

        houseLayout = findViewById(R.id.self_house);
        houseLayout.setOnClickListener(this);

        photoLayout = findViewById(R.id.self_photo);
        photoLayout.setOnClickListener(this);

        updatePasswordLayout = findViewById(R.id.self_updatepassword);
        updatePasswordLayout.setOnClickListener(this);

        signOutLayout = findViewById(R.id.self_signout);
        signOutLayout.setOnClickListener(this);

        signExitLayout = findViewById(R.id.self_exit);
        signExitLayout.setOnClickListener(this);

        helpLayout = findViewById(R.id.self_help);
        helpLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.self_photo:
                Toast.makeText(mContext,"功能未开放",Toast.LENGTH_SHORT).show();
                break;
            case R.id.self_house:
                startActivity(HouseActivity.class,null);
                break;
            case R.id.self_updatepassword:
                startActivity(UpdateActivity.class,null);
                break;
            case R.id.self_coupon:
                startActivity(CouponActivity.class,null);
                break;
            case R.id.self_signout:
                if(messageAlert !=null){
                    messageAlert.dismiss();
                    messageAlert = null;
                }
                messageAlert = new MessageAlert(mContext, R.style.Dialog, "注销", "是否要退出登录!", new MessageAlert.MessageAlertEvent() {
                    @Override
                    public void onCancel() {
                        messageAlert.dismiss();
                        messageAlert = null;
                    }

                    @Override
                    public void onConfirm() {
                        messageAlert.dismiss();
                        messageAlert = null;
                        showLoading("正在注销...");
                        postEvent(EVENT_WHAT_SIGN_OUT,1);
                    }
                });
                messageAlert.show();
                break;
            case R.id.self_exit:
                if(messageAlert !=null){
                    messageAlert.dismiss();
                    messageAlert = null;
                }
                messageAlert = new MessageAlert(mContext, R.style.Dialog, "退出", "是否要退出程序!", new MessageAlert.MessageAlertEvent() {
                    @Override
                    public void onCancel() {
                        messageAlert.dismiss();
                        messageAlert = null;
                    }

                    @Override
                    public void onConfirm() {
                        messageAlert.dismiss();
                        messageAlert = null;
                        showLoading("正在退出程序...");
                        postEvent(EVENT_WHAT_EXIT);
                    }
                });
                messageAlert.show();
                break;
            case R.id.self_help:
                startActivity(HelpActivity.class,null);
                break;
        }
    }

    @Override
    protected void initData() {

    }
}
