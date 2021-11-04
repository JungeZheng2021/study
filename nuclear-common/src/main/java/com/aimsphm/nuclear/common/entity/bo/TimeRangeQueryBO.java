package com.aimsphm.nuclear.common.entity.bo;

import com.aimsphm.nuclear.common.util.DateUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 功能描述:时间区间查询
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/4/30 20:48
 */
@Data
public class TimeRangeQueryBO implements Serializable {
    private static final long serialVersionUID = -5360205367689511954L;
    @ApiModelProperty(value = "开始时间")
    private Long start;
    @ApiModelProperty(value = "结束时间")
    private Long end;

    public TimeRangeQueryBO() {
    }

    public TimeRangeQueryBO(Long start, Long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return DateUtils.format(start) + " ~ " + DateUtils.format(end);
    }
}
