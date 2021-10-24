package com.aimsphm.nuclear.common.entity.bo;

import com.aimsphm.nuclear.common.entity.dto.HBaseParamDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 功能描述:
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/09/04 14:42
 */
@Data
public class DataAnalysisQueryBO {
    @ApiModelProperty(value = "表格名称", required = true)
    private String tableName;
    @ApiModelProperty(value = "查询参数", required = true)
    List<HBaseParamDTO> params;
}
