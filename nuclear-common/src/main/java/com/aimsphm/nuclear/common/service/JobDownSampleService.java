package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.JobDownSampleDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.common.service
 * @Description: <等间隔降采样数据服务类>
 * @Author: MILLA
 * @CreateDate: 2021-07-27
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-07-27
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface JobDownSampleService extends IService<JobDownSampleDO> {
    
    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    Page<JobDownSampleDO> listBizDownSampleByPageWithParams(QueryBO<JobDownSampleDO> queryBO);

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    List<JobDownSampleDO> listBizDownSampleWithParams(QueryBO<JobDownSampleDO> queryBO);
}
