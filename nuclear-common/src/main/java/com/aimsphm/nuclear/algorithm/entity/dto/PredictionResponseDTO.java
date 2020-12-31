package com.aimsphm.nuclear.algorithm.entity.dto;

import lombok.Data;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.algorithm.entity.bo
 * @Description: <滑动平均值结构体>
 * @Author: MILLA
 * @CreateDate: 2020/12/22 14:28
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/12/22 14:28
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class PredictionResponseDTO {

    /**
     * 返回值-数据值
     */
    private List<Double> predSignal;
    /**
     * 返回值-时间戳
     */
    private List<Long> predTimestamp;

}
