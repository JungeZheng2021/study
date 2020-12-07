package com.aimsphm.nuclear.history.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Package: com.aimsphm.nuclear.history.entity.vo
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2020/11/26 18:11
 * @UpdateUser: milla
 * @UpdateDate: 2020/11/26 18:11
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class HistoryDataWithThresholdVO extends HistoryDataVO {
    @ApiModelProperty(value = "高预警", notes = "")
    private Double earlyWarningHigh;

    @ApiModelProperty(value = "低预警", notes = "")
    private Double earlyWarningLow;

    @ApiModelProperty(value = "高报警", notes = "")
    private Double thresholdHigh;

    @ApiModelProperty(value = "低报警", notes = "")
    private Double thresholdLow;

    @ApiModelProperty(value = "高高报警", notes = "")
    private Double thresholdHigher;

    @ApiModelProperty(value = "低低报警", notes = "")
    private Double thresholdLower;

    @ApiModelProperty(value = "单位", notes = "")
    private String unit;
}
