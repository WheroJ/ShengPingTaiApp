package com.cmcc.pp.entity.transactiondata;

import java.util.ArrayList;

/**
 * Created by shopping on 2018/1/2 17:28.
 * https://github.com/wheroj
 */

public class RepairOrder {
    /**
     * 1.宽带;2.语音;3.OTT;4.其它
     */
    public int statisticsName;
    public String statisticsValue;

    public int parentImgResId;
    public String itemName;
    public ArrayList<RepairOderChild> dimensionDetailData;

    public int type;
    public int color;
    public float ratio;

    public static class RepairOderChild {
        /**
         * 1：新装 2：缴费开机 3： 欠费停机 4：客户申请停机 5： 客户申请复机 6：订户密码变更 7：修改速率信息 8：拆机 9:修改
         */
        public int dimensionDetailName;

        /**
         * 中文名称
         */
        public String dimensionDetailNameStr;

        public String dimensionDetailValue;
    }
}
