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
@TableName("algorithm_normal_feature_feature")
@ApiModel(value = "实体")
public class AlgorithmNormalFeatureFeatureDO extends BaseDO {
    /**
     * 序列化时候使用
     */
    private static final long serialVersionUID = -6569769948846945421L;

    @ApiModelProperty(value = "", notes = "")
    private Long featureId1;

    @ApiModelProperty(value = "", notes = "")
    private Long featureId2;

    @ApiModelProperty(value = "'规则和征兆的关联程度1为关联度100%，-1为负相关，0为不相关", notes = "' '")
    private Float correlation;

}