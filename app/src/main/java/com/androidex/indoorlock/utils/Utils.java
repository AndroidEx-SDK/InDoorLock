package com.androidex.indoorlock.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidex.indoorlock.AndroidexApplication;
import com.androidex.indoorlock.R;
import com.androidex.indoorlock.bean.SignModel;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2018/2/26.
 */

public class Utils {
    public static Dialog createDialog(Context context, String msg) {
        Dialog dialog = null;
        dialog = new Dialog(context, R.style.image_dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View main = View.inflate(context, R.layout.dialog_main, null);
        dialog.setContentView(main);
        TextView tv = (TextView) main.findViewById(R.id.msg);
        tv.setText(msg);
        dialog.setCancelable(false);
        return dialog;
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) AndroidexApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    networkInfo[i].isAvailable();
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void showCustomToast(Context context, boolean type, String msg) {
        Toast toast = new Toast(context);
        View contentView = View.inflate(AndroidexApplication.getContext(), R.layout.custom_toast_layout, null);
        ImageView iv_icon = (ImageView) contentView.findViewById(R.id.iv_icon);
        TextView tv_info = (TextView) contentView.findViewById(R.id.tv_info);
        if (type) {
            iv_icon.setImageResource(R.mipmap.utils_toast_ok);
        } else {
            iv_icon.setImageResource(R.mipmap.utils_toast_error);
        }
        tv_info.setText(msg);
        toast.setView(contentView);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static String getKey() {
        SignModel model = (SignModel) SharedPreTool.getObject(SharedPreTool.sign_model);
        if (model == null) {
            try {
                JSONObject j = new JSONObject();
                j.put("rid", 0);
                j.put("unitId", 0);
                j.put("communityId", 0);
                return new String(Base64.encode(j.toString().getBytes(), Base64.DEFAULT));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                JSONObject j = new JSONObject();
                j.put("rid", Integer.valueOf(model.user.rid));
                j.put("unitId", model.data.get(0).rid != -1 ? Integer.valueOf(model.data.get(0).rid) : 0);
                j.put("communityId", model.data.get(0).communityId != -1 ? Integer.valueOf(model.data.get(0).communityId) : 0);
                Log.e("xiao_", j.toString());
                return new String(Base64.encode(j.toString().getBytes(), Base64.DEFAULT));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String getDefKey() {
        try {
            JSONObject j = new JSONObject();
            j.put("rid", 0);
            j.put("unitId", 0);
            j.put("communityId", 0);
            return new String(Base64.encode(j.toString().getBytes(), Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String UTCStringtODefaultString(String UTCString) {
        try {
            UTCString = UTCString.replace("Z", " UTC");
            SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
            SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = utcFormat.parse(UTCString);
            return defaultFormat.format(date);
        } catch (ParseException pe) {
            pe.printStackTrace();
            return null;
        }
    }

    public static String judgeHouseType(String type) {
        if (type == null || type.length() <= 0) {
            return "";
        }
        if (type.equals("A")) {
            return "高层";
        }
        if (type.equals("B")) {
            return "多层";
        }
        if (type.equals("C")) {
            return "联排";
        }
        if (type.equals("D")) {
            return "别墅";
        }
        if (type.equals("S")) {
            return "商铺";
        }
        if (type.equals("O")) {
            return "写字楼";
        }
        return "";
    }

    public static String judgeUserType(String type) {
        if (type == null || type.length() <= 0) {
            return "";
        }
        if (type.equals("O")) {
            return "业主";
        }
        if (type.equals("F")) {
            return "家属";
        }
        if (type.equals("R")) {
            return "租客";
        }
        return "";
    }

    public static String judgeCarState(String type) {
        if (type == null || type.length() <= 0) {
            return "";
        }
        if (type.equals("P")) {
            return "等待确认";
        }
        if (type.equals("N")) {
            return "已确认";
        }
        if (type.equals("C")) {
            return "等待删除";
        }
        if (type.equals("D")) {
            return "已删除";
        }
        return "";
    }

    public static String judgeAdviceState(String state) {
        if (state == null || state.length() <= 0) {
            return "";
        }
        if (state.equals("N")) {
            return "新建议";
        }
        if (state.equals("S")) {
            return "已采纳";
        }
        if (state.equals("W")) {
            return "已完成";
        }
        if (state.equals("D")) {
            return "已删除";
        }
        return "";
    }

    public static String judgeTroubleState(String state) {
        if (state == null || state.length() <= 0) {
            return "";
        }
        if (state.equals("N")) {
            return "新建议";
        }
        if (state.equals("S")) {
            return "已采纳";
        }
        if (state.equals("W")) {
            return "已完成";
        }
        if (state.equals("D")) {
            return "已删除";
        }
        return "";
    }
}
