package com.aimsphm.nuclear.common.entity;

import com.aimsphm.nuclear.common.entity.ModelBase;
import lombok.Data;

/**
 * 
 *
 * @author lu.yi
 * @since 2020-06-10
 */
@Data
public class MdVirtualSensor extends ModelBase {
    private static final long serialVersionUID = 8442427306073020903L;
    /**
     * 测点名称
     */
private String tagId;
private Long subSystemId;
private Long siteId;
private Long deviceId;
private String sensorName;
private String sensorDesc;
private Boolean piSensor;
private Boolean systemSensor;
private Boolean siteSensor;
private Integer major;
private String sensorTagids;
    /**
     * 表达式
     */
private String expression;
    /**
     * 0.流量 1.温度 2.转速和电信号 3.振动 4.振动特征-1X 5.振动特征-2X 6.额外公共量7.报警测点
     */
private Integer sensorType;
private Double earlyWarningHi;
private Double earlyWarningLo;
private Double thrHi;
private Double thrLo;
private Double thrHihi;
private Double thrLolo;
private String unit;
private Boolean clusterAnalysis;
private Integer importance;
private String relatedGroup;
}