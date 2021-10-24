package com.aimsphm.nuclear.report.job;

import com.aimsphm.nuclear.common.entity.CommonDeviceDO;
import com.aimsphm.nuclear.common.entity.bo.ReportQueryBO;
import com.aimsphm.nuclear.common.service.CommonDeviceService;
import com.aimsphm.nuclear.common.util.DateUtils;
import com.aimsphm.nuclear.report.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * <p>
 * 功能描述:泵类状态监测报告定时任务
 * </p>
 *
 * @author MILLA
 * @version 1.0
 * @since 2020/5/12 18:58
 */
@Slf4j
public class PumReportJob implements Job {

    @Resource
    private ReportService service;

    @Resource
    private CommonDeviceService deviceService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        ReportQueryBO queryBO = new ReportQueryBO(2L);
        try {
            List<CommonDeviceDO> deviceList = deviceService.listCommonDeviceBySubSystemId(queryBO.getSubSystemId());
            if (CollectionUtils.isEmpty(deviceList)) {
                return;
            }
            CountDownLatch count = new CountDownLatch(deviceList.size());
            deviceList.stream().forEach(device -> {
                try {
                    queryBO.setDeviceId(device.getId());
                    queryBO.setDeviceName(device.getDeviceName());
                    queryBO.setReportName(device.getDeviceCode() + "性能监测报告");
                    queryBO.setEndTime(DateUtils.previousMonthLastDay().getTime());
                    queryBO.setStartTime(DateUtils.previousMonthFirstDay().getTime());
                    service.saveAutoReport(queryBO);
                } catch (Exception e) {
                    log.error("device ->{} create report failed ：{}", device.getDeviceName(), e);
                } finally {
                    count.countDown();
                }
            });
            count.await();
        } catch (Exception e) {
            log.error("create report failed ：{}", e);
        }
    }
}
