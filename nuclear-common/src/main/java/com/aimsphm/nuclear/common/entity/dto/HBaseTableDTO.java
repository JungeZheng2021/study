package com.aimsphm.nuclear.common.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 功能描述:
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/3/5 17:50
 */
@Data
public class HBaseTableDTO {
    @ApiModelProperty(value = "表格名", required = true)
    private String tableName;
    @ApiModelProperty(value = "列族集合", required = true)
    private List<String> families;

}
