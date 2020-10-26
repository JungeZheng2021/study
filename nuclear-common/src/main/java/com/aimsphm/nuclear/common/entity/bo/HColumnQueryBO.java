package com.aimsphm.nuclear.common.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Package: com.aimsphm.nuclear.hbase.entity.bo
 * @Description: <查询参数>
 * @Author: MILLA
 * @CreateDate: 2020/3/6 15:27
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/3/6 15:27
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class HColumnQueryBO {
    @ApiModelProperty(value = "表格名称")
    private String tableName;
    @ApiModelProperty(value = "列族")
    private String family;
    @ApiModelProperty(value = "行键值")
    private String tag;
    @ApiModelProperty(value = "结束行")
    private Long startTime;
    @ApiModelProperty(value = "开始行")
    private Long endTime;
    @ApiModelProperty(value = "指定值")
    private Object qualifier;
}
