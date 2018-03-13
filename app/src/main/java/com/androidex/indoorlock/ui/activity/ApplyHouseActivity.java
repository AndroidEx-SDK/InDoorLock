package com.androidex.indoorlock.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidex.indoorlock.R;
import com.androidex.indoorlock.base.BaseActivity;
import com.androidex.indoorlock.bean.ApplyHouseModel;
import com.androidex.indoorlock.bean.BaseApplyModel;
import com.androidex.indoorlock.bean.BlockListModel;
import com.androidex.indoorlock.bean.CityListModel;
import com.androidex.indoorlock.bean.CommuntityListModel;
import com.androidex.indoorlock.bean.Event;
import com.androidex.indoorlock.bean.OwnerListModel;
import com.androidex.indoorlock.bean.UnitListModel;
import com.androidex.indoorlock.utils.SMSSDKTools;
import com.androidex.indoorlock.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/10.
 */

public class ApplyHouseActivity extends BaseActivity {
    private LinearLayout backLayout;
    private TextView title;
    private RelativeLayout layout1;
    private RelativeLayout layout2;
    private RelativeLayout layout3;
    private RelativeLayout layout4;
    private TextView content1;
    private TextView content2;
    private TextView content3;
    private TextView content4;
    private TextView name;
    private TextView phone;
    private TextView message;
    private RelativeLayout ownerLayout;
    private TextView ownerPhone;
    private LinearLayout codeLayout;
    private EditText code;
    private TextView getCode;
    private Button cancel;
    private Button confirm;
    private TextView type;

    private String cityData[];
    private String communityData[];
    private String blockData[];
    private String unitData[];
    private String ownerData[];

    private CityListModel cityModel;
    private CommuntityListModel communtityModel;
    private BlockListModel blockModel;
    private UnitListModel unitModel;
    private OwnerListModel ownerModel;

    private CityListModel.City city;
    private CommuntityListModel.Communtity communtity;
    private BlockListModel.Block block;
    private UnitListModel.Unit unit;
    private OwnerListModel.Owner owner;

    private String applyType = "O";

    private SMSSDKTools smssdkTools;

