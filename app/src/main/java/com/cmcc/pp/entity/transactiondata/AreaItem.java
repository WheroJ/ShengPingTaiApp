package com.cmcc.pp.entity.transactiondata;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by shopping on 2018/1/18 14:53.
 * https://github.com/wheroj
 */

public class AreaItem extends DataSupport implements Serializable{
    public String areaCode;

    @Column(unique = true, defaultValue = "unknown")
    public String areaId;
    public String areaName;
    public String parentId;

    /**
     * 1:否  2:是
     */
    public int isRecent = 1;
    public long recentTime = Long.MAX_VALUE;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AreaItem) {
            return ((AreaItem) obj).areaId.equals(this.areaId);
        }
        return super.equals(obj);
    }
}
