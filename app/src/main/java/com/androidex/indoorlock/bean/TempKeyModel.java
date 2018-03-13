package com.androidex.indoorlock.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/3/6.
 */

public class TempKeyModel implements Serializable{
    public int code;
    public List<Data> data;
    public class Data implements Serializable{
        public int rid = -1;
        public int userId = -1;
        public int communityId = -1;
        public String unitNo;
        public int enterTime = -1;
        public String realname;
        public String mobile;
        public String tempkey;
        public String startDate;
        public String endDate;
        public String state;
    }
}
