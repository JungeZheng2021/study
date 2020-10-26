package com.aimsphm.nuclear.common.entity.vo;

import com.aimsphm.nuclear.common.enums.SensorTypeEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;


/**
 * @Package: com.aimsphm.nuclear.data.entity.dto
 * @Description: <测点信息>
 * @Author: MILLA
 * @CreateDate: 2020/4/2 13:43
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/2 13:43
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class MeasurePointVO implements Serializable {
    private static final long serialVersionUID = -2071565876962058344L;

    /**
     * 父测点code
     */
    private String parentTag;
    /**
     * 父测点名称
     */
    private String parentTagName;
    /**
     * 子系统编号
     */
    private String subSystemId;
    /**
     * 测点code
     */
    private String tag;
    /**
     * 测点位置
     */
    private String locationCode;
    /**
     * 测点描述
     */
    private String sensorDesc;
    /**
     * 旋机中使用的测点位置
     */
    private String sensorDes;
    /**
     * 测点名称
     */
    private String tagName;
    /**
     * 测点单位
     */
    private String unit;
    /**
     * 传感器种类
     */
    private byte sensorType;
    /**
     * 测点值
     */
    private Double value;
    /**
     * 预警值高报
     */
    private Double alarmThresholdHigh;
    /**
     * 预警值低报
     */
    private Double alarmThresholdLow;

    /**
     * 信号值高报
     */
    private Double thresholdHigh;
    /**
     * 信号值低报
     */
    private Double thresholdLow;
    /**
     * 信号值高高报
     */
    private Double thresholdHighHigh;
    /**
     * 信号值低低报
     */
    private Double thresholdLowLow;

    /**
     * 测点状态
     */
    private Byte status;

    /**
     * 状态原因
     */
    @TableField(exist = false)
    private String statusCause;

    private String relatedGroup;

    private Boolean piSensor;
    private Boolean iswifi;
    private String featureType;

    /**
     * 环境温度
     */
    private Double temp1;
    /**
     * 传感器温度
     */
    private Double temp2;

    private String alias;
    public String getDesc() {
        return SensorTypeEnum.getDesc(this.sensorType);
    }
    public String getDesCode(){
        if(alias == null)
        {
            return "";
        }
            return alias.substring(alias.length()-3,alias.length());
    }
}
