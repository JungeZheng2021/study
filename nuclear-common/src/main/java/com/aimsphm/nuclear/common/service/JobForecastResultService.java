package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.CommonComponentDO;
import com.aimsphm.nuclear.common.entity.JobForecastResultDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.vo.JobForecastResultVO;
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
 * @since 2021-07-15 14:30
 */
public interface JobForecastResultService extends IService<JobForecastResultDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 分页
     */
    Page<JobForecastResultDO> listJobForecastResultByPageWithParams(QueryBO<JobForecastResultDO> queryBO);

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 集合
     */
    List<JobForecastResultDO> listJobForecastResultWithParams(QueryBO<JobForecastResultDO> queryBO);

    /**
     * 根据设备id和部件id查询性能预测结果
     *
     * @param deviceId    设备id
     * @param componentId 部件id
     * @return 对象
     */
    JobForecastResultVO listJobForecastResultByIds(Long deviceId, Long componentId);

    /**
     * 根据设备id查询部件信息
     *
     * @param deviceId 设备id
     * @return 集合
     */
    List<CommonComponentDO> listCommonComponentByDeviceId(Long deviceId);
}
