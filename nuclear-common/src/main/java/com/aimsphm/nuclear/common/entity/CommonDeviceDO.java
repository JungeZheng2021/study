package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.aimsphm.nuclear.common.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Package: com.aimsphm.nuclear.common.entity
 * @Description: <设备信息实体>
 * @Author: MILLA
 * @CreateDate: 2020-11-30
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-11-30
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("common_device")
@ApiModel(value = "设备信息实体")
public class CommonDeviceDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -8550983655851151891L;

    @ApiModelProperty(value = "电厂id", notes = "")
    private Long siteId;

    @ApiModelProperty(value = "电厂名称", notes = "")
    private String siteName;

    @ApiModelProperty(value = "机组id", notes = "")
    private Long setId;

    @ApiModelProperty(value = "机组名称", notes = "")
    private String setName;

    @ApiModelProperty(value = "系统id", notes = "")
    private Long systemId;

    @ApiModelProperty(value = "系统名称", notes = "")
    private String systemName;

    @ApiModelProperty(value = "子系统", notes = "")
    private Long subSystemId;

    @ApiModelProperty(value = "子系统名称", notes = "")
    private String subSystemName;

    @ApiModelProperty(value = "设备名称", notes = "")
    private String deviceName;

    @ApiModelProperty(value = "设备code", notes = "")
    private String deviceCode;

    @ApiModelProperty(value = "设备类型", notes = "0:主泵 1:气机 2:水泵 3：换热器 4：调节阀 5：风机 6:变压器")
    private Integer deviceType;

    @ApiModelProperty(value = "", notes = "")
    private Integer additionalType;

    @ApiModelProperty(value = "设备描述", notes = "")
    private String deviceDesc;

    @ApiModelProperty(value = "", notes = "")
    private Boolean enableMonitor;

    @ApiModelProperty(value = "", notes = "")
    private Long monitorAlgorithmId;

    @ApiModelProperty(value = "", notes = "")
    private String pklPath;

}