package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.aimsphm.nuclear.common.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Package: com.aimsphm.nuclear.common.entity
 * @Description: <测点信息实体>
 * @Author: MILLA
 * @CreateDate: 2020-12-08
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-12-08
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("common_measure_point")
@ApiModel(value = "测点信息实体")
public class CommonMeasurePointDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -6531060348995816218L;

    @ApiModelProperty(value = "系统id", notes = "")
    private Long systemId;

    @ApiModelProperty(value = "子系统", notes = "")
    private Long subSystemId;

    @ApiModelProperty(value = "设备id", notes = "")
    private Long deviceId;

    @ApiModelProperty(value = "设备名称", notes = "")
    private String deviceName;

    @ApiModelProperty(value = "传感器编码", notes = "")
    private String sensorCode;

    @ApiModelProperty(value = "传感器名称", notes = "")
    private String sensorName;

    @ApiModelProperty(value = "特征类型", notes = "")
    private String featureType;

    @ApiModelProperty(value = "特征名称-英文", notes = "")
    private String feature;

    @ApiModelProperty(value = "特征名称-中文", notes = "")
    private String featureName;

    @ApiModelProperty(value = "测点编号", notes = "")
    private String tagId;

    @ApiModelProperty(value = "测点名称", notes = "")
    private String tagName;

    @ApiModelProperty(value = "测点种类", notes = "1：网络采集（PI测点） 2：硬件（边缘端）采集 3：算法生成（特征测点）4：指令与反馈")
    private Integer tagType;

    @ApiModelProperty(value = "测点描述", notes = "")
    private String tagDesc;

    @ApiModelProperty(value = "占位符", notes = "同一个设备中的测点占位符不允许重复")
    private String placeholder;

    @ApiModelProperty(value = "位置", notes = "")
    private String location;

    @ApiModelProperty(value = "位置编码", notes = "")
    private String locationCode;

    @ApiModelProperty(value = "状态显示(质数的积)", notes = "总览/检测:3 数据分析:5 历史数据:7 11,13,17...")
    private Integer visible;

    @ApiModelProperty(value = "测点类型：1-温度、2-压力、3-流量、4-液位、5-振动、6-位移、7-电信号、8-声学、9-油质、10-状态类", notes = "")
    private Integer category;

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

    @ApiModelProperty(value = "关系分组", notes = "")
    private String relatedGroup;

    @ApiModelProperty(value = "重要程度", notes = "")
    private Integer importance;

}