package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.BizOriginalDataDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.common.service
 * @Description: <波形数据信息服务类>
 * @Author: MILLA
 * @CreateDate: 2021-02-03
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-02-03
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface BizOriginalDataService extends IService<BizOriginalDataDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    Page<BizOriginalDataDO> listBizOriginalDataByPageWithParams(QueryBO<BizOriginalDataDO> queryBO);

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    List<BizOriginalDataDO> listBizOriginalDataWithParams(QueryBO<BizOriginalDataDO> queryBO);

    /**
     * 根据时间区间和传感器code获取原始数据
     *
     * @param sensorCode
     * @param start
     * @param end
     * @return
     */
    List<Long> listBizOriginalDataByParams(String sensorCode, Long start, Long end);
}
