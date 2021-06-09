package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.AlgorithmNormalFaultConclusionDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.common.service
 * @Description: <服务类>
 * @Author: MILLA
 * @CreateDate: 2021-06-09
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-06-09
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface AlgorithmNormalFaultConclusionService extends IService<AlgorithmNormalFaultConclusionDO> {
    
    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    Page<AlgorithmNormalFaultConclusionDO> listAlgorithmNormalFaultConclusionByPageWithParams(QueryBO<AlgorithmNormalFaultConclusionDO> queryBO);

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    List<AlgorithmNormalFaultConclusionDO> listAlgorithmNormalFaultConclusionWithParams(QueryBO<AlgorithmNormalFaultConclusionDO> queryBO);
}
