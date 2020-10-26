package com.aimsphm.nuclear.common.entity;

import com.aimsphm.nuclear.common.entity.ModelBase;
import lombok.Data;

/**
 * @author lu.yi
 * @since 2020-03-18
 */
@Data
public class MdSensor extends ModelBase {
    private static final long serialVersionUID = 3216982744456743977L;
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
    private String sensorDes;
    private String sensorDesCode;
    private Integer sensorType;
    private Double earlyWarningHi;
    private Double earlyWarningLo;
    private Double thrHi;
    private Double thrLo;
    private Double thrHihi;
    private Double thrLolo;
    private String unit;
    private Boolean clusterAnalysis;
//    private Boolean otherName;
//    private Integer torder;
    private Integer importance;
    private String relatedGroup;
    private Integer torder;
    private Boolean iswifi;
    private String featureType;
    private String alias;
    private Boolean mainSensor;
}