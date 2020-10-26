package com.aimsphm.nuclear.common.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

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
public class HColumnDoubleDTO implements Serializable {
    @ApiModelProperty(value = "指定列名", required = true)
    private Object qualifier;
    @ApiModelProperty(value = "指定列对应的值", required = true)
    private Double value;
    @ApiModelProperty(value = "数据产生时间戳")
    private Long timestamp;
}
