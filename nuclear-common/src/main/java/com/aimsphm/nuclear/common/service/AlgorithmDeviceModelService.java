package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.AlgorithmDeviceModelDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Package: com.aimsphm.nuclear.common.service
 * @Description: <算法模型关系信息服务类>
 * @Author: MILLA
 * @CreateDate: 2020-12-23
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-12-23
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface AlgorithmDeviceModelService extends IService<AlgorithmDeviceModelDO> {
    
    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    Page<AlgorithmDeviceModelDO> listAlgorithmDeviceModelByPageWithParams(QueryBO<AlgorithmDeviceModelDO> queryBO);
}
