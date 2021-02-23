package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.aimsphm.nuclear.common.entity.BaseDO;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Package: com.aimsphm.nuclear.common.entity
 * @Description: <故障诊断信息实体>
 * @Author: MILLA
 * @CreateDate: 2021-02-03
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-02-03
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("biz_diagnosis_result")
@ApiModel(value = "故障诊断信息实体")
public class BizDiagnosisResultDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -8100230611506463885L;

    @ApiModelProperty(value = "事件id", notes = "")
    private Long eventId;

    @ApiModelProperty(value = "设备id", notes = "")
    private Long deviceId;

    @ApiModelProperty(value = "子系统id", notes = "")
    private Long subSystemId;

    @ApiModelProperty(value = "模型id", notes = "")
    private Long modelId;

    @ApiModelProperty(value = "设备编号", notes = "")
    private String deviceCode;

    @ApiModelProperty(value = "设备名称", notes = "")
    private String deviceName;

    @ApiModelProperty(value = "故障推理时间", notes = "")
    private Date gmtDiagnosis;

    @ApiModelProperty(value = "故障推理结果", notes = "规则id集合")
    private String diagnosisResult;

    @ApiModelProperty(value = "诊断状态", notes = "1:生成中 2:生成成功 3:生成失败")
    private Integer status;

    @ApiModelProperty(value = "备注", notes = "")
    private String remark;

}