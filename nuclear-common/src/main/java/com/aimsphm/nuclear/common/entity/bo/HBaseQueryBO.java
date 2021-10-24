package com.aimsphm.nuclear.common.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 功能描述:查询参数
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/3/6 15:27
 */
@Data
public class HBaseQueryBO {
    @ApiModelProperty(value = "表格名称")
    private String tableName;
    @ApiModelProperty(value = "列族")
    private String family;
    @ApiModelProperty(value = "行键值")
    private String pointId;
    @ApiModelProperty(value = "结束行")
    private Long startTime;
    @ApiModelProperty(value = "开始行")
    private Long endTime;
    @ApiModelProperty(value = "指定值")
    private Object qualifier;
}
