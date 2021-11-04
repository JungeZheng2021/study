package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.SparkDownSampleConfigDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 功能描述:降采样配置表服务类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021-11-01
 */
public interface SparkDownSampleConfigService extends IService<SparkDownSampleConfigDO> {
    
    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    Page<SparkDownSampleConfigDO> listSparkDownSampleConfigByPageWithParams(QueryBO<SparkDownSampleConfigDO> queryBO);

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    List<SparkDownSampleConfigDO> listSparkDownSampleConfigWithParams(QueryBO<SparkDownSampleConfigDO> queryBO);
}
