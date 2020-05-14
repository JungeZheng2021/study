package com.milla.report.pojo.bo;

import lombok.Data;

/**
 * @Package: com.aimsphm.nuclear.common.entity.bo
 * @Description: <报告声称查询参数类>
 * @Author: MILLA
 * @CreateDate: 2020/5/9 10:49
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/5/9 10:49
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class ReportQueryBO {
    //    @ApiModelProperty(value = "子系统编号")
    private Long subSystemId;
    //    @ApiModelProperty(value = "结束时间")
    private Long endTime;
    //    @ApiModelProperty(value = "开始时间")
    private Long startTime;

    //    @ApiModelProperty(value = "报告名称", notes = "手动报告时使用")
    private String reportName;

    public ReportQueryBO(Long subSystemId, Long startTime, Long endTime) {
        this.subSystemId = subSystemId;
        this.endTime = endTime;
        this.startTime = startTime;
    }
}
