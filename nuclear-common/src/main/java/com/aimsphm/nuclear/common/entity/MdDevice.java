package com.aimsphm.nuclear.common.entity;

import lombok.Data;

/**
 * @author lu.yi
 * @since 2020-03-26
 */
@Data
public class MdDevice extends ModelBase {
    private static final long serialVersionUID = 7824278330465676943L;
    private Long subSystemId;
    private Long systemId;
    private Long setId;
    private Long siteId;
    private String deviceName;
    private String deviceCode;
    private Integer deviceType;
    private Integer additionalType; //0 “设备冷却水泵”，1 :“主给水泵”, 2 “循环水泵" 3"油泵” 4"厂用水泵" 5 "安全壳再循环风机"
    private String deviceDesc;
    private Boolean enableMonitor;
    private Long monitorAlgorithmId;
}