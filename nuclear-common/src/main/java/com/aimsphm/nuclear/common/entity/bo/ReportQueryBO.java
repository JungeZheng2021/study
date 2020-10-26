package com.aimsphm.nuclear.common.entity.bo;

import com.aimsphm.nuclear.common.constant.ReportConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Objects;

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
    @ApiModelProperty(value = "子系统编号")
    private Long subSystemId;
    @ApiModelProperty(value = "结束时间")
    private Long endTime;
    @ApiModelProperty(value = "开始时间")
    private Long startTime;

    @ApiModelProperty(value = "报告名称", notes = "手动报告时使用")
    private String reportName;
    @ApiModelProperty(value = "测点前缀", notes = "后端使用")
    private String tagPre;

    @ApiModelProperty(value = "模板路径", notes = "后端使用")
    private String templatePath;


    public ReportQueryBO(Long subSystemId, Long startTime, Long endTime) {
        this.subSystemId = subSystemId;
        this.endTime = endTime;
        this.startTime = startTime;
    }

    public ReportQueryBO() {
    }

    //-------------------------------------业务代码----------------------------------------

    public String getSetName() {
        if (subSystemId == 1 || subSystemId == 2) {
            return "一号机";
        }
        if (subSystemId == 8) {
            return "二号机";
        }
        return "";
    }

    /**
     * 获取报告生成的类型[1,8-->主泵 2->汽机]
     *
     * @return
     */
    public String getReportCategory() {
        if (subSystemId == 1 || subSystemId == 8) {
            return "pump";
        }
        if (subSystemId == 2) {
            return "turbine";
        }
        return "";
    }

    public String getTemplatePath() {
        if (subSystemId == 1 || subSystemId == 8) {
            return ReportConstant.PROJECT_TEMPLATES_ROOT_DIR + ReportConstant.PUMP_TEMPLATE_DOC_NAME;
        }
        if (subSystemId == 2) {
            return ReportConstant.PROJECT_TEMPLATES_ROOT_DIR + ReportConstant.TURBINE_TEMPLATE_DOC_NAME;
        }
        return "";
    }

    public String getTagPre() {
        if (Objects.nonNull(this.tagPre) && this.tagPre.length() > 0) {
            return this.tagPre;
        }
        if (subSystemId == 1) {
            return "10";
        }
        if (subSystemId == 8) {
            return "20";
        }
        return "";
    }
}
