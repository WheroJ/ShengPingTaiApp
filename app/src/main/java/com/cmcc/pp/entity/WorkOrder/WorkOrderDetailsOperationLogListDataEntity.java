package com.cmcc.pp.entity.WorkOrder;

import java.io.Serializable;

/**
 * Created by ABC on 2018/1/23.
 */

public class WorkOrderDetailsOperationLogListDataEntity implements Serializable {
    public String operationLOID;//操作记录ID
    public String operationLogDayTime;//操作记录日期(格式:yyyy-MM-dd 24hh:mm:ss)
    public int operationLogStatus;//操作记录状态	0.成功;其它失败
}
