package com.aimsphm.nuclear.common.entity.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 功能描述:
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/09/04 14:42
 */
@Data
public class CustSensorConfigRaw implements Serializable {
    private static final long serialVersionUID = 4311213805639156749L;
    private Long unitId;
    private Integer sleepTime;
    private Integer daqFrequency;
    private Integer daqTime;
}
