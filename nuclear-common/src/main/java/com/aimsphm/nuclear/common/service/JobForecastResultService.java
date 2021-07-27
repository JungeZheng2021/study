package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.CommonComponentDO;
import com.aimsphm.nuclear.common.entity.JobForecastResultDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.vo.JobForecastResultVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.common.service
 * @Description: <预测结果信息服务类>
 * @Author: MILLA
 * @CreateDate: 2021-07-15
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-07-15
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface JobForecastResultService extends IService<JobForecastResultDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    Page<JobForecastResultDO> listJobForecastResultByPageWithParams(QueryBO<JobForecastResultDO> queryBO);

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    List<JobForecastResultDO> listJobForecastResultWithParams(QueryBO<JobForecastResultDO> queryBO);

    /**
     * 根据设备id和部件id查询性能预测结果
     *
     * @param deviceId    设备id
     * @param componentId 部件id
     * @return
     */
    JobForecastResultVO listJobForecastResultByIds(Long deviceId, Long componentId);

    /**
     * 根据设备id查询部件信息
     *
     * @param deviceId 设备id
     * @return
     */
    List<CommonComponentDO> listCommonComponentByDeviceId(Long deviceId);
}
