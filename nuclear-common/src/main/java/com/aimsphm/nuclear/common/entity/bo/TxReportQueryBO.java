package com.aimsphm.nuclear.common.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Package: com.aimsphm.nuclear.core.entity.bo
 * @Description: <报告生成查询BO>
 * @Author: MILLA
 * @CreateDate: 2020/4/30 20:51
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/30 20:51
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class TxReportQueryBO extends TimeRangeQueryBO {
    @ApiModelProperty(value = "关键字")
    private String keywords;
    @ApiModelProperty(value = "子系统编号")
    private Long subSystemId;
}
