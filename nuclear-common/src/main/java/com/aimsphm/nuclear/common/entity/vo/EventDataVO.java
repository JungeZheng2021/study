package com.aimsphm.nuclear.common.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.history.entity
 * @Description: <报警数据显示值>
 * @Author: MILLA
 * @CreateDate: 2020/11/23 16:01
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/11/23 16:01
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class EventDataVO extends HistoryDataWithThresholdVO {

    @ApiModelProperty(value = "实测值", notes = "")
    private List<List<Object>> actualData;

    @ApiModelProperty(value = "估计值", notes = "")
    private List<List<Object>> estimatedData;

    @ApiModelProperty(value = "报警值", notes = "")
    private List<Long> alarmData;

    @ApiModelProperty(value = "残差值", notes = "")
    private List<List<Object>> residualData;

    @ApiModelProperty(value = "测点名称", notes = "")
    private String pointName;

    @ApiModelProperty(value = "报警类型", notes = "1:阈值 5：算法 51：残差(在报告中使用)")
    private Integer alarmType;


}
