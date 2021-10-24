package com.aimsphm.nuclear.common.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 功能描述:振动分析调用算法
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/09/04 14:42
 */
@Data
public class DataAnalysisQueryMultiBO {

    @ApiModelProperty(value = "算法类型", notes = "")
    private String type;

    @ApiModelProperty(value = "多个测点", notes = "和时间戳一一对应")
    private List<String> pointIds;

    @ApiModelProperty(value = "多个时间戳", notes = "和测点一一对应")
    private List<Long> timestamps;
}
