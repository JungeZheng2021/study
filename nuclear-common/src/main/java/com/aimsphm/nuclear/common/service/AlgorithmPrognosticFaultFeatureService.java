package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.AlgorithmPrognosticFaultFeatureDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.common.service
 * @Description: <用于特征预测的数据	该表中的id和algorithm_normal_fault_feature中的id对应，但是该表中的数据可能比algorithm_normal_fault_feature中的数据少服务类>
 * @Author: MILLA
 * @CreateDate: 2021-07-15
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-07-15
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface AlgorithmPrognosticFaultFeatureService extends IService<AlgorithmPrognosticFaultFeatureDO> {
    
    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    Page<AlgorithmPrognosticFaultFeatureDO> listAlgorithmPrognosticFaultFeatureByPageWithParams(QueryBO<AlgorithmPrognosticFaultFeatureDO> queryBO);

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    List<AlgorithmPrognosticFaultFeatureDO> listAlgorithmPrognosticFaultFeatureWithParams(QueryBO<AlgorithmPrognosticFaultFeatureDO> queryBO);
}
