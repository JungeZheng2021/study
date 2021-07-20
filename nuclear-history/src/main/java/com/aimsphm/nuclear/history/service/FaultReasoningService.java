package com.aimsphm.nuclear.history.service;

import com.aimsphm.nuclear.algorithm.entity.dto.SymptomResponseDTO;
import com.aimsphm.nuclear.common.entity.vo.FaultReasoningVO;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.algorithm.service
 * @Description: <>
 * @Author: milla
 * @CreateDate: 2021/06/04 11:29
 * @UpdateUser: milla
 * @UpdateDate: 2021/06/04 11:29
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface FaultReasoningService {
    /**
     * 故障推列接口
     *
     * @param pointIds
     * @param deviceId
     * @return
     */
    List<FaultReasoningVO> faultReasoningVO(List<String> pointIds, Long deviceId);

    /**
     * 征兆判断
     *
     * @param pointIds
     * @return
     */
    SymptomResponseDTO symptomJudgment(List<String> pointIds);

}
