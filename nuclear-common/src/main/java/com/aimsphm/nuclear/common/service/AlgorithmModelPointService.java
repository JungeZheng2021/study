package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.AlgorithmModelPointDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 功能描述:模型对应测点信息服务类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020-12-23 14:30
 */
public interface AlgorithmModelPointService extends IService<AlgorithmModelPointDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 分页
     */
    Page<AlgorithmModelPointDO> listAlgorithmModelPointByPageWithParams(QueryBO<AlgorithmModelPointDO> queryBO);
}
