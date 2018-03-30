package com.androidex.indoorlock.bean;

import com.androidex.indoorlock.base.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/3/29.
 */

public class AroundListModel extends BaseBean {
    public List<Around> data;
    public class Around implements Serializable{
        public int rid;
        public int userId;
        public int categoryId;
        public String shopName;
        public String shopImage;
        public String address;
        public String tel;
        public String remark;
        public String images;
        public String lat;
        public String lng;
        public int listOrder;
        public int status;
        public int promotionId;
        public int communityId;
        public String title;
        public String startDate;
        public String endDate;
    }
}
