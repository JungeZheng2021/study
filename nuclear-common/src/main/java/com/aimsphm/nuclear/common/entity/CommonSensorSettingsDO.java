package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.aimsphm.nuclear.common.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Package: com.aimsphm.nuclear.common.entity
 * @Description: <传感器设置信息实体>
 * @Author: MILLA
 * @CreateDate: 2021-01-25
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-01-25
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("common_sensor_settings")
@ApiModel(value = "传感器设置信息实体")
public class CommonSensorSettingsDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -5408629726809233485L;

    @ApiModelProperty(value = "边缘端id", notes = "")
    private Integer edgeId;

    @ApiModelProperty(value = "边缘设备编码", notes = "")
    private String edgeCode;

    @ApiModelProperty(value = "类型：1-温度、2-压力、3-流量、4-液位、5-振动、6-位移、7-电信号、8-声学、9-油质、10-状态类", notes = "")
    private Integer category;

    @ApiModelProperty(value = "波形采样周期", notes = "单位秒")
    private Long waveformSamplePeriod;

    @ApiModelProperty(value = "特征值采样周期", notes = "单位秒")
    private Long eigenvalueSamplePeriod;

    @ApiModelProperty(value = "波形采样时长", notes = "单位秒")
    private Long waveformSampleDuration;

    @ApiModelProperty(value = "数据清零", notes = "- 油质使用")
    private Integer dataReset;

    @ApiModelProperty(value = "配置状态", notes = "1:配置中 2：配置成功 3： 配置失败")
    private Integer configStatus;

}