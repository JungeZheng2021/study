package com.aimsphm.nuclear.common.entity.bo;

import com.aimsphm.nuclear.common.util.DateUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 功能描述:补点实体
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021-06-09 14:30
 */
@Data
public class HistoryQueryFilledBO extends TimeRangeQueryBO implements Serializable {
    private static final long serialVersionUID = -5360205367689511954L;
    @ApiModelProperty(value = "开始时间")
    private Long start;
    @ApiModelProperty(value = "结束时间")
    private Long end;

    @ApiModelProperty(value = "需要补点的表格名称")
    private String tableName;

    @ApiModelProperty(value = "测点id")
    private String pointId;

    public HistoryQueryFilledBO() {
    }

    public HistoryQueryFilledBO(Long start, Long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return DateUtils.format(start) + " ~ " + DateUtils.format(end) + " pointId: " + pointId + " tableName: " + tableName;
    }
}
