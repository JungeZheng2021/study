package com.aimsphm.nuclear.common.entity.bo;

import com.aimsphm.nuclear.common.entity.dto.HBaseParamDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Package:com.aimsphm.nuclear.common.entity.bo
 * @Description: <>
 * @Author: MILLA
 * @CreateDate: 2020/3/6 10:58
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/3/6 10:58
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class DataAnalysisQueryBO {
    @ApiModelProperty(value = "表格名称", required = true)
    private String tableName;
    @ApiModelProperty(value = "查询参数", required = true)
    List<HBaseParamDTO> params;
}
