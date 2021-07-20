package com.aimsphm.nuclear.algorithm.service;

import com.aimsphm.nuclear.algorithm.entity.dto.FaultReportResponseDTO;
import com.aimsphm.nuclear.algorithm.entity.dto.SymptomResponseDTO;
import com.aimsphm.nuclear.common.entity.BizDiagnosisResultDO;
import com.aimsphm.nuclear.common.entity.vo.FaultReasoningVO;

import java.util.List;
import java.util.Map;

/**
 * @Package: com.aimsphm.nuclear.common.service
 * @Description: <故障诊断信息服务类>
 * @Author: MILLA
 * @CreateDate: 2021-02-01
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-02-01
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface BizDiagnosisService {

    /**
     * 根据测点id进行故障推理
     *
     * @param pointIdList  测点列表
     * @param result       推理实体[要保存推理结果的其他基本信息(deviceId、modelId...)]
     * @param isReportType 是否是自动报告的类型 1:报告类型 0:不是报告类型
     * @return
     */
    Map<String, List<FaultReportResponseDTO>> faultDiagnosis(List<String> pointIdList, BizDiagnosisResultDO result, Integer isReportType);

    /**
     * 故障推理
     *
     * @param responseDTO 激活的征兆
     * @param deviceId    设备di
     * @return
     */
    List<FaultReasoningVO> faultReasoning(SymptomResponseDTO responseDTO, Long deviceId);
}
