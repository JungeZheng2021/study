package com.aimsphm.nuclear.common.entity.dto;

import lombok.Data;



import java.util.Map;

@Data
public class DeviceCharasticDTO {
    private String tagId;
    private Long deviceId;
    private String deviceCode;
    private String sensorDes;
    private Long subSystemId;
    private Map<String,DeviceDetailMetaDTO> meataDatas;
}
