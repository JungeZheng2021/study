package com.aimsphm.nuclear.common.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Package: com.aimsphm.nuclear.common.entity
 * @Description: <降采样 实体>
 * @Author: MILLA
 * @CreateDate: 2020-12-14
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-12-14
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@ApiModel(value = "降采样实体")
public class SparkDownSample extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -7231329137513961541L;

    @ApiModelProperty(value = "算法类型", notes = "")
    private Integer algorithmType;

    @ApiModelProperty(value = "测点id", notes = "")
    private String pointId;

    @ApiModelProperty(value = "开始时间", notes = "")
    private Long startTimestamp;

    @ApiModelProperty(value = "测点数据值", notes = "")
    private String points;

}