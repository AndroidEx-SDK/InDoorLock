package com.androidex.indoorlock.ui.fragment;

import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.androidex.indoorlock.R;
import com.androidex.indoorlock.adapter.ManagerAdapter;
import com.androidex.indoorlock.base.BaseFragment;
import com.androidex.indoorlock.ui.activity.AccessActivity;
import com.androidex.indoorlock.ui.activity.AdviceActivity;
import com.androidex.indoorlock.ui.activity.CarDetailsActivity;
import com.androidex.indoorlock.ui.activity.ContactPropertyActivity;
import com.androidex.indoorlock.ui.activity.DoorLockActivity;
import com.androidex.indoorlock.ui.activity.TempKeyActivity;
import com.androidex.indoorlock.ui.activity.HouseActivity;
import com.androidex.indoorlock.ui.activity.TroubleActivity;
import com.androidex.indoorlock.utils.SharedPreTool;

/**
 * Created by Administrator on 2018/2/28.
 */

public class ManagerFragment extends BaseFragment {
    private GridView gridView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_manager;
    }

    @Override
    protected void initView() {
        gridView = findViewById(R.id.manager_gridview);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {

    }
    @Override
    protected void mainThread() {
        gridView.setAdapter(new ManagerAdapter(mContext));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0: //小区门禁
                        startActivity(DoorLockActivity.class,null);
                        break;
                    case 1: //乘坐电梯
                        break;
                    case 2: //访客通行
                        startActivity(TempKeyActivity.class,null);
                        break;
                    case 3: //开门记录
                        startActivity(AccessActivity.class,null);
                        break;
                    case 4: //物业缴费
                        break;
                    case 5: //我的房屋
                        startActivity(HouseActivity.class,null);
                        break;
                    case 6: //我的车辆
                        if(SharedPreTool.getIntValue(SharedPreTool.house_rid) == -1){
                           showToast(false,"请选择房屋");
                            return;
                        }
                        startActivity(CarDetailsActivity.class,null);
                        break;
                    case 7: //投诉建议
                        startActivity(AdviceActivity.class,null);
                        break;
                    case 8: //维修申报
                        startActivity(TroubleActivity.class,null);
                        break;
                    case 9: //物业联系
                        startActivity(ContactPropertyActivity.class,null);
                        break;
                    case 10: //社区论坛
                        break;
                    case 11: //家庭电话
                        break;
                }
            }
        });
    }
}
