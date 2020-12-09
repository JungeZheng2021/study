package com.aimsphm.nuclear.common.entity.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Package: com.aimsphm.nuclear.common.entity.bo
 * @Description: <单个测点>
 * @Author: MILLA
 * @CreateDate: 2020/11/21 17:33
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/11/21 17:33
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@ApiModel(value = "历史查询单个测点")
public class HistoryQuerySingleWithFeatureBO extends TimeRangeQueryBO {
    @ApiModelProperty(value = "测点编号", notes = "如果是自装传感器的话包含特征")
    private String sensorCode;
    @ApiModelProperty(value = "特征名称-英文", notes = "")
    private String feature;
}
