package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.CommonMeasurePointDO;
import com.aimsphm.nuclear.common.entity.JobAlarmEventDO;
import com.aimsphm.nuclear.common.entity.bo.CommonQueryBO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

/**
 * @Package: com.aimsphm.nuclear.ext.service
 * @Description: <报警事件扩展服务类>
 * @Author: MILLA
 * @CreateDate: 2020-12-05
 * @UpdateUser: MILLA
 * @UpdateDate: 2020-12-05
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface JobAlarmEventService extends IService<JobAlarmEventDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    Page<JobAlarmEventDO> listJobAlarmEventByPageWithParams(QueryBO<JobAlarmEventDO> queryBO);

    /**
     * 获取报警管理中所有的测点信息
     *
     * @param queryBO
     * @return
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
     * @return
     */
    Long getNewestEventIdByDeviceId(Long deviceId);

    /**
     * 获取报警事件列表
     *
     * @param subSystemId 子系统id
     * @param deviceId    设备id
     * @param start       开始时间
     * @param end         结束时间
     * @return
     */
    List<JobAlarmEventDO> listPointsWithAlarm(Long subSystemId, Long deviceId, Long start, Long end);
}
