package com.aimsphm.nuclear.common.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 功能描述: 报告中显示的报警事件信息
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021/08/10 16:11
 */
@Data
public class ReportFaultReasoningVO {
    @ApiModelProperty(value = "报警事件/事件名称", notes = "")
    private String eventName;
    @ApiModelProperty(value = "推理信息", notes = "")
    private List<FaultReasoningVO> reasoningList;
}
