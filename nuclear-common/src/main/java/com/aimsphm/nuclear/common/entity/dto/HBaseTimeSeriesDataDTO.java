package com.aimsphm.nuclear.common.entity.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.converters.date.DateNumberConverter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Package: com.aimsphm.nuclear.common.util
 * @Description: <HBase中时序数据传输实体>
 * @Author: milla
 * @CreateDate: 2020/10/28 10:04
 * @UpdateUser: milla
 * @UpdateDate: 2020/10/28 10:04
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class HBaseTimeSeriesDataDTO implements Serializable {
    @ApiModelProperty(value = "指定列对应的值", required = true)
    @ColumnWidth(25)
    @ExcelProperty(value = "value", index = 1)
    private Double value;
    @ApiModelProperty(value = "数据产生时间戳")
    @ColumnWidth(25)
    @ExcelProperty(value = "time", index = 0, converter = DateNumberConverter.class)
    private Long timestamp;
}
