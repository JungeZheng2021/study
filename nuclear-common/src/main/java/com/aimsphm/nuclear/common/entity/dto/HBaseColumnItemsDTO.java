package com.aimsphm.nuclear.common.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

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
public class HBaseColumnItemsDTO extends HBaseParamDTO {
    @ApiModelProperty(value = "指定列集合", required = true)
    private List<HBaseColumnDoubleDTO> qualifiers;
}
