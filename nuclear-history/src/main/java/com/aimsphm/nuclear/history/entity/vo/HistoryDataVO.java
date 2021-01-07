package com.aimsphm.nuclear.history.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.history.entity
 * @Description: <历史数据显示实体>
 * @Author: MILLA
 * @CreateDate: 2020/11/23 16:01
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/11/23 16:01
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class HistoryDataVO {
    @ApiModelProperty(value = "显示值", notes = "实测值、预测值、去噪处理[滑动平均值]")
    private List<List<Object>> chartData;
    @ApiModelProperty(value = "显示值", notes = "实测值、预测值、去噪处理[滑动平均值]")
    private List<Long> chartData1;
}
