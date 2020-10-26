package com.aimsphm.nuclear.core.service;

import com.aimsphm.nuclear.common.entity.TxAlarmEvent;
import com.aimsphm.nuclear.common.entity.TxAlarmRealtime;
import com.aimsphm.nuclear.common.entity.bo.AlarmEventQueryPageBO;
import com.aimsphm.nuclear.common.entity.bo.AlarmRealtimeQueryPageBO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @Package: com.aimsphm.nuclear.core.service
 * @Description: <热点数据操作类>
 * @Author: MILLA
 * @CreateDate: 2020/4/3 14:19
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/3 14:19
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface CommonAlarmService {
    Page<TxAlarmEvent> searchTxAlarmEvents(AlarmEventQueryPageBO alarmEventQueryPageBO);

    Page<TxAlarmRealtime> searchTxAlarmRealTime(AlarmRealtimeQueryPageBO alarmRealtimeQueryPageBO);
}
