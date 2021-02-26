package com.aimsphm.nuclear.common.service;

import com.aimsphm.nuclear.common.entity.JobAlarmThresholdDO;
import com.aimsphm.nuclear.common.entity.bo.QueryBO;
import com.aimsphm.nuclear.common.entity.vo.MeasurePointVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Package: com.aimsphm.nuclear.common.service
 * @Description: <阈值报警信息服务类>
 * @Author: MILLA
 * @CreateDate: 2021-01-04
 * @UpdateUser: MILLA
 * @UpdateDate: 2021-01-04
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public interface JobAlarmThresholdService extends IService<JobAlarmThresholdDO> {

    /**
     * 根据条件获取分页查询数据
     *
     * @param queryBO 查询条件
     * @return
     */
    Page<JobAlarmThresholdDO> listJobAlarmThresholdByPageWithParams(QueryBO<JobAlarmThresholdDO> queryBO);

    /**
     * 保存阈值报警信息
     *
     * @param vos
     */
    void saveOrUpdateThresholdAlarmList(List<MeasurePointVO> vos);

    /**
     * 导出阈值报警
     *
     * @param queryBO
     * @param response
     */
    void listJobAlarmThresholdByPageWithParams(QueryBO queryBO, HttpServletResponse response);

    /**
     * 仍然存在的阈值报警
     *
     * @param deviceId 设备id
     * @return
     */
    List<JobAlarmThresholdDO> listCurrentThresholdAlarm(Long deviceId);
}
