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
@ToString(callSuper = true)
public class HColumnItemDTO extends HBaseParamDTO {
    @ApiModelProperty(value = "指定列名", required = true)
    private Object qualifier;
    @ApiModelProperty(value = "指定列对应的值", required = true)
    private Double value;
}
