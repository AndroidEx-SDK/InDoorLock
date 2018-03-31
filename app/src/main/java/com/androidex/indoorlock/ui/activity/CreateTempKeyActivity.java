package com.androidex.indoorlock.ui.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.androidex.indoorlock.R;
import com.androidex.indoorlock.base.BaseActivity;
import com.androidex.indoorlock.bean.CreateTempKeyModel;
import com.androidex.indoorlock.bean.Event;
import com.androidex.indoorlock.bean.SignModel;
import com.androidex.indoorlock.utils.SharedPreTool;
import com.androidex.indoorlock.utils.Utils;
import com.androidex.indoorlock.view.TempKeyHouseAlert;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/6.
 */

public class CreateTempKeyActivity extends BaseActivity {
    private LinearLayout backLayout;
    private TextView title;
    private EditText name;
    private EditText mobile;
    private RadioGroup limit;
    private LinearLayout countLayout;
    private EditText count;
    private LinearLayout timeLayout;
    private TextView time;
    private TextView lock;
    private Button cancel;
    private Button confirm;
    private int limitMode = 1;
    private DatePickerDialog datePickerDialog;
    private TempKeyHouseAlert tempKeyHouseAlert;
    private SignModel.Data signData;
    private SimpleDateFormat simpleDateFormat;
    private Calendar calendar;

    @Override
    public void initParms(Bundle parms) {
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    }

    @Override
    public int bindView() {
        return R.layout.activity_createtempkey;
    }

    @Override
    public void initView(View v) {
        backLayout = findViewById(R.id.back);
        backLayout.setOnClickListener(this);
        title = findViewById(R.id.title);
        title.setText("创建访客密码");

        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        countLayout = findViewById(R.id.count_layout);
        count = findViewById(R.id.count);
        timeLayout = findViewById(R.id.time_layout);
        time = findViewById(R.id.time);
        time.setOnClickListener(this);
        countLayout.setVisibility(View.VISIBLE);
        timeLayout.setVisibility(View.GONE);
        limit = findViewById(R.id.limit_rg);
        limit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rb_count) {
                    limitMode = 1;
                    countLayout.setVisibility(View.VISIBLE);
                    timeLayout.setVisibility(View.GONE);
                } else if (i == R.id.rb_time) {
                    limitMode = 2;
                    countLayout.setVisibility(View.GONE);
                    timeLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        lock = findViewById(R.id.lock);
        lock.setOnClickListener(this);
        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        confirm = findViewById(R.id.confirm);
        confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                this.finish();
                break;
            case R.id.time:
                showDatePickerDialog();
                break;
            case R.id.lock:
                showHouseAlert();
                break;
            case R.id.cancel:
                this.finish();
                break;
            case R.id.confirm:
                createTempKey();
                break;
        }
    }

    @Override
    public void onMessage(Message msg) {
        if (msg.what == 0x01) {
            hideLoadingDialog();
        }
    }

    @Override
    public void onEvent(Event event) {
        if (event.what == EVENT_WHAT_CREATE_TEMPKEY_RESULT) {
            hideLoadingDialog();
            CreateTempKeyModel model = (CreateTempKeyModel) event.msg;
            if (model != null) {
                if (model.code == 0) {
                    showToast(true, "创建成功");
                    this.finish();
                } else if (model.code == NETWORK_ERROR) {
                    showToast(false, "请检查网络");
                } else if (model.code == SERVER_ERROR) {
                    showToast(false, "服务器异常");
                }
            } else {
                showToast(false, "密码创建失败");
            }
        }
    }

    @Override
    public void mainThread() {

    }

    private void createTempKey() {
        if (name.getText().toString().trim().length() <= 0) {
            showToast(false, "请输入姓名");
            return;
        }
        if (mobile.getText().toString().trim().length() != 11) {
            showToast(false, "请输入正确的电话号码");
            return;
        }
        if (limitMode == 1) { //次数限制
            if (count.getText().toString().trim().length() <= 0) {
                showToast(false, "请输入密码可用次数");
                return;
            } else {
                int c = Integer.valueOf(count.getText().toString().trim());
                if (c <= 0) {
                    showToast(false, "请输入有效次数");
                    return;
                }
            }
        } else {
            if (time.getText().toString().trim().length() <= 0) {
                showToast(false, "请设置密码可以用时间");
                return;
            } else {
                if (calendar != null) {
                    if (Math.abs(calendar.getTimeInMillis() - Calendar.getInstance().getTimeInMillis()) > 1000 * 60 * 60 * 24 * 8) {
                        showToast(false, "请设置7天内的时间");
                        return;
                    }
                } else {
                    showToast(false, "请设置密码可以用时间");
                    return;
                }
            }
        }

        if (lock.getText().toString().trim().length() <= 0 || signData == null) {
            showToast(false, "请设置小区");
            return;
        }
        SignModel model = (SignModel) SharedPreTool.getObject(SharedPreTool.sign_model);
        Map<String, String> data = new HashMap<>();
        data.put("communityId", signData.communityId + "");
        data.put("userId", model.user.rid + "");
        data.put("unitId", signData.rid + "");
        data.put("unitNo", signData.unitNo);
        data.put("state", "N");
        data.put("realname", name.getText().toString().trim());
        data.put("mobile", mobile.getText().toString().trim());
        if (limitMode == 1) {
            data.put("enterTime", count.getText().toString().trim());
            data.put("endDate", simpleDateFormat.format(new Date(new Date().getTime() + 1000 * 60 * 60 * 24 * 7)));
        } else {
            data.put("enterTime", "-1");
            data.put("endDate", simpleDateFormat.format(calendar.getTime()));
        }
        data.put("token", model.token);
        data.put("appKey", Utils.getKey());
        postEvent(EVENT_WHAT_CREATE_TEMPKEY, data);
        showLoading("正在创建...");
        mHandler.sendEmptyMessageDelayed(0x01, 5000);
    }


    private void showDatePickerDialog() {
//        if (datePickerDialog == null) {
//            Calendar c = Calendar.getInstance();
//            datePickerDialog = new DatePickerDialog(this, 3, new DatePickerDialog.OnDateSetListener() {
//                @Override
//                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
//                    //2018-03-07
//                    if (calendar == null) {
//                        calendar = Calendar.getInstance();
//                    }
//                    calendar.set(i, i1, i2, 23, 59, 59);
//                    String strTime = simpleDateFormat.format(calendar.getTime());
//                    time.setText(strTime);
//                }
//            }, c.get(Calendar.YEAR)
//                    , c.get(Calendar.MONTH)
//                    , c.get(Calendar.DAY_OF_MONTH));
//        }
//        datePickerDialog.show();
    }

    private void showHouseAlert() {
        if (tempKeyHouseAlert == null) {
            tempKeyHouseAlert = new TempKeyHouseAlert(this, R.style.Dialog, new TempKeyHouseAlert.HouseSetLinsten() {
                @Override
                public void onHouseSet(SignModel.Data data) {
                    if (data != null) {
                        signData = data;
                        lock.setText(signData.unitName);
                    }
                }
            });
        }
        tempKeyHouseAlert.show();
    }
}
