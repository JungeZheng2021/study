package com.aimsphm.nuclear.common.entity;

import lombok.Data;

import java.util.Date;

/**
 * 报告表
 *
 * @author lu.yi
 * @since 2020-04-30
 */
@Data
public class TxReport extends ModelBase {
    private static final long serialVersionUID = -7311106639601913021L;
    /**
     * 报告名称
     */
    private String reportName;
    /**
     * 报告类型 1：自动报告 2：手动报告
     */
    private Byte reportType;
    /**
     * 报告生成周期 1：按月
     */
    private Byte reportPeriod;
    /**
     * 报告路径
     */
    private String reportPath;
    /**
     * 子系统id
     */
    private Long subSystemId;
    /**
     * 报告生成时间
     */
    private Date reportTime;
    /**
     * 报告生成开始时间
     */
    private Date reportStartTime;
    /**
     * 报告生成结束时间
     */
    private Date reportEndTime;
}