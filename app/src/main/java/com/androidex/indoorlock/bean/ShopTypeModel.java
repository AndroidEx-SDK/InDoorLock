package com.androidex.indoorlock.bean;

import com.androidex.indoorlock.base.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/29.
 */

public class ShopTypeModel extends BaseBean {
    public List<Type> data;
    public class Type{
        public int rid;
        public String categoryName;
        public int communityId;
        public int shopNum;
    }
}
