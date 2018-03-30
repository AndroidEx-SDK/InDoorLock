package com.androidex.indoorlock.bean;

import com.androidex.indoorlock.base.BaseBean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/30.
 */

public class ShopListModel extends BaseBean {
    public List<Shop> data;
    public class Shop{
        public int rid;
        public int shopId;
        public String name;
        public String title;
        public String cover;
        public String images;
        public int price;
        public int originPrice;
        public int costPrice;
        public int status;
        public String content;
        public String createTime;
    }
}
