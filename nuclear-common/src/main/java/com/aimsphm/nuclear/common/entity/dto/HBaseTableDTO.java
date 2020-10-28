package com.aimsphm.nuclear.common.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.hbase.entity
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/3/5 17:50
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/3/5 17:50
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class HBaseTableDTO {
    @ApiModelProperty(value = "表格名", required = true)
    private String tableName;
    @ApiModelProperty(value = "列族集合", required = true)
    private List<String> families;

}
