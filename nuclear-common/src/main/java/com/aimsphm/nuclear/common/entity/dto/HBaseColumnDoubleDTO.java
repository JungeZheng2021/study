package com.aimsphm.nuclear.common.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Package: com.aimsphm.nuclear.hbase.entity.dto
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/3/6 10:04
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/3/6 10:04
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class HBaseColumnDoubleDTO extends HBaseTimeSeriesDataDTO {
    @ApiModelProperty(value = "指定列名", required = true)
    private Object qualifier;
}
