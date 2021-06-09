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
 * @CreateDate: 2021-06-09
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-06-09
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("algorithm_normal_fault_conclusion")
@ApiModel(value = "实体")
public class AlgorithmNormalFaultConclusionDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -7927092716344878888L;

    @ApiModelProperty(value = "", notes = "")
    private Integer deviceType;

    @ApiModelProperty(value = "", notes = "")
    private Integer additionalType;

    @ApiModelProperty(value = "组件id", notes = "")
    private Long componentId;

    @ApiModelProperty(value = "", notes = "")
    private String conclusionCode;

    @ApiModelProperty(value = "", notes = "")
    private String conclusion;

    @ApiModelProperty(value = "", notes = "")
    private String solution;

    @ApiModelProperty(value = "", notes = "")
    private String reason;

    @ApiModelProperty(value = "0:其它", notes = "1：机械故障 2:电气故障 3：仪表故障 4：泄漏故障 5：天气因素 6:排气故障 7：热负荷变化 8：操作故障")
    private Integer faultType;

}