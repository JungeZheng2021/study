package com.aimsphm.nuclear.common.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Package: com.aimsphm.nuclear.core.entity.bo
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/12/04 17:39
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/12/04 17:39
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class CommonQueryBO {
    @ApiModelProperty(value = "系统id", notes = "")
    private Long systemId;

    @ApiModelProperty(value = "子系统", notes = "")
    private Long subSystemId;

    @ApiModelProperty(value = "设备id", notes = "")
    private Long deviceId;

    @ApiModelProperty(value = "状态显示(质数的积)", notes = "总览/检测:3 数据分析:5 历史数据:7 11,13,17...")
    private Integer visible;

}
