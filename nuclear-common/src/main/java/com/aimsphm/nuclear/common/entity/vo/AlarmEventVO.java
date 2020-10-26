package com.aimsphm.nuclear.common.entity.vo;

import com.aimsphm.nuclear.common.enums.AlarmTypeEnum;
import lombok.Data;

import java.util.Date;

/**
 * @Package: com.aimsphm.nuclear.common.entity.vo
 * @Description: <报警事件记录vo>
 * @Author: MILLA
 * @CreateDate: 2020/5/9 9:38
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/5/9 9:38
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class AlarmEventVO {

    /**
     * 报警事件名称 设备+异常content
     */
    private String deviceName;
    /**
     * 报警内容
     */
    private String alarmContent;
    /**
     * 报警类型
     */
    private int alarmType;
    /**
     * 报警级别
     */
    private int alarmLevel;
    /**
     * 发生次数
     */
    private int alarmCount;
    /**
     * 关联设备
     */
    private String deviceCode;
    /**
     * 最近报警
     */
    private Date lastAlarm;
    /**
     * 异常测点
     */
    private String tagList;

    public String alarmType() {
        return AlarmTypeEnum.getByValue(this.alarmType).getDesc();
    }
}
