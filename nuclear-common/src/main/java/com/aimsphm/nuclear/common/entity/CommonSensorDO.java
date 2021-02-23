package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.aimsphm.nuclear.common.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Package: com.aimsphm.nuclear.common.entity
 * @Description: <传感器信息实体>
 * @Author: MILLA
 * @CreateDate: 2021-02-04
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-02-04
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("common_sensor")
@ApiModel(value = "传感器信息实体")
public class CommonSensorDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -5549963336426959251L;

    @ApiModelProperty(value = "传感器编号", notes = "")
    private String sensorCode;

    @ApiModelProperty(value = "传感器型号", notes = "")
    private String sensorModel;

    @ApiModelProperty(value = "传感器序列号", notes = "")
    private String sensorSerialNumber;

    @ApiModelProperty(value = "传感器", notes = "")
    private String sensorName;

    @ApiModelProperty(value = "边缘设备号", notes = "")
    private Integer edgeId;

    @ApiModelProperty(value = "边缘设备编码", notes = "")
    private String edgeCode;

    @ApiModelProperty(value = "边缘设备名称", notes = "")
    private String edgeName;

    @ApiModelProperty(value = "设备名称", notes = "")
    private String deviceName;

    @ApiModelProperty(value = "子系统id", notes = "")
    private Long subSystemId;

    @ApiModelProperty(value = "设备Id", notes = "")
    private Long deviceId;

    @ApiModelProperty(value = "位置", notes = "")
    private String locationCode;

    @ApiModelProperty(value = "位置编码", notes = "")
    private String location;

    @ApiModelProperty(value = "测点类型", notes = "1-温度、2-压力、3-流量、4-液位、5-振动、6-位移、7-电信号、8-声学、9-油质、10-状态类")
    private Integer category;

    @ApiModelProperty(value = "描述", notes = "")
    private String sensorDesc;

    @ApiModelProperty(value = "加速度频率下限", notes = "")
    private Double accFrequencyMin;

    @ApiModelProperty(value = "加速度频率上限", notes = "")
    private Double accFrequencyMax;

    @ApiModelProperty(value = "速度频率下限", notes = "")
    private Double vecFrequencyMin;

    @ApiModelProperty(value = "速度频率上限", notes = "")
    private Double vecFrequencyMax;

    @ApiModelProperty(value = "包络滤波器下限", notes = "")
    private Double envelopeFilterMin;

    @ApiModelProperty(value = "包络滤波器上限", notes = "")
    private Double envelopeFilterMax;

    @ApiModelProperty(value = "包络滤波器带宽", notes = "")
    private Double envelopeBandwidth;

    @ApiModelProperty(value = "加速度频率下限", notes = "")
    private Double offsetFrequencyMin;

    @ApiModelProperty(value = "位移频率上限", notes = "")
    private Double offsetFrequencyMax;

    @ApiModelProperty(value = "灵敏度", notes = "-振动传感器使用")
    private Double sensitivity;

    @ApiModelProperty(value = "粘度计算方式", notes = "-声学传感器使用")
    private Integer viscosityCalculateMethod;

    @ApiModelProperty(value = "采样周期", notes = "- 声学传感器使用")
    private Long samplePeriod;

    @ApiModelProperty(value = "配置状态", notes = "1:配置中 2：配置成功 3： 配置失败")
    private Integer configStatus;

}