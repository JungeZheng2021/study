package com.aimsphm.nuclear.common.entity.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.converters.date.DateNumberConverter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 功能描述:HBase中时序数据传输实体
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/10/28 10:04
 */
@Data
public class HBaseTimeSeriesObjectDTO implements Serializable {
    @ApiModelProperty(value = "指定列对应的值", required = true)
    @ColumnWidth(25)
    @ExcelProperty(value = "value", index = 1)
    private Object value;
    @ApiModelProperty(value = "数据产生时间戳")
    @ColumnWidth(25)
    @ExcelProperty(value = "time", index = 0, converter = DateNumberConverter.class)
    private Long timestamp;
}
