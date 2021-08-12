package com.aimsphm.nuclear.common.entity.vo;

import com.aimsphm.nuclear.common.entity.BizReportConfigDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

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
public class ReportAlarmEventVO {
    @ApiModelProperty(value = "报警事件/事件名称", notes = "")
    private String eventName;

    @ApiModelProperty(value = "图片集合", notes = "")
    private List<Map<String, List<BizReportConfigDO>>> images;
}
