package com.aimsphm.nuclear.algorithm.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.algorithm.entity.bo
 * @Description: <预估计值数据>
 * @Author: MILLA
 * @CreateDate: 2020/12/23 15:34
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/12/23 15:34
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class EstimateDataBO {
    @ApiModelProperty(value = "模型id", notes = "")
    private Long modelId;

    @ApiModelProperty(value = "模型名称", notes = "")
    private String modelName;
}
