package com.aimsphm.nuclear.algorithm.entity.bo;

import lombok.Data;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.algorithm.entity.bo
 * @Description: <测点预估数据值>
 * @Author: MILLA
 * @CreateDate: 2020/12/23 15:34
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/12/23 15:34
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Data
public class PointEstimateResultsDataBO {

    private List<PointEstimateDataBO> estimateResults;
}
