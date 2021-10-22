package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Package: com.aimsphm.nuclear.common.entity
 * @Description: <实体>
 * @Author: MILLA
 * @CreateDate: 2021-01-29
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-01-29
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("algorithm_rules_parameter")
@ApiModel(value = "实体")
public class AlgorithmRulesParameterDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -7799082287177522286L;

    @ApiModelProperty(value = "algorithm_rules中的id", notes = "")
    private Integer ruleId;

    @ApiModelProperty(value = "参数名称，显示在前端", notes = "")
    private String name;

    @ApiModelProperty(value = "离散参数值，波形特征值该字段为空", notes = "")
    private Float value;

    @ApiModelProperty(value = "", notes = "")
    private String sensorCode;

    @ApiModelProperty(value = "acc/vec/enve", notes = "")
    private String sensorSignalType;

    @ApiModelProperty(value = "当前的参数序号", notes = "")
    private Integer parameterOrder;

    @ApiModelProperty(value = "0", notes = "对应离散参数值, 1对应波形特征值")
    private Integer parameterType;

}