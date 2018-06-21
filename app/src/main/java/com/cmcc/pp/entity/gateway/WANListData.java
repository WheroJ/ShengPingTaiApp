package com.cmcc.pp.entity.gateway;

import java.io.Serializable;

/**
 * Created by ABC on 2018/1/22.
 */

public class WANListData implements Serializable {
    public String wanConnectionType;// WAN类型
    public String connectionType;//WAN连接的类型
    public String serviceList;//WAN连接承载的什么业务
    public String userName;//PPPoE鉴权的用户名
    public String vlanIdMark;//WAN连接的VLANID
    public String addressingType;//IPv4地址分配方式
    public String connectionStatus;//IPv4协议的连接状态
    public String wanPath;//WAN连接地址

    public boolean isChecked = false;
}
