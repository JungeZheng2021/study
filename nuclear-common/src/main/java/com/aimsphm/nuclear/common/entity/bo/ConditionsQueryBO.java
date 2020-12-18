package com.aimsphm.nuclear.common.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Package: com.aimsphm.nuclear.common.entity.bo
 * @Description: <条件查询实体>
 * @Author: MILLA
 * @CreateDate: 2020/4/30 20:48
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/30 20:48
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class ConditionsQueryBO extends TimeRangeQueryBO {
    @ApiModelProperty(value = "结束时间")
    private String keyword;
}
