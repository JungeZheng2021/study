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
 * @CreateDate: 2021-06-01
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-06-01
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("algorithm_parameter_alarm_event")
@ApiModel(value = "实体")
public class AlgorithmParameterAlarmEventDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -6589020711009445920L;

    @ApiModelProperty(value = "", notes = "")
    private Long systemId;

    @ApiModelProperty(value = "", notes = "")
    private Long subSystemId;

    @ApiModelProperty(value = "", notes = "")
    private Long deviceId;

    @ApiModelProperty(value = "", notes = "")
    private String eventCode;

    @ApiModelProperty(value = "", notes = "")
    private Long alarmType;

    @ApiModelProperty(value = "", notes = "")
    private String alarmContent;

    @ApiModelProperty(value = "1：测点间为或关系；2：测点间为与关系", notes = "")
    private Integer logicRelation;

}