package com.androidex.indoorlock.ui.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.androidex.indoorlock.R;
import com.androidex.indoorlock.adapter.ManagerAdapter;
import com.androidex.indoorlock.base.BaseFragment;
import com.androidex.indoorlock.ui.activity.AccessActivity;
import com.androidex.indoorlock.ui.activity.DoorLockActivity;
import com.androidex.indoorlock.ui.activity.TempKeyActivity;
import com.androidex.indoorlock.ui.activity.HouseActivity;

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
                    case 0:
                        startActivity(DoorLockActivity.class,null);
                        break;
                    case 1:
                        break;
                    case 2:
                        startActivity(TempKeyActivity.class,null);
                        break;
                    case 3:
                        startActivity(AccessActivity.class,null);
                        break;
                    case 4:
                        break;
                    case 5:
                        startActivity(HouseActivity.class,null);
                        break;
                    case 6:
                        break;
                    case 7:
                        break;
                    case 8:
                        break;
                    case 9:
                        break;
                    case 10:
                        break;
                    case 11:
                        break;
                }
            }
        });
    }
}
