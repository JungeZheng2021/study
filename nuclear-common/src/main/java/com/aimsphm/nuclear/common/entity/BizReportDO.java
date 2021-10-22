package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Package: com.aimsphm.nuclear.common.entity
 * @Description: <报告表实体>
 * @Author: MILLA
 * @CreateDate: 2021-03-09
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-03-09
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("biz_report")
@ApiModel(value = "报告表实体")
public class BizReportDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -6448017594676196443L;

    @ApiModelProperty(value = "设备id", notes = "")
    private Long deviceId;

    @ApiModelProperty(value = "子系统id", notes = "")
    private Long subSystemId;

    @ApiModelProperty(value = "设备名称", notes = "")
    private String deviceName;

    @ApiModelProperty(value = "报告名称", notes = "")
    private String reportName;

    @ApiModelProperty(value = "报告类型", notes = "1：自动报告 2：手动报告")
    private Integer reportType;

    @ApiModelProperty(value = "报告生成周期", notes = "1：按月 2：季度 3：自定义")
    private Integer reportPeriod;

    @ApiModelProperty(value = "数据状态", notes = "1:生成中 2:生成成功 3:生成失败")
    private Integer status;

    @ApiModelProperty(value = "报告路径", notes = "")
    private String reportPath;

    @ApiModelProperty(value = "报告生成时间", notes = "")
    private Date reportTime;

    @ApiModelProperty(value = "报告开始时间", notes = "")
    private Date reportStartTime;

    @ApiModelProperty(value = "报告结束时间", notes = "")
    private Date reportEndTime;

    @ApiModelProperty(value = "备注", notes = "")
    private String remark;

}