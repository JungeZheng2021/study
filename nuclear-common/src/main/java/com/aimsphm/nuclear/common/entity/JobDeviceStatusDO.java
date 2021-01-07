package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.aimsphm.nuclear.common.entity.BaseDO;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Package: com.aimsphm.nuclear.common.entity
 * @Description: <设备状态实体>
 * @Author: MILLA
 * @CreateDate: 2020-12-24
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-12-24
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("job_device_status")
@ApiModel(value = "设备状态实体")
public class JobDeviceStatusDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -7783007188201340366L;

    @ApiModelProperty(value = "设备id", notes = "")
    private Long deviceId;

    @ApiModelProperty(value = "设备健康状态", notes = "0:健康 1：待观察 2：预警 3：报警 4：停机")
    private Integer status;

    @ApiModelProperty(value = "状态持续时间", notes = "单位毫秒")
    private Long statusDuration;

    @ApiModelProperty(value = "状态开始时间", notes = "")
    private Date gmtStart;

    @ApiModelProperty(value = "状态结束时间", notes = "")
    private Date gmtEnd;

}