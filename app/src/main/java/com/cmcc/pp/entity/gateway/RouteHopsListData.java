package com.cmcc.pp.entity.gateway;

import java.io.Serializable;

/**
 * Created by ABC on 2018/1/23.
 */

public class RouteHopsListData implements Serializable {
    public String hopHost;
    public String hopHostAddress;
    public String hopErrorCode;
    public String hopRTTimes;
}
