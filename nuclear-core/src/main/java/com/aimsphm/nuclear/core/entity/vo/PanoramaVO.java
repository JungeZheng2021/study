package com.aimsphm.nuclear.core.entity.vo;

import com.aimsphm.nuclear.common.entity.JobDeviceStatusDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @Package: com.aimsphm.nuclear.core.vo
 * @Description: <系统总览VO>
 * @Author: MILLA
 * @CreateDate: 2020/4/3 13:46
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/3 13:46
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class PanoramaVO extends JobDeviceStatusDO {

    @ApiModelProperty(value = "设备编号", notes = "")
    private String deviceCode;

    @ApiModelProperty(value = "设备名称", notes = "")
    private String deviceName;

    @ApiModelProperty(value = "设备类型", notes = "")
    private Integer deviceType;

    @ApiModelProperty(value = "系统id", notes = "")
    private Long systemId;

    @ApiModelProperty(value = "子系统", notes = "")
    private Long subSystemId;

    @ApiModelProperty(value = "持续运行时常", notes = "单位毫秒")
    private Long continuousRunningTime;

    @ApiModelProperty(value = "共计运行时常", notes = "单位毫秒")
    private Long totalRunningTime;

    @ApiModelProperty(value = "需要显示的条目", notes = "条目是有序的")
    private Map<Integer, String> items;

}
