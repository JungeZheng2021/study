package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.AlgorithmNormalFeatureFeatureDO;
import com.aimsphm.nuclear.common.entity.CommonDeviceDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.vo.SymptomCorrelationVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 功能描述:服务类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021-06-03 14:30
 */
public interface AlgorithmNormalFeatureFeatureService extends IService<AlgorithmNormalFeatureFeatureDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 分页
     */
    Page<AlgorithmNormalFeatureFeatureDO> listAlgorithmNormalFeatureFeatureByPageWithParams(QueryBO<AlgorithmNormalFeatureFeatureDO> queryBO);

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 集合
     */
    List<AlgorithmNormalFeatureFeatureDO> listAlgorithmNormalFeatureFeatureWithParams(QueryBO<AlgorithmNormalFeatureFeatureDO> queryBO);

    /**
     * 查询征兆之间的关联度信息
     *
     * @param device 设备信息
     * @return 集合
     */
    List<SymptomCorrelationVO> listSymptomCorrelationVO(CommonDeviceDO device);
}
