package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.aimsphm.nuclear.common.entity.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Package: com.aimsphm.nuclear.common.entity
 * @Description: <实体>
 * @Author: MILLA
 * @CreateDate: 2021-06-04
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-06-04
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
@TableName("algorithm_normal_fault_feature")
@ApiModel(value = "实体")
public class AlgorithmNormalFaultFeatureDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -8312969175847042619L;

    @ApiModelProperty(value = "组件id", notes = "")
    private Long componentId;

    @ApiModelProperty(value = "传感器code", notes = "")
    private String sensorCode;

    @ApiModelProperty(value = "特征关联测点", notes = "")
    private String sensorDesc;

    @ApiModelProperty(value = "", notes = "")
    private String featureName;

    @ApiModelProperty(value = "", notes = "")
    private Integer deviceType;

    @ApiModelProperty(value = "", notes = "")
    private Integer additionalType;

    @ApiModelProperty(value = "", notes = "")
    private Double featureHi;

    @ApiModelProperty(value = "", notes = "")
    private Double featureLo;

    @ApiModelProperty(value = "自定义:", notes = "0, 上升: 1, 下降: 2, 阶跃陡升: 3, 阶跃陡降: 4, 超阈值上限: 5,超阈值下限: 6, 存在差异: 7, 触发: 8, 数据异常: 9, 测量值为零: 10,超阈值: 11, 波动: 12, 阶跃突变: 13, 出现: 14, 闪发: 15")
    private Integer featureType;

    @ApiModelProperty(value = "", notes = "")
    private String plusValue;

    @ApiModelProperty(value = "", notes = "")
    private Integer priority;

    @ApiModelProperty(value = "", notes = "")
    private Long offsetEarly;

    @ApiModelProperty(value = "", notes = "")
    private Double frequency;

    @ApiModelProperty(value = "", notes = "")
    private String featureValue;

    @ApiModelProperty(value = "", notes = "")
    private String featureCode;

    @ApiModelProperty(value = "", notes = "")
    private String examCode;

    @ApiModelProperty(value = "特征值时间范围", notes = "")
    private String timeRange;

    @ApiModelProperty(value = "特征值间隔时间", notes = "")
    private String timeGap;

    @ApiModelProperty(value = "同一个间隔时间段内执行的采样方法，只保留1个点", notes = "")
    private String sampleMethod;

}