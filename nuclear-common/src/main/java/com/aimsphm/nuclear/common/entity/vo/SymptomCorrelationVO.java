package com.aimsphm.nuclear.common.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Package: com.aimsphm.nuclear.common.entity.vo
 * @Description: <征兆之间关联关系>
 * @Author: milla
 * @CreateDate: 2021/06/04 13:33
 * @UpdateUser: milla
 * @UpdateDate: 2021/06/04 13:33
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class SymptomCorrelationVO {
    @ApiModelProperty(value = "征兆1", notes = "")
    private Long sym1;
    @ApiModelProperty(value = "征兆2", notes = "")
    private Long sym2;
    @ApiModelProperty(value = "征兆间关联度", notes = "")
    private Double corr;
}
