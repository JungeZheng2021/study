package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.JobDownSampleDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
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
 * @since 2021-07-17 14:30
 */
public interface JobDownSampleService extends IService<JobDownSampleDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 分页
     */
    Page<JobDownSampleDO> listBizDownSampleByPageWithParams(QueryBO<JobDownSampleDO> queryBO);

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 集合
     */
    List<JobDownSampleDO> listBizDownSampleWithParams(QueryBO<JobDownSampleDO> queryBO);
}
