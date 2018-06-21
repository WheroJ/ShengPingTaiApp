package com.cmcc.pp.entity.gateway;

import java.io.Serializable;

/**
 * Created by ABC on 2018/1/18.
 */

public class GatewayInfo  implements Serializable {
    /**
     * 网关ID
     */
    public String gatewayId;


    /**
     * 网关SN
     */
    public String gatewaySN;

    /**
     * 生产商
     */
    public String manufacturerName;

    /**
     * MAC地址
     */
    public String macAddress;

    /**
     * 宽带账号
     */
    public String wbandAccount;

    /**
     * LOID(网关逻辑标识)
     */
    public String loid;

    /**
     * 网关SN/MAC地址/宽带账号/LOID匹配标识(1. 网关SN;2. MAC地址;3. 宽带账号;4. LOID
     其它值未知)
     */
    public int accountMatchingFlag;
}
