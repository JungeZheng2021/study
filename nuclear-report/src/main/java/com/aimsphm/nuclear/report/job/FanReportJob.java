package com.aimsphm.nuclear.report.job;

import com.aimsphm.nuclear.common.entity.CommonDeviceDO;
import com.aimsphm.nuclear.common.entity.bo.ReportQueryBO;
import com.aimsphm.nuclear.common.service.CommonDeviceService;
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
 * @Package: com.aimsphm.nuclear.report.job
 * @Description: <风机状态监测报告定时任务>
 * @Author: MILLA
 * @CreateDate: 2020/5/12 18:58
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/5/12 18:58
 * @UpdateRemark: <>
 * @Version: 1.0
 */
@Slf4j
public class FanReportJob implements Job {

    @Resource
    private ReportService service;

    @Resource
    private CommonDeviceService deviceService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        ReportQueryBO queryBO = new ReportQueryBO(1L);
        try {
            List<CommonDeviceDO> deviceList = deviceService.listCommonDeviceBySubSystemId(queryBO.getSubSystemId());
            if (CollectionUtils.isEmpty(deviceList)) {
                return;
            }
            CountDownLatch count = new CountDownLatch(deviceList.size());
            deviceList.stream().forEach((device) -> {
                try {
                    queryBO.setDeviceName(device.getDeviceName());
                    queryBO.setDeviceId(device.getId());
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
