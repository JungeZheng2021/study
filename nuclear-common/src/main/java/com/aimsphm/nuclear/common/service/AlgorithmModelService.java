package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.AlgorithmModelDO;
import com.aimsphm.nuclear.common.entity.bo.ConditionsQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.common.service
 * @Description: <算法模型信息服务类>
 * @Author: MILLA
 * @CreateDate: 2020-12-23
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-12-23
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface AlgorithmModelService extends IService<AlgorithmModelDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    Page<AlgorithmModelDO> listAlgorithmModelByPageWithParams(QueryBO<AlgorithmModelDO> queryBO);

    /**
     * 根据条件查询数据，不分页
     *
     * @param queryBO
     * @return
     */
    List<AlgorithmModelDO> listAlgorithmModelWithParams(QueryBO<AlgorithmModelDO> queryBO);
}
