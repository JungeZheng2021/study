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
 * @CreateDate: 2020-11-23
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-11-23
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

    @ApiModelProperty(value = "测点种类", notes = "1：PI测点 2：wifi测点 3：有线测点")
    private Integer tagType;

    @ApiModelProperty(value = "测点描述", notes = "")
    private String tagDesc;

    @ApiModelProperty(value = "占位符", notes = "同一个设备中的测点占位符不允许重复")
    private String placeholder;

    @ApiModelProperty(value = "位置", notes = "")
    private String location;

    @ApiModelProperty(value = "位置编码", notes = "")
    private String locationCode;

    @ApiModelProperty(value = "是否显示", notes = "PI测点和rms测点显示其他测点不显示")
    private Boolean visible;

    @ApiModelProperty(value = "测点类型", notes = "0.流量 1.温度 2.转速和电信号 3.振动 4.振动特征-1X 5.振动特征-2X 6.额外公共量7.报警测点8.压力相关9.位移相关")
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

}