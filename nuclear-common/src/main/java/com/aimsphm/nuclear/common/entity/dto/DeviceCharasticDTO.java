package com.aimsphm.nuclear.common.entity.dto;

import lombok.Data;


import java.util.Map;

/**
 * <p>
 * 功能描述:
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/3/6 10:04
 */
@Data
public class DeviceCharasticDTO {
    private String tagId;
    private Long deviceId;
    private String deviceCode;
    private String sensorDes;
    private Long subSystemId;
    private Map<String, DeviceDetailMetaDTO> meataDatas;
}
