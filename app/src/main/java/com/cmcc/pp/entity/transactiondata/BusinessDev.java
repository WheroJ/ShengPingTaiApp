package com.cmcc.pp.entity.transactiondata;

import java.io.Serializable;

/**
 * Created by shopping on 2018/1/2 11:05.
 * https://github.com/wheroj
 */

public class BusinessDev implements Serializable {
    /**
     * 入库总量
     */
    public String bePutInStorageTotalQuantity;

    /**
     * 入库量比例
     */
    public float inventoryRatio;

    /**
     * 放装总量
     */
    public String installQuantity;

    /**
     * 放装总量比例
     */
    public float putPackRatio;

    public String manufacturerName;
}
