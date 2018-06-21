package com.cmcc.pp.entity.WorkOrder;

import java.io.Serializable;

/**
 * Created by ABC on 2018/1/23.
 */

public class WorkOrderOperationLogDetailEntity implements Serializable {
    public String operationLogDetailId;//操作记录明细ID
    public String operationLogDetailStepName;//操作步骤名称
    public String operationLogDetailBeginDayTime;//  操作记录开始日期(格式:yyyy-MM-dd 24hh:mm:ss)
    public String operationLogDetailEndDayTime;// 操作记录结束日期(格式:yyyy-MM-dd 24hh:mm:ss)
    public int operationLogDetailStatus;//操作记录状态 0.成功;其它失败
}
