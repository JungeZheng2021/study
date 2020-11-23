package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.aimsphm.nuclear.common.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Package: com.aimsphm.nuclear.common.entity
 * @Description: <设备详细信息实体>
 * @Author: MILLA
 * @CreateDate: 2020-11-23
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-11-23
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("common_device_details")
@ApiModel(value = "设备详细信息实体")
public class CommonDeviceDetailsDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -7985654081304942027L;

    @ApiModelProperty(value = "系统id", notes = "")
    private Long systemId;

    @ApiModelProperty(value = "子系统id", notes = "")
    private Long subSystemId;

    @ApiModelProperty(value = "设备id", notes = "")
    private Long deviceId;

    @ApiModelProperty(value = "字段名称-中文", notes = "")
    private String fieldNameEn;

    @ApiModelProperty(value = "字段名称-英文", notes = "")
    private String fieldNameZh;

    @ApiModelProperty(value = "字段值", notes = "")
    private String fieldValue;

    @ApiModelProperty(value = "字段类型", notes = "")
    private String fieldType;

    @ApiModelProperty(value = "字段单位", notes = "")
    private String unit;

    @ApiModelProperty(value = "是否显示", notes = "1：显示 0：不显示")
    private Boolean visible;

}