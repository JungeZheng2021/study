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
 * @CreateDate: 2020-12-05
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-12-05
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

    @ApiModelProperty(value = "传感器", notes = "")
    private String sensorName;

    @ApiModelProperty(value = "边缘设备号", notes = "")
    private Long edgeId;

    @ApiModelProperty(value = "边缘设备名称", notes = "")
    private String edgeName;

    @ApiModelProperty(value = "设备名称", notes = "")
    private String deviceName;

    @ApiModelProperty(value = "设备Id", notes = "")
    private Long deviceId;

    @ApiModelProperty(value = "位置", notes = "")
    private String sensorLocation;

    @ApiModelProperty(value = "类型（1：温度，2：压力，3：流量，4：液位，5：振动，6：声学，7：油质，8：电信号，9：位移）", notes = "")
    private Integer sensorType;

    @ApiModelProperty(value = "描述", notes = "")
    private String sensorDesc;

}