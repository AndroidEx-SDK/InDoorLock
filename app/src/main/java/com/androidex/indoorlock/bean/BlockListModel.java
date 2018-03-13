package com.androidex.indoorlock.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/12.
 */

public class BlockListModel extends BaseApplyModel {
    public List<Block> data;
    public class Block{
        public int rid;
        public int communityId;
        public String blockName;
        public String blockNo;
        public String creDate;
        public String creBy;
    }
}
