package com.aimsphm.nuclear.common.entity.bo;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * 功能描述:多测点查询
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/11/21 17:33
 */
@Data
public class HistoryQueryResult {
    private String tag;
    private Double earlyWarningHi;
    private Double earlyWarningLo;
    private Double thrHi;
    private Double thrLo;
    private Double thrHihi;
    private Double thrLolo;
    private String unit;
    private List<List<Object>> pointList;
}
