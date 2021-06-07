package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.aimsphm.nuclear.common.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Package: com.aimsphm.nuclear.common.entity
 * @Description: <实体>
 * @Author: MILLA
 * @CreateDate: 2021-06-03
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-06-03
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("common_sensor_component")
@ApiModel(value = "实体")
public class CommonSensorComponentDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -6230783489326538792L;

    @ApiModelProperty(value = "部件id", notes = "")
    private Long componentId;

    @ApiModelProperty(value = "", notes = "")
    private String sensorCode;

}