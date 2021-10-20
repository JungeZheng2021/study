package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.JobAlarmThresholdDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 功能描述:服务类
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2021-01-04 14:30
 */
public interface JobAlarmThresholdService extends IService<JobAlarmThresholdDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 分页
     */
    Page<JobAlarmThresholdDO> listJobAlarmThresholdByPageWithParams(QueryBO<JobAlarmThresholdDO> queryBO);

    /**
     * 保存阈值报警信息
     *
     * @param vos 集合
     */
    void saveOrUpdateThresholdAlarmList(List<MeasurePointVO> vos);

    /**
     * 导出阈值报警
     *
     * @param queryBO  条件
     * @param response 响应对象
     */
    void listJobAlarmThresholdByPageWithParams(QueryBO queryBO, HttpServletResponse response);

    /**
     * 仍然存在的阈值报警
     *
     * @param deviceId 设备id
     * @return 集合
     */
    List<Object[]> listCurrentThresholdAlarm(Long deviceId);
}
