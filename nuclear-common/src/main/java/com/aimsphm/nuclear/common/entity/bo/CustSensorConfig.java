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
public class CustSensorConfig implements Serializable {

    private static final long serialVersionUID = 1768561068801303784L;
    private Long productId;
    private Long edgeId;
    private Long timestamp;
    private CustSensorConfigRaw config;

}
