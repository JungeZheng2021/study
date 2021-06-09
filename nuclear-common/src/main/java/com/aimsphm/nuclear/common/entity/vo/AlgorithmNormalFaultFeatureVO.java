package com.aimsphm.nuclear.common.entity.vo;

import com.aimsphm.nuclear.common.entity.AlgorithmNormalFaultFeatureDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Package: com.aimsphm.nuclear.common.entity.vo
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2021/06/09 15:58
 * @UpdateUser: milla
 * @UpdateDate: 2021/06/09 15:58
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class AlgorithmNormalFaultFeatureVO extends AlgorithmNormalFaultFeatureDO {

    @ApiModelProperty(value = "组件名称")
    private String componentName;
    @ApiModelProperty(value = "组件描述")
    private String componentDesc;
}
