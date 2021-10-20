package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.BizReportConfigDO;
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
 * @since 2021-02-23 14:30
 */
public interface BizReportConfigService extends IService<BizReportConfigDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 分页
     */
    Page<BizReportConfigDO> listBizReportConfigByPageWithParams(QueryBO<BizReportConfigDO> queryBO);

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 集合
     */
    List<BizReportConfigDO> listBizReportConfigWithParams(QueryBO<BizReportConfigDO> queryBO);

    /**
     * 根据subsystemId获取配置
     *
     * @param deviceId 设备id
     * @return 集合
     */
    List<BizReportConfigDO> listConfigByDeviceId(Long deviceId);
}
