package com.aimsphm.nuclear.common.entity.bo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CustSensorConfig implements Serializable {

    private static final long serialVersionUID = 1768561068801303784L;
    private Long productId;
    private Long edgeId;
    private Long timestamp;
    private CustSensorConfigRaw config;

}
