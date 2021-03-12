package com.aimsphm.nuclear.common.entity.vo;

import com.aimsphm.nuclear.common.entity.JobDeviceStatusDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Package: com.aimsphm.nuclear.core.vo
 * @Description: <设备运行状态>
 * @Author: MILLA
 * @CreateDate: 2020/4/3 13:46
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/3 13:46
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@ApiModel(value = "设备状态显示实体")
public class DeviceStatusVO extends JobDeviceStatusDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -7783007188201340166L;

    @ApiModelProperty(value = "设备名称", notes = "")
    private String deviceName;

    @ApiModelProperty(value = "启停次数", notes = "")
    private Integer stopTimes;

    @ApiModelProperty(value = "启动时间", notes = "")
    private Long startTime;

    @ApiModelProperty(value = "持续运行时常", notes = "单位毫秒")
    private Long continuousRunningTime;

    @ApiModelProperty(value = "共计运行时常", notes = "单位毫秒")
    private Long totalRunningTime;

    public DeviceStatusVO() {
        //初始化
        init();
    }

    private void init() {
        this.startTime = 0L;
        this.stopTimes = 0;
        this.setTotalRunningTime(0L);
    }
}
