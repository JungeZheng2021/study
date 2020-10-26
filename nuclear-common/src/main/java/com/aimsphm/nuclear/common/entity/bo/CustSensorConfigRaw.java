package com.aimsphm.nuclear.common.entity.bo;
import lombok.Data;

import java.io.Serializable;

@Data
public class CustSensorConfigRaw implements Serializable {
    private static final long serialVersionUID = 4311213805639156749L;
    private Long unitId;
    private Integer sleepTime;
    private Integer daqFrequency;
    private Integer daqTime;
    /*
    private Double fmin;
    private Double fmax;
    private Double ytMin;
    private Double ytMax;
     */
}
