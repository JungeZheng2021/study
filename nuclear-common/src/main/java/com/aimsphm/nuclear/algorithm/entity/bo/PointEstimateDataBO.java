package com.aimsphm.nuclear.algorithm.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Package: com.aimsphm.nuclear.algorithm.entity.bo
 * @Description: <测点预估数据值>
 * @Author: MILLA
 * @CreateDate: 2020/12/23 15:34
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/12/23 15:34
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class PointEstimateDataBO implements Serializable {
    @ApiModelProperty(value = "测点id", notes = "")
    private String pointId;
    @ApiModelProperty(value = "原始值", notes = "")
    private Double actual;
    @ApiModelProperty(value = "估计值", notes = "")
    private Double estimate;
    @ApiModelProperty(value = "残差", notes = "")
    private Double residual;
    @ApiModelProperty(value = "告警编号", notes = "")
    private Integer alarmCode;
    @ApiModelProperty(value = "时间毫秒值", notes = "")
    private Long timestamp;

}
