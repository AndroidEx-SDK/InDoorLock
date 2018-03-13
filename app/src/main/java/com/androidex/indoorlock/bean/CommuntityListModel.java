package com.androidex.indoorlock.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/12.
 */

public class CommuntityListModel extends BaseApplyModel {
    public List<Communtity> data;
    public class Communtity{
        public int rid;
        public int companyId;
        public String communityName;
        public String contact;
        public String phone;
        public String lat;
        public String lng;
        public String address;
        public String city;
        public String province;
        public String changeRate;
        public int forbid;
        public String needIdentity;
    }
}
