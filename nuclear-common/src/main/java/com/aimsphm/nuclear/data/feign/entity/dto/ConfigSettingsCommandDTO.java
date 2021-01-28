package com.aimsphm.nuclear.data.feign.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Package: com.aimsphm.nuclear.data.feign.entity.dto
 * @Description: <油液配置>
 * @Author: MILLA
 * @CreateDate: 2021/01/22 11:10
 * @UpdateUser: MILLA
 * @UpdateDate: 2021/01/22 11:10
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class ConfigSettingsCommandDTO {

    @ApiModelProperty(value = "边缘端编号", notes = "")
    private String edgeCode;

    @ApiModelProperty(value = "时间戳", notes = "")
    private Long timestamp;

    @ApiModelProperty(value = "指令", notes = "")
    private ConfigSettingsDTO configCommand;

}
