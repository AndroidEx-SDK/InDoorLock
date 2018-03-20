package com.androidex.indoorlock.bean;

import com.androidex.indoorlock.base.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/8.
 */

public class AccessModel extends BaseBean{
    public List<Data> data;
    public class Data{
        public int rid;
        public int communityId;
        public int unitId;
        public int userId;
        public int lockId;
        public String type;
        public String creDate;
        public int tempKeyId;
        public String cardNo;
        public String imageUrl;
        public int employeeId;
        public String imageUuid;
        public String lockName;
    }
}
