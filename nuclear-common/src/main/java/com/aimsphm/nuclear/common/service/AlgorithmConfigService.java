package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.AlgorithmConfigDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Package: com.aimsphm.nuclear.common.service
 * @Description: <服务类>
 * @Author: MILLA
 * @CreateDate: 2020-12-22
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-12-22
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface AlgorithmConfigService extends IService<AlgorithmConfigDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    Page<AlgorithmConfigDO> listAlgorithmConfigByPageWithParams(QueryBO<AlgorithmConfigDO> queryBO);
}
