package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.aimsphm.nuclear.common.entity.JobAlarmEventDO;
import com.aimsphm.nuclear.common.entity.bo.CommonQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
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
 * @since 2020-12-05 14:30
 */
public interface JobAlarmEventService extends IService<JobAlarmEventDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return 分页
     */
    Page<JobAlarmEventDO> listJobAlarmEventByPageWithParams(QueryBO<JobAlarmEventDO> queryBO);

    /**
     * 获取报警管理中所有的测点信息
     *
     * @param queryBO 条件
     * @return 集合
     */
    List<CommonMeasurePointDO> listPointByConditions(CommonQueryBO queryBO);

    /**
     * 报警事件导出
     *
     * @param queryBO  实体
     * @param response 响应对象
     */
    void listJobAlarmEventWithParams(QueryBO queryBO, HttpServletResponse response);


    /**
     * 获取最新的报警事件id
     *
     * @param deviceId 设备id
     * @return 长整型
     */
    Long getNewestEventIdByDeviceId(Long deviceId);

    /**
     * 获取报警事件列表
     *
     * @param subSystemId 子系统id
     * @param deviceId    设备id
     * @param start       开始时间
     * @param end         结束时间
     * @return 集合
     */
    List<JobAlarmEventDO> listPointsWithAlarm(Long subSystemId, Long deviceId, Long start, Long end);
}
