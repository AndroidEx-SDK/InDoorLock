package com.androidex.indoorlock.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/13.
 */

public class CarListModel {
    public int code;
    public List<Car> cars;
    public class Car{
        public int rid;
        public int userId;
        public int unitId;
        public int communityId;
        public String carNo;
        public String creDate;
        public String remark;
        public String state;
        public String parkUserCode;
        public String beginTime;
        public String endTime;
        public String chargeMoney;
        public String carStatus;
    }
}
