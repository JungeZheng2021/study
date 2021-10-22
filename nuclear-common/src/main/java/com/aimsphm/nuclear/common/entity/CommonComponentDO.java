package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Package: com.aimsphm.nuclear.common.entity
 * @Description: <组件信息实体>
 * @Author: MILLA
 * @CreateDate: 2021-07-15
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-07-15
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("common_component")
@ApiModel(value = "组件信息实体")
public class CommonComponentDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -7835153213300476809L;

    @ApiModelProperty(value = "", notes = "")
    private Integer deviceType;

    @ApiModelProperty(value = "", notes = "")
    private String componentName;

    @ApiModelProperty(value = "", notes = "")
    private String componentDesc;

    @ApiModelProperty(value = "", notes = "")
    private Long deviceId;

    @ApiModelProperty(value = "", notes = "")
    private String componentNumber;

    @ApiModelProperty(value = "", notes = "")
    private Long parentComponentId;

    @ApiModelProperty(value = "啮合频率，单位", notes = "Hz")
    private String meshFrequency;

    @ApiModelProperty(value = "转速，单位", notes = "r/min")
    private Double rotationSpeed;

}