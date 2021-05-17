package com.aimsphm.nuclear.common.entity.bo;

import com.aimsphm.nuclear.common.enums.PointCategoryEnum;
import com.aimsphm.nuclear.common.enums.PointFeatureEnum;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
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

    @ApiModelProperty(value = "模型id", notes = "传此参数就不考虑deviceId和subSystemId")
    private Long modelId;

    @ApiModelProperty(value = "状态显示(质数的积)", notes = "总览/检测:3 数据分析:5 历史数据:7 11,13,17...")
    private Integer visible;

    @ApiModelProperty(value = "特征类型", notes = "abr：磨损分析,acc：加速度分,ana：油品分析,raw：时频分析,vec：速度分析,wei：诊断分析")
    private String featureType;

    @ApiModelProperty(value = "具体特征值")
    private String feature;

    @ApiModelProperty(value = "测点类型", notes = "1-温度、2-压力、3-流量、4-液位、5-振动、6-位移、7-电信号、8-声学、9-油质、10-状态类")
    private Integer category;

    @ApiModelProperty(value = "测点位置")
    private String locationCode;

    @ApiModelProperty(value = "传感器编号")
    private String sensorCode;


}
