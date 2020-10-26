package com.aimsphm.nuclear.core.vo;


import lombok.Data;

import java.util.Date;

/**
 *
 *
 * @author lu.yi
 * @since 2020-03-26
 */
@Data
public class DeviceAlarmSettingVO  {
    private Long id;
    private Long subSystemId;
    private Long systemId;
    private Long setId;
    private Long siteId;
    private String deviceName;
    private String deviceCode;
    private Integer deviceType;
    private String deviceDesc;
    private Boolean enableMonitor;
    private Long monitorAlgorithmId;
    private String lastAlarmTime;
}
