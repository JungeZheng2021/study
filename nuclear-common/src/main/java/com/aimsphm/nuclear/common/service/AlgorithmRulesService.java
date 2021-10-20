package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.AlgorithmRulesDO;
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
 * @since 2021-01-29 14:30
 */
public interface AlgorithmRulesService extends IService<AlgorithmRulesDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 分页
     */
    Page<AlgorithmRulesDO> listAlgorithmRulesByPageWithParams(QueryBO<AlgorithmRulesDO> queryBO);

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 集合
     */
    List<AlgorithmRulesDO> listAlgorithmRulesWithParams(QueryBO<AlgorithmRulesDO> queryBO);

    /**
     * 根据传感器编号查询规则
     *
     * @param sensorCodeList 传感器列表
     * @return 集合
     */
    List<AlgorithmRulesDO> listRulesBySensorCodeList(List<String> sensorCodeList);
}
