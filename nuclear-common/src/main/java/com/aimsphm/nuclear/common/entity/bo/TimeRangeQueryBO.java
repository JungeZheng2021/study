package com.aimsphm.nuclear.common.entity.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Package: com.aimsphm.nuclear.common.entity.bo
 * @Description: <时间区间查询>
 * @Author: MILLA
 * @CreateDate: 2020/4/30 20:48
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/4/30 20:48
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class TimeRangeQueryBO implements Serializable {
    private static final long serialVersionUID = -5360205367689511954L;
    @ApiModelProperty(value = "开始时间")
    private Long start;
    @ApiModelProperty(value = "结束时间")
    private Long end;
}
