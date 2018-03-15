package com.androidex.indoorlock.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/3/14.
 */

public class AdviceListModel implements Serializable{
    public int code;
    public List<Advice> data;
    public class Advice implements Serializable{
        public int rid;
        public String adviceTitle;
        public String remark;
        public String images;
        public String creDate;
        public int unitId;
        public int communityId;
        public int userId;
        public String state;
        public String completeBy;
        public String completeDate;
        public String completeNote;
        public String type;
        public String realname;
        public String headimgurl;
    }
}
