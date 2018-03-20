package com.androidex.indoorlock.bean;

import com.androidex.indoorlock.base.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/13.
 */

public class CarListModel extends BaseBean {
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
