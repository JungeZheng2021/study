package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.AlgorithmNormalFaultFeatureDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.vo.AlgorithmNormalFaultFeatureVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 功能描述:服务实现类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021-06-03 14:30
 */
public interface AlgorithmNormalFaultFeatureService extends IService<AlgorithmNormalFaultFeatureDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 分页结果
     */
    Page<AlgorithmNormalFaultFeatureDO> listAlgorithmNormalFaultFeatureByPageWithParams(QueryBO<AlgorithmNormalFaultFeatureDO> queryBO);

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 集合
     */
    List<AlgorithmNormalFaultFeatureDO> listAlgorithmNormalFaultFeatureWithParams(QueryBO<AlgorithmNormalFaultFeatureDO> queryBO);

    /**
     * 根据组件id查询组件信息
     *
     * @param componentId 部件id
     * @return 对象
     */
    AlgorithmNormalFaultFeatureVO getAlgorithmNormalFaultFeatureByComponentId(Long componentId);
}
