package com.aimsphm.nuclear.common.entity.vo;

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

    @ApiModelProperty(value = "测点名称", notes = "")
    private String pointName;

    @ApiModelProperty(value = "传感器名称", notes = "")
    private String sensorName;

    @ApiModelProperty(value = "测点种类", notes = "1：网络采集（PI测点） 2：硬件（边缘端）采集 3：算法生成（特征测点）4：指令与反馈")
    private Integer pointType;

    @ApiModelProperty(value = "单位", notes = "")
    private String unit;

    @ApiModelProperty(value = "测点别名", notes = "")
    private String alias;
}
