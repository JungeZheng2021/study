package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.BizReportDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.bo.ReportQueryBO;
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
public interface BizReportService extends IService<BizReportDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 分页
     */
    Page<BizReportDO> listBizReportByPageWithParams(QueryBO<BizReportDO> queryBO);

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 集合
     */
    List<BizReportDO> listBizReportWithParams(QueryBO<BizReportDO> queryBO);


    /**
     * 下载文件到网页
     *
     * @param id 报告id
     */
    void downLoad2Website(Long id);

    /**
     * 删除报告
     *
     * @param id 主键
     * @return 布尔
     */
    boolean removeReportById(Long id);

    /**
     * 根据条件查询报告
     *
     * @param query query
     * @return 对象
     */
    BizReportDO getReport(ReportQueryBO query);

    /**
     * 更新报告的状态
     *
     * @param reportId 报告id
     * @param status   指定状态
     */
    void updateReportStatus(Long reportId, Integer status, String cause);
}
