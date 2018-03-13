package com.androidex.indoorlock.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/12.
 */

public class UnitListModel extends BaseApplyModel {
    public List<Unit> data;
    public class Unit{
        public int rid;
        public int communityId;
        public int blockId;
        public String unitNo;
        public String unitName;
        public String unitType;
        public int unitArea;
        public String creDate;
        public int creBy;
        public String state;
        public String ownerName1;
        public String ownerName2;
        public String cardType1;
        public String cardType2;
        public String cardNo1;
        public String cardNo2;
        public String tel1;
        public String tel2;
    }
}
