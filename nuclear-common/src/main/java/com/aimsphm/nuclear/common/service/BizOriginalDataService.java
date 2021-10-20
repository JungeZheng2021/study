package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.BizOriginalDataDO;
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
 * @since 2021-02-03 14:30
 */
public interface BizOriginalDataService extends IService<BizOriginalDataDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 分页
     */
    Page<BizOriginalDataDO> listBizOriginalDataByPageWithParams(QueryBO<BizOriginalDataDO> queryBO);

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 集合
     */
    List<BizOriginalDataDO> listBizOriginalDataWithParams(QueryBO<BizOriginalDataDO> queryBO);

    /**
     * 根据时间区间和传感器code获取原始数据
     *
     * @param sensorCode 编码
     * @param start      开始
     * @param end        结束
     * @return 集合
     */
    List<Long> listBizOriginalDataByParams(String sensorCode, Long start, Long end);
}
