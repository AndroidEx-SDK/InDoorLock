package com.androidex.indoorlock.bean;

import com.androidex.indoorlock.base.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/15.
 */

public class PropertyDataModel extends BaseBean {
    public List<Property> data;
    public class Property{
        public int rid;
        public int communityId;
        public String department;
        public String tel;
        public String type;
    }
}
