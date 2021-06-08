package com.aimsphm.nuclear.algorithm.service;

import com.aimsphm.nuclear.algorithm.entity.dto.FaultReasoningResponseDTO;

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
     * 故障推理
     *  @param pointIds
     * @param deviceId
     * @return
     */
    FaultReasoningResponseDTO faultReasoning(List<String> pointIds, Long deviceId);
}
