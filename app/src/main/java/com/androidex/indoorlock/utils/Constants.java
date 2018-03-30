package com.androidex.indoorlock.utils;

/**
 * Created by Administrator on 2018/2/27.
 */

public interface Constants {
    int NETWORK_ERROR = 9999; //网络异常
    int SERVER_ERROR = 8888; //服务器拒绝访问
    int EVENT_WHAT_SERVICE_INIT = 0; //androidexServicec初始化
    int EVENT_WHAT_SERVICE_LOGIN = 1; //请求登录
    int EVENT_WHAT_SERVICE_LOGIN_CALLBACK = 2; //登录结果
    int EVENT_WHAT_HOUSE_CHANGE = 3; //选择房屋变化
    int EVENT_WHAT_INITRTC = 4;// 初始化RTC
    int EVENT_WHAT_RTC_NOTIFY = 5; //RTC注册结果
    int EVENT_WHAT_APPEND_IMAGE = 6; //来电更新头像
    int EVENT_WHAT_OPEN_DOOR = 7; //开门
    int EVENT_WHAT_ANSWER = 8; //接听电话
    int EVENT_WHAT_ON_DISCONNECTED = 9; //拨号连接失败
    int EVENT_WHAT_ON_VIDEO = 10; //通话连接成功
    int EVENT_WHAT_OFFHOOK_REJECT = 11; //通话挂断
    int EVENT_WHAT_OFFHOOK_OPEN_DOOR = 12; //通话开门
    int EVENT_WHAT_PORTRAIT_BITMAP = 13; //获取了访客头像
    int EVENT_WHAT_CANCEL_CALL = 14; //用户挂断了
    int EVENT_WHAT_SIGN_OUT = 15; //用户注销
    int EVENT_WHAT_SIGN_OUT_CALLBACK = 16; //服务注销完成
    int EVENT_WHAT_EXIT = 17; //退出程序
    int EVENT_WHAT_TOP_ACCOUNT = 18; //账号被顶
    int EVENT_WHAT_UPDATE_PASSWORD = 19; //修改密码
    int EVENT_WHAT_UPDATE_PASSWORD_CALLBACK = 20; //修改密码回调
    int EVENT_WHAT_OPEN_LOCK = 21;//打开小区门禁
    int EVENT_WHAT_TEMPKEY = 22; //获取临时密码
    int EVENT_WHAT_TEMPKEY_RESULT = 23; //获取临时密码,返回结果
    int EVENT_WHAT_CREATE_TEMPKEY = 24; //创建临时密码
    int EVENT_WHAT_CREATE_TEMPKEY_RESULT = 25; //创建临时密码结果
    int EVENT_WHAT_RECEVICE_ACCESS = 26; //请求访客列表
    int EVENT_WHAT_RECEVICE_ACCESS_RESULT = 27; //访客列表返回
    int EVENT_WHAT_REGISTER = 28;//用户注册
    int EVENT_WHAT_REGISTER_RESULT = 29; //注册回调
    int EVENT_WHAT_HOUSEINFO = 30;//房屋信息信息
    int EVENT_WHAT_HOUSEINFO_RESULT = 31; //房屋信息回调
    int EVENT_WHAT_RECEVICE_CITYLIST = 32; //请求城市信息
    int EVENT_WHAT_RECEVICE_CITYLIST_RESULT = 33; //请求城市信息回调
    int EVENT_WHAT_RECEVICE_COMMUNITY = 34;//请求小区信息
    int EVENT_WHAT_RECEVICE_COMMUNITY_RESULT = 35; //请求小区信息回调
    int EVENT_WHAT_RECEVICE_BLOCK = 36 ;//请求楼栋信息
    int EVENT_WHAT_RECEVICE_BLOCK_RESULT = 37; //请求楼栋信息回调
    int EVENT_WHAT_RECEVICE_UNIT = 38; //请求单元信息
    int EVENT_WHAT_RECEVICE_UNIT_RESULT = 39; //请求单元信息回调
    int EVENT_WHAT_OWNER = 40; //请求单元业主信息
    int EVENT_WHAT_OWNER_RESULT = 41; //请求业主信息回调
    int EVENT_WHAT_APPLY_HOUSE = 42; //申请房屋
    int EVENT_WHAT_APPLY_HOUSE_RESULT = 43; //申请房屋回调
    int EVENT_WHAT_CAR = 44; //我的车辆
    int EVENT_WHAT_CAR_RESULT = 45; //我的车辆回调
    int EVENT_WHAT_APPLYCAT = 46; //申请车辆
    int EVENT_WHAT_APPLYCAT_RESULT = 47; //申请车辆回调
    int EVENT_WHAT_ADVICE = 48;// 投诉建议
    int EVENT_WHAT_ADVICE_RESULT = 49; //投诉建议回调
    int EVENT_WHAT_PROPERTY = 50; //获取物业联系方式
    int EVENT_WHAT_PROPERTY_RESULT = 51; //物业联系方式回调
    int EVENT_WHAT_TROUBLE = 52; //投诉建议
    int EVENT_WHAT_TROUBLE_RESULT = 53; //投诉建议回调
    int EVENT_WHAT_UPLOAD_IMAGE = 54;//文件上传
    int EVENT_WHAT_UPLOAD_IMAGE_RESULT = 55;//文件上传回调
    int EVENT_WHAT_AROUND = 56; //周边数据
    int EVENT_WHAT_AROUND_RESULT = 57;// 周边数据回调
    int EVENT_WHAT_SHOPTYPE = 58; //商户类型
    int EVENT_WHAT_SHOPTYPE_RESULT = 59;//商户类型回调
    int EVENT_WHAT_SHOPLIST = 60;// 商户商品列表
    int EVENT_WHAT_SHOPLIST_RESULT = 61; //商户商品列表回调
}
