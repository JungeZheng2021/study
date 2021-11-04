package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 功能描述:降采样配置表实体
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021-11-01
 */
@Data
@TableName("spark_down_sample_config")
@ApiModel(value = "降采样配置表实体")
public class SparkDownSampleConfigDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -7148396230454237191L;

    @ApiModelProperty(value = "传感器编号")
    private String sensorCode;

    @ApiModelProperty(value = "特征值/列族")
    private String feature;

    @ApiModelProperty(value = "算法类型")
    private Integer algorithmType;

    @ApiModelProperty(value = "比率")
    private Integer rate;

    @ApiModelProperty(value = "频率")
    private String frequency;

    @ApiModelProperty(value = "目标数量")
    private Integer targetNum;

    @ApiModelProperty(value = "比例")
    private String portion;

}