package com.milla.study.quartz.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @Package: com.milla.study.quartz.job
 * @Description: <定时任务>
 * @Author: MILLA
 * @CreateDate: 2020/5/12 18:58
 * @UpdateUser: MILLA
 * @UpdateDate: 2020/5/12 18:58
 * @UpdateRemark: <>
 * @Version: 1.0
 */
public class ExampleJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //此处是需要定时执行的业务
        System.out.println("就做个打印吧");
    }
}
