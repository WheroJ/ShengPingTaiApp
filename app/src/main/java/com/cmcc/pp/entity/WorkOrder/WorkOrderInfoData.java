package com.cmcc.pp.entity.WorkOrder;

import java.io.Serializable;

/**
 * Created by ABC on 2018/1/23.
 */

public class WorkOrderInfoData implements Serializable {
    public String workOrderId;//工单ID
    public String workOrderNo;//工单号
    public int businessType;//业务类型
    public String wbandAccount;//宽带账号
    public String loid;//LOID(网关逻辑标识)
    public int accountMatchingFlag;// 工单号/宽带账号/LOID匹配标识(1.工单号;2.宽带账;3. LOID;其它值未知)
    public String workOrderDayTime;//工单时间(格式: yyyy-MM-dd 24hh:mm:ss)
}
