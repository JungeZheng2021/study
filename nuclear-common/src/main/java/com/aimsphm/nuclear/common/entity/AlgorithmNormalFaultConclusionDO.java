package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 功能描述:模型对应测点信息实体
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021-06-09 14:30
 */
@Data
@TableName("algorithm_normal_fault_conclusion")
@ApiModel(value = "实体")
public class AlgorithmNormalFaultConclusionDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -7927092716344878888L;

    private Integer deviceType;

    private Integer additionalType;

    private Long componentId;

    private String conclusionCode;

    private String conclusion;

    private String solution;

    private String reason;

    @ApiModelProperty(value = "0:其它", notes = "1：机械故障 2:电气故障 3：仪表故障 4：泄漏故障 5：天气因素 6:排气故障 7：热负荷变化 8：操作故障")
    private Integer faultType;

}