    @Override
    public void initParms(Bundle parms) {
        smssdkTools = SMSSDKTools.newInstance();
        smssdkTools.initSMS(new SMSSDKTools.OnSMSEvent() {
            @Override
            public void onCode(int code) {
                hideLoadingDialog();
                switch (code){
                    case SMSSDKTools.OnSMSEvent.SMS_EVENT_NETWORK:
                        showToast(false,"请检查网络");
                        break;
                    case SMSSDKTools.OnSMSEvent.SMS_EVENT_MOBILE_NULL:
                        showToast(false,"手机号码不能为空");
                        break;
                    case SMSSDKTools.OnSMSEvent.SMS_EVENT_CODE_NULL:
                        showToast(false,"验证码不能为空");
                        break;
                    case SMSSDKTools.OnSMSEvent.SMS_EVENT_GET_COMPLETE:
                        showToast(true,"验证码获取成功");
                        break;
                    case SMSSDKTools.OnSMSEvent.SMS_EVENT_GET_ERROR:
                        showToast(false,"验证码获取失败");
                        break;
                    case SMSSDKTools.OnSMSEvent.SMS_EVENT_CHECK_COMPLETE:
                        showToast(true,"验证码校验成功");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                applyHouseData();
                            }
                        });
                        break;
                    case SMSSDKTools.OnSMSEvent.SMS_EVENT_CHECK_ERROR:
                        showToast(false,"验证码校验失败");
                        break;
                }
            }
        });
    }

    @Override
    public int bindView() {
        return R.layout.activity_applyhouse;
    }

    @Override
    public void initView(View v) {
        backLayout = findViewById(R.id.back);
        backLayout.setOnClickListener(this);
        title = findViewById(R.id.title);
        title.setText("房屋申请");
        layout1 = findViewById(R.id.layout1);
        layout2 = findViewById(R.id.layout2);
        layout3 = findViewById(R.id.layout3);
        layout4 = findViewById(R.id.layout4);
        content1 = findViewById(R.id.content1);
        layout1.setOnClickListener(this);
        content2 = findViewById(R.id.content2);
        layout2.setOnClickListener(this);
        content3 = findViewById(R.id.content3);
        layout3.setOnClickListener(this);
        content4 = findViewById(R.id.content4);
        layout4.setOnClickListener(this);

        name = findViewById(R.id.name);
        name.setText(signModel.user.realname);
        phone = findViewById(R.id.phone);
        phone.setText(signModel.user.mobile);
        message = findViewById(R.id.message);

        ownerLayout = findViewById(R.id.owner_layout);
        ownerPhone = findViewById(R.id.owner_phone);
        ownerPhone.setOnClickListener(this);
        codeLayout = findViewById(R.id.code_layout);
        code = findViewById(R.id.code);
        getCode = findViewById(R.id.getcode);
        getCode.setOnClickListener(this);

        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        confirm = findViewById(R.id.confirm);
        confirm.setOnClickListener(this);
        type = findViewById(R.id.type);
        type.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.layout1:
                startChooseCity();
                break;
            case R.id.layout2:
                startChooseCommunity();
                break;
            case R.id.layout3:
                startChooseBlock();
                break;
            case R.id.layout4:
                startChooseUnit();
                break;
            case R.id.type:
                selectType();
                break;
            case R.id.owner_phone:
                selectOwner();
                break;
            case R.id.getcode:
                if(ownerPhone.getText().toString().trim().length()<=0 || owner == null){
                    showToast(false,"业主手机号码不能为空");
                    return;
                }
                showLoading("正在获取验证码");
                smssdkTools.getVerificationCode(owner.mobile);
                break;
            case R.id.cancel:
                this.finish();
                break;
            case R.id.confirm:
                applyHouse();
                break;
        }
    }

    @Override
    public void onMessage(Message msg) {

    }

    @Override
    public void onEvent(Event event) {
        switch (event.what){
            case EVENT_WHAT_RECEVICE_CITYLIST_RESULT:
                hideLoadingDialog();
                cityModel= judgeResult((CityListModel) event.msg);
                if(cityModel != null && cityModel.data!= null && cityModel.data.size()>0){
                    cityData = new String[cityModel.data.size()];
                    for(int i=0;i<cityModel.data.size();i++){
                        cityData[i] = cityModel.data.get(i).city;
                    }
                    selectCity();
                }else{
                    showToast(false,"城市信息获取失败，请重试");
                }
                break;
            case EVENT_WHAT_RECEVICE_COMMUNITY_RESULT:
                hideLoadingDialog();
                communtityModel = judgeResult((CommuntityListModel)event.msg);
                communityData = new String[communtityModel.data.size()];
                if(communtityModel != null
                        && communtityModel.data != null
                        && communtityModel.data.size()>0){
                    for(int i=0;i<communtityModel.data.size();i++){
                        communityData[i] = communtityModel.data.get(i).communityName;
                    }
                    selectCommunity();
                }else{
                    showToast(false,"小区信息获取失败，请重试");
                }
                break;
            case EVENT_WHAT_RECEVICE_BLOCK_RESULT:
                hideLoadingDialog();
                blockModel = judgeResult((BlockListModel) event.msg);
                blockData = new String[blockModel.data.size()];
                if(blockModel!=null && blockModel.data != null && blockModel.data.size()>0){
                    for(int i=0;i<blockModel.data.size();i++){
                        String blockName = blockModel.data.get(i).blockName.trim();
                        String blockNo = blockModel.data.get(i).blockNo.trim();
                        if(blockName.equals(blockNo)){
                            blockData[i] = blockNo;
                        }else{
                            blockData[i] = blockNo+blockName;
                        }
                    }
                    selectBlock();
                }else{
                    showToast(false,"楼栋信息获取失败，请重试");
                }
                break;
            case EVENT_WHAT_RECEVICE_UNIT_RESULT:
                hideLoadingDialog();
                unitModel = judgeResult((UnitListModel) event.msg);
                unitData = new String[unitModel.data.size()];
                if(unitModel != null && unitModel.data != null && unitModel.data.size()>0){
                    for(int i = 0;i<unitModel.data.size();i++){
                        unitData[i] = unitModel.data.get(i).unitName;
                    }
                    selectUnit();
                }
                break;
            case EVENT_WHAT_OWNER_RESULT:
                hideLoadingDialog();
                ownerModel = judgeResult((OwnerListModel) event.msg);
                ownerData = new String[ownerModel.data.size()];
                if(ownerModel != null && ownerModel.data != null && ownerModel.data.size()>0){
                    for(int i=0;i<ownerModel.data.size();i++){
                        ownerData[i] = ownerModel.data.get(i).mobile;
                    }
                    owner = ownerModel.data.get(0);
                    ownerPhone.setText(ownerData[0]);
                }else{
                    ownerPhone.setText("");
                }
                break;
            case EVENT_WHAT_APPLY_HOUSE_RESULT:
                hideLoadingDialog();
                ApplyHouseModel model = (ApplyHouseModel) event.msg;
                if(model.code == 0){
                    showToast(true,"您提交的申请已经发给管理处，请耐心等待");
                    this.finish();
                }else if(model.code == 1){
                    showToast(false,"您输入的验证码无效或已过期");
                }else if(model.code == 3){
                    showToast(false,"您已经提交了申请，不能重复申请");
                }else if(model.code == NETWORK_ERROR){
                    showToast(false,"请检查网络");
                }else if(model.code == SERVER_ERROR){
                    showToast(false,"服务器异常");
                }
                break;
        }
    }

    @Override
    public void mainThread() {

    }

    private void applyHouse(){
        if(city == null){
            showToast(false,"请选择城市");
            return;
        }
        if(communtity == null){
            showToast(false,"请选择社区");
            return;
        }
        if(block == null){
            showToast(false,"请选择楼栋");
            return;
        }
        if(unit == null){
            showToast(false,"请选择单元");
            return;
        }
        if(ownerModel!=null && ownerModel.data!=null && ownerModel.data.size()>0){
            for(int i=0;i<ownerModel.data.size();i++){
                if(signModel.user.rid == ownerModel.data.get(i).rid){
                    showToast(false,"您已经是该单元用户，不能重复添加");
                    return;
                }
            }
        }
        if(!applyType.equals("O")){
            if(code.getText().toString().trim().length()<=0){
                showToast(false,"请输入验证码");
                return;
            }
            smssdkTools.submitVerificationCode(owner.mobile,code.getText().toString().trim());
            return;
        }
        applyHouseData();
    }

    private void applyHouseData(){
        showLoading("正在提交申请");
        Map<String,String> data = new HashMap<>();
        data.put("realname",signModel.user.realname);
        data.put("mobile",signModel.user.mobile);
        data.put("cardNo","");
        data.put("unitId",unit.rid+"");
        data.put("communityId",communtity.rid+"");
        data.put("userType",applyType);
        data.put("code",code.getText().toString().trim());
        data.put("ownerMobile",ownerPhone.getText().toString().trim());
        data.put("appKey",Utils.getKey());
        data.put("token",signModel.token);
        postEvent(EVENT_WHAT_APPLY_HOUSE,data);
    }

    private void startChooseUnit(){
        if(block == null){
            showToast(false,"请先选择楼栋");
            return;
        }else{
            getUnitList();
        }
    }

    private void startChooseBlock(){
        if(communtity == null){
            showToast(false,"请先选择小区");
            return;
        }else{
            getBlockList();
        }
    }

    private void startChooseCommunity(){
        if(city == null){
            showToast(false,"请先选择城市");
            return;
        }else{
            getCommuntList();
        }
    }

    private void startChooseCity(){
        getCityList();
    }

    private void getOwnerList(){
        showLoading("正在加载业主数据...");
        Map<String,String> data = new HashMap<>();
        data.put("blockUnitId",unit.rid+"");
        data.put("appKey", Utils.getKey());
        data.put("token",signModel.token);
        postEvent(EVENT_WHAT_OWNER,data);
    }

    private void getUnitList(){
        showLoading("正在加载单元数据...");
        Map<String,String> data = new HashMap<>();
        data.put("blockId",block.rid+"");
        data.put("arrayLength",0+"");
        data.put("appKey", Utils.getKey());
        data.put("token",signModel.token);
        postEvent(EVENT_WHAT_RECEVICE_UNIT,data);
    }

    private void getBlockList(){
        showLoading("正在加载楼栋数据...");
        Map<String,String> data = new HashMap<>();
        data.put("communityId",communtity.rid+"");
        data.put("arrayLength",0+"");
        data.put("appKey", Utils.getKey());
        data.put("token",signModel.token);
        postEvent(EVENT_WHAT_RECEVICE_BLOCK,data);
    }

    private void getCommuntList(){
        showLoading("正在加载小区数据...");
        Map<String,String> data = new HashMap<>();
        data.put("city",content1.getText().toString().trim());
        data.put("arrayLength",0+"");
        data.put("appKey", Utils.getKey());
        data.put("token",signModel.token);
        postEvent(EVENT_WHAT_RECEVICE_COMMUNITY,data);
    }

    private void getCityList(){
        showLoading("正在加载城市数据...");
        Map<String,String> data = new HashMap<>();
        data.put("appKey", Utils.getKey());
        data.put("token",signModel.token);
        postEvent(EVENT_WHAT_RECEVICE_CITYLIST,data);
    }

    private AlertDialog ownerDialog = null;
    private void selectOwner(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(ownerData, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ownerPhone.setText(ownerData[i]);
                owner = ownerModel.data.get(i);
                ownerDialog.dismiss();
            }
        });
        ownerDialog = builder.create();
        ownerDialog.show();
    }

    private AlertDialog unitDialog = null;
    private void selectUnit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(unitData, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                content4.setText(unitData[i]);
                unit = unitModel.data.get(i);
                unitDialog.dismiss();
                getOwnerList();
            }
        });
        unitDialog = builder.create();
        unitDialog.show();
    }

    private AlertDialog blockDialog = null;
    private void selectBlock(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(blockData, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(block!=null && block.rid == blockModel.data.get(i).rid){
                    blockDialog.dismiss();
                    return;
                }
                content3.setText(blockData[i]);
                block = blockModel.data.get(i);
                unit = null;
                content4.setText("");
                blockDialog.dismiss();
            }
        });
        blockDialog = builder.create();
        blockDialog.show();
    }

    private AlertDialog communityDialog = null;
    private void selectCommunity(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(communityData, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(communtity != null && communtity.rid == communtityModel.data.get(i).rid){
                    communityDialog.dismiss();
                    return;
                }
                content2.setText(communityData[i]);
                communtity = communtityModel.data.get(i);
                block = null;
                content3.setText("");
                unit = null;
                content4.setText("");
                communityDialog.dismiss();
            }
        });
        communityDialog = builder.create();
        communityDialog.show();
    }

    private AlertDialog cityDialog = null;
    private void selectCity(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(cityData, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(city!=null && city.city.equals(cityModel.data.get(i).city)){
                    cityDialog.dismiss();
                    return;
                }
                content1.setText(cityData[i]);
                city = cityModel.data.get(i);

                communtity = null;
                content2.setText("");
                block = null;
                content3.setText("");
                unit = null;
                content4.setText("");
                cityDialog.dismiss();
            }
        });
        cityDialog = builder.create();
        cityDialog.show();
    }

    private AlertDialog typeDialog = null;
    private void selectType(){
        final String userType[] = {"业主","家属","租客"};
        if(typeDialog == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setItems(userType, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    String strType = userType[i];
                    type.setText(strType);
                    if(strType.equals(userType[0])){
                        message.setText("您申请成为该房屋业主，需要管理处审核");
                        applyType = "O";
                        ownerLayout.setVisibility(View.GONE);
                        codeLayout.setVisibility(View.GONE);
                    }else{
                        if(strType.equals(userType[1])){
                            message.setText("您申请成为该房屋家属，需要发送短信校验码给业主进行校验");
                            applyType = "F";
                        }else{
                            message.setText("您申请成为该房屋租客，需要发送短信校验码给业主进行校验");
                            applyType = "R";
                        }
                        ownerLayout.setVisibility(View.VISIBLE);
                        codeLayout.setVisibility(View.VISIBLE);
                    }
                    typeDialog.dismiss();
                }
            });
            typeDialog = builder.create();
        }
        typeDialog.show();
    }

    private <T extends BaseApplyModel> T judgeResult(T t){
        if(t.code == 0){
            return t;
        }else if(t.code ==NETWORK_ERROR){
            showToast(false,"请检查网络");
        }else if(t.code == SERVER_ERROR){
            showToast(false,"服务器异常");
        }
        return null;
    }
}
