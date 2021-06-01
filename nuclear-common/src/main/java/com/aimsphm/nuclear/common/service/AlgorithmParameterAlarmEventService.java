package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.AlgorithmParameterAlarmEventDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.common.service
 * @Description: <服务类>
 * @Author: MILLA
 * @CreateDate: 2021-06-01
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-06-01
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface AlgorithmParameterAlarmEventService extends IService<AlgorithmParameterAlarmEventDO> {
    
    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    Page<AlgorithmParameterAlarmEventDO> listAlgorithmParameterAlarmEventByPageWithParams(QueryBO<AlgorithmParameterAlarmEventDO> queryBO);

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    List<AlgorithmParameterAlarmEventDO> listAlgorithmParameterAlarmEventWithParams(QueryBO<AlgorithmParameterAlarmEventDO> queryBO);
}
