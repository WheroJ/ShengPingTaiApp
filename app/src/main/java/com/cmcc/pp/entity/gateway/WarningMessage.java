package com.cmcc.pp.entity.gateway;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by ABC on 2018/1/22.
 */

public class WarningMessage implements Serializable {
    public String alarmNum;//告警编号
    public String alarmName;//告警名称
    public String alarmType;//告警类型
    public String alarmToggle;//触发条件
    public String alarmAppendInfo;//附加信息
}
