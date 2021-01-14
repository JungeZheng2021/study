package com.aimsphm.nuclear.common.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @Package: com.aimsphm.nuclear.hbase.entity.dto
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/3/6 10:58
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/3/6 10:58
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class HBaseParamDTO {
    @ApiModelProperty(value = "表格名称", required = true)
    private String tableName;
    @ApiModelProperty(value = "列族", required = true)
    private String family;
    @ApiModelProperty(value = "行键值", required = true)
    private String pointId;
    @ApiModelProperty(value = "时间戳[可做rowKey组成部分]")
    private Long timestamp;
}
