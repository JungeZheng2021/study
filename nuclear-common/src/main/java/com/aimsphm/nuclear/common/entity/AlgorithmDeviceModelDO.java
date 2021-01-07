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
 * @Description: <算法模型关系信息实体>
 * @Author: MILLA
 * @CreateDate: 2021-01-05
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-01-05
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("algorithm_device_model")
@ApiModel(value = "算法模型关系信息实体")
public class AlgorithmDeviceModelDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -8663126285955173857L;

    @ApiModelProperty(value = "子系统id", notes = "")
    private Long subSystemId;

    @ApiModelProperty(value = "", notes = "")
    private Long deviceId;

    @ApiModelProperty(value = "", notes = "")
    private String deviceCode;

    @ApiModelProperty(value = "", notes = "")
    private Long algorithmId;

    @ApiModelProperty(value = "", notes = "")
    private String pklPath;

    @ApiModelProperty(value = "", notes = "")
    private String deviceModelName;

    @ApiModelProperty(value = "", notes = "")
    private String remark;

    @ApiModelProperty(value = "", notes = "")
    private Date gmtTrainingDataFrom;

    @ApiModelProperty(value = "", notes = "")
    private Date gmtTrainingDataTo;

    @ApiModelProperty(value = "", notes = "")
    private Integer status;

}