package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.BizReportDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.bo.ReportQueryBO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.common.service
 * @Description: <报告表服务类>
 * @Author: MILLA
 * @CreateDate: 2021-02-23
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-02-23
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface BizReportService extends IService<BizReportDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    Page<BizReportDO> listBizReportByPageWithParams(QueryBO<BizReportDO> queryBO);

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
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
     * @return
     */
    boolean removeReportById(Long id);

    /**
     * 根据条件查询报告
     *
     * @param query
     * @return
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
