package com.aimsphm.nuclear.common.entity.dto;

import lombok.Data;

/**
 * <p>
 * 功能描述:HBase中时序数据传输实体
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/10/28 10:04
 */
@Data
public class TrendFeatureCellDTO {
    private Long timestamp;
    private Object mean;
    private Object std;
}
