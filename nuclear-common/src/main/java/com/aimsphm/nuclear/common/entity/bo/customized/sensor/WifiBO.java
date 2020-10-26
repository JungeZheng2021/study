package com.aimsphm.nuclear.common.entity.bo.customized.sensor;

import lombok.Data;

import java.io.Serializable;

@Data
public class WifiBO implements Serializable {
    private static final long serialVersionUID = -451006683822262657L;
    private Integer productId;
    private Integer edgeId;
    private Long timestamp;
    private Integer firmware;
    private Double temperature1;
    private Double temperature2;
    private Double voltage;
    private Integer unitId;
    private Integer sleepTime;
    private Integer daqFrequency;
    private Integer daqTime;
    private String apSsid;
    private String apKey;
    private String clientIp;
    private String clientMask;
    private String clientGateway;
    private String tcpServerIp;
    private Integer tcpServerPort;
    private RawDataBase raw;
}
