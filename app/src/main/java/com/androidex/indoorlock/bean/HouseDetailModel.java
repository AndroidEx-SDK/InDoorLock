package com.androidex.indoorlock.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/9.
 */

public class HouseDetailModel {
    public int code;
    public List<Members> members;
    public class Members{
        public int rid;
        public String username;
        public String password;
        public String headimgurl;
        public String mobile;
        public String realname;
        public String cardType;
        public String cardNo;
        public String startDate;
        public String endDate;
        public String openid;
        public String finger;
        public String cardImage;
        public String isVip;
        public String lastAccessTime;
        public String lastAccessUuid;
        public int lastDeviceSwitch;
        public String userType;
    }
}
