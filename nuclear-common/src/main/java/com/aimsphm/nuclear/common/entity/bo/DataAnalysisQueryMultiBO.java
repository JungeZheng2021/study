package com.aimsphm.nuclear.common.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.common.entity.bo
 * @Description: <振动分析调用算法>
 * @Author: MILLA
 * @CreateDate: 2020/3/6 10:58
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/3/6 10:58
 * @UpdateRemark: <>
 * @Version: 1.0
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
