package com.cmcc.pp.entity;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by shopping on 2018/1/3 14:26.
 * https://github.com/wheroj
 */

public class NetworkElement extends DataSupport implements Serializable {
    @Column(unique = true, defaultValue = "unknown")
    public String name;

    /**
     * 1: 否  2: 是
     */
    public int isRecent = 1;

    public long recentTime = Long.MAX_VALUE;

    public long id;

    public boolean addToList(ArrayList<NetworkElement> list) {
        if (list != null) {
            this.id = list.size() + 1;
            list.add(this);
            return true;
        }
        return false;
    }
}
