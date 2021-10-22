package com.aimsphm.nuclear.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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

    @ApiModelProperty(value = "组件id")
    private Long componentId;

    @ApiModelProperty(value = "传感器code")
    private String sensorCode;

    @ApiModelProperty(value = "特征关联测点")
    private String sensorDesc;

    private String featureName;

    private Integer deviceType;

    private Integer additionalType;

    private Double featureHi;

    private Double featureLo;

    @ApiModelProperty(value = "自定义:", notes = "0, 上升: 1, 下降: 2, 阶跃陡升: 3, 阶跃陡降: 4, 超阈值上限: 5,超阈值下限: 6, 存在差异: 7, 触发: 8, 数据异常: 9, 测量值为零: 10,超阈值: 11, 波动: 12, 阶跃突变: 13, 出现: 14, 闪发: 15")
    private Integer featureType;

    private String plusValue;

    private Integer priority;

    private Long offsetEarly;

    private Double frequency;

    private String featureValue;

    private String featureCode;

    private String examCode;

    @ApiModelProperty(value = "特征值时间范围")
    private String timeRange;

    @ApiModelProperty(value = "特征值间隔时间")
    private String timeGap;

    @ApiModelProperty(value = "同一个间隔时间段内执行的采样方法，只保留1个点")
    private String sampleMethod;

}