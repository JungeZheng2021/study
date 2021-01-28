package com.aimsphm.nuclear.common.entity.vo;

import com.aimsphm.nuclear.common.entity.CommonSensorDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.common.entity.vo
 * @Description: <传感器展示类>
 * @Author: MILLA
 * @CreateDate: 2020/5/9 9:38
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/5/9 9:38
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class SensorVO {

    @ApiModelProperty(value = "边缘设备名称", notes = "")
    private String edgeName;

    @ApiModelProperty(value = "传感器列表", notes = "")
    private List<CommonSensorDO> sensorList;

    @ApiModelProperty(value = "传感器设置状态", notes = " 1:配置中 2：配置成功 3： 配置失败 ")
    private Integer settingsStatus;
}
