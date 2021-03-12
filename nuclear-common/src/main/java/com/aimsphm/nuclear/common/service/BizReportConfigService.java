package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.BizReportConfigDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.common.service
 * @Description: <报告生成测点配置表服务类>
 * @Author: MILLA
 * @CreateDate: 2021-02-23
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-02-23
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface BizReportConfigService extends IService<BizReportConfigDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    Page<BizReportConfigDO> listBizReportConfigByPageWithParams(QueryBO<BizReportConfigDO> queryBO);

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    List<BizReportConfigDO> listBizReportConfigWithParams(QueryBO<BizReportConfigDO> queryBO);

    /**
     * 根据subsystemId获取配置
     *
     * @param deviceId
     * @return
     */
    List<BizReportConfigDO> listConfigByDeviceId(Long deviceId);
}
