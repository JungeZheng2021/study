package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.AlgorithmRulesDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.common.service
 * @Description: <服务类>
 * @Author: MILLA
 * @CreateDate: 2021-01-29
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-01-29
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface AlgorithmRulesService extends IService<AlgorithmRulesDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    Page<AlgorithmRulesDO> listAlgorithmRulesByPageWithParams(QueryBO<AlgorithmRulesDO> queryBO);

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    List<AlgorithmRulesDO> listAlgorithmRulesWithParams(QueryBO<AlgorithmRulesDO> queryBO);

    /**
     * 根据传感器编号查询规则
     *
     * @param sensorCodeList 传感器列表
     * @return
     */
    List<AlgorithmRulesDO> listRulesBySensorCodeList(List<String> sensorCodeList);
}
