package com.aimsphm.nuclear.common.entity.bo.customized.sensor;

import lombok.Data;

import java.io.Serializable;

@Data
public class FwsBO implements Serializable {
    private static final long serialVersionUID = 6643063052646325594L;
    private Integer productId;
    private Long timestamp;
    private Integer sleepTime;
    private Integer daqFrequency;
    private Integer daqTime;
    private Integer edgeId;
    private String serverIp;
    private Integer serverPort;
    private FwsRawData raw;
}
