package com.androidex.indoorlock.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/2/27.
 */

public class SignModel implements Serializable {
    public int code;
    public List<Data> data;
    public List<LockList> lockList;
    public String token;
    public User user;
    public class User implements Serializable {
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

    }
    public class LockList implements Serializable {
        public int blockId;
        public int communityId;
        public String lastConnectTime;
        public String lockCom;

        public String lockKey;
        public String lockMac;
        public String lockName;
        public String lockPosition;

        public String lockSN;
        public String lockType;
        public int resetFlag;
        public int rid;

    }
    public class Data implements Serializable {
        public int blockId;
        public String cardNo1;
        public String cardNo2;
        public String cardType1;

        public String cardType2;
        public int communityId = -1;
        public int creBy;
        public String creDate;

        public String defaultFlag;
        public String ownerName1;
        public String ownerName2;
        public int rid = -1;

        public String state;
        public String tel1;
        public String tel2;
        public int unitArea;

        public String unitName;
        public String unitNo;
        public String unitType;
        public String userType;
    }

    @Override
    public String toString() {
        return "SignModel{" +
                "code=" + code +
                ", data=" + data +
                ", lockList=" + lockList +
                ", token='" + token + '\'' +
                ", user=" + user +
                '}';
    }
}
