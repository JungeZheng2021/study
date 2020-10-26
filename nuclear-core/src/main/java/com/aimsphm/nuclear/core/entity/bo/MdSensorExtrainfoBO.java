package com.aimsphm.nuclear.core.entity.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "传感器信息实体类",description = "传感器型号/序列号/灵敏度")
public class MdSensorExtrainfoBO {

    @ApiModelProperty(value = "传感器id",required = true)
    private Long id;
    @ApiModelProperty(value = "传感器型号",required = true)
    private String sensorModel;
    @ApiModelProperty(value = "序列号",required = true)
    private String sersorSerial;
    @ApiModelProperty(value = "灵敏度",required = true)
    private Double sensitivity;
}
